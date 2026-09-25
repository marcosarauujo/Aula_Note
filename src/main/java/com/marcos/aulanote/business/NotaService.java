package com.marcos.aulanote.business;

import com.marcos.aulanote.business.dto.in.GeminiContent;
import com.marcos.aulanote.business.dto.in.GeminiDTORequest;
import com.marcos.aulanote.business.dto.in.GeminiPart;
import com.marcos.aulanote.business.dto.out.GeminiDTOResponse;
import com.marcos.aulanote.business.dto.out.NotaDTOResponse;
import com.marcos.aulanote.business.mapper.NotaMapper;
import com.marcos.aulanote.infrastructure.client.GeminiClient;
import com.marcos.aulanote.infrastructure.entity.Nota;
import com.marcos.aulanote.infrastructure.entity.Sessao;
import com.marcos.aulanote.infrastructure.entity.Transcricao;
import com.marcos.aulanote.infrastructure.exception.ResourceNotFoundException;
import com.marcos.aulanote.infrastructure.repository.NotaRepository;
import com.marcos.aulanote.infrastructure.repository.SessaoRepository;
import com.marcos.aulanote.infrastructure.repository.TranscricaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotaService {
    private final NotaRepository notaRepository;
    private final TranscricaoRepository transcricaoRepository;
    private final SessaoRepository sessaoRepository;
    private final NotaMapper notaMapper;
    private final GeminiClient geminiClient;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public NotaDTOResponse gerarNota(String sessaoId) {
        Sessao sessao = sessaoRepository.findById(sessaoId).orElseThrow(() -> new ResourceNotFoundException(
                "Sessão não encontrada com o id: " + sessaoId)
        );

        // Junta todas as transcrições
        String transcricaoCompleta = transcricaoRepository.findBySessaoId(sessaoId)
                .stream()
                .map(Transcricao::getConteudo)
                .collect(Collectors.joining(" "));

        // Monta o prompt para o Gemini
        String prompt = """
                Você é um assistente de estudos. Com base na transcrição da aula abaixo,
                gere um resumo estruturado em tópicos com os conceitos principais, 
                pontos importantes e exemplos citados.
                
                Transcrição:
                """ + transcricaoCompleta;

        // Monta o request e chama o Gemini
        GeminiDTORequest geminiRequest = GeminiDTORequest.builder()
                .contents(List.of(
                        GeminiContent.builder()
                                .parts(List.of(
                                        GeminiPart.builder().text(prompt).build()
                                ))
                                .build()
                ))
                .build();

        GeminiDTOResponse geminiResponse = geminiClient.gerarConteudo(geminiApiKey, geminiRequest);
        String notaGerada = geminiResponse.extrairTexto();


        // Salva a nota gerada pelo Gemini
        Nota nota = Nota.builder()
                .conteudo(notaGerada)
                .criadaEm(LocalDateTime.now())
                .sessao(sessao)
                .build();
        log.info("Nota gerada pelo Gemini para a sessão: {}", sessaoId);
        return notaMapper.paraDTOResponse(notaRepository.save(nota));
    }

    public NotaDTOResponse buscarNotaPorSessao(String sessaoId) {
        Nota nota = notaRepository.findBySessaoId(sessaoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Nota não encontrada para a sessão: " + sessaoId));
        return notaMapper.paraDTOResponse(nota);
    }

}

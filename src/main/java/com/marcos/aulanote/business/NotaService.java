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

        // Busca cada parte da transcrição separadamente
        List<Transcricao> transcricoes = transcricaoRepository.findBySessaoId(sessaoId);
        log.info("Processando {} parte(s) de transcrição para a sessão: {}", transcricoes.size(), sessaoId);

        // Formata cada parte individualmente com o Gemini e junta os resultados
        StringBuilder notaFinal = new StringBuilder();
        for (int i = 0; i < transcricoes.size(); i++) {
            String parteTexto = transcricoes.get(i).getConteudo();
            log.info("Enviando parte {}/{} para o Gemini ({} caracteres)...", i + 1, transcricoes.size(), parteTexto.length());

            String prompt = """
                    Você é um assistente técnico encarregado de organizar transcrições de aulas de programação.
                    Sua missão é pegar a transcrição bruta da aula abaixo e formatá-la usando Markdown (para ficar legível),
                    MAS PRESERVANDO 100% DAS PALAVRAS, DA EXPLICAÇÃO E DA LINHA DE RACIOCÍNIO DA PROFESSORA.

                    Regras obrigatórias:
                    1. NÃO resuma a aula. Não corte ideias. Mantenha o exato ponto de vista e a voz da professora.
                    2. Divida o texto gigante em parágrafos fluidos.
                    3. Identifique trechos que são linhas de código ou comandos e coloque-os dentro de blocos de código markdown.
                    4. Apenas remova gaguejos, vícios de linguagem ("ééé", "tipo assim") ou repetições de palavras soltas que a ferramenta de áudio pegou errado.

                    Transcrição bruta da aula:
                    """ + parteTexto;

            GeminiDTORequest geminiRequest = GeminiDTORequest.builder()
                    .contents(List.of(
                            GeminiContent.builder()
                                    .parts(List.of(
                                            GeminiPart.builder().text(prompt).build()
                                    ))
                                    .build()
                    ))
                    .build();

            try {
                GeminiDTOResponse geminiResponse = geminiClient.gerarConteudo(geminiApiKey, geminiRequest);
                notaFinal.append(geminiResponse.extrairTexto());
                if (i < transcricoes.size() - 1) {
                    notaFinal.append("\n\n---\n\n"); // separador entre partes
                }
            } catch (Exception e) {
                log.error("Erro ao chamar o Gemini na parte {}/{}. Erro: {}", i + 1, transcricoes.size(), e.getMessage());
                throw e;
            }
        }

        // Salva a nota final (junção de todas as partes formatadas)
        Nota nota = Nota.builder()
                .conteudo(notaFinal.toString())
                .criadaEm(LocalDateTime.now())
                .sessao(sessao)
                .build();
        log.info("Nota completa gerada e salva para a sessão: {}", sessaoId);
        return notaMapper.paraDTOResponse(notaRepository.save(nota));
    }

    public NotaDTOResponse buscarNotaPorSessao(String sessaoId) {
        Nota nota = notaRepository.findBySessaoId(sessaoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Nota não encontrada para a sessão: " + sessaoId));
        return notaMapper.paraDTOResponse(nota);
    }

}

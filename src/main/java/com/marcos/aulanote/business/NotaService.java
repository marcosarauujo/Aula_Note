package com.marcos.aulanote.business;

import com.marcos.aulanote.business.dto.out.NotaDTOResponse;
import com.marcos.aulanote.business.mapper.NotaMapper;
import com.marcos.aulanote.infrastructure.entity.Nota;
import com.marcos.aulanote.infrastructure.entity.Sessao;
import com.marcos.aulanote.infrastructure.entity.Transcricao;
import com.marcos.aulanote.infrastructure.exception.ResourceNotFoundException;
import com.marcos.aulanote.infrastructure.repository.NotaRepository;
import com.marcos.aulanote.infrastructure.repository.SessaoRepository;
import com.marcos.aulanote.infrastructure.repository.TranscricaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotaService {
    private final NotaRepository notaRepository;
    private final TranscricaoRepository transcricaoRepository;
    private final SessaoRepository sessaoRepository;
    private final NotaMapper notaMapper;


    public NotaDTOResponse gerarNota(String sessaoId) {
        Sessao sessao = sessaoRepository.findById(sessaoId).orElseThrow(() -> new ResourceNotFoundException(
                "Sessão não encontrada com o id: " + sessaoId)
        );
        // 1 e 2: Pega todos os pedaços da aula e junta (separados por espaço)
        String transcricaoCompleta = transcricaoRepository.findBySessaoId(sessaoId)
                .stream()
                .map(Transcricao::getConteudo)
                .collect(Collectors.joining(" "));

        // 3: Cria a nota e salva
        Nota nota = Nota.builder()
                .conteudo(transcricaoCompleta)
                .criadaEm(LocalDateTime.now())
                .sessao(sessao)
                .build();
        log.info("Nota gerada com sucesso para a sessão: {}", sessaoId);
        return notaMapper.paraDTOResponse(notaRepository.save(nota));

    }
    public NotaDTOResponse buscarNotaPorSessao(String sessaoId) {
        Nota nota = notaRepository.findBySessaoId(sessaoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Nota não encontrada para a sessão: " + sessaoId));
        return notaMapper.paraDTOResponse(nota);
    }

}

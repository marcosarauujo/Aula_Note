package com.marcos.aulanote.business;

import com.marcos.aulanote.business.dto.in.TranscricaoDTORequest;
import com.marcos.aulanote.business.dto.out.TranscricaoDTOResponse;
import com.marcos.aulanote.business.mapper.TranscricaoMapper;
import com.marcos.aulanote.infrastructure.entity.Sessao;
import com.marcos.aulanote.infrastructure.entity.Transcricao;
import com.marcos.aulanote.infrastructure.exception.ResourceNotFoundException;
import com.marcos.aulanote.infrastructure.repository.SessaoRepository;
import com.marcos.aulanote.infrastructure.repository.TranscricaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j


public class TranscricaoService {
    private final TranscricaoRepository transcricaoRepository;
    private final TranscricaoMapper transcricaoMapper;
    private final SessaoRepository sessaoRepository;

    public TranscricaoDTOResponse salvarTranscricao(TranscricaoDTORequest dtoRequest, String sessaoId) {
        Sessao sessao = sessaoRepository.findById(sessaoId).orElseThrow(() -> new ResourceNotFoundException(
                "Sessão não encontrada com o id: " + sessaoId)
        );

        Transcricao transcricao = transcricaoMapper.paraEntity(dtoRequest);
        transcricao.setCriadaEm(LocalDateTime.now());
        transcricao.setSessao(sessao);

        log.info("Salvando transcrição para sessão: {} ", sessaoId);

        return transcricaoMapper.paraDTOResponse(transcricaoRepository.save(transcricao));
    }
    public List<TranscricaoDTOResponse> bucasPorSessao(String sessaoId){
        return transcricaoRepository.findBySessaoId(sessaoId)
                .stream()
                .map(transcricaoMapper::paraDTOResponse)
                .toList();
    }
}

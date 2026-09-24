package com.marcos.aulanote.business;

import com.marcos.aulanote.business.dto.in.SessaoDTORequest;
import com.marcos.aulanote.business.dto.out.SessaoDTOResponse;
import com.marcos.aulanote.business.mapper.SessaoMapper;
import com.marcos.aulanote.infrastructure.entity.Sessao;
import com.marcos.aulanote.infrastructure.enums.StatusSessaoEnum;
import com.marcos.aulanote.infrastructure.exception.ResourceNotFoundException;
import com.marcos.aulanote.infrastructure.repository.SessaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessaoService {
    private final SessaoRepository sessaoRepository;
    private final SessaoMapper sessaoMapper;

    public SessaoDTOResponse iniciarSessao(SessaoDTORequest dtoRequest) {
        Sessao sessao = sessaoMapper.paraEntity(dtoRequest);
        sessao.setIniciadaEm(LocalDateTime.now());
        sessao.setStatusSessaoEnum(StatusSessaoEnum.GRAVANDO);

        return sessaoMapper.paraDTOResponse(sessaoRepository.save(sessao)
        );

    }

    public SessaoDTOResponse pausarSessao(String id) {
        Sessao sessao = sessaoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Sessão nao encontrada com o id " + id)
        );
        sessao.setStatusSessaoEnum(StatusSessaoEnum.PAUSADA);
        return sessaoMapper.paraDTOResponse(sessaoRepository.save(sessao)
        );
    }

    public SessaoDTOResponse finalizarSessao(String id) {
        Sessao sessao = sessaoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Sessão não encontrada com o id: " + id)
        );
        sessao.setStatusSessaoEnum(StatusSessaoEnum.FINALIZADA);
        sessao.setFinalizadaEm(LocalDateTime.now());
        return sessaoMapper.paraDTOResponse(sessaoRepository.save(sessao)
        );
    }

    public SessaoDTOResponse buscarSessaoPorId(String id) {
        Sessao sessao = sessaoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Sessão não encontrada com o id: " + id)
        );
        return sessaoMapper.paraDTOResponse(sessao);
    }
}

package com.marcos.aulanote.business.mapper;

import com.marcos.aulanote.business.dto.in.SessaoDTORequest;
import com.marcos.aulanote.business.dto.out.SessaoDTOResponse;
import com.marcos.aulanote.infrastructure.entity.Sessao;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface SessaoMapper {

    SessaoDTOResponse paraDTOResponse(Sessao sessao);

    Sessao paraEntity(SessaoDTORequest dtoRequest);
}

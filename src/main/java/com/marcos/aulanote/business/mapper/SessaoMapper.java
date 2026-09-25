package com.marcos.aulanote.business.mapper;

import com.marcos.aulanote.business.dto.in.SessaoDTORequest;
import com.marcos.aulanote.business.dto.out.SessaoDTOResponse;
import com.marcos.aulanote.infrastructure.entity.Sessao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface SessaoMapper {

    SessaoDTOResponse paraDTOResponse(Sessao sessao);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "iniciadaEm", ignore = true)
    @Mapping(target = "finalizadaEm", ignore = true)
    @Mapping(target = "statusSessaoEnum", ignore = true)
    Sessao paraEntity(SessaoDTORequest dtoRequest);
}

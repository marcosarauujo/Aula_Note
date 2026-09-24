package com.marcos.aulanote.business.mapper;

import com.marcos.aulanote.business.dto.in.TranscricaoDTORequest;
import com.marcos.aulanote.business.dto.out.TranscricaoDTOResponse;
import com.marcos.aulanote.infrastructure.entity.Transcricao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TranscricaoMapper {

    TranscricaoDTOResponse paraDTOResponse(Transcricao transcricao);
    @Mapping(target = "sessao", ignore = true)
    @Mapping(target = "criadaEm", ignore = true)
    Transcricao paraEntity(TranscricaoDTORequest dtoRequest);
}

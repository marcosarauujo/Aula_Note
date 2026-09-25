package com.marcos.aulanote.business.mapper;

import com.marcos.aulanote.business.dto.out.TranscricaoDTOResponse;
import com.marcos.aulanote.infrastructure.entity.Transcricao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TranscricaoMapper {

    @Mapping(source = "sessao.id", target = "sessaoId")
    TranscricaoDTOResponse paraDTOResponse(Transcricao transcricao);


}

package com.marcos.aulanote.business.mapper;

import com.marcos.aulanote.business.dto.out.TranscricaoDTOResponse;
import com.marcos.aulanote.infrastructure.entity.Transcricao;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TranscricaoMapper {

    TranscricaoDTOResponse paraDTOResponse(Transcricao transcricao);


}

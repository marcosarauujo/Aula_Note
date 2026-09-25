package com.marcos.aulanote.business.mapper;

import com.marcos.aulanote.business.dto.out.NotaDTOResponse;
import com.marcos.aulanote.infrastructure.entity.Nota;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface NotaMapper {

    @Mapping(source = "sessao.id", target = "sessaoId")
    NotaDTOResponse paraDTOResponse(Nota nota);
}

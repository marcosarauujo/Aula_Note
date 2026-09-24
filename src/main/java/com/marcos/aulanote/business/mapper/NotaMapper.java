package com.marcos.aulanote.business.mapper;

import com.marcos.aulanote.business.dto.out.NotaDTOResponse;
import com.marcos.aulanote.infrastructure.entity.Nota;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface NotaMapper {
    NotaDTOResponse paraDTOResponse(Nota nota);
}

package com.marcos.aulanote.business.dto.out;

import com.marcos.aulanote.infrastructure.enums.StatusSessaoEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessaoDTOResponse {
    private String id;
    private String titulo;
    private LocalDateTime iniciadaEm;
    private LocalDateTime finalizadaEm;
    private StatusSessaoEnum statusSessaoEnum;
}

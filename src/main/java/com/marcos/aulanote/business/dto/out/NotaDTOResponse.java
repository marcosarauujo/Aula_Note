package com.marcos.aulanote.business.dto.out;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotaDTOResponse {
    private String id;
    private String conteudo;
    private LocalDateTime criadaEm;
    private String sessaoId;

}

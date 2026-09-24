package com.marcos.aulanote.business.dto.in;

import com.marcos.aulanote.infrastructure.enums.StatusSessaoEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessaoDTORequest {
    @NotBlank(message = "O título da sessão é obrigatório")
    private StatusSessaoEnum statusSessaoEnum;
}

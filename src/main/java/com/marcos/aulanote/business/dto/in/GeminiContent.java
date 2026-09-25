package com.marcos.aulanote.business.dto.in;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeminiContent {
    private List<GeminiPart> parts;
}

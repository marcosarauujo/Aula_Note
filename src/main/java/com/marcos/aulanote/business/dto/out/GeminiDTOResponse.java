package com.marcos.aulanote.business.dto.out;

import com.marcos.aulanote.business.dto.in.GeminiPart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeminiDTOResponse {
    private List<GeminiCandidate> candidates;

    public String extrairTexto() {
        if (candidates != null && !candidates.isEmpty()) {
            List<GeminiPart> parts = candidates.get(0).getContent().getParts();
            if (parts != null && !parts.isEmpty()) {
                return parts.get(0).getText();
            }
        }
        return "";
    }
}

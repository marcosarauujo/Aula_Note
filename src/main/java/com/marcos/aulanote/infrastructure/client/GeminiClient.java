package com.marcos.aulanote.infrastructure.client;

import com.marcos.aulanote.business.dto.in.GeminiDTORequest;
import com.marcos.aulanote.business.dto.out.GeminiDTOResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gemini-client", url = "${gemini.api.url}")
public interface GeminiClient {

    @PostMapping("/v1beta/models/gemini-2.0-flash:generateContent")
    GeminiDTOResponse gerarConteudo(
            @RequestParam("key") String apiKey,
            @RequestBody GeminiDTORequest request
    );
}

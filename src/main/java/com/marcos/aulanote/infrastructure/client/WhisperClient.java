package com.marcos.aulanote.infrastructure.client;

import com.marcos.aulanote.business.dto.out.WhisperDTOResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class WhisperClient {
    private final RestClient whisperRestClient;

    public String transcrever(byte[] audioBytes, String nomeArquivo) {
        log.info("Enviando áudio para a OpenAI Whisper...");

        // Empacota os bytes do áudio como um arquivo
        ByteArrayResource audioResource = new ByteArrayResource(audioBytes) {
            @Override
            public String getFilename() {
                return nomeArquivo;
            }
        };

        // Monta o corpo multipart (como um formulário com arquivo)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", audioResource);
        body.add("model", "whisper-1");
        body.add("language", "pt");

        // Faz a chamada e retorna só o texto
        WhisperDTOResponse response = whisperRestClient.post()
                .uri("/audio/transcriptions")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(WhisperDTOResponse.class);
        log.info("Transcrição recebida com sucesso!");

        return response != null ? response.getText() : "";
    }


}

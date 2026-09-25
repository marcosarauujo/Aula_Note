package com.marcos.aulanote.controller;

import com.marcos.aulanote.business.TranscricaoService;
import com.marcos.aulanote.business.dto.out.TranscricaoDTOResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessoes/{sessaoId}/transcricoes")
@Slf4j
public class TranscricaoController {
    private final TranscricaoService transcricaoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TranscricaoDTOResponse> salvarTranscricao(
            @PathVariable String sessaoId,
            @RequestParam("audio") MultipartFile arquivoAudio) throws IOException {

        log.info("Recebendo áudio para a sessão ID: {}", sessaoId);

        TranscricaoDTOResponse response = transcricaoService.salvarTranscricao(
                arquivoAudio.getBytes(),
                arquivoAudio.getOriginalFilename(),
                sessaoId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TranscricaoDTOResponse>> buscarPorSessao(@PathVariable String sessaoId) {
        log.info("Buscando transcrições da sessão ID: {}", sessaoId);

        return ResponseEntity.ok(transcricaoService.bucasrorSessao(sessaoId));

    }
}


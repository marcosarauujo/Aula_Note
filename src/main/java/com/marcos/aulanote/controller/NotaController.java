package com.marcos.aulanote.controller;

import com.marcos.aulanote.business.NotaService;
import com.marcos.aulanote.business.dto.out.NotaDTOResponse;
import com.marcos.aulanote.business.dto.out.SessaoDTOResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("//api/sessoes/{sessaoId}/notas")
@Slf4j
public class NotaController {
    private final NotaService notaService;

    @PostMapping
    public ResponseEntity<NotaDTOResponse> gerarNota(@PathVariable String sessaoId) {
        log.info("Recebendo requisição para gerar nota da sessão ID: {}", sessaoId);

        return ResponseEntity.status(HttpStatus.CREATED).body(notaService.gerarNota(sessaoId));
    }

    @GetMapping
    public ResponseEntity<NotaDTOResponse> buscarNota(@PathVariable String sessaoId) {
        log.info("Buscando nota da sessão ID: {}", sessaoId);

        return ResponseEntity.ok(notaService.buscarNotaPorSessao(sessaoId));
    }
}

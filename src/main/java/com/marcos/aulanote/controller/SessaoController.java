package com.marcos.aulanote.controller;

import com.marcos.aulanote.business.SessaoService;
import com.marcos.aulanote.business.dto.in.SessaoDTORequest;
import com.marcos.aulanote.business.dto.out.SessaoDTOResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessoes")
@Slf4j
public class SessaoController {

    private final SessaoService sessaoService;

    @PostMapping
    public ResponseEntity<SessaoDTOResponse> iniciarSessao(@RequestBody @Valid SessaoDTORequest dtoRequest) {
        log.info("Recebendo requisição para iniciar sessão: {}", dtoRequest.getTitulo());

        return ResponseEntity.status(HttpStatus.CREATED).body(sessaoService.iniciarSessao(dtoRequest)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessaoDTOResponse> buscarSessao(@PathVariable String id) {
        log.info("Recebendo requisição para buscar sessão ID: {}", id);

        return ResponseEntity.ok(sessaoService.buscarSessaoPorId(id));
    }

    @PutMapping("/{id}/pausar")
    public ResponseEntity<SessaoDTOResponse> pausarSessao(@PathVariable String id) {
        log.info("Recebendo requisição para pausar sessão ID: {}", id);

        return ResponseEntity.ok(sessaoService.pausarSessao(id));
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<SessaoDTOResponse> finalizarSessao(@PathVariable String id) {
        log.info("Recebendo requisição para finalizar sessão ID: {}", id);

        return ResponseEntity.ok(sessaoService.finalizarSessao(id));
    }
}

package com.marcos.aulanote.infrastructure.config;

import com.marcos.aulanote.business.TranscricaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.nio.ByteBuffer;

@Component
@RequiredArgsConstructor
@Slf4j
public class AudioWebSocketHandler extends BinaryWebSocketHandler {

    private final TranscricaoService transcricaoService;

    // Quando a extensão se conecta com sucesso
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Nova conexão WebSocket estabelecida. ID da Sessão WebSocket: {}", session.getId());
    }

    // Quando um pedaço de áudio chega pelo túnel
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        log.info("Recebido um pedaço de áudio em formato binário");
        try {
            // Pegamos o ID da Sessão da Aula via URL (ex: ws://localhost:8080/ws/audio?sessaoId=123)
            String query = session.getUri() != null ? session.getUri().getQuery() : "";
            String sessaoId = extrairSessaoIdDaQuery(query);
            if (sessaoId == null || sessaoId.isEmpty()) {
                log.error("Sessão ID não informado na conexão WebSocket!");
                return;
            }
            // Pega os bytes do áudio
            ByteBuffer byteBuffer = message.getPayload();
            byte[] audioBytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(audioBytes);
            log.info("Enviando {} bytes para o Whisper...", audioBytes.length);
            // Chama nosso Service que já está prontinho!
            transcricaoService.salvarTranscricao(audioBytes, "audio_chunk.webm", sessaoId);
        } catch (Exception e) {
            log.error("Erro ao processar áudio do WebSocket", e);
        }
    }

    // Quando a extensão fecha a conexão
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Conexão WebSocket fechada. ID: {}", session.getId());
    }

    // Método auxiliar para pegar o sessaoId da URL
    private String extrairSessaoIdDaQuery(String query) {
        if (query != null && query.contains("sessaoId=")) {
            return query.split("sessaoId=")[1].split("&")[0];
        }
        return null;
    }
}

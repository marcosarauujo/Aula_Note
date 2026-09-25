package com.marcos.aulanote.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final AudioWebSocketHandler audioWebSocketHandler;

    public WebSocketConfig(AudioWebSocketHandler audioWebSocketHandler) {
        this.audioWebSocketHandler = audioWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Abre a rota /ws/audio para receber conexões
        registry.addHandler(audioWebSocketHandler, "/ws/audio")
                .setAllowedOrigins("*"); // Permite que a extensão do Chrome consiga conectar
    }

}


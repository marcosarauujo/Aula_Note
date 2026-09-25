package com.marcos.aulanote.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${openai.api.key}")
    private String openAiKey;
    @Value("${openai.api.url}")
    private String openAiUrl;

    @Bean
    public RestClient whisperRestClient() {
        return RestClient.builder()
                .baseUrl(openAiUrl)
                .defaultHeader("Authorization", "Bearer " + openAiKey)
                .build();
    }
}

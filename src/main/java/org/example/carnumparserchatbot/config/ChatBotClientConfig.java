package org.example.carnumparserchatbot.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;


@Configuration
public class ChatBotClientConfig {

    // из application.yml подгружается токен бота по ключу telegram.bot.token
    @Value("${telegram.bot.token}")
    private String botToken;

    @Bean
    public RestTemplate telegramRestTemplate() {
        return new RestTemplateBuilder()
                .rootUri("https://api.telegram.org/bot" + botToken)
                // ставим таймауты, чтобы избежать черезмерного ожидания, если коннект не состоялся
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
    }
}
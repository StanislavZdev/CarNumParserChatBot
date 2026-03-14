package org.example.carnumparserchatbot.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ChatBotClientConfig {

    // из application.yml подгружается токен бота по ключу telegram.bot.token
    @Value("${telegram.bot.token}")
    private String botToken;

    @Bean
    public WebClient telegramWebClient() {
          return WebClient.builder()
                  .baseUrl("https://api.telegram.org/bot" + botToken)
                  .build();
    }
}

package org.example.carnumparserchatbot.service.client;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor

public class TelegramClient {

    private final RestTemplate telegramRestTemplate;

    public void sendText(String chatId, String text) {
        try {
            telegramRestTemplate.postForObject("/sendMessage",
                    Map.of("chat_id", chatId, "text", text), Void.class);

        } catch (RestClientException e) {
            log.error("Error sending text to telegram", e);
        }
    }
}

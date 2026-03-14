package org.example.carnumparserchatbot.controller;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.example.carnumparserchatbot.service.ChatBotService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;


@RestController
@RequiredArgsConstructor
public class ChatBotWebhookController {

    private final ChatBotService chatBotService;

    // телеграм будет постить json на этот путь
    @PostMapping(value = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> onUpdate(@RequestBody JsonNode update) {
        chatBotService.handleUpdate(update)
                .doOnError(error -> log.error("Ошибка обработки обновления", error))
                .subscibe();

        return ResponseEntity.ok().build();
    }
}

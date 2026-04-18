package org.example.carnumparserchatbot.controller;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.carnumparserchatbot.dto.TelegramDTOUpdate;
import org.example.carnumparserchatbot.service.ChatBotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
@Slf4j

public class ChatBotWebhookController {

    private final ChatBotService chatBotService;

    // телеграм будет постить json на этот путь
    @PostMapping("${telegram.bot.webhook-path}")
    public ResponseEntity<Void> onUpdate(@RequestBody TelegramDTOUpdate update) {

       chatBotService.handleMessage(update);
        return ResponseEntity.ok().build();
    }
}
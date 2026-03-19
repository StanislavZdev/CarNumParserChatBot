package org.example.carnumparserchatbot.service;


import lombok.RequiredArgsConstructor;
import org.example.carnumparserchatbot.entity.CarNumParserEntity;
import org.example.carnumparserchatbot.repository.CarNumParserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class ChatBotCommands {

    private final WebClient telegramWebClient;
    private final CarNumParserRepository carNumParserRepository;

    public Mono<Void> sendMessage(String chatId, String text) {
        return telegramWebClient.post()
                .uri("/sendMessage")
                .bodyValue(Map.of(
                        "chat_id", chatId,
                        "text", text
                ))
                .retrieve()
                .bodyToMono(Void.class)
                .then();
    }

    public Mono<Void> sendList(String chatId) {
        List<CarNumParserEntity> list = carNumParserRepository.findAllByChatId(chatId);
        if (list.isEmpty()) {
            return sendMessage(chatId, "📭 В этом чате пока нет сохранённых номеров.");
        }
        StringBuilder stringBuilder = new StringBuilder("📭 Сохраненные номера: \n");
        for (CarNumParserEntity entity : list) {
            stringBuilder
                    .append(entity.getId()).append(" ").append(entity.getNumber())
                    .append(" ").append(entity.getNameSender()).append("\n");
        }
        return sendMessage(chatId, stringBuilder.toString());
    }

    public Mono<Void> clearNumbers(String chatId) {
        carNumParserRepository.deleteAllByChatId(chatId);
        return sendMessage(chatId, "🗑️ Все номера удалены.");
    }
}

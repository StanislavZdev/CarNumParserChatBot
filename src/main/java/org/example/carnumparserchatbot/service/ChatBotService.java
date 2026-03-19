package org.example.carnumparserchatbot.service;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.carnumparserchatbot.entity.CarNumParserEntity;
import org.example.carnumparserchatbot.repository.CarNumParserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Scanner;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatBotService {

    private final WebClient telegramWebClient;
    private final CarNumParserRepository carNumParserRepository;

    private final SearchNumber searchNumber;
    private final ChatBotCommands chatBotCommands;

    public Mono<Void> handleUpdate(JsonNode update) {

        JsonNode message = update.path("message");
        // игнорируем все виды обновлений, кроме message
        if (message.isMissingNode()) {
            return Mono.empty();
        }
        // парсим id чата
        String chatId = message.path("chat").path("id").asText();
        // в asText создаем пустую строку, чтобы не ловить исключение т.к. (api тг в "message" также отправляет изображения и войсы)
        String text = message.path("text").asText("");
        // парсим имя отправителя
        String nameSender = message.path("from").path("first_name").asText();

        // команды из api тг
        if (text.startsWith("/list")) {
            return chatBotCommands.sendList(chatId);
        }
        if (text.endsWith("/clear")) {
            return chatBotCommands.clearNumbers(chatId);
        }
        // флаг валидного номера в сообщении
        boolean found = false;

        // логика поиска номеров в сообщении
        Scanner scan = new Scanner(text);

        while (scan.hasNext()) {
            String number = searchNumber.numParser(scan.next());
/*
          !!! нужно проверять, есть ли уже этот номер, если есть - выводить сообщения пользователю
 */
            if (!number.isBlank()) {
                found = true;
                // если есть номер, создаем сущность для бд
                CarNumParserEntity carNumParserEntity = CarNumParserEntity.builder()
                        .chatId(chatId)
                        .number(number)
                        .nameSender(nameSender)
                        .build();
                // асинхронно сохраняем в бд
                Mono.fromCallable(() -> carNumParserRepository.save(carNumParserEntity))
                        .subscribeOn(Schedulers.boundedElastic())
                        .subscribe(
                                savedEntity -> {
                                    log.info("Entity was saved: {}", savedEntity);
                                },
                                error -> {
                                    log.error("Error saved entity: {}", error.getMessage(), error);
                                }
                        );
            }
        }
        if (found) return chatBotCommands.sendMessage(chatId, "✅ Номер(а) сохранен(ы).");
        else return Mono.empty();
    }
}
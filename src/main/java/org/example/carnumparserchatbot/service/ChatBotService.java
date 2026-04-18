package org.example.carnumparserchatbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.carnumparserchatbot.dto.TelegramDTOUpdate;
import org.example.carnumparserchatbot.entity.CarNumParserEntity;
import org.example.carnumparserchatbot.repository.CarNumParserRepository;
import org.example.carnumparserchatbot.service.client.TelegramClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class ChatBotService {

    private final TelegramClient telegramClient;
    private final CarNumParserRepository carNumParserRepository;
    private final SearchNumber searchNumber;
    private final ChatBotCommands chatBotCommands;

    @Transactional
    public void handleMessage(TelegramDTOUpdate dto) {
        if(dto.message() == null) return;

        String chatId = dto.message().chat().id();
        String text = dto.message().text();
        String nameSender = dto.message().from().firstName();

        if(text == null) return;

        String inputMessage = text.trim().toLowerCase();
        if(inputMessage.equals("/list") || inputMessage.equals("/clear")) {
            chatBotCommands.commandHandler(inputMessage, chatId);
            return;
        }

        List<String> numbers = searchNumber.numParser(text);
        if(numbers.isEmpty()) return;

        List<CarNumParserEntity> toSave = numbers.stream()
                .map(number -> CarNumParserEntity.builder()
                        .chatId(chatId)
                        .number(number)
                        .nameSender(nameSender)
                        .build())
                        .toList();

        carNumParserRepository.saveAll(toSave);
        telegramClient.sendText(chatId, "✅ Номер(а) сохранен(ы).");
    }
}

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
import java.util.Set;
import java.util.stream.Collectors;

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
        if (dto.message() == null) return;

        String chatId = dto.message().chat().id();
        String text = dto.message().text();
        String nameSender = dto.message().from().firstName();

        if (text == null) return;

        String inputMessage = text.trim().toLowerCase();
        if (inputMessage.equals("/list") || inputMessage.equals("/clear")) {
            chatBotCommands.commandHandler(inputMessage, chatId);
            return;
        }

        List<String> foundNumbers = searchNumber.numParser(text);
        if (foundNumbers.isEmpty()) return;

        Set<String> allNumInBase = carNumParserRepository.findAllByChatId(chatId).stream()
                .map(CarNumParserEntity::getNumber)
                .collect(Collectors.toSet());

        List<CarNumParserEntity> toSave = foundNumbers.stream()
                .peek(number -> {
                    if (allNumInBase.contains(number)) telegramClient.sendText(chatId, number + " уже был сохранен 📭");
                })
                .filter(number -> !allNumInBase.contains(number))
                .map(number -> CarNumParserEntity.builder()
                        .chatId(chatId)
                        .number(number)
                        .nameSender(nameSender)
                        .build())
                .toList();

        if (toSave.isEmpty()) return;

        carNumParserRepository.saveAll(toSave);
        telegramClient.sendText(chatId, "✅ Номер(а) сохранен(ы).");
    }
}

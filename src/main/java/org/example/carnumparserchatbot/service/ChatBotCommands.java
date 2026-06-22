package org.example.carnumparserchatbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.carnumparserchatbot.entity.CarNumParserEntity;
import org.example.carnumparserchatbot.repository.CarNumParserRepository;
import org.example.carnumparserchatbot.service.client.TelegramClient;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j

public class ChatBotCommands {

           private final TelegramClient  telegramClient;
           private final CarNumParserRepository carNumParserRepository;

           public void commandHandler(String inputMessage, String chatId) {
               switch (inputMessage) {
                   case "/list" -> sendList(chatId);
                   case "/clear" -> clear(chatId);
                   default -> System.out.println("Invalid command: " + inputMessage);
               }
           }

           private void sendList(String chatId) {
               List<CarNumParserEntity> list =  carNumParserRepository.findAllByChatId(chatId);
               if(list.isEmpty()){
                   telegramClient.sendText(chatId, "📭 В этом чате пока нет сохранённых номеров.");
                 return;
               }

               int autoIncrement = 1;
               var listCarNumBuilder = new StringBuilder();
               listCarNumBuilder.append("📭 Сохранённые номера:\n");

               for (CarNumParserEntity carNumParserEntity : list) {
                   listCarNumBuilder.append("\n")
                           .append(autoIncrement)
                           .append("  ")
                           .append(carNumParserEntity.getNumber()).append("  ")
                           .append(carNumParserEntity.getNameSender());

                   autoIncrement++;
               }

               String allListCarNum =  listCarNumBuilder.toString();
               telegramClient.sendText(chatId, allListCarNum);
           }
           private void clear (String chatId) {
               carNumParserRepository.deleteAllByChatId(chatId);
               telegramClient.sendText(chatId, "🗑️ Все номера удалены.");
           }
}
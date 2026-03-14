package org.example.carnumparserchatbot.service;





/*
...............
нужно парсить имя и фамилию пользователя, который прислал номер и сохранять его в бд
..............
 */

import lombok.RequiredArgsConstructor;
import org.example.carnumparserchatbot.config.ChatBotClientConfig;
import org.example.carnumparserchatbot.repository.CarNumParserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor

public class ChatBotService {

    private final WebClient telegramWebClient;
    private final CarNumParserRepository carNumParserRepository;


}

package org.example.carnumparserchatbot.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record TelegramDTOUpdate(@JsonProperty("update_id") Long updateId, Message message) {
    public record Message(@JsonProperty("message_id") Long messageId, Chat chat, From from, String text) {
        public record Chat(@JsonProperty("id") String id) {}
        public record From(@JsonProperty("first_name") String firstName) {}
    }
}
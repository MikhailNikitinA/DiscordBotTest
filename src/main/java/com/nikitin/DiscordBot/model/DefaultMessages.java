package com.nikitin.DiscordBot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultMessages {
    private List<Message> messages;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Message {
        private String command;
        private List<String> values;
    }
}

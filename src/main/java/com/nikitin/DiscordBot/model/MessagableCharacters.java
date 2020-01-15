package com.nikitin.DiscordBot.model;

import lombok.Data;

import java.util.List;

@Data
public class MessagableCharacters {

    private String name;
    private List<messagableCharacter> characters;

    @Data
    public static class messagableCharacter {
        private String name;
        private List<String> messages;
    }
}

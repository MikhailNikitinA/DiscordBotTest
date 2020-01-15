package com.nikitin.DiscordBot.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum GameMappings {
    WC3("WC3", "wc3_messages.json"),
    GOTHIC("GOTHIC", "gothic_messages.json"),
    SILENCE("SILENCE", "");

    private String name;
    private String filePath;

    public static GameMappings getByName(String name){
        return Arrays.stream(values())
                .filter(gm -> gm.getName().equalsIgnoreCase(name))
                .findAny()
                .orElse(getDefault());
    }

    public static GameMappings getDefault() {
        return SILENCE;
    }
}

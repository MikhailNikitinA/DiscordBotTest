package com.nikitin.DiscordBot.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GameMappings {
    WC3("WC3", "wc3_messages.json", true),
    GOTHIC("GOTHIC", "gothic_messages.json", true),
    SC2("SC2", "sc2.json", true),
    JAPAN("JAPAN", "japanese_april_fool.json", true),
    DIALOGFLOW("DIALOGFLOW-BETA", "", false);

    private String name;
    private String filePath;
    private boolean hasResourceFile;

}

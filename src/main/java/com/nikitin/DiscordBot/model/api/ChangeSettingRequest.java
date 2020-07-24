package com.nikitin.DiscordBot.model.api;

import lombok.Data;

@Data
public class ChangeSettingRequest {
    private String guildID;
    private String alias;
    private boolean value;
}

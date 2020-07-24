package com.nikitin.DiscordBot.model;

import com.nikitin.DiscordBot.utils.Constants;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//ToDo: remove synchronized
@Data
public class GuildSettings {
    private Long guildID;
    private Map<String, Boolean> settings;

    public GuildSettings(Long guildID) {
        this.settings = new ConcurrentHashMap<>();
        this.guildID = guildID;
    }

    public synchronized boolean getSetting(String setting) {
        settings.putIfAbsent(setting, Constants.DEFAULT_FEATURE_ENABLED_VALUE);
        return settings.get(setting);
    }

    public synchronized void setSetting(String key, boolean value) {
        settings.put(key, value);
    }
}

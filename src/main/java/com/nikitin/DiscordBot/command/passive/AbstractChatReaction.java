package com.nikitin.DiscordBot.command.passive;

import com.nikitin.DiscordBot.service.GuildSettingsService;

public abstract class AbstractChatReaction implements ChatReaction {
    protected final GuildSettingsService guildSettingsService;

    public AbstractChatReaction(GuildSettingsService guildSettingsService) {
        this.guildSettingsService = guildSettingsService;
        guildSettingsService.registerSetting(this.getAlias());
    }

}

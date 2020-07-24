package com.nikitin.DiscordBot.strategy.responses;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ResponseStrategy {
    String getAlias();
    String getResponseMessage(MessageReceivedEvent event);
}

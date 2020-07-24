package com.nikitin.DiscordBot.command.passive;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ChatReaction {
    String getAlias();
    void onMessageReceived(MessageReceivedEvent event);
}

package com.nikitin.DiscordBot.command.passive;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ChatReaction {
    void onMessageReceived(MessageReceivedEvent event);
}

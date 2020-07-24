package com.nikitin.DiscordBot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public interface GenericCommand {
    List<String> getCommandAliases();

    void onMessageReceived(MessageReceivedEvent event);
}

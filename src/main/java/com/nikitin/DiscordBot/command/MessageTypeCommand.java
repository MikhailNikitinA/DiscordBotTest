package com.nikitin.DiscordBot.command;


import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface MessageTypeCommand {
    MessageType getMessageType();
    void onMessageReceived(MessageReceivedEvent event);
}

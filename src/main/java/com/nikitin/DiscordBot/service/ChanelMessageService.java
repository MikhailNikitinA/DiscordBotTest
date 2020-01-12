package com.nikitin.DiscordBot.service;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class ChanelMessageService {

    public void sendMessageToChanel(String message, MessageChannel channel) {
        channel.sendMessage(message).queue();
    }

    public void sendMessageToChanel(String message, MessageChannel channel, Consumer<Message> callback) {
        channel.sendMessage(message).queue(callback);
    }

    public void delete(Message message) {
        message.delete().queue();
    }
}

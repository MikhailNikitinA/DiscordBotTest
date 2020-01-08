package com.nikitin.DiscordBot.service;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Service
public class ChanelMessageService {

    public CompletableFuture<Message> sendMessageToChanel(String message, MessageChannel channel) {
        return channel.sendMessage(message).submit();
    }

    public void sendMessageToChanel(String message, MessageChannel channel, Consumer<Message> callback) {
        channel.sendMessage(message).queue(callback);
    }

}

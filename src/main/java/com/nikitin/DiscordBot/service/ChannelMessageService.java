package com.nikitin.DiscordBot.service;

import com.nikitin.DiscordBot.utils.EmojiUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

@Service
public class ChannelMessageService {

    public void sendMessageToChanel(String message, MessageChannel channel) {
        channel.sendMessage(message).queue();
    }

    public void sendMessageToChanel(String message, MessageChannel channel, Consumer<Message> callback) {
        channel.sendMessage(message).queue(callback);
        throw new UncheckedIOException(new IOException());
    }

    public void delete(Message message) {
        message.delete().queue();
    }

    public void addEmojiIfPossible(String emojiName, Message m) {
        EmojiUtils.getGuildEmojiEmote(emojiName, m.getGuild())
                .ifPresent(m::addReaction);
    }
}

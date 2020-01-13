package com.nikitin.DiscordBot.command.passive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikitin.DiscordBot.model.DefaultMessages;
import com.nikitin.DiscordBot.service.ChanelMessageService;
import com.nikitin.DiscordBot.utils.EmojiUtils;
import com.nikitin.DiscordBot.utils.RandomUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class DefaultCommandChatReaction implements ChatReaction {
    private final DefaultMessages resourceFile;
    private final ChanelMessageService chanelMessageService;

    @Autowired
    public DefaultCommandChatReaction(ChanelMessageService chanelMessageService) throws IOException {
        this.resourceFile = new ObjectMapper().readValue(new ClassPathResource("messages.json").getInputStream(), DefaultMessages.class);
        this.chanelMessageService = chanelMessageService;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        List<String> responses = resourceFile.getMessages().stream()
                .filter(m -> event.getMessage().getContentDisplay().equalsIgnoreCase(m.getCommand()))
                .map(DefaultMessages.Message::getValues)
                .findFirst()
                .orElse(Collections.emptyList());

        if (!responses.isEmpty()) {
            String response = EmojiUtils.handleEmojis(RandomUtils.getRandomValue(responses), event.getGuild());
            chanelMessageService.sendMessageToChanel(response, event.getChannel());
        }
    }
}

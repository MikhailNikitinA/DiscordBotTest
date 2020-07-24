package com.nikitin.DiscordBot.command.passive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikitin.DiscordBot.model.DefaultMessages;
import com.nikitin.DiscordBot.service.ChannelMessageService;
import com.nikitin.DiscordBot.service.GuildSettingsService;
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
public class DefaultCommandChatReaction extends AbstractChatReaction implements ChatReaction {
    private final DefaultMessages resourceFile;
    private final ChannelMessageService channelMessageService;

    @Autowired
    public DefaultCommandChatReaction(GuildSettingsService guildSettingsService, ChannelMessageService channelMessageService) throws IOException {
        super(guildSettingsService);
        this.resourceFile = new ObjectMapper().readValue(new ClassPathResource("messages.json").getInputStream(), DefaultMessages.class);
        this.channelMessageService = channelMessageService;
    }

    @Override
    public String getAlias() {
        return "обычные-текстовые-команды";
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
            channelMessageService.sendMessageToChanel(response, event.getChannel());
        }
    }
}

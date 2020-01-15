package com.nikitin.DiscordBot.command.passive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikitin.DiscordBot.model.MessagableCharacters;
import com.nikitin.DiscordBot.service.ChanelMessageService;
import com.nikitin.DiscordBot.utils.RandomUtils;
import com.nikitin.DiscordBot.utils.enums.GameMappings;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.MessageFormat;

import static com.nikitin.DiscordBot.utils.enums.GameMappings.SILENCE;

@Component
public class BotAsMentionReaction implements ChatReaction {

    private MessagableCharacters characterMessages;

    private ChanelMessageService chanelMessageService;

    @Autowired
    public BotAsMentionReaction(ChanelMessageService chanelMessageService) {
        this.chanelMessageService = chanelMessageService;
        changeSources(GameMappings.WC3);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        boolean botMentioned = event.getMessage().getMentionedMembers().contains(event.getGuild().getSelfMember());
        boolean silenceMode = SILENCE.getName().equalsIgnoreCase(characterMessages.getName());

        if (!botMentioned || silenceMode) {
            return;
        }

        String message = MessageFormat.format(getRandomMessage(), event.getAuthor().getAsMention());
        chanelMessageService.sendMessageToChanel(message, event.getChannel());
    }

    private String getRandomMessage() {
        return RandomUtils.getRandomValue(RandomUtils.getRandomValue(characterMessages.getCharacters()).getMessages());
    }

    public void changeSources(GameMappings gameMappings) {
        if (SILENCE.equals(gameMappings)) {
            MessagableCharacters messagableCharacters = new MessagableCharacters();
            messagableCharacters.setName(SILENCE.getName());
            this.characterMessages = messagableCharacters;
        } else {
            this.characterMessages = loadResource(gameMappings.getFilePath());
        }
    }

    private MessagableCharacters loadResource(String path) {
        try {
            return new ObjectMapper().readValue(new ClassPathResource(path).getInputStream(), MessagableCharacters.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

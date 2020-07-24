package com.nikitin.DiscordBot.strategy.responses;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikitin.DiscordBot.model.MessagableCharacters;
import com.nikitin.DiscordBot.utils.RandomUtils;
import com.nikitin.DiscordBot.utils.enums.GameMappings;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.UncheckedIOException;

public class BundleFileStrategy implements ResponseStrategy {

    private GameMappings gameMappings;
    private MessagableCharacters characterMessages;

    public BundleFileStrategy(GameMappings gameMappings) {
        this.gameMappings = gameMappings;
        this.characterMessages = loadResource(gameMappings.getFilePath());
    }

    @Override
    public String getAlias() {
        return gameMappings.getName();
    }

    @Override
    public String getResponseMessage(MessageReceivedEvent event) {
        return RandomUtils.getRandomValue(RandomUtils.getRandomValue(characterMessages.getCharacters()).getMessages());
    }

    private MessagableCharacters loadResource(String path) {
        try {
            return new ObjectMapper().readValue(new ClassPathResource(path).getInputStream(), MessagableCharacters.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

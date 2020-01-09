package com.nikitin.DiscordBot.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikitin.DiscordBot.model.DefaultMessages;
import com.nikitin.DiscordBot.service.ChanelMessageService;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class DefaultTextCommand implements ChatCommand {


    private DefaultMessages resourceFile;
    private ChanelMessageService chanelMessageService;

    @Autowired
    public DefaultTextCommand(ChanelMessageService chanelMessageService) throws IOException {
        this.resourceFile = new ObjectMapper().readValue(new ClassPathResource("messages.json").getInputStream(), DefaultMessages.class);
        this.chanelMessageService = chanelMessageService;
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String message = event.getMessage().getContentDisplay();
        if (StringUtils.isEmpty(message) || !message.trim().startsWith("!")) {
            return;
        }

        List<String> responses = resourceFile.getMessages().stream()
                .filter(m -> message.equalsIgnoreCase(m.getCommand()))
                .map(DefaultMessages.Message::getValues)
                .findFirst()
                .orElse(Collections.emptyList());

        if (!responses.isEmpty()) {
            String response = responses.get(new Random().nextInt(responses.size()));
            chanelMessageService.sendMessageToChanel(response, event.getChannel());
        }

    }
}

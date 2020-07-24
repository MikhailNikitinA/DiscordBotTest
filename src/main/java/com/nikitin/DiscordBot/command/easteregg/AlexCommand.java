package com.nikitin.DiscordBot.command.easteregg;

import com.nikitin.DiscordBot.command.PrivateCommand;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AlexCommand implements PrivateCommand {
    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("Александр");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        log.info("Author: {}, message: {}", event.getAuthor().getName(), event.getMessage().getContentDisplay());
        if (ChannelType.PRIVATE.equals(event.getChannelType()) && "Александр".equals(event.getMessage().getContentDisplay())) {
            event.getChannel().sendMessage("Поздравляю, ты прошел мини-квест :) Напиши Михаилу Никитину в личку в дискорд с кодом 'я прошел пасхалку 123'").queue();
        }
    }
}

package com.nikitin.DiscordBot.config;

import com.nikitin.DiscordBot.command.active.ChatCommand;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class GenericListener extends ListenerAdapter {

    private Set<ChatCommand> commands;
    private ChatCommand defaultTextCommand;


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            //Hide bot spam
            return;
        }

        try {
            if (event.isFromType(ChannelType.TEXT) && MessageType.DEFAULT.equals(event.getMessage().getType())) {

                getCommand(event.getMessage().getContentDisplay())
                        .orElse(defaultTextCommand)
                        .onMessageReceived(event);

            }
        } catch (Throwable e) {
            System.out.println(e);
        }
    }

    private Optional<ChatCommand> getCommand(String textInput) {
        if (StringUtils.isEmpty(textInput)) {
            return Optional.empty();
        }
        String upperInput = textInput.toUpperCase();
        return commands.stream()
                .filter(c -> containsInput(c, upperInput))
                .findAny();
    }

    private boolean containsInput(ChatCommand command, String upperInput) {
        return command.getCommandAliases()
                .stream()
                .map(String::toUpperCase)
                .anyMatch(upperInput::startsWith);
    }
}

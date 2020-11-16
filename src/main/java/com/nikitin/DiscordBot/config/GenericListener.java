package com.nikitin.DiscordBot.config;

import com.nikitin.DiscordBot.command.GenericCommand;
import com.nikitin.DiscordBot.command.PrivateCommand;
import com.nikitin.DiscordBot.command.active.ChatCommand;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class GenericListener extends ListenerAdapter {

    private Set<ChatCommand> chatCommands;
    private Set<PrivateCommand> privateCommands;
    private ChatCommand defaultTextCommand;

    static {
        log.info("CLASSINIT");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        log.debug(String.valueOf(event));
        if (event.getAuthor().isBot()) {
            //Hide bot spam
            return;
        }

        try {
            MessageType type = event.getMessage().getType();
            if (event.isFromType(ChannelType.TEXT) && (MessageType.DEFAULT.equals(type) || MessageType.GUILD_MEMBER_JOIN.equals(type))) {

                getCommand(event.getMessage().getContentDisplay(), chatCommands)
                        .orElse(defaultTextCommand)
                        .onMessageReceived(event);



            }
            if (ChannelType.PRIVATE.equals(event.getChannelType())) {
                getCommand(event.getMessage().getContentDisplay(), privateCommands)
                        .ifPresent(c -> c.onMessageReceived(event));
            }
        } catch (Throwable e) {
            log.error("Error: ", e);
        }
    }

    private <T extends GenericCommand> Optional<T> getCommand(String textInput, Collection<T> commands) {
        if (StringUtils.isEmpty(textInput)) {
            return Optional.empty();
        }
        String upperInput = textInput.toUpperCase();
        return commands.stream()
                .filter(c -> containsInput(c, upperInput))
                .findAny();
    }

    private boolean containsInput(GenericCommand command, String upperInput) {
        return command.getCommandAliases()
                .stream()
                .map(String::toUpperCase)
                .anyMatch(upperInput::startsWith);
    }
}

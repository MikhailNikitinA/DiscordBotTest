package com.nikitin.DiscordBot.command.active;

import com.nikitin.DiscordBot.command.passive.BotAsMentionReaction;
import com.nikitin.DiscordBot.model.CommandWithParameters;
import com.nikitin.DiscordBot.utils.CommandWithParametersParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
//ToDo: delete?
public class RepliesChatCommand implements ChatCommand {

    private BotAsMentionReaction botAsMentionReaction;

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("!ответы", "!replies");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        CommandWithParameters cwp = CommandWithParametersParser.parseCommand(event.getMessage().getContentDisplay());

//        String gameName = cwp.getParameters().getOrDefault(Constants.Parameters.SWITCH_TO, GameMappings.getDefault().getName());
        log.info("onMessageReceived; name: {}, author: {}", event.getAuthor().getName(), event.getMessage());
//        botAsMentionReaction.changeSources(GameMappings.getByName(gameName));
    }
}

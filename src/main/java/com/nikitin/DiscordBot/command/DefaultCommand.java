package com.nikitin.DiscordBot.command;

import com.nikitin.DiscordBot.command.active.ChatCommand;
import com.nikitin.DiscordBot.command.passive.ChatReaction;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Primary
@AllArgsConstructor
public class DefaultCommand implements ChatCommand {

    private List<ChatReaction> chatReactions;

    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        chatReactions.forEach(chatReaction -> chatReaction.onMessageReceived(event));
    }


}

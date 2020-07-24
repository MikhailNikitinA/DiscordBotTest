package com.nikitin.DiscordBot.command;

import com.nikitin.DiscordBot.command.active.ChatCommand;
import com.nikitin.DiscordBot.command.passive.ChatReaction;
import com.nikitin.DiscordBot.service.GuildSettingsService;
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
    private GuildSettingsService guildSettingsService;

    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        chatReactions
                .stream()
                .filter(cr -> guildSettingsService.isEnabled(event.getGuild().getIdLong(), cr.getAlias()))
                .forEach(chatReaction -> chatReaction.onMessageReceived(event));
    }


}

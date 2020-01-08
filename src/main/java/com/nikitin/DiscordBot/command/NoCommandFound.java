package com.nikitin.DiscordBot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Component
public class NoCommandFound implements ChatCommand {

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

        String format = String.format("Unknown command: [%s][%s] %#s: %s%n",
                event.getGuild().getName(),
                event.getChannel().getName(),
                event.getAuthor(),
                message);

//        System.out.println(format);
    }
}

package com.nikitin.DiscordBot.command.passive;

import com.nikitin.DiscordBot.utils.RandomUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class RandomEmojiChatReaction implements ChatReaction {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        if (StringUtils.isEmpty(message) || !message.trim().startsWith("!")) {
            addRandomEmoji(event.getMessage(), event.getGuild());
        }
    }

    private void addRandomEmoji(Message message, Guild guild) {
        if (guild == null || guild.getEmotes().isEmpty() || message == null || RandomUtils.nextInt(150) != 42) {
            return;
        }
        message.addReaction(RandomUtils.getRandomValue(guild.getEmotes())).complete();
    }
}

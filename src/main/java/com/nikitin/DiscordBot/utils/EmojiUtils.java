package com.nikitin.DiscordBot.utils;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Optional;

public class EmojiUtils {

    public static Optional<String> getGuildEmoji(String emojiText, Guild guild) {
        return guild.getEmotesByName(emojiText.trim().substring(1, emojiText.trim().length()-1), true)
                .stream()
                .findFirst()
                .map(Emote::getAsMention);
    }
}

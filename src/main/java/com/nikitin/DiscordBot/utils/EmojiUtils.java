package com.nikitin.DiscordBot.utils;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class EmojiUtils {

    public static Optional<String> getGuildEmojiString(String emojiText, Guild guild) {
        Optional<Emote> first = getGuildEmojiEmote(emojiText, guild);
        return first.map(Emote::getAsMention);
    }

    public static Optional<Emote> getGuildEmojiEmote(String emojiText, Guild guild) {
        return guild.getEmotesByName(emojiText.trim().substring(1, emojiText.trim().length() - 1), true)
                    .stream()
                    .findFirst();
    }

    public static String handleEmojis(String response, Guild guild) {
        if (guild == null) {
            return response;
        }

        Pattern pattern = Pattern.compile(":[a-zA-Z0-9]{1,200}:");
        Matcher matcher = pattern.matcher(response);

        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String replacementSmile = response.substring(matcher.start(), matcher.end());
            String replacementEmoji = replaceEmoji(replacementSmile, guild);
            matcher.appendReplacement(stringBuffer, replacementEmoji);
        }
        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }

    public static String replaceEmoji(String replacementSmile, Guild guild) {
        return getGuildEmojiString(replacementSmile, guild)
                .orElse(replacementSmile);
    }
}

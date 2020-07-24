package com.nikitin.DiscordBot.command.passive;

import com.nikitin.DiscordBot.service.ChannelMessageService;
import com.nikitin.DiscordBot.service.GuildSettingsService;
import com.nikitin.DiscordBot.strategy.responses.ResponseStrategy;
import com.nikitin.DiscordBot.utils.RandomUtils;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BotAsMentionReaction extends AbstractChatReaction implements ChatReaction {

    private static final String ANIME_ENDINGS = "японские-окончания-на-чат-реакции";
    private static final List<String> ENDINGS = Arrays.asList("-kun", "-chan", "-sempai", "-san", "-sama", "-dono", "-sensei");

    private final ChannelMessageService channelMessageService;
    private final List<ResponseStrategy> responseStrategies;

    @Autowired
    public BotAsMentionReaction(ChannelMessageService channelMessageService, GuildSettingsService guildSettingsService, @Qualifier("rs") List<ResponseStrategy> responseStrategies) {
        super(guildSettingsService);
        this.channelMessageService = channelMessageService;
        this.responseStrategies = responseStrategies;
        guildSettingsService.registerSetting(ANIME_ENDINGS);
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        boolean botMentioned = event.getMessage().getMentionedMembers().contains(event.getGuild().getSelfMember());

        if (!botMentioned) {
            return;
        }

        Set<String> repliesAliases = guildSettingsService.getOrCreateRepliesOptions(event.getGuild().getIdLong())
                .entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        List<ResponseStrategy> availableStrategies =
                responseStrategies.stream()
                        .filter(rs -> repliesAliases.contains(rs.getAlias()))
                        .collect(Collectors.toList());

        if (availableStrategies.isEmpty()) {
            return;
        }

        String response = RandomUtils.getRandomValue(availableStrategies).getResponseMessage(event);

        if (response != null) {
            boolean animeEndings = Optional
                    .of(event)
                    .map(GenericMessageEvent::getGuild)
                    .map(ISnowflake::getIdLong)
                    .map(guildSettingsService::getSettings)
                    .map(gs -> gs.getSetting(ANIME_ENDINGS))
                    .orElse(false);

            if (animeEndings) {
                response = response.replace("{0}", "{0}" + RandomUtils.getRandomValue(ENDINGS));
            }
            String message = MessageFormat.format(response, event.getAuthor().getAsMention());
            channelMessageService.sendMessageToChanel(message, event.getChannel());
        }
    }

    @Override
    public String getAlias() {
        return "ответ-на-упоминание-бота-в-чате";
    }

}

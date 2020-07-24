package com.nikitin.DiscordBot.command.passive;

import com.nikitin.DiscordBot.service.BadWordsRecognitionService;
import com.nikitin.DiscordBot.service.ChannelMessageService;
import com.nikitin.DiscordBot.service.GuildSettingsService;
import com.nikitin.DiscordBot.service.PorferichRestService;
import com.nikitin.DiscordBot.utils.RandomUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.List;

@Component
public class RandomCommentChatReaction extends AbstractChatReaction implements ChatReaction {

    private final PorferichRestService porferichRestService;
    private final ChannelMessageService channelMessageService;
    private final BadWordsRecognitionService badWordsRecognitionService;

    public RandomCommentChatReaction(GuildSettingsService guildSettingsService, PorferichRestService porferichRestService, ChannelMessageService channelMessageService, BadWordsRecognitionService badWordsRecognitionService) {
        super(guildSettingsService);
        this.porferichRestService = porferichRestService;
        this.channelMessageService = channelMessageService;
        this.badWordsRecognitionService = badWordsRecognitionService;
    }

    @Override
    public String getAlias() {
        return "случайный-ответ-от-нейросетки";
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        MessageChannel channel = event.getChannel();

        if (RandomUtils.nextInt(250) != 42) {
            return;
        }

        List<String> responses = porferichRestService.getResponse(message);

        boolean isNSFW = channel instanceof TextChannel && ((TextChannel) channel).isNSFW();

        responses.stream()
                .filter(s -> !StringUtils.isEmpty(s))
                .filter(s -> isNSFW || !badWordsRecognitionService.hasBadWords(s))
                .findAny()
                .map(s -> MessageFormat.format("{0}: {1} {2}", event.getAuthor().getAsMention(), message, s))
                .ifPresent(m -> channelMessageService.sendMessageToChanel(m, event.getChannel()));
    }
}

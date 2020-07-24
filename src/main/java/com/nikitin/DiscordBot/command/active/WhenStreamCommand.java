package com.nikitin.DiscordBot.command.active;

import com.nikitin.DiscordBot.service.ChannelMessageService;
import com.nikitin.DiscordBot.utils.Constants;
import com.nikitin.DiscordBot.utils.EmojiUtils;
import com.nikitin.DiscordBot.utils.RandomUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
@AllArgsConstructor
@Slf4j
public class WhenStreamCommand implements ChatCommand {

    private ChatCommand defaultTextCommand;
    private ChannelMessageService channelMessageService;

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("!Когдастрим");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        log.info("Message: {}. User: {}", event.getMessage().getContentDisplay(), event.getAuthor().getName());
        Guild guild = event.getGuild();

        if (noAccessToRkaneAnonsChanel(guild)) {
            defaultTextCommand.onMessageReceived(event);
            return;
        }

        GuildChannel anonsChannel = guild.getGuildChannelById(Constants.RKANE_ANONS_CHANNEL_ID);

        if (anonsChannel instanceof TextChannel && event.getMember() != null) {
            TextChannel textAnonsChanel = (TextChannel) anonsChannel;
            Message anonsMessage = textAnonsChanel.getHistory().retrievePast(1).complete().get(0);

            OffsetDateTime lastAnounceTime = anonsMessage.getTimeCreated();
            long daysWithoutStream = DAYS.between(lastAnounceTime, OffsetDateTime.now());
            if (lastAnounceTime.isAfter(OffsetDateTime.now().minusHours(6))) {
                channelMessageService.sendMessageToChanel(event.getMember().getAsMention() + ": " + anonsMessage.getContentDisplay() + " (" + anonsMessage.getJumpUrl() + ")", event.getChannel());
                return;
            } else if (daysWithoutStream >= Constants.STREAM_AWAITING_INTERVAL &&
                    RandomUtils.nextInt(10) == 1) {
                String rKane =
                        guild.getMembersByNickname("RKane", true)
                        .stream()
                        .findAny()
                        .map(Member::getAsMention)
                        .orElse("@RKane");
                String message = EmojiUtils.handleEmojis(MessageFormat.format("Серьезно, {0} уже {1} дней нет стримов :pain:", rKane, daysWithoutStream), event.getGuild());
                channelMessageService.sendMessageToChanel(message, event.getChannel());
                return;
            }


        }

        defaultTextCommand.onMessageReceived(event);
    }

    private boolean noAccessToRkaneAnonsChanel(Guild guild) {
        return guild == null ||
                !Constants.RKANE_GUILD_ID.equals(guild.getIdLong()) ||
                guild.getGuildChannelById(Constants.RKANE_ANONS_CHANNEL_ID) == null;
    }
}

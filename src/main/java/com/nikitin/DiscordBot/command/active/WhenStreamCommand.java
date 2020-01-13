package com.nikitin.DiscordBot.command.active;

import com.nikitin.DiscordBot.service.ChanelMessageService;
import com.nikitin.DiscordBot.utils.Constants;
import com.nikitin.DiscordBot.utils.RandomUtils;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
@AllArgsConstructor
public class WhenStreamCommand implements ChatCommand {

    private ChatCommand defaultTextCommand;
    private ChanelMessageService chanelMessageService;

    private static Long RKANE_GUILD_ID = 475897248325173250L;
    private static Long RKANE_ANONS_CHANNEL_ID = 656072627848478731L;

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("!Когдастрим");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        Guild guild = event.getGuild();

        if (noAccessToRkaneAnonsChanel(guild)) {
            defaultTextCommand.onMessageReceived(event);
            return;
        }

        GuildChannel anonsChannel = guild.getGuildChannelById(RKANE_ANONS_CHANNEL_ID);

        if (anonsChannel instanceof TextChannel && event.getMember() != null) {
            TextChannel textAnonsChanel = (TextChannel) anonsChannel;
            Message anonsMessage = textAnonsChanel.getHistory().retrievePast(1).complete().get(0);

            OffsetDateTime lastAnounceTime = anonsMessage.getTimeCreated();
            long daysWithoutStream = DAYS.between(OffsetDateTime.now(), lastAnounceTime);
            if (lastAnounceTime.isAfter(OffsetDateTime.now().minusHours(6))) {
                chanelMessageService.sendMessageToChanel(event.getMember().getAsMention() + ": " + anonsMessage.getContentDisplay() + " (" + anonsMessage.getJumpUrl() + ")", event.getChannel());
                return;
            } else if (daysWithoutStream >= Constants.STREAM_AWAITING_INTERVAL &&
                    RandomUtils.nextInt(25) == 1) {
                String message = MessageFormat.format("Серьезно, @RKane уже {0} дней нет стримов :pain:", daysWithoutStream);
                chanelMessageService.sendMessageToChanel(message, event.getChannel());
                return;
            }


        }

        defaultTextCommand.onMessageReceived(event);
    }

    private boolean noAccessToRkaneAnonsChanel(Guild guild) {
        return guild == null ||
                !RKANE_GUILD_ID.equals(guild.getIdLong()) ||
                guild.getGuildChannelById(RKANE_ANONS_CHANNEL_ID) == null;
    }
}

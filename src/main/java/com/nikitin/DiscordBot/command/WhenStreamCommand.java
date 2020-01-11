package com.nikitin.DiscordBot.command;

import com.nikitin.DiscordBot.service.ChanelMessageService;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

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

        if (anonsChannel instanceof TextChannel) {
            TextChannel textAnonsChanel = (TextChannel) anonsChannel;
            Message anonsMessage = textAnonsChanel.getHistory().retrievePast(1).complete().get(0);

            if (anonsMessage.getTimeCreated().isAfter(OffsetDateTime.now().minusHours(6))) {
                chanelMessageService.sendMessageToChanel(event.getMember().getAsMention() + ": " + anonsMessage.getJumpUrl(), event.getChannel());
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

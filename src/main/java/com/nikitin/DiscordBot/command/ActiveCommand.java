package com.nikitin.DiscordBot.command;

import com.nikitin.DiscordBot.service.ChanelMessageService;
import com.nikitin.DiscordBot.service.MemberStatisticService;
import com.nikitin.DiscordBot.utils.TimeUtils;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class ActiveCommand implements ChatCommand {

    private static final String TOTAL_USERS_COUNT = "Активных чижей: {0}";
    private static final String TOTAL_USERS_COUNT_NO_BOTS = "Активных чижей (без ботов): {0}";

    private MemberStatisticService memberStatisticService;
    private ChanelMessageService chanelMessageService;

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("!active", "!Active");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        MessageChannel chanel = event.getChannel();

        OffsetDateTime before = TimeUtils.getOffsetBefore();
        OffsetDateTime after = OffsetDateTime.now();

        Map<Long, Member> activeMembers = memberStatisticService.getActiveGuildUsersForPeriod(guild, before, after);
        sendCountStatistics(chanel, activeMembers);

//        activeMembers.values().forEach(this::sendCongratsNotification);
    }


    private void sendCountStatistics(MessageChannel chanel, Map<Long, Member> activeMembers) {
        String message = MessageFormat.format(TOTAL_USERS_COUNT, activeMembers.size());
        chanelMessageService.sendMessageToChanel(message, chanel);

        long count = activeMembers.values().stream().filter(member -> !member.getUser().isBot()).count();
        message = MessageFormat.format(TOTAL_USERS_COUNT_NO_BOTS, count);
        chanelMessageService.sendMessageToChanel(message, chanel);
    }

    private void sendCongratsNotification(Member member) {
        if (!member.getUser().isBot() && member.getDefaultChannel() != null) {
            chanelMessageService.sendMessageToChanel(member.getEffectiveName() + " - ты пережил чистку", member.getDefaultChannel());
        }
    }
}

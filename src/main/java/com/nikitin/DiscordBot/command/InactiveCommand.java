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
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class InactiveCommand implements ChatCommand {
    private static final String ALL_ACTIVE = "Все чижы активны";
    private static final String INACTIVE_TOTAL = "Всего неактивных чижей: {0}";
    private static final String FIRST_10_INACTIVE = "Первые 10 чижей: {0}";
    private static final String INACTIVE_NAMES_WARNING = "Для вывода всех неактивных чижей используйте '!inactive_names'. Предупреждение: печатает всех чижей построчно";
    private MemberStatisticService memberStatisticService;
    private ChanelMessageService chanelMessageService;

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("!Inactive", "!inactive");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        MessageChannel chanel = event.getChannel();

        OffsetDateTime before = TimeUtils.getOffsetBefore();
        OffsetDateTime after = OffsetDateTime.now();

//        Map<Long, Member> activeMembers = memberStatisticService.getActiveGuildUsersForPeriod(guild, before, after);
//        List<Member> inactiveMembers = memberStatisticService.getNonActiveNonBotMembers(guild, activeMembers);
//
//        sendInactiveMembersCount(chanel, inactiveMembers);
    }

    private void sendInactiveMembersCount(MessageChannel chanel, List<Member> inactiveMembers) {
        if (inactiveMembers.isEmpty()) {
            chanelMessageService.sendMessageToChanel(ALL_ACTIVE, chanel);
            return;
        }

        String inactiveTotalMessage = MessageFormat.format(INACTIVE_TOTAL, inactiveMembers.size());
        chanelMessageService.sendMessageToChanel(inactiveTotalMessage, chanel);


        String names = inactiveMembers.stream()
                .map(Member::getEffectiveName)
                .limit(10)
                .collect(Collectors.joining(", "));

        String inactiveChizhNames = MessageFormat.format(FIRST_10_INACTIVE, names);

        chanelMessageService.sendMessageToChanel(inactiveChizhNames, chanel);
        chanelMessageService.sendMessageToChanel(INACTIVE_NAMES_WARNING, chanel);
    }
}

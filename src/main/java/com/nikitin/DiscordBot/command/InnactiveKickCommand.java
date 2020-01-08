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

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class InnactiveKickCommand implements ChatCommand {

    private static final String KICK_REASON_MESSAGE = "Отсутствие активности в дискорде на протяжении {0} месяцев. Мы будем рады приветствовать вас снова в нашем дискорде";
    private static final String SUCCESS_MESSAGE = "Неактивные пользователи были удалены";
    private MemberStatisticService memberStatisticService;
    private ChanelMessageService chanelMessageService;

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("!Inactive_kick", "!inactive_kick");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        MessageChannel chanel = event.getChannel();

        OffsetDateTime before = TimeUtils.getOffsetBefore();
        OffsetDateTime after = OffsetDateTime.now();

        Map<Long, Member> activeMembers = memberStatisticService.getActiveGuildUsersForPeriod(guild, before, after);
        List<Member> inactiveMembers = memberStatisticService.getNonActiveNonBotMembers(guild, activeMembers);

        inactiveMembers.forEach(member -> member.kick(KICK_REASON_MESSAGE).submit());

        chanelMessageService.sendMessageToChanel(SUCCESS_MESSAGE, chanel);
    }
}

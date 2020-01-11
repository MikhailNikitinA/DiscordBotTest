package com.nikitin.DiscordBot.strategy;

import com.nikitin.DiscordBot.model.CommandWithParameters;
import com.nikitin.DiscordBot.model.statistic.GuildChannelsGroupStatistic;
import com.nikitin.DiscordBot.model.statistic.GuildChannelsGroupStatistic.ChanelMemberStatistic;
import com.nikitin.DiscordBot.model.statistic.GuildChannelsGroupStatistic.ChannelStatistic;
import com.nikitin.DiscordBot.service.MemberStatisticService;
import com.nikitin.DiscordBot.utils.Constants;
import com.nikitin.DiscordBot.utils.MemberStatisticTransformer;
import com.nikitin.DiscordBot.utils.TimeUtils;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static com.nikitin.DiscordBot.utils.Constants.Parameters.DAYS;

@Component
@AllArgsConstructor
public class ChannelUserStatisticStrategy implements IUserStatisticStrategy<GuildChannelsGroupStatistic> {
    private MemberStatisticService memberStatisticService;

    @Override
    public String getArgumentType() {
        return Constants.Parameters.SOURCE_CHANNEL;
    }

    @Override
    public GuildChannelsGroupStatistic getStatistic(MessageReceivedEvent event, CommandWithParameters commandWithParameters) {

        Integer daysOffset = TimeUtils.parseDays(commandWithParameters.getParameters().get(DAYS));

        OffsetDateTime startDate = OffsetDateTime.now().minusDays(daysOffset);
        OffsetDateTime endDate = OffsetDateTime.now();

        GuildChannelsGroupStatistic chanelMemberGroupStatistic = new GuildChannelsGroupStatistic();
        ArrayList<ChannelStatistic> channelStatistics = new ArrayList<>();
        HashMap<Long, ChanelMemberStatistic> userStatistics = new HashMap<>();


        for (TextChannel channel : event.getGuild().getTextChannels()) {
            ChannelStatistic cStat = new ChannelStatistic();
            cStat.setChannelName(channel.getName());
            cStat.setTotalMessages(0L);

            boolean success = memberStatisticService
                    .processChanelMessagesForPeriod(channel,
                            m -> gatherStatisticFromMessage(userStatistics, cStat, m),
                            startDate, endDate);

            if (!success) {
                cStat.setErrorMessage("Ошибка доступа");
            }

            channelStatistics.add(cStat);
        }
        chanelMemberGroupStatistic.setChannelStatistics(channelStatistics);
        chanelMemberGroupStatistic.setUserStatistics(userStatistics);
        return chanelMemberGroupStatistic;
    }

    private void gatherStatisticFromMessage(HashMap<Long, ChanelMemberStatistic> userStatistics, ChannelStatistic cStat, Message m) {
        Member guildMember = m.getMember();

        ChanelMemberStatistic member = userStatistics.getOrDefault(guildMember.getIdLong(),
                MemberStatisticTransformer.transform(guildMember));

        member.setMessageCount(member.getMessageCount() + 1);

        ChanelMemberStatistic.LastMessage oldMessage = member.getLastMessage();
        ChanelMemberStatistic.LastMessage newMessage = MemberStatisticTransformer.transform(m);
        if (oldMessage == null || oldMessage.getOffsetDateTime().isBefore(newMessage.getOffsetDateTime())) {
            member.setLastMessage(newMessage);
        }

        userStatistics.put(guildMember.getIdLong(), member);
        cStat.setTotalMessages(cStat.getTotalMessages() + 1);
    }
}

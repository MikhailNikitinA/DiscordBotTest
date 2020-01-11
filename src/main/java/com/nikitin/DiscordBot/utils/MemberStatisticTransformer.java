package com.nikitin.DiscordBot.utils;

import com.nikitin.DiscordBot.model.statistic.GuildChannelsGroupStatistic.ChanelMemberStatistic;
import net.dv8tion.jda.api.entities.Member;


public class MemberStatisticTransformer {

    public static ChanelMemberStatistic transform(Member member, String message) {
        ChanelMemberStatistic statistic = new ChanelMemberStatistic();
        statistic.setId(member.getIdLong());
        statistic.setName(member.getEffectiveName());
        statistic.setMessageCount(0L);
        statistic.setLastMessage(message);
        return statistic;
    }
}

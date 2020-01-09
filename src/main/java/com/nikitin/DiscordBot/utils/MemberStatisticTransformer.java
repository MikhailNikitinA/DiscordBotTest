package com.nikitin.DiscordBot.utils;

import com.nikitin.DiscordBot.model.ChanelStatistic;
import com.nikitin.DiscordBot.model.ChanelStatistic.ChanelMemberStatistic;
import net.dv8tion.jda.api.entities.Member;


public class MemberStatisticTransformer {

    public static ChanelMemberStatistic transform(Member member, String message) {
        ChanelMemberStatistic statistic = new ChanelMemberStatistic();
        statistic.setId(member.getIdLong());
        statistic.setMemberName(member.getEffectiveName());
        statistic.setMessageCount(0L);
        statistic.setLastMessage(message);
        return statistic;
    }
}

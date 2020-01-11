package com.nikitin.DiscordBot.utils;

import com.nikitin.DiscordBot.model.statistic.GuildChannelsGroupStatistic.ChanelMemberStatistic;
import com.nikitin.DiscordBot.model.statistic.GuildChannelsGroupStatistic.ChanelMemberStatistic.LastMessage;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;


public class MemberStatisticTransformer {

    public static ChanelMemberStatistic transform(Member member) {
        ChanelMemberStatistic statistic = new ChanelMemberStatistic();
        statistic.setId(member.getIdLong());
        statistic.setName(member.getEffectiveName());
        statistic.setMessageCount(0L);
        return statistic;
    }

    public static LastMessage transform(Message message) {
        LastMessage lastMessage = new LastMessage();
        lastMessage.setChannelName(message.getChannel().getName());
        lastMessage.setText(message.getContentDisplay());
        lastMessage.setOffsetDateTime(message.getTimeEdited() == null ? message.getTimeCreated() : message.getTimeEdited());
        return lastMessage;
    }
}

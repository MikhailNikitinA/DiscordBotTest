package com.nikitin.DiscordBot.service;

import com.nikitin.DiscordBot.model.ChanelStatistic;
import com.nikitin.DiscordBot.model.ChanelStatistic.ChanelMemberStatistic;
import com.nikitin.DiscordBot.utils.MemberStatisticTransformer;
import com.nikitin.DiscordBot.utils.TimeUtils;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MemberStatisticService {

    private static final int BATCH_SIZE = 90;

    private PermissionCheckService permissionCheckService;


    public List<ChanelStatistic> getActiveGuildUsersForPeriod(Guild guild, OffsetDateTime startDate, OffsetDateTime endDate) {
        Member selfMember = guild.getSelfMember();
        return guild.getTextChannels().stream()
                .map(c -> getActiveChannelUsersForPeriod(c, startDate, endDate, selfMember))
                .collect(Collectors.toList());
    }

    public List<Member> getNonActiveNonBotMembers(Guild guild, Map<Long, Member> activeMembers) {
        Set<Long> memberIds = activeMembers.keySet();
        return guild.getMembers()
                .stream()
                .filter(Objects::nonNull)
                .filter(member -> !memberIds.contains(member.getIdLong()))
                .filter(member -> !member.getUser().isBot())
                .collect(Collectors.toList());
    }

    private ChanelStatistic getActiveChannelUsersForPeriod(MessageChannel channel, OffsetDateTime startDate, OffsetDateTime endDate, Member selfMember) {
        ChanelStatistic chanelStatistic = new ChanelStatistic();
        chanelStatistic.setName(channel.getName());

        List<Message> retrievedHistory;
        long latestMessageIdLong = channel.getLatestMessageIdLong();

        try {
            retrievedHistory = channel.getHistoryBefore(latestMessageIdLong, BATCH_SIZE).complete().getRetrievedHistory();
        } catch (InsufficientPermissionException e) {
            System.out.println("Игнорирую канал " + channel.getName());
            return chanelStatistic;
        }

        boolean messagesWithinTimeRange = true;
        long messageCount = 0;


        while (!retrievedHistory.isEmpty() && messagesWithinTimeRange) {

            for (Message message : retrievedHistory) {
                OffsetDateTime timeCreated = message.getTimeCreated();
                Member member = message.getMember();

                messageCount++;

                if (timeCreated.isAfter(startDate)
                        && timeCreated.isBefore(endDate)
                        && member != null
                        && !StringUtils.isEmpty(message.getId())) {


                    Map<Long, ChanelMemberStatistic> allMembersStatistic = chanelStatistic.getMemberStatistics();

                    ChanelMemberStatistic memberStatistic =
                            allMembersStatistic
                                    .getOrDefault(member.getIdLong(),
                                            MemberStatisticTransformer.transform(member, message.getContentDisplay()));

                    memberStatistic.setMessageCount(memberStatistic.getMessageCount() + 1);
                    allMembersStatistic.put(memberStatistic.getId(), memberStatistic);


                }
            }
            Message lastMessage = retrievedHistory.get(0);
            Message firstMessage = retrievedHistory.get(retrievedHistory.size() - 1);
            messagesWithinTimeRange = TimeUtils.isBetween(lastMessage.getTimeCreated(), startDate, endDate)
                    &&  TimeUtils.isBetween(firstMessage.getTimeCreated(), startDate, endDate);

            latestMessageIdLong = Math.min(firstMessage.getIdLong(), lastMessage.getIdLong());
            retrievedHistory = channel.getHistoryBefore(latestMessageIdLong, BATCH_SIZE).complete().getRetrievedHistory();
        }

        chanelStatistic.setTotalMessages(messageCount);

        return chanelStatistic;
    }
}

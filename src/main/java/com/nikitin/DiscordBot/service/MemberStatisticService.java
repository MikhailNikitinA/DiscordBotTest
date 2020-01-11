package com.nikitin.DiscordBot.service;

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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MemberStatisticService {

    private static final int BATCH_SIZE = 90;

    private PermissionCheckService permissionCheckService;

    public List<Member> getNonActiveNonBotMembers(Guild guild, Map<Long, Member> activeMembers) {
        Set<Long> memberIds = activeMembers.keySet();
        return guild.getMembers()
                .stream()
                .filter(Objects::nonNull)
                .filter(member -> !memberIds.contains(member.getIdLong()))
                .filter(member -> !member.getUser().isBot())
                .collect(Collectors.toList());
    }

    public boolean processChanelMessagesForPeriod(MessageChannel channel, Consumer<Message> messageConsumer, OffsetDateTime startDate, OffsetDateTime endDate) {

        List<Message> retrievedHistory;
        long latestMessageIdLong = channel.getLatestMessageIdLong();

        try {
            retrievedHistory = channel.getHistoryBefore(latestMessageIdLong, BATCH_SIZE).complete().getRetrievedHistory();
        } catch (InsufficientPermissionException e) {
            return false;
        }

        boolean messagesWithinTimeRange = true;

        while (!retrievedHistory.isEmpty() && messagesWithinTimeRange) {

            for (Message message : retrievedHistory) {
                OffsetDateTime timeCreated = message.getTimeCreated();
                Member member = message.getMember();


                if (timeCreated.isAfter(startDate)
                        && timeCreated.isBefore(endDate)
                        && member != null
                        && !StringUtils.isEmpty(message.getId())) {

                    messageConsumer.accept(message);
                }
            }
            Message lastMessage = retrievedHistory.get(0);
            Message firstMessage = retrievedHistory.get(retrievedHistory.size() - 1);

            messagesWithinTimeRange = TimeUtils.isBetween(lastMessage.getTimeCreated(), startDate, endDate)
                    && TimeUtils.isBetween(firstMessage.getTimeCreated(), startDate, endDate);

            latestMessageIdLong = Math.min(firstMessage.getIdLong(), lastMessage.getIdLong());
            retrievedHistory = channel.getHistoryBefore(latestMessageIdLong, BATCH_SIZE).complete().getRetrievedHistory();
        }

        return true;
    }
}

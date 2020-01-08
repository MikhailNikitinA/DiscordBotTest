package com.nikitin.DiscordBot.service;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MemberStatisticService {

    public Map<Long, Member> getActiveGuildUsersForPeriod(Guild guild, OffsetDateTime startDate, OffsetDateTime endDate) {
        return guild.getTextChannels().stream()
                .map(c -> getActiveChannelUsersForPeriod(c, startDate, endDate))
                .map(Map::values)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(m -> !StringUtils.isEmpty(m.getId()))
                .collect(Collectors.toMap(Member::getIdLong, Function.identity(), (k, v) -> k));
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

    private Map<Long, Member> getActiveChannelUsersForPeriod(MessageChannel channel, OffsetDateTime startDate, OffsetDateTime endDate) {
        Map<Long, Member> members = new HashMap<>();

        for (Message message : channel.getIterableHistory()) {
            OffsetDateTime timeCreated = message.getTimeCreated();

            if (timeCreated.isAfter(startDate) && timeCreated.isBefore(endDate)) {
                Optional.ofNullable(message.getMember())
                        .filter(m -> !StringUtils.isEmpty(m.getId()))
                        .ifPresent(m -> members.putIfAbsent(m.getIdLong(), m));
            }
        }
        return members;
    }
}

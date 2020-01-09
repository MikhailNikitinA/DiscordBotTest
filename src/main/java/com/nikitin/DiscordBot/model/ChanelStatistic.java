package com.nikitin.DiscordBot.model;

import lombok.Data;

import java.util.Map;

@Data
public class ChanelStatistic {
    private String name;
    private Long totalMessages;
    private Map<Long, ChanelMemberStatistic> memberStatistics;

    @Data
    public static class ChanelMemberStatistic {
        private Long id;
        private String memberName;
        private Long messageCount;
        private String lastMessage;
    }
}

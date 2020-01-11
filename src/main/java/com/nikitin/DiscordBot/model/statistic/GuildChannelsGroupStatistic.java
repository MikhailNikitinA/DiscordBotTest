package com.nikitin.DiscordBot.model.statistic;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Data
public class GuildChannelsGroupStatistic {
    private List<ChannelStatistic> channelStatistics;
    private Map<Long, ChanelMemberStatistic> userStatistics;

    @Data
    public static class ChanelMemberStatistic {
        private Long id;
        private String name;
        private Long messageCount;
        private LastMessage lastMessage;

        @Data
        public static class LastMessage {
            private String text;
            private String channelName;
            private OffsetDateTime offsetDateTime;

            @Override
            public String toString() {
                return "Канал: '" + channelName +  "'. Cообщение='" + text + "'";
            }
        }

    }

    @Data
    public static class ChannelStatistic {
        private String channelName;
        private String errorMessage;
        private Long totalMessages;
    }
}

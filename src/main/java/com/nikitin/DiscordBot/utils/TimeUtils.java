package com.nikitin.DiscordBot.utils;

import java.time.OffsetDateTime;

public class TimeUtils {
    private static final Long MONTHS_INACTIVE = 2L;

    public static OffsetDateTime getOffsetBefore() {
        return OffsetDateTime.now().minusMinutes(MONTHS_INACTIVE);
    }
}

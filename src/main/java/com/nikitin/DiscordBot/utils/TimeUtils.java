package com.nikitin.DiscordBot.utils;

import org.apache.commons.lang3.math.NumberUtils;

import java.time.OffsetDateTime;
import java.util.Optional;

import static com.nikitin.DiscordBot.utils.Constants.Parameters.DAYS;

public class TimeUtils {
    private static final Long MONTHS_INACTIVE = 2L;

    public static OffsetDateTime getOffsetBefore() {
        return OffsetDateTime.now().minusMonths(MONTHS_INACTIVE);
    }

    public static boolean isBetween(OffsetDateTime time, OffsetDateTime start, OffsetDateTime end){
        return time.isAfter(start) && time.isBefore(end);
    }

    public static Integer parseDays(String days){
        return Optional.ofNullable(days)
                .filter(NumberUtils::isDigits)
                .map(Integer::parseInt)
                .filter(d -> d <= Constants.Parameters.DAYS_MAX)
                .orElse(Constants.Parameters.DAYS_DEFAULT);
    }
}

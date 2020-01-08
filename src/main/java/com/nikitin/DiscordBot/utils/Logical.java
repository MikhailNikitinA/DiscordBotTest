package com.nikitin.DiscordBot.utils;

import java.util.Arrays;
import java.util.List;

public class Logical {
    private static List<String> yes = Arrays.asList("Yes", "Y", "Да");

    public static boolean isTrue(String message) {
        if (message == null) {
            return false;
        }
        return yes.stream().map(String::toUpperCase).anyMatch(message.toUpperCase()::equals);
    }
}

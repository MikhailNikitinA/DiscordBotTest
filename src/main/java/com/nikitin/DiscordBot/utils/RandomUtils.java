package com.nikitin.DiscordBot.utils;

import java.util.List;
import java.util.Random;

public class RandomUtils {

    public static <T> T getRandomValue(List<T> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    public static int nextInt(int bound) {
        return new Random().nextInt(bound);
    }
}

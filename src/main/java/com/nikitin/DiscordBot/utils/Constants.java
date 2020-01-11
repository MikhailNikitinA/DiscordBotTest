package com.nikitin.DiscordBot.utils;

public class Constants {
    public static final String PARAMS_SEPARATOR = "=";

    public static class Arguments {
        public static final String ALL = "ALL";
        public static String ACTIVE = "ACTIVE";
        public static String INACTIVE = "INACTIVE";
        public static String LIST = "LIST";
        public static String COUNT = "COUNT";
    }

    public static class Parameters {
        public static final String SOURCE = "SOURCE";
        public static final String SOURCE_CHANNEL = "CHANNEL";
        public static final String SOURCE_GUILD = "GUILD";
        public static String DAYS = "DAYS";
        public static Integer DAYS_DEFAULT = 7;
        public static Integer DAYS_MAX = 40;
        public static final String TOP = "TOP";
        public static Integer TOP_DEFAULT = 5;
    }
}

package com.nikitin.DiscordBot.utils;

public class Constants {
    public static final String PARAMS_SEPARATOR = "=";
    public static Long RKANE_GUILD_ID = 475897248325173250L;
    public static Long RKANE_ANONS_CHANNEL_ID = 656072627848478731L;
    public static final double MIN_SIMILARITY_LEVEL = 0.975D;

    public static class Arguments {
        public static final String ALL = "ALL";
        public static String ACTIVE = "ACTIVE";
        public static String INACTIVE = "INACTIVE";
        public static String LIST = "LIST";
        public static String COUNT = "COUNT";
        public static String HELP = "HELP";
        public static final Integer MAX_SAME_IMAGE_MESSAGES = 10;
        public static final Integer DEFAULT_SAME_IMAGE_MESSAGES = 10;
    }

    public static class Parameters {
        public static final String SOURCE = "SOURCE";
        public static final String SOURCE_CHANNEL = "CHANNEL";
        public static final String SOURCE_GUILD = "GUILD";
        public static final String DAYS = "DAYS";
        public static final Integer DAYS_DEFAULT = 7;
        public static final Integer DAYS_MAX = 40;
        public static final String TOP = "TOP";
        public static final Integer TOP_DEFAULT = 5;
        public static final String SWITCH_TO = "SWITCHTO";
    }

    public static class Emojis {
        public static final String BOYAN = ":emoji_48:";
    }

    public static final Integer STREAM_AWAITING_INTERVAL = 10;
    public static final Boolean DEFAULT_FEATURE_ENABLED_VALUE = true;

}

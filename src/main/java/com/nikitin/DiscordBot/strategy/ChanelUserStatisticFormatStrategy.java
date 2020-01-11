package com.nikitin.DiscordBot.strategy;

import com.nikitin.DiscordBot.model.CommandWithParameters;
import com.nikitin.DiscordBot.model.statistic.GuildChannelsGroupStatistic;
import com.nikitin.DiscordBot.model.statistic.GuildChannelsGroupStatistic.ChanelMemberStatistic;
import com.nikitin.DiscordBot.utils.Constants;
import com.nikitin.DiscordBot.utils.Constants.Parameters;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.nikitin.DiscordBot.utils.Constants.Parameters.DAYS;

@Component
public class ChanelUserStatisticFormatStrategy implements IFormatStrategy<GuildChannelsGroupStatistic> {

    @Override
    public String getStatisticClass() {
        return GuildChannelsGroupStatistic.class.getCanonicalName();
    }

    @Override
    public List<String> formatOutputMessages(GuildChannelsGroupStatistic userGroupStatistics, CommandWithParameters commandWithParameters) {
        List<String> result = new ArrayList<>();
        String tittle = formatTittle(commandWithParameters);
        result.add(tittle);

        if (commandWithParameters.getArguments().contains(Constants.Arguments.COUNT)) {
            String countStatistic = formatCountStatistics(userGroupStatistics);
        }

//        if (commandWithParameters.getArguments().contains(Constants.Arguments.LIST)) {
//            String
//        }

        Long chanelMessagesCount = userGroupStatistics.getChannelStatistics().stream()
                .map(GuildChannelsGroupStatistic.ChannelStatistic::getTotalMessages)
                .filter(Objects::nonNull)
                .reduce(Long::sum).orElse(0L);


        String totalMessagesPattern = "Всего сообщений: {0}";
        String totalMessages = MessageFormat.format(totalMessagesPattern, chanelMessagesCount);
        result.add(totalMessages);

        String topCount = commandWithParameters.getParameters().getOrDefault(Parameters.TOP, String.valueOf(Parameters.TOP_DEFAULT));
        result.add(MessageFormat.format("Топ {0} пользователей (из {1}):", topCount, userGroupStatistics.getUserStatistics().size()));
        userGroupStatistics.getUserStatistics().values()
                .stream()
                .sorted(Comparator.comparingLong(ChanelMemberStatistic::getMessageCount).reversed())
                .limit(Integer.parseInt(topCount))
                .map(this::formatMemberData)
                .forEach(result::add);
        return result;
    }

    private String formatCountStatistics(GuildChannelsGroupStatistic userGroupStatistics) {
        List<GuildChannelsGroupStatistic.ChannelStatistic> channelStatistics = userGroupStatistics.getChannelStatistics();

        return null;
    }

    @NotNull
    private String formatTittle(CommandWithParameters commandWithParameters) {
        String tittlePattern = "Команда {0}, статистика за {1} дней, основываясь на данных из сообщений";

        return MessageFormat.format(tittlePattern,
                commandWithParameters.getCommand(),
                commandWithParameters.getParameters().get(DAYS));
    }

    private String formatMemberData(ChanelMemberStatistic userData) {
        return MessageFormat.format("Пользователь ''{0}'', всего сообщений: {1}. Последнее сообщение: ''{2}''",
                userData.getName(),
                userData.getMessageCount(),
                userData.getLastMessage());
    }

    private String formatChannelStatistic(GuildChannelsGroupStatistic.ChannelStatistic cs) {
        if (!StringUtils.isEmpty(cs.getErrorMessage())) {
            return MessageFormat.format("Ошибка чтения канала {0}: {1}",
                    cs.getChannelName(),
                    cs.getErrorMessage());
        }

        return MessageFormat.format("Канал {0}, всего сообщений: {1}",
                cs.getChannelName(),
                cs.getTotalMessages());
    }

}

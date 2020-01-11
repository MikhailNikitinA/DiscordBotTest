package com.nikitin.DiscordBot.strategy;

import com.nikitin.DiscordBot.model.CommandWithParameters;

import java.util.List;

public interface IFormatStrategy<T> {
    String getStatisticClass();

    List<String> formatOutputMessages(T userGroupStatistics, CommandWithParameters commandWithParameters);

}

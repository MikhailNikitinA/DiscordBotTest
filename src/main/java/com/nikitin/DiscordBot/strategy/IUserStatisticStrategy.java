package com.nikitin.DiscordBot.strategy;

import com.nikitin.DiscordBot.model.CommandWithParameters;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface IUserStatisticStrategy<T> {

    String getArgumentType();

    T getStatistic(MessageReceivedEvent event, CommandWithParameters commandWithParameters);
}

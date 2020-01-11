package com.nikitin.DiscordBot.strategy;

import com.nikitin.DiscordBot.model.CommandWithParameters;
import com.nikitin.DiscordBot.model.statistic.Errorable;
import com.nikitin.DiscordBot.model.statistic.UserGroupStatistics;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Primary
@Component
public class ErrorFormatStrategy implements IFormatStrategy<Errorable> {
    @Override
    public String getStatisticClass() {
        return Errorable.class.getCanonicalName();
    }

    @Override
    public List<String> formatOutputMessages(Errorable userGroupStatistics, CommandWithParameters commandWithParameters) {
        return Collections.singletonList("Error during selecting statistics formatting");
    }

}

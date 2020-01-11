package com.nikitin.DiscordBot.service;

import com.nikitin.DiscordBot.strategy.IFormatStrategy;
import com.nikitin.DiscordBot.strategy.IUserStatisticStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StrategyResolverService {

    private List<IUserStatisticStrategy> userStrategies;
    private List<IFormatStrategy> formatStrategies;
    private IFormatStrategy errorFormatStrategy;

    public Optional<IUserStatisticStrategy> getUserStatisticStrategy(String source) {
        return userStrategies
                .stream()
                .filter(strategy -> strategy.getArgumentType().equalsIgnoreCase(source))
                .findFirst();
    }

    public IFormatStrategy getFormatStrategy(Object statistic) {
        return formatStrategies.stream()
                .filter(s -> s.getStatisticClass().equals(statistic.getClass().getCanonicalName()))
                .findFirst()
                .orElse(errorFormatStrategy);
    }
}

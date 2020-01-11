package com.nikitin.DiscordBot.command;

import com.nikitin.DiscordBot.model.CommandWithParameters;
import com.nikitin.DiscordBot.model.statistic.UserGroupStatistics;
import com.nikitin.DiscordBot.service.ChanelMessageService;
import com.nikitin.DiscordBot.service.StrategyResolverService;
import com.nikitin.DiscordBot.strategy.IFormatStrategy;
import com.nikitin.DiscordBot.strategy.IUserStatisticStrategy;
import com.nikitin.DiscordBot.utils.CommandWithParametersParser;
import com.nikitin.DiscordBot.utils.Constants;
import com.nikitin.DiscordBot.utils.Constants.Arguments;
import com.nikitin.DiscordBot.utils.Constants.Parameters;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nikitin.DiscordBot.utils.Constants.Parameters.*;

@Component
public class UsersCommand implements ChatCommand {

    @Autowired
    private StrategyResolverService resolverService;
    @Autowired
    private ChanelMessageService chanelMessageService;

    private static final List<String> VALID_SOURCES = Arrays.asList(Parameters.SOURCE_CHANNEL, Parameters.SOURCE_GUILD);
    private static final List<String> VALID_USER_TYPES = Arrays.asList(Arguments.ACTIVE, Arguments.INACTIVE, Arguments.ALL);
    private static final List<String> VALID_OPERATION_TYPES = Arrays.asList(Arguments.LIST, Arguments.COUNT);
    private static final String DEFAULT_OPERATION_TYPE = Arguments.COUNT;

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("!Users", "!users");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        CommandWithParameters commandWithParameters = CommandWithParametersParser.parseCommand(message);
        System.out.println("Before validation");

        if (!isValid(event.getChannel(), commandWithParameters)) {
            return;
        }

        Map<String, String> parameters = commandWithParameters.getParameters();
        List<String> arguments = commandWithParameters.getArguments();

        System.out.println("augument");
        augmentDefaultValues(parameters, arguments);

        String infoSource = parameters.get(Parameters.SOURCE);

        System.out.println("gathering stats");
        IUserStatisticStrategy statisticStrategy = resolverService.getUserStatisticStrategy(infoSource).orElseThrow(IllegalArgumentException::new);
        Object statistic = statisticStrategy.getStatistic(event, commandWithParameters);

        System.out.println("gathering format strategy");
        IFormatStrategy formatStrategy = resolverService.getFormatStrategy(statistic);
        List<String> responses = formatStrategy.formatOutputMessages(statistic, commandWithParameters);

        System.out.println("Printing");
        responses.forEach(m -> chanelMessageService.sendMessageToChanel(m, event.getChannel()));
    }

    private void augmentDefaultValues(Map<String, String> parameters, List<String> arguments) {
        parameters.putIfAbsent(DAYS, String.valueOf(DAYS_DEFAULT));
        parameters.putIfAbsent(TOP, String.valueOf(TOP_DEFAULT));

        boolean hasOperationType = arguments.stream().map(String::toUpperCase).anyMatch(VALID_OPERATION_TYPES::contains);
        if (!hasOperationType) {
            arguments.add(DEFAULT_OPERATION_TYPE);
        }
    }

    public String getValidationError(CommandWithParameters commandWithParameters) {
        boolean hasSource = VALID_SOURCES.contains(commandWithParameters.getParameters().get(Parameters.SOURCE).toUpperCase());
        if (!hasSource) {
            return "Нет источника данных по пользователям, добавьте к команде source=X, где X=" + formatArray(VALID_SOURCES);
        }

        boolean hasUserType = commandWithParameters.getArguments()
                .stream()
                .map(String::toUpperCase)
                .anyMatch(VALID_USER_TYPES::contains);
        if (!hasUserType) {
            return "Нет указаного типа пользователей, добавьте к команде один из типов пользователя: " + formatArray(VALID_USER_TYPES);
        }

        return null;
    }

    private String formatArray(List<String> arr) {
        return arr.stream()
                .map(String::toLowerCase)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    private boolean isValid(MessageChannel chanel, CommandWithParameters commandWithParameters) {
        String validationError = getValidationError(commandWithParameters);
        if (validationError != null) {
            String infoMessage = MessageFormat.format("Вид команды: '!users userType{0} " +
                            "source{8}sourceType{1}' " +
                            "days{8}int(Optional, defaultValue: {2}, maxValue: {3}) " +
                            "operationType{4}(Optional, defaultValue: {5}) " +
                            "top{8}int(Optional for operationType: {6}, defaultValue: {7}",
                    formatArray(VALID_USER_TYPES),
                    formatArray(VALID_SOURCES),
                    Parameters.DAYS_DEFAULT,
                    Parameters.DAYS_MAX,
                    formatArray(VALID_OPERATION_TYPES),
                    DEFAULT_OPERATION_TYPE.toLowerCase(),
                    Arguments.LIST.toLowerCase(),
                    Parameters.TOP_DEFAULT,
                    Constants.PARAMS_SEPARATOR);
            chanelMessageService.sendMessageToChanel(validationError, chanel);
            chanelMessageService.sendMessageToChanel(infoMessage, chanel);
            return false;
        }
        return true;
    }
}

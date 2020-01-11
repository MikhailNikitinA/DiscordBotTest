package com.nikitin.DiscordBot.utils;

import com.nikitin.DiscordBot.model.CommandWithParameters;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class CommandWithParametersParser {

    public static CommandWithParameters parseCommand(String str) {
        CommandWithParameters commandWithParameters = new CommandWithParameters();
        Map<String, String> parameters = new HashMap<>();
        List<String> arguments = new ArrayList<>();


        str = str.replaceAll("\\s+", " ").trim();


        String[] split = str.split(" ");

        String command = split[0];

        commandWithParameters.setCommand(command);
        commandWithParameters.setArguments(arguments);
        commandWithParameters.setParameters(parameters);

        if (split.length == 1) {
            return commandWithParameters;
        }

        for (int i = 1; i < split.length; i++) {

            String s = split[i].trim();

            if (s.contains(Constants.PARAMS_SEPARATOR) && !s.endsWith(Constants.PARAMS_SEPARATOR)) {
                String[] keyValue = s.split(Constants.PARAMS_SEPARATOR);
                parameters.put(keyValue[0].toUpperCase().trim(), keyValue[1].toUpperCase().trim());
            } else {
                arguments.add(s.toUpperCase());
            }
        }

        commandWithParameters.setCommand(command);
        commandWithParameters.setArguments(arguments);
        commandWithParameters.setParameters(parameters);

        return commandWithParameters;
    }
}

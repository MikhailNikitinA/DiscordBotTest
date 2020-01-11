package com.nikitin.DiscordBot.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CommandWithParameters {
    private String command;
    private List<String> arguments;
    private Map<String, String> parameters;
}

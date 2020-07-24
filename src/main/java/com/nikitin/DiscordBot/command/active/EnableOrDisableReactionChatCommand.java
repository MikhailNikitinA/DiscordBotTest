package com.nikitin.DiscordBot.command.active;

import com.nikitin.DiscordBot.command.passive.ChatReaction;
import com.nikitin.DiscordBot.model.CommandWithParameters;
import com.nikitin.DiscordBot.service.ChannelMessageService;
import com.nikitin.DiscordBot.service.GuildSettingsService;
import com.nikitin.DiscordBot.utils.CommandWithParametersParser;
import com.nikitin.DiscordBot.utils.Constants;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
//ToDo: delete?
public class EnableOrDisableReactionChatCommand implements ChatCommand {
    private List<ChatReaction> chatReactions;
    private ChannelMessageService channelMessageService;
    private GuildSettingsService guildSettingsService;

    private static final List<String> ENABLE_LIST = Arrays.asList("!Включить", "!Вкл", "!Enable");
    private static final List<String> DISABLE_LIST = Arrays.asList("!Выключить", "!Выкл", "!Disable");

    @Override
    public List<String> getCommandAliases() {
        List<String> commands = new ArrayList<>();
        commands.addAll(ENABLE_LIST);
        commands.addAll(DISABLE_LIST);
        return commands;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        CommandWithParameters cwp = CommandWithParametersParser.parseCommand(event.getMessage().getContentDisplay());
        boolean enable = ENABLE_LIST.stream().anyMatch(cwp.getCommand()::equalsIgnoreCase);

        List<String> arguments = cwp.getArguments().stream().map(String::toLowerCase).collect(Collectors.toList());

        if (arguments.isEmpty() || arguments.stream().anyMatch(Constants.Arguments.HELP::equalsIgnoreCase)) {
            String message = MessageFormat.format("Вид команды: !{0} [args], где [args] = {1}",
                    enable ? ENABLE_LIST.get(0) : DISABLE_LIST.get(0),
                    String.join(", ", getAvailableArguments()));
            channelMessageService.sendMessageToChanel(message, event.getAuthor().openPrivateChannel().complete());
            return;
        }

        if (!guildSettingsService.hasPermissionToChangeSettings(event.getGuild(), event.getMember())) {
            String message = MessageFormat.format("{0} - у тебя нет здесь власти", event.getAuthor().getAsMention());
            channelMessageService.sendMessageToChanel(message, event.getChannel());
            return;
        }


        List<String> settingsToSwitch = getAvailableArguments().stream().filter(arguments::contains).collect(Collectors.toList());
        PrivateChannel privateChannel = event.getAuthor().openPrivateChannel().complete();

        for (String s : settingsToSwitch) {
            guildSettingsService.setSetting(event.getGuild(), event.getMember(), s, enable);
            String message = MessageFormat.format("Настройка {0} для гильдии {1} {2}",
                    s, event.getGuild().getName(),
                    enable ? "включена" : "отключена");
            channelMessageService.sendMessageToChanel(message, privateChannel);
        }
    }

    private List<String> getAvailableArguments() {
        return chatReactions.stream().map(ChatReaction::getAlias).collect(Collectors.toList());
    }
}

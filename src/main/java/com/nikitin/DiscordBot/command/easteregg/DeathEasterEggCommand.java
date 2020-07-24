package com.nikitin.DiscordBot.command.easteregg;

import com.nikitin.DiscordBot.command.PrivateCommand;
import com.nikitin.DiscordBot.model.CommandWithParameters;
import com.nikitin.DiscordBot.service.ChannelMessageService;
import com.nikitin.DiscordBot.utils.CommandWithParametersParser;
import com.nikitin.DiscordBot.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class DeathEasterEggCommand implements PrivateCommand {
    private ChannelMessageService channelMessageService;

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("!конкурс-смертb", "!конкурс-смеpтb", "!конкуpс-смертb", "!конкуpс-смеpтb");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();

        CommandWithParameters cwp = CommandWithParametersParser.parseCommand(event.getMessage().getContentDisplay());

        log.info("Author: {}, message: {}", event.getAuthor().getName(), event.getMessage().getContentDisplay());

        if (cwp.getArguments().stream().anyMatch(Constants.Arguments.HELP::equalsIgnoreCase)) {
            channelMessageService.sendMessageToChanel("Мелодия подскажет номер, мне нужно только имя", channel);
            return;
        }

        try (InputStream inputStream = new ClassPathResource("easteregg/name.jpg").getInputStream()) {
            channelMessageService.sendMessageToChanel("Дай мне имя.", channel);

            channel
                    .sendFile(inputStream, "name.jpg")
                    .complete();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

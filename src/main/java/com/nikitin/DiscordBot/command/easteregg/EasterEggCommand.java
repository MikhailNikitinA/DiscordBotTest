package com.nikitin.DiscordBot.command.easteregg;

import com.nikitin.DiscordBot.command.PrivateCommand;
import com.nikitin.DiscordBot.command.active.ChatCommand;
import com.nikitin.DiscordBot.model.CommandWithParameters;
import com.nikitin.DiscordBot.service.ChannelMessageService;
import com.nikitin.DiscordBot.utils.CommandWithParametersParser;
import com.nikitin.DiscordBot.utils.Constants;
import com.nikitin.DiscordBot.utils.RandomUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.PrivateChannel;
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
public class EasterEggCommand implements PrivateCommand, ChatCommand {

    private ChannelMessageService channelMessageService;
    private static final List<String> hints = Arrays.asList("Возможно тебе нужно сохранить оригинал :)",
            "Картинки и слова не случайны");

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("!пасхалкa", "!пасхалка");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        CommandWithParameters cwp = CommandWithParametersParser.parseCommand(event.getMessage().getContentDisplay());

        log.info("Author: {}, message: {}", event.getAuthor().getName(), event.getMessage().getContentDisplay());

        PrivateChannel channel = event.getAuthor().openPrivateChannel().complete();

        if (cwp.getArguments().stream().anyMatch(Constants.Arguments.HELP::equalsIgnoreCase)) {
            channelMessageService.sendMessageToChanel(RandomUtils.getRandomValue(hints), channel);
            return;
        }

        try (InputStream inputStream = new ClassPathResource("easteregg/open_me.jpg").getInputStream()) {

            String tittleMessage = "Привет, я пасхалка. Поиграй со мной если скучно. Все ответы работают только в чате бота, дабы не спамить в общий чат и не портить прохождение окружающим.";
            channelMessageService.sendMessageToChanel(tittleMessage, channel);

            String helpMessage = "Если ты хочешь подсказку, добавь help, к предыдущей команде. Например: !пасхалкa help";
            channelMessageService.sendMessageToChanel(helpMessage, channel);


            channel
                    .sendFile(inputStream, "open_me.jpg")
                    .complete();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

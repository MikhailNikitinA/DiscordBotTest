package com.nikitin.DiscordBot.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikitin.DiscordBot.model.DefaultMessages;
import com.nikitin.DiscordBot.service.BadWordsRecognitionService;
import com.nikitin.DiscordBot.service.ChanelMessageService;
import com.nikitin.DiscordBot.service.PermissionCheckService;
import com.nikitin.DiscordBot.utils.EmojiUtils;
import com.nikitin.DiscordBot.utils.RandomUtils;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Primary
public class DefaultTextCommand implements ChatCommand {


    private DefaultMessages resourceFile;
    private ChanelMessageService chanelMessageService;
    private BadWordsRecognitionService badWordsRecognitionService;
    private PermissionCheckService permissionCheckService;

    @Autowired
    public DefaultTextCommand(ChanelMessageService chanelMessageService,
                              BadWordsRecognitionService badWordsRecognitionService,
                              PermissionCheckService permissionCheckService) throws IOException {
        this.resourceFile = new ObjectMapper().readValue(new ClassPathResource("messages.json").getInputStream(), DefaultMessages.class);
        this.chanelMessageService = chanelMessageService;
        this.badWordsRecognitionService = badWordsRecognitionService;
        this.permissionCheckService = permissionCheckService;
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String message = event.getMessage().getContentDisplay();

        MessageChannel channel = event.getChannel();
        boolean isNSFW = channel instanceof TextChannel && ((TextChannel) channel).isNSFW();
        if (!isNSFW
                && !StringUtils.isEmpty(message)
                && event.getMember() != null
                && badWordsRecognitionService.hasBadWords(message)) {
            if (!hideBadWords(event.getMessage(), event.getGuild())) {
                chanelMessageService.sendMessageToChanel(event.getMember().getAsMention() + " - я сейчас Хиданку позову", channel);
            }

        }

        if (StringUtils.isEmpty(message) || !message.trim().startsWith("!")) {
            addRandomEmoji(event.getMessage(), event.getGuild());
            return;
        }

        List<String> responses = resourceFile.getMessages().stream()
                .filter(m -> message.equalsIgnoreCase(m.getCommand()))
                .map(DefaultMessages.Message::getValues)
                .findFirst()
                .orElse(Collections.emptyList());

        if (!responses.isEmpty()) {
            String response = handleEmojis(RandomUtils.getRandomValue(responses), event.getGuild());
            chanelMessageService.sendMessageToChanel(response, event.getChannel());
        }

    }

    private boolean hideBadWords(Message message, Guild guild) {
        if (!permissionCheckService.hasMessageManagePermission(guild)) {
            return false;
        }
        String censoredMessage = badWordsRecognitionService.maskBadWords(message.getContentDisplay());
        MessageChannel channel = message.getChannel();
        chanelMessageService.delete(message);
        String m = MessageFormat.format("{0} написал: ''{1}''. {2}",
                Optional.ofNullable(message.getMember())
                        .map(Member::getAsMention)
                        .orElse("какой-то матершинник"),
                censoredMessage,
                badWordsRecognitionService.getBadWarning());
        chanelMessageService.sendMessageToChanel(m, channel);
        //ToDo: to message service
        return true;
    }

    private void addRandomEmoji(Message message, Guild guild) {
        Random random = new Random();
        if (guild == null || message == null || random.nextInt(400) != 42) {
            return;
        }

        List<Emote> guildEmotes = guild.getEmotes();
        if (guildEmotes.isEmpty()) {
            return;
        }

        message.addReaction(guildEmotes.get(random.nextInt(guildEmotes.size()))).complete();
    }

    private String handleEmojis(String response, Guild guild) {
        if (guild == null) {
            return response;
        }

        Pattern pattern = Pattern.compile(":[a-zA-Z0-9]{1,200}:");
        Matcher matcher = pattern.matcher(response);

        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String replacementSmile = response.substring(matcher.start(), matcher.end());
            String replacementEmoji = replaceEmoji(replacementSmile, guild);
            matcher.appendReplacement(stringBuffer, replacementEmoji);
        }
        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }

    private String replaceEmoji(String replacementSmile, Guild guild) {
        return EmojiUtils.getGuildEmoji(replacementSmile, guild)
                .orElse(replacementSmile);
    }

}

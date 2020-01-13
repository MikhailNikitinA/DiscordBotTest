package com.nikitin.DiscordBot.command.passive;

import com.nikitin.DiscordBot.service.BadWordsRecognitionService;
import com.nikitin.DiscordBot.service.ChanelMessageService;
import com.nikitin.DiscordBot.service.PermissionCheckService;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Optional;

@Component
public class NonNSFWBadWordsBanChatReaction implements ChatReaction {

    private BadWordsRecognitionService badWordsRecognitionService;
    private ChanelMessageService chanelMessageService;
    private PermissionCheckService permissionCheckService;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        String message = event.getMessage().getContentDisplay();

        boolean isNSFW = channel instanceof TextChannel && ((TextChannel) channel).isNSFW();
        if (!isNSFW
                && !StringUtils.isEmpty(message)
                && event.getMember() != null
                && badWordsRecognitionService.hasBadWords(message)) {
            if (!hideBadWords(event.getMessage(), event.getGuild())) {
                chanelMessageService.sendMessageToChanel(event.getMember().getAsMention() + " - я сейчас Хиданку позову", channel);
            }

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
        return true;
    }
}

package com.nikitin.DiscordBot.command.passive;

import com.nikitin.DiscordBot.service.BadWordsRecognitionService;
import com.nikitin.DiscordBot.service.ChannelMessageService;
import com.nikitin.DiscordBot.service.GuildSettingsService;
import com.nikitin.DiscordBot.service.PermissionCheckService;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Optional;

@Component
public class NonNSFWBadWordsBanChatReaction extends AbstractChatReaction implements ChatReaction {

    private final BadWordsRecognitionService badWordsRecognitionService;
    private final ChannelMessageService channelMessageService;
    private final PermissionCheckService permissionCheckService;

    @Autowired
    public NonNSFWBadWordsBanChatReaction(GuildSettingsService guildSettingsService, BadWordsRecognitionService badWordsRecognitionService, ChannelMessageService channelMessageService, PermissionCheckService permissionCheckService) {
        super(guildSettingsService);
        this.badWordsRecognitionService = badWordsRecognitionService;
        this.channelMessageService = channelMessageService;
        this.permissionCheckService = permissionCheckService;
    }

    @Override
    public String getAlias() {
        return "фильтр-чата";
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        String message = event.getMessage().getContentDisplay();

        boolean isNSFW = channel instanceof TextChannel && ((TextChannel) channel).isNSFW();
        if (!isNSFW
                && !StringUtils.isEmpty(message)
                && event.getMember() != null
                && !sendByGuildMaster(event)
                && badWordsRecognitionService.hasBadWords(message)) {
            if (!hideBadWords(event.getMessage(), event.getGuild())) {
                channelMessageService.sendMessageToChanel(event.getMember().getAsMention() + " - я сейчас Хиданку позову", channel);
            }
        }
    }

    private boolean sendByGuildMaster(MessageReceivedEvent event){
        if (event.getGuild().getOwner() == null) {
            return false;
        }

        return event.getGuild().getOwner().equals(event.getMember());
    }

    private boolean hideBadWords(Message message, Guild guild) {
        if (!permissionCheckService.hasMessageManagePermission(guild)) {
            return false;
        }
        String censoredMessage = badWordsRecognitionService.maskBadWords(message.getContentDisplay());
        MessageChannel channel = message.getChannel();
        channelMessageService.delete(message);
        String m = MessageFormat.format("{0} написал: ''{1}''. {2}",
                Optional.ofNullable(message.getMember())
                        .map(Member::getAsMention)
                        .orElse("какой-то матершинник"),
                censoredMessage,
                badWordsRecognitionService.getBadWarning());
        channelMessageService.sendMessageToChanel(m, channel);
        return true;
    }
}

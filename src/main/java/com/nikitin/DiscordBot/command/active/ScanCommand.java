package com.nikitin.DiscordBot.command.active;

import com.nikitin.DiscordBot.service.ChannelMessageService;
import com.nikitin.DiscordBot.service.ChannelProcessingService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

//@Component
//@AllArgsConstructor
@Slf4j
public class ScanCommand implements ChatCommand {

    private ChannelProcessingService channelProcessingService;
    private ChannelMessageService channelMessageService;

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("!scan");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        log.info("scan; author = " + event.getAuthor().getName());
        PrivateChannel messageChannel = event.getAuthor().openPrivateChannel().complete();

        if (event.getAuthor().getIdLong() != 639820219249983489L) {
            channelMessageService.sendMessageToChanel("К сожалению, эта команда служит исключительно триггером, доступном только Михаилу Никитину, пожалуйста, не ломайте бота :(", messageChannel);
            return;
        }

        OffsetDateTime endDate = OffsetDateTime.now();
        OffsetDateTime startDate = OffsetDateTime.now().minusMonths(3);


        channelMessageService.sendMessageToChanel("Запускаю сканирование " + event.getMessageId(), messageChannel);

//        Images support
//        List<TextChannel> channels = event.getGuild().getTextChannels();
//        for (TextChannel gch : channels) {
//            channelMessageService.sendMessageToChanel("Сканирую канал " + gch.getName(), messageChannel);
//            if (ChannelType.TEXT.equals(gch.getType())) {
//                channelProcessingService.processChanelMessagesForPeriod(gch, imageService::saveImagesFromMessage, startDate, endDate);
//            }
//
//        }

        channelMessageService.sendMessageToChanel("Сканирование завершено " + event.getMessageId(), messageChannel);

    }


}

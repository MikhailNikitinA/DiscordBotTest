package com.nikitin.DiscordBot.command.passive;

import com.nikitin.DiscordBot.service.ImageService;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SaveImageReaction implements ChatReaction {

    private ImageService imageService;

    @Override
    public String getAlias() {
        return "сохранение-картинок-чата-в-базе";
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        imageService.saveImagesFromMessage(event.getMessage());
    }
}

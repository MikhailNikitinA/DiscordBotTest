package com.nikitin.DiscordBot.strategy.responses;

import com.google.cloud.dialogflow.v2.QueryResult;
import com.nikitin.DiscordBot.service.DialogFlowService;
import com.nikitin.DiscordBot.utils.enums.GameMappings;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DialogFlowStrategy implements ResponseStrategy {

    private final DialogFlowService dialogFlowService;

    @Override
    public String getAlias() {
        return GameMappings.DIALOGFLOW.getName();
    }

    @Override
    public String getResponseMessage(MessageReceivedEvent event) {
        QueryResult queryResult = dialogFlowService.generateResponse(event.getMessage().getContentDisplay(), event.getAuthor().getId());

        return queryResult.getQueryText();
    }
}

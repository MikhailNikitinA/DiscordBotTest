package com.nikitin.DiscordBot.service;

import com.google.cloud.dialogflow.v2.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;

@Service
@Slf4j
public class DialogFlowService {

    private static final String PROJECT_ID = "small-talk-1-wcgkcq";
    private static final String LANGUAGE_CODE = "ru";

    /**
     * Returns the result of detect intent with texts as inputs.
     *
     * @param sessionId Identifier of the DetectIntent session. (discord user id)
     * @return The QueryResult for each input text.
     */
    public QueryResult generateResponse(
            String text,
            String sessionId) {

        // Instantiates a client
        try (SessionsClient sessionsClient = SessionsClient.create()) {
            // Set the session name using the sessionId (UUID) and PROJECT_ID (my-project-id)
            SessionName session = SessionName.of(PROJECT_ID, sessionId);
            log.trace("Session Path: " + session.toString());


            TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(LANGUAGE_CODE);
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

            QueryResult queryResult = response.getQueryResult();
            return queryResult;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

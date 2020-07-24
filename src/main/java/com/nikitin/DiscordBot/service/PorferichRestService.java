package com.nikitin.DiscordBot.service;

import com.nikitin.DiscordBot.model.api.PorferichRequest;
import com.nikitin.DiscordBot.model.api.PorferichResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class PorferichRestService {

    private RestTemplate restTemplate;


    public List<String> getResponse(String s) {
        PorferichRequest request = new PorferichRequest();
        request.setLength(40);
        request.setNum_samples(2);
        request.setPrompt(s);

        ResponseEntity<PorferichResponse> response = restTemplate.postForEntity("https://models.dobro.ai/gpt2/medium/", request, PorferichResponse.class);

        if (HttpStatus.OK.equals(response.getStatusCode()) && response.getBody() != null && response.getBody().getReplies() != null) {
            return response.getBody().getReplies();
        }

        return Collections.emptyList();
    }

}

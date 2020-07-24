package com.nikitin.DiscordBot.model.api;

import lombok.Data;

@Data
public class PorferichRequest {
    private String prompt;
    private int length;
    private int num_samples;
}

package com.nikitin.DiscordBot.model.statistic;

import lombok.Data;

@Data
public class UserStatistic extends Errorable{
    private Long id;
    private String name;
}

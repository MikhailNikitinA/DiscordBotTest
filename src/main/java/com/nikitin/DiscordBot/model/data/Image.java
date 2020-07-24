package com.nikitin.DiscordBot.model.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long guildId;
    private String linkAsMention;
    private String url;
    private String proxyUrl;
    private Long authorID;
    private String radialHash;
}

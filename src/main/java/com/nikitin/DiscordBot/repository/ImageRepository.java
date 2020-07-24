package com.nikitin.DiscordBot.repository;

import com.nikitin.DiscordBot.model.data.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByGuildIdAndUrl(Long guildId, String url);
    Optional<Image> findByGuildIdAndProxyUrl(Long guildId, String proxyUrl);
    Optional<Image> findByGuildIdAndRadialHash(Long guildId, String radialHash);
    List<Image> findByGuildId(Long guildId);
}

package com.nikitin.DiscordBot.service;

import com.nikitin.DiscordBot.model.GuildSettings;
import com.nikitin.DiscordBot.utils.enums.GameMappings;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GuildSettingsService {

    private final Map<Long, GuildSettings> guildSettings;
    private final Map<Long, Map<String, Boolean>> repliesOptions;
    private List<String> chatReactionList;

    public GuildSettingsService() {
        this.guildSettings = new ConcurrentHashMap<>();
        this.repliesOptions = new ConcurrentHashMap<>();
    }

    public boolean isEnabled(Long guildID, String setting) {
        if (guildID == null) {
            return false;
        }

        return getOrCreateGuildSettings(guildID).getSetting(setting);
    }

    public GuildSettings getSettings(Long guildID) {
        return getOrCreateGuildSettings(guildID);
    }

    public void setSetting(Guild guild, Member member, String setting, boolean value) {
        if (!hasPermissionToChangeSettings(guild, member) || StringUtils.isEmpty(setting)) {
            return;
        }

        log.info("setSetting; Member: {}({}). Guild: {}. Setting: {}, Value: {}",
                member.getEffectiveName(),
                member.getId(),
                guild.getName(),
                setting,
                value);

        setSetting(setting, guild.getIdLong(), value);
    }

    public void setSetting(String setting, long guildID, boolean value) {
        getOrCreateGuildSettings(guildID).setSetting(setting, value);
    }

    public boolean hasPermissionToChangeSettings(Guild guild, Member member) {
        return guild != null && member != null && guild.getOwnerIdLong() == member.getIdLong();
    }

    //ToDo: remove synchronized, add locks
    private synchronized GuildSettings getOrCreateGuildSettings(Long guildID) {
        if (guildSettings.get(guildID) == null) {
            GuildSettings gs = new GuildSettings(guildID);
            chatReactionList
                    .stream()
                    .forEach(a -> gs.setSetting(a, true));
            guildSettings.putIfAbsent(guildID, gs);
        }

        return guildSettings.get(guildID);
    }

    public synchronized void setReplyOption(String alias, long guildID, boolean value) {
        getOrCreateRepliesOptions(guildID).put(alias, value);
    }

    public synchronized Map<String, Boolean> getOrCreateRepliesOptions(long guildID) {
        if (!repliesOptions.containsKey(guildID)) {
            ConcurrentMap<String, Boolean> strategies =
                    Arrays.stream(GameMappings.values()).map(GameMappings::getName)
                            .collect(Collectors.toConcurrentMap(Function.identity(), x -> false));
            repliesOptions.putIfAbsent(guildID, strategies);
        }
        return repliesOptions.get(guildID);
    }

    public void registerSetting(String setting) {
        if (this.chatReactionList == null) {
            this.chatReactionList = new ArrayList<>();
        }
        chatReactionList.add(setting);
    }
}

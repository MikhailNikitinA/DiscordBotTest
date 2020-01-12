package com.nikitin.DiscordBot.service;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionCheckService {

    public boolean hasGuildWritePermission(MessageReceivedEvent event) {
        return Optional.ofNullable(event)
                .map(GenericMessageEvent::getGuild)
                .map(Guild::getSelfMember)
                .map(member -> member.hasPermission(Permission.MESSAGE_WRITE))
                .orElse(false);
    }

    public boolean hasMessageManagePermission(Guild guild) {
        return Optional.ofNullable(guild)
                .map(Guild::getSelfMember)
                .map(member -> member.hasPermission(Permission.MESSAGE_MANAGE))
                .orElse(false);
    }

//    public boolean hasHistoryReadPermission(MessageChannel channel) {
//        return channel.get
//    }
}

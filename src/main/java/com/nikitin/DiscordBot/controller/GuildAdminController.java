package com.nikitin.DiscordBot.controller;

import com.nikitin.DiscordBot.model.api.ChangeSettingRequest;
import com.nikitin.DiscordBot.model.api.ReactionDTO;
import com.nikitin.DiscordBot.service.GuildSettingsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class GuildAdminController {

    private GuildSettingsService guildSettingsService;

    @GetMapping(path = "/admin")
    public ModelAndView getAdminPage(@RequestParam(name = "guildID", required = true) Long guildID) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("settings", getChatReactionsAliases(guildID));
        modelAndView.addObject("replies", getRepliesAliases(guildID));
        modelAndView.addObject("guildID", guildID.toString());

        modelAndView.setViewName("index");
        return modelAndView;
    }

    @ResponseBody
    @PostMapping(path = "/admin/reaction")
    public HttpStatus setReaction(ChangeSettingRequest request) {
        long guildID = Long.parseLong(request.getGuildID());
        guildSettingsService.setSetting(request.getAlias(), guildID, request.isValue());
        return HttpStatus.NO_CONTENT;
    }


    @ResponseBody
    @PostMapping(path = "/admin/replies")
    public HttpStatus setReply(ChangeSettingRequest request) {
        long guildID = Long.parseLong(request.getGuildID());
        guildSettingsService.setReplyOption(request.getAlias(), guildID, request.isValue());
        return HttpStatus.NO_CONTENT;
    }

    private List<ReactionDTO> getRepliesAliases(Long guildID) {
        return guildSettingsService.getOrCreateRepliesOptions(guildID)
                .entrySet()
                .stream()
                .map(e -> new ReactionDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private List<ReactionDTO> getChatReactionsAliases(Long guildID) {
        return guildSettingsService.getSettings(guildID)
                .getSettings()
                .entrySet()
                .stream()
                .map(e -> new ReactionDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

}

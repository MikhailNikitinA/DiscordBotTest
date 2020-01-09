//package com.nikitin.DiscordBot.command;
//
//import com.nikitin.DiscordBot.service.ChanelMessageService;
//import com.nikitin.DiscordBot.service.MemberStatisticService;
//import com.nikitin.DiscordBot.utils.TimeUtils;
//import lombok.AllArgsConstructor;
//import net.dv8tion.jda.api.entities.Guild;
//import net.dv8tion.jda.api.entities.Member;
//import net.dv8tion.jda.api.entities.MessageChannel;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//import org.springframework.stereotype.Component;
//
//import java.text.MessageFormat;
//import java.time.OffsetDateTime;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//@Component
//@AllArgsConstructor
//public class InactiveNamesCommand implements ChatCommand {
//
//    private MemberStatisticService memberStatisticService;
//    private ChanelMessageService chanelMessageService;
//
//    @Override
//    public List<String> getCommandAliases() {
//        return Collections.singletonList("!inactive_names");
//    }
//
//    @Override
//    public void onMessageReceived(MessageReceivedEvent event) {
//        Guild guild = event.getGuild();
//        MessageChannel chanel = event.getChannel();
//
//        OffsetDateTime before = TimeUtils.getOffsetBefore();
//        OffsetDateTime after = OffsetDateTime.now();
//
//        Map<Long, Member> activeMembers = memberStatisticService.getActiveGuildUsersForPeriod(guild, before, after);
//        List<Member> inactiveMembers = memberStatisticService.getNonActiveNonBotMembers(guild, activeMembers);
//
//        sendInactiveMembersNamesNotification(chanel, inactiveMembers);
//    }
//
//    private void sendInactiveMembersNamesNotification(MessageChannel chanel, List<Member> inactiveMembers) {
//
//        for (int i = 0; i < inactiveMembers.size(); i++) {
//            String message = MessageFormat.format("{0}. {1}", i, inactiveMembers.get(i).getEffectiveName());
//            chanelMessageService.sendMessageToChanel(message, chanel);
//        }
//    }
//}

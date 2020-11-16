//package com.nikitin.DiscordBot.command.active;
//
//import com.nikitin.DiscordBot.model.CommandWithParameters;
//import com.nikitin.DiscordBot.model.data.Image;
//import com.nikitin.DiscordBot.service.ChannelMessageService;
//import com.nikitin.DiscordBot.service.ChannelProcessingService;
//import com.nikitin.DiscordBot.service.GuildSettingsService;
//import com.nikitin.DiscordBot.service.ImageService;
//import com.nikitin.DiscordBot.utils.CommandWithParametersParser;
//import com.nikitin.DiscordBot.utils.Constants;
//import com.nikitin.DiscordBot.utils.EmojiUtils;
//import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.entities.Message.Attachment;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//import org.apache.commons.lang3.math.NumberUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.text.MessageFormat;
//import java.util.Arrays;
//import java.util.List;
//
//import static com.nikitin.DiscordBot.utils.Constants.Arguments.DEFAULT_SAME_IMAGE_MESSAGES;
//import static com.nikitin.DiscordBot.utils.Constants.Arguments.MAX_SAME_IMAGE_MESSAGES;
//
//@Component
//public class ShowSimilaritiesCommand implements ChatCommand {
//
//    public static final String DEEP_SEARCH_PARAMETER = "Боянометр-глубокий-поиск";
//
//    private final ChannelProcessingService channelProcessingService;
//    private final ChannelMessageService channelMessageService;
//    private final ImageService imageService;
//    private final GuildSettingsService guildSettingsService;
//
//    @Autowired
//    public ShowSimilaritiesCommand(ChannelProcessingService channelProcessingService, ChannelMessageService channelMessageService, ImageService imageService, GuildSettingsService guildSettingsService) {
//        this.channelProcessingService = channelProcessingService;
//        this.channelMessageService = channelMessageService;
//        this.imageService = imageService;
//        this.guildSettingsService = guildSettingsService;
//        guildSettingsService.registerSetting(DEEP_SEARCH_PARAMETER);
//    }
//
//
//    @Override
//    public List<String> getCommandAliases() {
//        return Arrays.asList("!боян", "!Боян", "!Боянометр");
//    }
//
//    @Override
//    public void onMessageReceived(MessageReceivedEvent event) {
//        CommandWithParameters cwp = CommandWithParametersParser.parseCommand(event.getMessage().getContentDisplay());
//        Integer maxMessageSearch = cwp.getArguments()
//                .stream()
//                .filter(NumberUtils::isDigits)
//                .map(Integer::parseInt)
//                .map(n -> n > MAX_SAME_IMAGE_MESSAGES ? MAX_SAME_IMAGE_MESSAGES : n)
//                .findFirst()
//                .orElse(DEFAULT_SAME_IMAGE_MESSAGES);
//
//        channelProcessingService.processLatestChanelMessages(event.getChannel(),
//                m -> checkSameMessage(m, event),
//                maxMessageSearch);
//
//    }
//
//    private void checkSameMessage(Message m, MessageReceivedEvent event) {
//        long guildID = event.getGuild().getIdLong();
//
//        String boyanEmoji = EmojiUtils.getGuildEmojiString(Constants.Emojis.BOYAN, m.getGuild()).orElse(Constants.Emojis.BOYAN);
//
//        for (Attachment a : m.getAttachments()) {
//            Image imageInDataBase = imageService.findImageInDataBase(a, guildID);
//
//            boolean deepSearchEnabled = guildSettingsService.isEnabled(guildID, DEEP_SEARCH_PARAMETER);
//
//            if (imageInDataBase != null
//                    && !imageInDataBase.getLinkAsMention().equals(m.getJumpUrl())
//                    && !event.getMessage().getJumpUrl().equals(imageInDataBase.getLinkAsMention())) {
//
//                String message = MessageFormat.format("Найдено 100% совпадение картинок в постах {0} и {1}",
//                        m.getJumpUrl(),
//                        imageInDataBase.getLinkAsMention());
//                channelMessageService.sendMessageToChanel(message, event.getChannel());
//                channelMessageService.addEmojiIfPossible(boyanEmoji, m);
//
//
//            } else if (deepSearchEnabled) {
//                List<String> results = imageService.findSimilarities(a, guildID, m.getJumpUrl());
//
//                String resultMessage = MessageFormat.format("Картинка {0}. Найдены совпадения: {1}",
//                        a.getUrl(),
//                        String.join(System.lineSeparator(), results));
//                channelMessageService.sendMessageToChanel(resultMessage, event.getChannel());
//                channelMessageService.addEmojiIfPossible(boyanEmoji, m);
//            }
//        }
//    }
//}

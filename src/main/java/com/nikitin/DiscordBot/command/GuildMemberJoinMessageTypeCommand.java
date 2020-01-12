package com.nikitin.DiscordBot.command;

import com.nikitin.DiscordBot.service.ChanelMessageService;
import com.nikitin.DiscordBot.utils.RandomUtils;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Component
public class GuildMemberJoinMessageTypeCommand implements MessageTypeCommand {
    private static final String GREETING_MESSAGE = "Добро пожаловать в наш дискорд канал. Уважительно просим соблюдать правила сообщества:";
    private static final String WARNING_MESSAGE = "1. Не рекламировать другие сообщества и каналы Ютуб.\n" +
            "2. Для мата и содомии комната Токсичного чата18+ к вашим услугам.\n" +
            "3. Включить музыкального бота можно командой !play (ссылка ютуб на музыку) в комнате #bot\n" +
            "4. Добавить в плейлист трек можно написав название или кинув ссылку с ютуба в комнату #Заказ музыки\n" +
            "5. (Новичкам) Для выдачи прав обращятся к Стримеру.\n" +
            "6. По любым другим вопросам обращятся к Стримеру.\n" +
            "7. Просьба(не обязательно) использовать Ники те же что и на стриме(хотя бы в скобках после ника) у Кейна, что-бы администрация корректно могла раздавать права";
    private static final List<String> GREETING_CHANNEL_MESSAGE =
            Arrays.asList("Свежее МЯСО!", "Fresh meat  ლ(ಠ益ಠ)ლ", "Свежее мяяяясо ^w^");
    private ChanelMessageService chanelMessageService;

    @Override
    public MessageType getMessageType() {
        return MessageType.GUILD_MEMBER_JOIN;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getMember() == null){
            return;
        }
        event.getMember().getUser().openPrivateChannel().queue(this::sendWarning);
        chanelMessageService.sendMessageToChanel(RandomUtils.getRandomValue(GREETING_CHANNEL_MESSAGE), event.getChannel());

    }

    private void sendWarning(PrivateChannel privateChannel) {
        chanelMessageService.sendMessageToChanel(GREETING_MESSAGE, privateChannel);
        chanelMessageService.sendMessageToChanel(WARNING_MESSAGE, privateChannel);
    }
}

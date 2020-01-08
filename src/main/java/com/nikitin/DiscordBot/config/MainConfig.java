package com.nikitin.DiscordBot.config;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

@Configuration
public class MainConfig {

    @Value("#{environment.BOT_TOKEN}")
    private String token;

    @Bean
    public JDA jda(GenericListener listener) throws LoginException, InterruptedException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(token);
        builder.addEventListeners(listener);
        JDA jda = builder.build();
        jda.awaitReady();
        return jda;
    }

}

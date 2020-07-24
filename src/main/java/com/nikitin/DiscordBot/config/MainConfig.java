package com.nikitin.DiscordBot.config;

import com.nikitin.DiscordBot.service.DialogFlowService;
import com.nikitin.DiscordBot.strategy.responses.BundleFileStrategy;
import com.nikitin.DiscordBot.strategy.responses.DialogFlowStrategy;
import com.nikitin.DiscordBot.strategy.responses.ResponseStrategy;
import com.nikitin.DiscordBot.utils.enums.GameMappings;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPath("classpath:/templates/");
        return bean;
    }

    @Bean
    @Qualifier("rs")
    public List<ResponseStrategy> responseStrategies(DialogFlowService dialogFlowService) {
        List<ResponseStrategy> strategies = new ArrayList<>();

        DialogFlowStrategy dialogFlowStrategy = new DialogFlowStrategy(dialogFlowService);
        strategies.add(dialogFlowStrategy);

        Arrays.stream(GameMappings.values())
                .filter(GameMappings::isHasResourceFile)
                .map(BundleFileStrategy::new)
                .forEach(strategies::add);
        return strategies;
    }

}

package com.nikitin.DiscordBot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikitin.DiscordBot.model.BadWords;
import com.nikitin.DiscordBot.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BadWordsRecognitionService {

    private final BadWords badWords;
    private static final List<String> WARNINGS = Arrays.asList(
            "Я все Хиданке расскажу.",
            "Ругаться нехорошо",
            "Доигрался, спускаю прихвостней",
            "Ты доигрался, спускаю Магистро",
            "(Нарушение правил канала)",
            "Мы в цивилизованном обществе");

    @Autowired
    public BadWordsRecognitionService() throws IOException {
        this.badWords = new ObjectMapper().readValue(new ClassPathResource("bad_words.json").getInputStream(), BadWords.class);
    }


    public boolean hasBadWords(String s){
        return badWords.getWords().stream().map(w -> ".*" + w + ".*").anyMatch(s.toLowerCase()::matches);
    }


    public String maskBadWords(String s){
        for (String pattern : badWords.getWords()) {
            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(s);

            StringBuffer stringBuffer = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(stringBuffer, generateMaskSymbols(matcher.end() - matcher.start()));
            }
            matcher.appendTail(stringBuffer);

            s = stringBuffer.toString();
        }
        return s;
    }

    public String generateMaskSymbols(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("\\\\*");
        }
        return sb.toString();
    }

    public String getBadWarning() {
        return RandomUtils.getRandomValue(WARNINGS);
    }
}

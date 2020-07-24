package com.nikitin.DiscordBot.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class BadWordsRecognitionServiceTest {


    @Test
    public void hasBadWords() throws IOException {
        BadWordsRecognitionService badWordsRecognitionService = new BadWordsRecognitionService();
        Assert.assertTrue(badWordsRecognitionService.hasBadWords("хуй"));
        Assert.assertTrue(badWordsRecognitionService.hasBadWords("нахуй"));
        Assert.assertTrue(badWordsRecognitionService.hasBadWords("похуй"));
        Assert.assertTrue(badWordsRecognitionService.hasBadWords("схуя"));
        Assert.assertTrue(badWordsRecognitionService.hasBadWords("нехуй"));
        Assert.assertTrue(badWordsRecognitionService.hasBadWords("нихуя"));
        Assert.assertTrue(badWordsRecognitionService.hasBadWords("анал"));

        Assert.assertFalse(badWordsRecognitionService.hasBadWords("психую"));
        Assert.assertFalse(badWordsRecognitionService.hasBadWords("психуй"));

        Assert.assertFalse(badWordsRecognitionService.hasBadWords("ипа"));
        Assert.assertFalse(badWordsRecognitionService.hasBadWords("скипать"));
    }
}
package com.nikitin.DiscordBot.utils;

import com.nikitin.DiscordBot.model.CommandWithParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class CommandWithParametersParserTest {

    @Test
    public void testSingleString(){
        String test = "!test";

        CommandWithParameters result = CommandWithParametersParser.parseCommand(test);

        assertEquals("!test", result.getCommand());
        assertTrue(result.getArguments().isEmpty());
        assertTrue(result.getParameters().isEmpty());
    }

    @Test
    public void testWithParamsAndArguments() {
        String test = "!users active days=14 source=channels count list";

        CommandWithParameters result = CommandWithParametersParser.parseCommand(test);

        assertEquals("!users", result.getCommand());
        assertEquals(Arrays.asList("ACTIVE", "COUNT", "LIST"), result.getArguments());
        Map<String, String> parameters = result.getParameters();
        assertEquals("14", parameters.get("DAYS"));
        assertEquals("CHANNELS", parameters.get("SOURCE"));
    }
}
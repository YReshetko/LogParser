package com.support.log.storage.parser.config;

import com.support.log.storage.parser.mapping.NodeEntryRegExp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(JUnitPlatform.class)
class RegExpParserConfigurationTest {
    private static ParserConfiguration configuration;

    @BeforeAll
    private static void setUpAll(){
        List<NodeEntryRegExp> regExps = Arrays.asList(
                new NodeEntryRegExp("date", "date_reg_exp", 1),
                new NodeEntryRegExp("time", "time_reg_exp", 2),
                new NodeEntryRegExp("milli", "milli_reg_exp", 3),
                new NodeEntryRegExp("unknown", "unknown_reg_exp", 3)
        );
        configuration = new RegExpParserConfiguration(
                regExps,
                "date some_message time some_symbols milli \\[unknown\\]",
                "date some_message time some_symbols milli",
                "yyyy-MM-dd",
                "HH:mm:ss"
                );
    }


    @Test
    void testFullLogRegExp(){
        String regExp = configuration.getFullLogRegExp();
        String expectedPattern = "(?<date>date_reg_exp) some_message (?<time>time_reg_exp) some_symbols (?<milli>milli_reg_exp) \\[(?<unknown>unknown_reg_exp)\\]";
        assertEquals(expectedPattern, regExp);
    }
    @Test
    void testTimeStampPattern(){
        String timestampRegExp = configuration.getTimestampRegExp();
        String expectedPattern = "(?<date>date_reg_exp) some_message (?<time>time_reg_exp) some_symbols (?<milli>milli_reg_exp)";
        assertEquals(expectedPattern, timestampRegExp);
    }

    @Test
    void testBuildTemplateNoMessage(){
        List<String> strings = Arrays.asList("date_value", "time_value", "milli_value", "unknown_value");
        StringBuildTemplateByLogNodeEntries template = configuration.getLogLineTemplate();
        assertThrows(
                ArrayIndexOutOfBoundsException.class,
                () -> template.build(strings),
                "Build must fail as the list has to contain log message at the end");
    }
    @Test
    void testBuildTemplateWithLogMessage(){
        List<String> strings = Arrays.asList("date_value", "time_value", "milli_value", "unknown_value", "messagevalue");
        String expected = "date_value some_message time_value some_symbols milli_value [unknown_value]messagevalue";
        StringBuildTemplateByLogNodeEntries template = configuration.getLogLineTemplate();
        String result = template.build(strings);
        assertEquals(expected, result);
    }

    @Test
    void testLogLineTemplate(){
        StringBuildTemplateByLogNodeEntries buildStringTemplate = configuration.getLogLineTemplate();
        String[] template = buildStringTemplate.getTemplate();
        String[] toCompare = {"", " some_message ", " some_symbols ", " [", "]"};
        assertEquals(toCompare.length, template.length);
        IntStream.range(0, template.length).forEach(i -> assertEquals(template[i], toCompare[i]));
    }
}

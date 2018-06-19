package com.support.log.storage.parser;

import com.support.log.storage.parser.config.StringBuildTemplateByLogNodeEntries;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.stream.IntStream;

import static com.support.log.storage.util.CommonTestConstants.*;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitPlatform.class)
public class RegExpParserConfigurationTest{

    @Test
    void testFullRegExpBuilding(){
        String fullRegExp = configuration.getFullLogRegExp();
        assertEquals("" +
                "(?<date>"+ DATE_REGEXP + ") " +
                "(?<time>"+ TIME_REGEXP + ")," +
                "(?<milli>" + MILLI_REGEXP + ") " +
                "\\[(?<debug>" + DEBUG_REGEXP + ")\\] " +
                "\\[(?<name>"+ NAME_REGEXP + ")\\] " +
                "\\[(?<class>" + CLASS_REGEXP + ")\\] ", fullRegExp);
    }

    @Test
    void testTimestampRegExpBuilding(){
        String timestampRegExp = configuration.getTimestampRegExp();
        assertEquals("" +
                "(?<date>"+ DATE_REGEXP + ") " +
                "(?<time>"+ TIME_REGEXP + ")," +
                "(?<milli>" + MILLI_REGEXP + ")", timestampRegExp);
    }

    @Test
    void testLogLineTemplate(){
        StringBuildTemplateByLogNodeEntries buildStringTemplate = configuration.getLogLineTemplate();
        String[] template = buildStringTemplate.getTemplate();
        String[] toCompare = {"", " ", ",", " [", "] [", "] [", "] "};
        assertEquals(toCompare.length, template.length);
        IntStream.range(0, template.length).forEach(i -> assertEquals(template[i], toCompare[i]));
    }

    @Test
    void testStringBuild(){
        StringBuildTemplateByLogNodeEntries buildStringTemplate = configuration.getLogLineTemplate();
        String[] toUnparse = {"some_date", "some_time", "some_milli", "log_level", "thread_name", "class_name", "log_message"};
        String logLine = buildStringTemplate.build(Arrays.asList(toUnparse));
        assertEquals("some_date some_time,some_milli [log_level] [thread_name] [class_name] log_message", logLine);
    }
}

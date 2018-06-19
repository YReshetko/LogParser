package com.support.log.storage.parser;

import com.support.core.util.JsonUtils;
import com.support.log.model.node.LogEntry;
import com.support.log.model.node.LogNode;
import com.support.log.storage.util.TestDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static com.support.log.storage.util.CommonTestConstants.*;

@RunWith(JUnitPlatform.class)
public class RegExpParserTest {
    private static final String LOG_LINE_MATCH_REGEXP = "2018-01-18 16:57:20,264 [ERROR] [http-0.0.0.0-8080-1] [com.my.package.MyClass] Log message";
    private static final String LOG_LINE_DOESNT_MATCH_REGEXP = "2018-01-18 16:57:20 [ERROR] [http-0.0.0.0-8080-1] [com.my.package.MyClass] Log message";

    private RegExpBasedParser parser;
    @BeforeEach
    public void setUp(){
        parser = new RegExpBasedParser(configuration);
    }

    @Test
    void testParseStringSuccess() throws ParseException {
        LogNode node = parser.parse(LOG_LINE_MATCH_REGEXP);
        assertAll("All parts of log should match",
                () -> assertEquals("2018-01-18", node.getDate()),
                () -> assertEquals("16:57:20", node.getTime()),
                () -> assertEquals("264", node.getMillisecond()),
                () -> assertEquals("http-0.0.0.0-8080-1", node.getName()),
                () -> assertEquals("Log message", node.getMessage())
                );
        List<String> otherLogLineEntries = node
                .getAdditionalEntries()
                .stream()
                .map(LogEntry::getValue)
                .collect(Collectors.toList());
        assertTrue(otherLogLineEntries.contains("ERROR"));
        assertTrue(otherLogLineEntries.contains("com.my.package.MyClass"));
        assertEquals(2, otherLogLineEntries.size());
    }

    @Test
    void testParseStringDoesntMatchRegexp() throws ParseException {
        LogNode node = parser.parse(LOG_LINE_DOESNT_MATCH_REGEXP);
        assertEquals(LOG_LINE_DOESNT_MATCH_REGEXP, node.getMessage());
    }

    @Test
    void testMatchToRegexp(){
        assertTrue(parser.hasStamp(LOG_LINE_MATCH_REGEXP));
    }

    @Test
    void testDoesntMatchToRegexp(){
        assertFalse(parser.hasStamp(LOG_LINE_DOESNT_MATCH_REGEXP));
    }

    @Test
    void testLogNodeToString(){
        LogNode node = TestDataLoader.loadLogNode("lognodes/parser/LogNodeParseToString");
        String logLine = parser.parse(node);
        assertEquals(LOG_LINE_MATCH_REGEXP, logLine);
    }
}

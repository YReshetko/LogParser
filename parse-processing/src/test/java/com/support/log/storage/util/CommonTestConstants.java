package com.support.log.storage.util;

import com.support.log.storage.parser.config.RegExpParserConfiguration;
import com.support.log.storage.parser.mapping.NodeEntryRegExp;

import java.util.Arrays;
import java.util.List;

public class CommonTestConstants {
    public static final String DATE_REGEXP = "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}";
    public static final String TIME_REGEXP = "[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}";
    public static final String MILLI_REGEXP = "[0-9]{3}";
    public static final String DEBUG_REGEXP = "[A-Z ]+";
    public static final String NAME_REGEXP = ".+?";
    public static final String CLASS_REGEXP = "[[a-z0-9]+\\.]+[a-zA-Z]{1}[a-zA-Z0-9]+";


    public static final List<NodeEntryRegExp> nodeEntryRegExps = Arrays.asList(
            new NodeEntryRegExp("date", DATE_REGEXP, 1),
            new NodeEntryRegExp("time", TIME_REGEXP, 2),
            new NodeEntryRegExp("milli", MILLI_REGEXP, 3),
            new NodeEntryRegExp("debug", DEBUG_REGEXP, 4),
            new NodeEntryRegExp("name", NAME_REGEXP, 5),
            new NodeEntryRegExp("class", CLASS_REGEXP, 6)
    );
    public static final RegExpParserConfiguration configuration = new RegExpParserConfiguration(
            nodeEntryRegExps,
            "date time,milli \\[debug\\] \\[name\\] \\[class\\] ",
            "date time,milli",
            "yyyy-MM-dd",
            "HH:mm:ss"
    );
}

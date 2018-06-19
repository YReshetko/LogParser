package com.support.log.storage.parser;

import com.support.log.model.node.LogNode;
import com.support.log.storage.Parser;
import com.support.log.storage.parser.config.RegExpParserConfiguration;
import com.support.log.storage.parser.config.StringBuildTemplateByLogNodeEntries;
import com.support.log.storage.parser.mapping.LogNodeToStringMapping;
import com.support.log.storage.parser.mapping.NodeEntryRegExp;
import com.support.log.storage.parser.mapping.StringToLogNodeMapping;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *   Parser of log nodes from/to string
 */
public class RegExpBasedParser implements Parser
{

    private final RegExpParserConfiguration configuration;
    private final Pattern fullStampPattern;
    private final StringToLogNodeMapping stringToLogNodeMapping;
    private final LogNodeToStringMapping logNodeToStringMapping;

    public RegExpBasedParser(RegExpParserConfiguration configuration) {
        this.configuration = configuration;
        fullStampPattern = Pattern.compile(configuration.getFullLogRegExp());
        stringToLogNodeMapping = new StringToLogNodeMapping();
        logNodeToStringMapping = new LogNodeToStringMapping();
    }

    @Override
    public LogNode parse(String value) throws ParseException {
        Matcher matcher = fullStampPattern.matcher(value);
        if (matcher.find()) {
            return createLogNodeMatchToPattern(matcher, value);
        } else {
            return createLogNodeDoesntMatchToPattern(value);
        }
    }

    @Override
    public String parse(LogNode node) {
        if (node.isIsStamped()){
            return parseStampedLogNodeToString(node);
        } else {
            return node.getMessage();
        }
    }

    @Override
    public boolean hasStamp(String value)
    {
        Matcher matcher = fullStampPattern.matcher(value);
        return matcher.find();
    }

    private LogNode createLogNodeMatchToPattern(Matcher matcher, String value)  throws ParseException{
        LogNode out = new LogNode();
        List<NodeEntryRegExp> entryRegExps = configuration.getNodeEntries();
        entryRegExps.forEach(entryRegExp ->
                stringToLogNodeMapping.populate(entryRegExp, out, matcher.group(entryRegExp.getName())));
        out.setMessage(value.replaceAll(configuration.getFullLogRegExp(), ""));
        out.setLongDateTime(getMillisecond(out.getDate(), out.getTime(), out.getMillisecond()));
        out.setIsStamped(true);
        return out;
    }
    private LogNode createLogNodeDoesntMatchToPattern(String value) {
        LogNode node = new LogNode();
        node.setMessage(value);
        return node;
    }

    private String parseStampedLogNodeToString(LogNode node){
        List<String> logEntries = new ArrayList<>();
        configuration.getNodeEntries().forEach(nodeEntryRegExp ->
                logNodeToStringMapping.populate(nodeEntryRegExp, node, logEntries));
        logEntries.add(node.getMessage());
        StringBuildTemplateByLogNodeEntries logLineTemplate = configuration.getLogLineTemplate();
        return logLineTemplate.build(logEntries);
    }

    private long getMillisecond(String sDate, String sTime, String milliseconds) throws ParseException
    {
        DateFormat dateFormat = new SimpleDateFormat(configuration.getDateFormat());
        DateFormat timeFormat = new SimpleDateFormat(configuration.getTimeFormat());
        Date date = dateFormat.parse(sDate);
        Date time = timeFormat.parse(sTime);
        long millis = Long.parseLong(milliseconds);
        return millis + date.getTime() + time.getTime();
    }
}

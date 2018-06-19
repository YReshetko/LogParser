package com.support.log.storage.parser.config;

import com.support.log.storage.parser.mapping.NodeEntryRegExp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RegExpParserConfiguration implements ParserConfiguration{


    private final List<NodeEntryRegExp> nodeEntries;

    private final String commonStampPattern;
    private final String commonDataTimePattern;

    private final String dateFormat;
    private final String timeFormat;

    //private String stampPattern;
    private StringBuildTemplateByLogNodeEntries template;


    public RegExpParserConfiguration(List<NodeEntryRegExp> regExps,
                                     String fullLogStampPattern,
                                     String fullDataTimePattern,
                                     String dateFormat,
                                     String timeFormat) {
        this.nodeEntries = regExps;
        this.commonStampPattern = fullLogStampPattern;
        this.commonDataTimePattern = fullDataTimePattern;
        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
        template = new BuildStringTemplate();
    }

    @Override
    public String getFullLogRegExp() {
        return getFullRegExp(commonStampPattern);
    }

    @Override
    public String getTimestampRegExp() {
        return getFullRegExp(commonDataTimePattern);
    }

    private String getFullRegExp(String pattern){
        String fullRegExp = pattern;
        for (NodeEntryRegExp entry: nodeEntries){
            fullRegExp = fullRegExp.replace(entry.getName(), "(?<" + entry.getName() + ">" + entry.getRegexp() + ")");
        }
        return fullRegExp;
    }

    @Override
    public StringBuildTemplateByLogNodeEntries getLogLineTemplate(){
        return template;
    }

    @Override
    public List<NodeEntryRegExp> getNodeEntries() {
        return nodeEntries;
    }

    @Override
    public String getDateFormat() {
        return dateFormat;
    }

    @Override
    public String getTimeFormat() {
        return timeFormat;
    }

    public class BuildStringTemplate implements StringBuildTemplateByLogNodeEntries {
        private String[] template;
        private BuildStringTemplate() {
            List<String> logEntryNames = nodeEntries
                    .stream()
                    .map(NodeEntryRegExp::getName)
                    .collect(Collectors.toList());
            List<String> out = splitLogStamp(logEntryNames);
            template = out.toArray(new String[out.size()]);
        }

        private List<String> splitLogStamp(List<String> logEntryNames) {
            List<String> out = new ArrayList<>();
            int startIndex = 0;
            int endIndex;
            for (String logEntryName : logEntryNames){
                endIndex = commonStampPattern.indexOf(logEntryName);
                if (endIndex != -1) {
                    out.add(getSubstringWithoutShielding(commonStampPattern, startIndex, endIndex));
                    startIndex = endIndex + logEntryName.length();
                }
            }
            out.add(getSubstringWithoutShielding(commonStampPattern, startIndex, commonStampPattern.length()));
            return out;
        }

        @Override
        public String build(List<String> logEntries){
            StringBuilder builder = new StringBuilder();
            IntStream.range(0, template.length).forEach(i -> {
                builder.append(template[i]);
                builder.append(logEntries.get(i));
            });
            return builder.toString();
        }
        private String getSubstringWithoutShielding(String value, int from, int to){
            String out = value.substring(from, to);
            return out.replace("\\", "");
        }

        @Override
        public String[] getTemplate() {
            return template;
        }
    }




}

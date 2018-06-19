package com.support.log.storage.parser.config;

import com.support.log.storage.parser.mapping.NodeEntryRegExp;

import java.util.List;

public interface ParserConfiguration {
    String getFullLogRegExp();
    String getTimestampRegExp();
    StringBuildTemplateByLogNodeEntries getLogLineTemplate();
    List<NodeEntryRegExp> getNodeEntries();
    String getDateFormat();
    String getTimeFormat();
}

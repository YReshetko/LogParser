package com.support.log.storage.parser.mapping;

import com.support.log.model.node.LogNode;

import java.util.List;

public class LogNodeToStringMapping {

    public void populate(NodeEntryRegExp entry, LogNode node, List<String> logEntries){
        LogLineToFromNodeMapping mapper = LogLineToFromNodeMapping.getLogMapperByPatternName(entry.getName());
        mapper.populateLogNodeToListStrings(node, logEntries, entry.getPositionInLine());
    }
}

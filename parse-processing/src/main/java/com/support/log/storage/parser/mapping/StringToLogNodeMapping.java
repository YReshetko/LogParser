package com.support.log.storage.parser.mapping;

import com.support.log.model.node.LogNode;

public class StringToLogNodeMapping {

    public void populate(NodeEntryRegExp nodeEntryRegExp, LogNode logNode, String value){
        LogLineToFromNodeMapping mapper = LogLineToFromNodeMapping.getLogMapperByPatternName(nodeEntryRegExp.getName());
        mapper.populateStringToLogNode(logNode, value, nodeEntryRegExp.getPositionInLine());
    }
}

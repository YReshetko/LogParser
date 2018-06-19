package com.support.log.storage.parser.mapping;

import com.support.log.model.node.LogEntry;
import com.support.log.model.node.LogNode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class StringToLogNodeMapping {
    private final static Map<String, BiVoidFunction<LogNode, String>> STANDARD_LOG_ENTRY_MAPPERS = new HashMap<>();
    static {
        STANDARD_LOG_ENTRY_MAPPERS.put(MappingConstants.DATE_REG_EXP_NAME, LogNode::setDate);
        STANDARD_LOG_ENTRY_MAPPERS.put(MappingConstants.TIME_REG_EXP_NAME, LogNode::setTime);
        STANDARD_LOG_ENTRY_MAPPERS.put(MappingConstants.MILLISECONDS_REG_EXP_NAME, LogNode::setMillisecond);
        STANDARD_LOG_ENTRY_MAPPERS.put(MappingConstants.NAME_REG_EXP_NAME, LogNode::setName);
    }
    private final static BiFunction<NodeEntryRegExp, String, LogEntry> CREATE_CUSTOM_LOG_ENTRY = (exp, value) -> {
        LogEntry logEntry = new LogEntry();
        logEntry.setValue(value);
        logEntry.setPosition(exp.getPositionInLine());
        return logEntry;
    };

    public void populate(NodeEntryRegExp nodeEntryRegExp, LogNode logNode, String value){
        BiVoidFunction<LogNode, String> func = STANDARD_LOG_ENTRY_MAPPERS.get(nodeEntryRegExp.getName());
        if (func != null){
            func.apply(logNode, value);
        } else {
            logNode.getAdditionalEntries().add(CREATE_CUSTOM_LOG_ENTRY.apply(nodeEntryRegExp, value));
        }
    }
}

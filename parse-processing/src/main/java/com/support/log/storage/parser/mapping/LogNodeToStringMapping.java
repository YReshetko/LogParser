package com.support.log.storage.parser.mapping;

import com.support.log.model.node.LogEntry;
import com.support.log.model.node.LogNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class LogNodeToStringMapping {
    private final static Map<String, BiVoidFunction<List<String>, LogNode>> STANDARD_LOG_ENTRY_MAPPERS = new HashMap<>();
    static {
        STANDARD_LOG_ENTRY_MAPPERS.put(MappingConstants.DATE_REG_EXP_NAME, (list, node) -> list.add(node.getDate()));
        STANDARD_LOG_ENTRY_MAPPERS.put(MappingConstants.TIME_REG_EXP_NAME, (list, node) -> list.add(node.getTime()));
        STANDARD_LOG_ENTRY_MAPPERS.put(MappingConstants.MILLISECONDS_REG_EXP_NAME, (list, node) -> list.add(node.getMillisecond()));
        STANDARD_LOG_ENTRY_MAPPERS.put(MappingConstants.NAME_REG_EXP_NAME, (list, node) -> list.add(node.getName()));
    }
    private final static BiFunction<NodeEntryRegExp, LogNode, LogEntry> CREATE_CUSTOM_LOG_ENTRY = (exp, node) -> node
            .getAdditionalEntries()
            .stream()
            .filter(logEntry -> exp.getPositionInLine() == logEntry.getPosition())
            .findFirst()
            .orElse(new LogEntry());

    public void populate(NodeEntryRegExp entry, LogNode node, List<String> logEntries){
        BiVoidFunction<List<String>, LogNode> func = STANDARD_LOG_ENTRY_MAPPERS.get(entry.getName());
        if (func != null){
            func.apply(logEntries, node);
        } else {
            logEntries.add(CREATE_CUSTOM_LOG_ENTRY.apply(entry, node).getValue());
        }
    }
}

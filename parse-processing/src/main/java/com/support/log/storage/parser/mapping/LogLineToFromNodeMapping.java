package com.support.log.storage.parser.mapping;

import com.support.log.model.node.LogEntry;
import com.support.log.model.node.LogNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

enum LogLineToFromNodeMapping {
    DATE(true) {
        @Override
        BiVoidFunction<LogNode, String> getStringToLogNodeSpecificMapper() {
            return LogNode::setDate;
        }

        @Override
        BiVoidFunction<List<String>, LogNode> getLogNodeToStringSpecificMapper() {
            return (list, node) -> list.add(node.getDate());
        }
    }, TIME(true) {
        @Override
        BiVoidFunction<LogNode, String> getStringToLogNodeSpecificMapper() {
            return LogNode::setTime;
        }

        @Override
        BiVoidFunction<List<String>, LogNode> getLogNodeToStringSpecificMapper() {
            return (list, node) -> list.add(node.getTime());
        }
    }, MILLI(true) {
        @Override
        BiVoidFunction<LogNode, String> getStringToLogNodeSpecificMapper() {
            return LogNode::setMillisecond;
        }

        @Override
        BiVoidFunction<List<String>, LogNode> getLogNodeToStringSpecificMapper() {
            return (list, node) -> list.add(node.getMillisecond());
        }
    }, NAME(true) {
        @Override
        BiVoidFunction<LogNode, String> getStringToLogNodeSpecificMapper() {
            return LogNode::setName;
        }

        @Override
        BiVoidFunction<List<String>, LogNode> getLogNodeToStringSpecificMapper() {
            return (list, node) -> list.add(node.getName());
        }
    }, DEFAULT(false) {
        @Override
        BiVoidFunction<LogNode, String> getStringToLogNodeSpecificMapper() {
            throw new UnsupportedOperationException(
                    "The DEFAULT LogLineToFromNodeMapping doesn't support StringToLogNode specific mapper");
        }

        @Override
        BiVoidFunction<List<String>, LogNode> getLogNodeToStringSpecificMapper() {
            throw new UnsupportedOperationException(
                    "The DEFAULT LogLineToFromNodeMapping doesn't support LogNodeToString specific mapper");
        }
    };

    private boolean isReservedName;
    LogLineToFromNodeMapping(boolean isReservedName){
        this.isReservedName = isReservedName;
    }
    public void populateStringToLogNode(LogNode logNode, String value, int positionInLogStamp){
        if (isReservedName){
            getStringToLogNodeSpecificMapper().apply(logNode, value);
        } else {
            createAndPopulateLogEntryForDefaultName(logNode, value, positionInLogStamp);
        }
    }
    public void populateLogNodeToListStrings(LogNode node, List<String> logEntries, int positionInLogStamp){
        if (isReservedName){
            getLogNodeToStringSpecificMapper().apply(logEntries, node);
        } else {
            populateLogNodeToStringDefault(node, logEntries, positionInLogStamp);
        }
    }

    abstract BiVoidFunction<LogNode, String> getStringToLogNodeSpecificMapper();
    abstract BiVoidFunction<List<String>, LogNode> getLogNodeToStringSpecificMapper();
    private void createAndPopulateLogEntryForDefaultName(LogNode logNode, String value, int positionInLogStamp){
        LogEntry logEntry = new LogEntry();
        logEntry.setValue(value);
        logEntry.setPosition(positionInLogStamp);
        logNode.getAdditionalEntries().add(logEntry);
    }
    private void populateLogNodeToStringDefault(LogNode logNode, List<String> logEntries, int positionInLogStamp){
        LogEntry entry = logNode
                .getAdditionalEntries()
                .stream()
                .filter(logEntry -> positionInLogStamp == logEntry.getPosition())
                .findFirst()
                .orElse(new LogEntry());
        logEntries.add(entry.getValue());
    }

    private static final Map<String, LogLineToFromNodeMapping> mappersCache = new HashMap<>();
    static LogLineToFromNodeMapping getLogMapperByPatternName(String patternName){
        LogLineToFromNodeMapping mapper = createAndCacheMapperIfAbsent(patternName);
        return mapper;
    }

    private static LogLineToFromNodeMapping createAndCacheMapperIfAbsent(String patternName){
        LogLineToFromNodeMapping mapper = mappersCache.get(patternName);
        if (mapper == null){
            mapper = Stream
                    .of(LogLineToFromNodeMapping.values())
                    .filter(currentMapper -> currentMapper.name().equals(patternName.toUpperCase()))
                    .findFirst()
                    .orElse(DEFAULT);
            mappersCache.put(patternName, mapper);
        }
        return mapper;
    }

    @FunctionalInterface
    private interface BiVoidFunction<K, V>{
        void apply(K k, V v);
    }
}

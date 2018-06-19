package com.support.log.storage.integration.dummy;

import com.support.core.BaseLogger;
import com.support.log.model.node.LogNode;
import com.support.log.model.thread.descriptor.ThreadDescriptor;
import com.support.log.model.thread.info.ThreadsInfo;
import com.support.log.storage.*;
import com.support.log.storage.parser.mapping.NodeEntryRegExp;
import com.support.log.storage.parser.RegExpBasedParser;
import com.support.log.storage.parser.config.RegExpParserConfiguration;

import java.util.*;

public class TestStorageContext implements StorageContext {
    RegExpParserConfiguration configuration;
    Parser parser;
    Saver saver;
    Retriever retriever;
    ParsingProgress progress;

    public TestStorageContext() {
        parser = new RegExpBasedParser(getConfiguration());
        saver = new TestSaver();
        retriever = new TestRetriever();
        progress = new TestParsingProgress();
    }

    @Override
    public Parser getParser() {
        return parser;
    }

    @Override
    public Saver getSaver() {
        return saver;
    }

    @Override
    public Retriever getRetriever() {
        return retriever;
    }

    @Override
    public ParsingProgress getParsingProgress() {
        return progress;
    }


    private RegExpParserConfiguration getConfiguration(){
        if (configuration == null) {
            final String DATE_REGEXP = "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}";
            final String TIME_REGEXP = "[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}";
            final String MILLI_REGEXP = "[0-9]{3}";
            final String DEBUG_REGEXP = "[A-Z ]+";
            final String NAME_REGEXP = ".+?";
            final String CLASS_REGEXP = "[[a-z0-9]+\\.]+[a-zA-Z]{1}[a-zA-Z0-9]+";

            List<NodeEntryRegExp> nodeEntryRegExps;
            nodeEntryRegExps = Arrays.asList(
                    new NodeEntryRegExp("date", DATE_REGEXP, 1),
                    new NodeEntryRegExp("time", TIME_REGEXP, 2),
                    new NodeEntryRegExp("milli", MILLI_REGEXP, 3),
                    new NodeEntryRegExp("debug", DEBUG_REGEXP, 4),
                    new NodeEntryRegExp("name", NAME_REGEXP, 5),
                    new NodeEntryRegExp("class", CLASS_REGEXP, 6)
            );
            configuration = new RegExpParserConfiguration(
                    nodeEntryRegExps,
                    "date time,milli \\[debug\\] \\[name\\] \\[class\\] ",
                    "date time,milli",
                    "yyyy-MM-dd",
                    "HH:mm:ss"
            );
        }
        return configuration;
    }

    private class TestSaver implements Saver {
        private Map<String, List<LogNode>> nodes = new HashMap<>();
        private Map<String, List<ThreadsInfo>> infos = new HashMap<>();
        private Map<String, List<ThreadDescriptor>> descriptors = new HashMap<>();
        @Override
        public boolean save(IdentifierParsedLog identifier, LogNode value) {
            List<LogNode> logNodes = nodes.computeIfAbsent(identifier.getKey(), k -> new LinkedList<>());
            logNodes.add(value);
            return true;
        }

        @Override
        public boolean save(IdentifierParsedLog identifier, ThreadsInfo value) {
            List<ThreadsInfo> threadsInfos = infos.computeIfAbsent(identifier.getKey(), k -> new LinkedList<>());
            threadsInfos.add(value);
            return true;
        }

        @Override
        public boolean save(IdentifierParsedLog identifier, ThreadDescriptor value) {
            List<ThreadDescriptor> threadDescriptors = descriptors.computeIfAbsent(identifier.getKey(), k -> new LinkedList<>());
            threadDescriptors.add(value);
            return true;
        }

        @Override
        public boolean complete(IdentifierParsedLog identifier) {
            return true;
        }
    }

    private class TestRetriever implements Retriever{
        @Override
        public <V> Iterator<V> get(IdentifierParsedLog identifier, StorageCommand<V> command) {
            if (saver != null) {
                if (command.getType() == LogNode.class) {
                    return (Iterator<V>) ((TestSaver)saver).nodes.get(identifier.getKey()).iterator();
                } else if (command.getType() == ThreadDescriptor.class) {
                    return (Iterator<V>) ((TestSaver)saver).descriptors.get(identifier.getKey()).iterator();
                } else if (command.getType() == ThreadsInfo.class) {
                    return (Iterator<V>) ((TestSaver)saver).infos.get(identifier.getKey()).iterator();
                }
            }
            return null;
        }
    }

    private class TestParsingProgress extends BaseLogger implements ParsingProgress{
        private long totalSize = 0;
        @Override
        public void addTotalSize(long size) {
            totalSize += size;
            //log("ADD Total size: " + totalSize);
        }

        @Override
        public void setTotalSize(long size) {
            totalSize = size;
            //log("SET  size: " + totalSize);
        }

        @Override
        public void subtractSize(long size) {
            totalSize -= size;
            //log("SUB Total size: " + totalSize);
        }

        @Override
        public double getProgress() {
            return totalSize;
        }
    }
}

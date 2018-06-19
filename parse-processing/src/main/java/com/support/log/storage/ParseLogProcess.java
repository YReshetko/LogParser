package com.support.log.storage;

import com.support.core.BaseLogger;
import com.support.log.model.block.LogBlock;
import com.support.log.model.node.LogNode;
import com.support.log.storage.saving.AsyncSavingStrategy;
import com.support.log.storage.saving.SavingStrategy;
import com.support.log.storage.saving.SyncSavingStrategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParseLogProcess extends BaseLogger implements Callable<IdentifierParsedLog>
{
    private static final String EMPTY_STRING = "";
    private static final String NEW_LINE = "\n";
    private final Parser parser;
    private final ParsingProgress progress;
    private final IdentifierParsedLog identifier;
    private final SavingStrategy savingStrategy;
    private List<File> files;
    private final LogLinesBuffer logBuffer = new LogLinesBuffer();
    private final AtomicLong index = new AtomicLong(0);
    public ParseLogProcess(StorageContext context, List<File> files, IdentifierParsedLog identifier, SavingStrategy strategy)
    {
        this.parser = context.getParser();
        this.progress = context.getParsingProgress();
        //SavingStrategyType savingType = SavingStrategyType.valueOf(context.getSavingStrategy());
        savingStrategy = strategy;
        this.identifier = identifier;
        this.files = files;
    }

    @Override
    public IdentifierParsedLog call()
    {
        List<LogBlock> blocks = getSortedLogBlocks();
        setupProgressByTotalSize(blocks);
        tryParseLogBlocks(blocks);
        return identifier;
    }

    private List<LogBlock> getSortedLogBlocks(){
        List<LogBlock> blocks = getLogBlocksByFiles(files);
        blocks.sort(Comparator.comparingLong(LogBlock::getStartTime));
        return blocks;
    }

    private List<LogBlock> getLogBlocksByFiles(List<File> files) {
        return files
                .stream()
                .map(this::prepareLogBlockByFile)
                .collect(Collectors.toList());
    }

    private LogBlock prepareLogBlockByFile(File file){
        String filePath = file.getAbsolutePath();
        Long logBlockStartTime = tryExtractStartedTimeFromFile(filePath);
        LogBlock logBlock = new LogBlock();
        logBlock.setFileName(filePath);
        logBlock.setSize(file.length());
        logBlock.setStartTime(logBlockStartTime);
        return logBlock;
    }

    private Long tryExtractStartedTimeFromFile(String filePath){
        try (Stream<String> stream = Files.lines(Paths.get(filePath))){
            String firstStampedLine = stream
                    .filter(parser::hasStamp)
                    .findFirst()
                    .orElse(EMPTY_STRING);
            if (!firstStampedLine.isEmpty()) {
                LogNode node = parser.parse(firstStampedLine);
                return node.getLongDateTime();
            } else {
                throw new RuntimeException("No one line in file " + filePath + " contains stamped line");
            }
        } catch (IOException e) {
            throw new RuntimeException("Problem with file Reading " + e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException("Problem with parsing line in file " + filePath);
        }
    }

    private void setupProgressByTotalSize(List<LogBlock> blocks){
        long totalSize = blocks.stream().mapToLong(LogBlock::getSize).sum();
        progress.addTotalSize(totalSize);
    }

    private void tryParseLogBlocks(List<LogBlock> blocks) {
        try {
            blocks.forEach(this::parseOneLogBlock);
            tryParseAndSaveLogLineWithId(logBuffer.get(), index.incrementAndGet());
        } finally {
            savingStrategy.invalidate(identifier);
        }
    }

    private void parseOneLogBlock(LogBlock block) {
        String file = block.getFileName();
        try (Stream<String> stream = Files.lines(Paths.get(file))){
            stream.forEach(this::processLogLine);
        } catch (IOException e) {
            throw new RuntimeException("ProblemWith file Reading " + e.getMessage());
        }
    }

    private void processLogLine(String line) {
        if (parser.hasStamp(line)) {
            tryParseAndSaveLogLineWithId(logBuffer.get(), index.incrementAndGet());
            logBuffer.clear();
        } else {
            logBuffer.add(NEW_LINE);
        }
        logBuffer.add(line);
        progress.subtractSize(line.length());
    }

    private void tryParseAndSaveLogLineWithId(String line, Long index)
    {
        try {
            LogNode node = parser.parse(line);
            node.setId(index);
            savingStrategy.saving(identifier, node);
        } catch (ParseException e) {
            throw new RuntimeException("The line " + index + " : " + line + " doesn't match to pattern");
        }

    }

    private enum SavingStrategyType
    {
        ASYNC {
            @Override
            public SavingStrategy getStrategy(Saver saver) {
                return new AsyncSavingStrategy(saver);
            }
        },
        SYNC {
            @Override
            public SavingStrategy getStrategy(Saver saver) {
                return new SyncSavingStrategy(saver);
            }
        };
        public abstract SavingStrategy getStrategy(Saver saver);
    }

    private class LogLinesBuffer{
        private StringBuilder buffer;

        LogLinesBuffer() {
            buffer = new StringBuilder();
        }

        void clear(){
            buffer = new StringBuilder();
        }
        String get(){
            return buffer.toString();
        }
        void add(String value){
            buffer.append(value);
        }
    }
}

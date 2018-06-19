package com.support.log.storage;

import com.support.log.model.node.LogNode;
import com.support.log.storage.saving.AsyncSavingStrategy;
import com.support.log.storage.saving.SyncSavingStrategy;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 *
 */
public class LogStorageImpl implements StorageService
{
    private static final String NEW_LINE = "\n";
    private StorageContext context;
    private Parser parser;
    private Saver saver;
    private Retriever retriever;
    private ParsingProgress progress;
    public LogStorageImpl(StorageContext storageContext){
        setStorageContext(storageContext);
    }
    @Override
    public Future<IdentifierParsedLog> process(IdentifierParsedLog identifier, List<File> files)
    {
        Callable<IdentifierParsedLog> parseLog = new ParseLogProcess(context, files, identifier, new SyncSavingStrategy(saver));
        FutureTask<IdentifierParsedLog> future = new FutureTask<IdentifierParsedLog>(parseLog);
        new Thread(future).start();
        return future;
    }

    @Override
    public void setStorageContext(StorageContext storageContext)
    {
        this.context = storageContext;
        this.parser = context.getParser();
        this.saver = context.getSaver();
        this.retriever = context.getRetriever();
        this.progress = context.getParsingProgress();
    }

    @Override
    public <V> Iterator<V> getIterator(IdentifierParsedLog identifier, StorageCommand<V> command)
    {
        command.setData(identifier);
        return retriever.get(identifier, command);
    }

    @Override
    public String getLog(IdentifierParsedLog identifier, StorageCommand<LogNode> iLogStorageCommand)
    {
        Iterator<LogNode> nodes = getIterator(identifier, iLogStorageCommand);
        StringBuilder buffer = new StringBuilder();
        while (nodes.hasNext())
        {
            buffer.append(parser.parse(nodes.next()));
            buffer.append(NEW_LINE);
        }
        return buffer.toString();
    }

    @Override
    public boolean changeLog(IdentifierParsedLog identifier, StorageCommand command)
    {
        //retriever.changeLog(identifier, command);
        return true;
    }


}

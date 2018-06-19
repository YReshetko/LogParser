package com.support.log.storage.saving;

import com.support.log.model.node.LogNode;
import com.support.log.storage.IdentifierParsedLog;
import com.support.log.storage.Saver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class AsyncSavingStrategy extends AbstractSavingStrategy
{
    private final Saver saver;
    private final ExecutorService executor;

    public AsyncSavingStrategy(Saver saver)
    {
        this.saver = saver;
        this.executor = Executors.newFixedThreadPool(10);
    }

    @Override
    public void saving(IdentifierParsedLog identifier, LogNode node)
    {
        //  Before saving log node we need to cache meta inf
        cachingMetaInf(node);
        executor.execute(new SimpleNodeSaverProcess(identifier, node));
    }

    @Override
    public void invalidate(IdentifierParsedLog identifier) {
        executor.shutdown();
        //  Wait all threads
        log("Waiting for termination of thread pull");
        while (!executor.isTerminated());
        log("Saving metainf");
        saveMetaInf(identifier, saver);
        saver.complete(identifier);
    }

    private class SimpleNodeSaverProcess implements Runnable
    {
        private IdentifierParsedLog identifier;
        private LogNode node;
        public SimpleNodeSaverProcess(IdentifierParsedLog identifier, LogNode node)
        {
            this.identifier = identifier;
            this.node = node;
        }
        @Override
        public void run() {
            saver.save(identifier, node);
        }
    }
}

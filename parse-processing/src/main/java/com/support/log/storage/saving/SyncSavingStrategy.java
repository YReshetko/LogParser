package com.support.log.storage.saving;

import com.support.log.model.node.LogNode;
import com.support.log.storage.IdentifierParsedLog;
import com.support.log.storage.Saver;

/**
 *
 */
public class SyncSavingStrategy extends AbstractSavingStrategy
{

    private final Saver saver;

    public SyncSavingStrategy(Saver saver)
    {
        this.saver = saver;
    }

    @Override
    public void saving(IdentifierParsedLog identifier, LogNode node)
    {
        //  Before saving log node we need to cache meta inf
        cachingMetaInf(node);
        saver.save(identifier, node);
    }

    @Override
    public void invalidate(IdentifierParsedLog identifier)
    {
        saveMetaInf(identifier, saver);
        saver.complete(identifier);
    }
}

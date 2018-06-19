package com.support.log.storage.saving;

import com.support.log.model.node.LogNode;
import com.support.log.storage.IdentifierParsedLog;

/**
 *
 */
public interface SavingStrategy
{
    void saving(IdentifierParsedLog identifier, LogNode node);
    void invalidate(IdentifierParsedLog identifier);
}

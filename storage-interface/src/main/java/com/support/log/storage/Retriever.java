package com.support.log.storage;

import java.util.Iterator;

/**
 *
 */
public interface Retriever
{
    <V> Iterator<V> get(IdentifierParsedLog identifier, StorageCommand<V> command);
    //<V> void changeLog(IdentifierParsedLog identifier, StorageCommand<V> command);
}

package com.support.log.storage;

import java.util.List;

/**
 *  Command defines how the storage should work with some data
 */
public interface StorageCommand<V>
{
    enum Command
    {
        FIND, FIND_ALL, UPDATE, REMOVE, REMOVE_ALL;
    }
    void setData(V... value);
    void setData(IdentifierParsedLog identifier, V... value);
    void setData(IdentifierParsedLog identifier);
    IdentifierParsedLog getIdentifier();
    String sortBy();
    Class<V> getType();
    Command getCommandType();
    String getSelector();
    List<String> getSelectors();
    String getOldValue();
    String getNewValue();
    long getSize();


}

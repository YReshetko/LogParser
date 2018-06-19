package com.support.log.storage;

import com.support.log.model.node.LogNode;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Determines list of method which we can use for work with log nodes
 */
public interface StorageService
{
    /**
     * Parses log files and save it into storage
     * @param identifier - identifier of log
     * @param files - list of log files
     */
    Future<IdentifierParsedLog> process(IdentifierParsedLog identifier, List<File> files);

    /**
     * Retrieves iterator of log nodes for command
     * @param identifier - identifier of log
     * @param command - determines what log we need to retrieve
     * @return - list of log nodes
     */
    <V> Iterator<V> getIterator(IdentifierParsedLog identifier, StorageCommand<V> command);

    /**
     *
     * @param identifier
     * @param command
     * @return
     */
    //  TODO make return value as OutputStream or similar in NIO
    String getLog(IdentifierParsedLog identifier, StorageCommand<LogNode> command);

    /**
     * Use for updating, removing etc
     * @param identifier - identifier of log
     * @param command - command to execute
     * @return - true if success
     */
    boolean changeLog(IdentifierParsedLog identifier, StorageCommand command);

    /**
     * Setup storage context
     * @param context - context, contains implementations of parser, saver, retriever etc
     */
    void setStorageContext(StorageContext context);
}

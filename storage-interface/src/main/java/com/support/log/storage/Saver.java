package com.support.log.storage;


import com.support.log.model.node.LogNode;
import com.support.log.model.thread.descriptor.ThreadDescriptor;
import com.support.log.model.thread.info.ThreadsInfo;

/**
 *
 */
public interface Saver
{
    /**
     * Save log node
     * @param value - log node
     * @param identifier - log identifier
     * @return - true if log node was saved successfully
     */
    boolean save(IdentifierParsedLog identifier, LogNode value);

    /**
     * Save threads info
     * @param value - log threads info
     * @param identifier - log identifier
     * @return - true if log node was saved successfully
     */
    boolean save(IdentifierParsedLog identifier, ThreadsInfo value);

    /**
     * Save thread descriptor
     * @param value - log thread descriptor
     * @param identifier - log identifier
     * @return - true if log node was saved successfully
     */
    boolean save(IdentifierParsedLog identifier, ThreadDescriptor value);
    /**
     * Finish save (save info files)
     * @param identifier - log identifier
     * @return - true if saving was successfully
     */
    boolean complete(IdentifierParsedLog identifier);
}

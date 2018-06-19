package com.support.plugin;

import com.support.log.model.node.LogNode;
import com.support.log.storage.StorageCommand;

/**
 *
 */
public interface IPluginPostProcessor extends IPluginSetup
{
    /**
     * Process separate log node to collect some information
     * @param node - log node
     */
    void process(LogNode node);
    /**
     * Prepares and returns command to execute on log storage after processing
     * @return - command to execute on storage side
     */
    StorageCommand<LogNode> getPostRequest();
}

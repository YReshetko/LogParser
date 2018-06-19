package com.support.plugin;

import com.support.log.model.node.LogNode;
import com.support.log.storage.StorageCommand;

/**
 *
 */
public interface IPluginSelector extends IPluginSetup
{
    /**
     * Prepare and return command to select LogNodes From Storage
     * @return - command to execute on storage side
     */
    StorageCommand<LogNode> getRequest();

}

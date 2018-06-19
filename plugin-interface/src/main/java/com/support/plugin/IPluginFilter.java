package com.support.plugin;

import com.support.log.model.node.LogNode;

/**
 *
 */
public interface IPluginFilter extends IPluginSetup
{
    /**
     * Returns true if we can proceed with this log node
     * @param node - log node to check
     * @return - bool
     */
    boolean filter(LogNode node);
}

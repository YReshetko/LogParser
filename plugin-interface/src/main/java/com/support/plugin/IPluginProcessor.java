package com.support.plugin;


import com.support.log.model.node.LogNode;
import com.support.plugin.model.output.PluginOutput;

/**
 *
 */
public interface IPluginProcessor extends IPluginSetup
{
    /**
     * Process separate log node to collect some information
     * @param node - log node
     */
    void process(LogNode node);

    /**
     * @return - result of plugin execution
     */
    PluginOutput getResult();
}

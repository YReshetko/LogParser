package com.support.plugin;

import com.support.plugin.model.descriptor.PluginToStore;

import java.io.File;
import java.util.List;

/**
 *
 */
public interface IPluginFactory
{
    List<PluginToStore> savePlugin(File file);
    void updatePlugins();
    <V> V getPlugin(PluginToStore plugin);

}

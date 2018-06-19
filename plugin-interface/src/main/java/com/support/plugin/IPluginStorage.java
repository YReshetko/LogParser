package com.support.plugin;

import com.support.plugin.model.descriptor.PluginToStore;

import java.util.List;

/**
 *
 */
public interface IPluginStorage
{
    void save(PluginToStore plugin);
    List<PluginToStore> get();
    void update(PluginToStore plugin);
    void remove(PluginToStore plugin);
}

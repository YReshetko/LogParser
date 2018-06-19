package com.support.log.storage.saving;

import com.support.core.BaseLogger;
import com.support.log.model.node.LogNode;
import com.support.log.model.thread.descriptor.ThreadDescriptor;
import com.support.log.model.thread.info.ThreadsInfo;
import com.support.log.storage.IdentifierParsedLog;
import com.support.log.storage.Saver;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Yurchik
 * Date: 13.07.17
 * Time: 21:18
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSavingStrategy extends BaseLogger implements SavingStrategy
{
    private final MetaInformationCollector cache;
    public AbstractSavingStrategy()
    {
        cache = new MetaInformationCollector();
    }
    protected void cachingMetaInf(LogNode node)
    {
        cache.cacheMetaInf(node);
    }
    /**
     * Save meta information about log (threads info)
     * @param identifier - log identifier
     * @return - true if saving has success
     */
    protected boolean saveMetaInf(IdentifierParsedLog identifier, Saver saver)
    {
        List<ThreadDescriptor> descriptors = cache.getThreadDescriptors();
        descriptors.forEach(value -> saver.save(identifier, value));
        ThreadsInfo info = cache.getThreadsInfo();
        saver.save(identifier, info);
        return true;
    }
}

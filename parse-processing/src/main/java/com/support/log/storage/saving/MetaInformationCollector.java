package com.support.log.storage.saving;

import com.support.log.model.node.LogNode;
import com.support.log.model.thread.descriptor.LogIdRange;
import com.support.log.model.thread.descriptor.ThreadDescriptor;
import com.support.log.model.thread.info.ThreadEntry;
import com.support.log.model.thread.info.ThreadsInfo;

import java.util.*;

public class MetaInformationCollector
{
    private Map<String, ThreadDescriptor> cache;
    public MetaInformationCollector() {
        cache = new LinkedHashMap<>();
    }
    public void cacheMetaInf(LogNode node) {
        String nodeFileName = String.valueOf(node.getId());
        ThreadDescriptor descriptor = computeThreadDescriptorIfAbsent(node);
        descriptor.getNodesNumbers().add(nodeFileName);
        descriptor.setEndTime(node.getLongDateTime());
    }

    private ThreadDescriptor computeThreadDescriptorIfAbsent(LogNode node) {
        String threadKey = node.getName();
        ThreadDescriptor descriptor;
        if (cache.containsKey(threadKey)) {
            descriptor = cache.get(threadKey);
        } else {
            descriptor = initThreadDescriptorInCache(node);
        }
        return descriptor;
    }

    private ThreadDescriptor initThreadDescriptorInCache(LogNode node) {
        String threadKey = node.getName();
        ThreadDescriptor descriptor = new ThreadDescriptor();
        descriptor.setName(threadKey);
        descriptor.setStartTime(node.getLongDateTime());
        cache.put(threadKey, descriptor);
        descriptor.setId((long) cache.size());
        return descriptor;
    }

    public ThreadsInfo getThreadsInfo() {
        ThreadsInfo threadsInfo = new ThreadsInfo();
        cache.forEach((key, value) -> threadsInfo
                .getThreads()
                .add(createThreadEntry(key, value)));
        threadsInfo
                .getThreads()
                .sort(Comparator.comparingLong(ThreadEntry::getStartTime));
        return threadsInfo;
    }

    private ThreadEntry createThreadEntry(String logNodesName, ThreadDescriptor threadDescriptor) {
        ThreadEntry threadEntry = new ThreadEntry();
        String id = String.valueOf(threadDescriptor.getId());
        threadEntry.setFileName(id);
        threadEntry.setThreadName(logNodesName);
        //TODO Fix that NullPointer que to StartTime and EndTime are required into xsd
        threadEntry.setStartTime(threadDescriptor.getStartTime()!=null?threadDescriptor.getStartTime():0);
        threadEntry.setEndTime(threadDescriptor.getEndTime()!=null?threadDescriptor.getEndTime():0);
        threadEntry.setIsDelete(false);
        return threadEntry;
    }

    public List<ThreadDescriptor> getThreadDescriptors()
    {
        final List<ThreadDescriptor> out = new ArrayList<>();
        cache.values().forEach(value -> out.add(calculateRange(value)));
        return out;
    }

    private ThreadDescriptor calculateRange(ThreadDescriptor descriptor)
    {
        // TODO needs optimization
        Collections.sort(descriptor.getNodesNumbers());
        LogIdRange range = null;
        List<LogIdRange> ranges = new ArrayList<>();
        for (String id : descriptor.getNodesNumbers())
        {
            Long lId = Long.valueOf(id);
            if(range == null)
            {
                range = new LogIdRange();
                range.setFirstId(lId);
                range.setLastId(lId);
                ranges.add(range);
                continue;
            }
            if(lId - range.getLastId() == 1)
            {
                range.setLastId(lId);
            }
            else
            {
                range = new LogIdRange();
                range.setFirstId(lId);
                range.setLastId(lId);
                ranges.add(range);
            }
        }
        descriptor.getIdRanges().addAll(ranges);
        return descriptor;
    }

}

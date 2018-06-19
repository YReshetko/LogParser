package com.support.log.storage.saving;

import com.support.log.model.node.LogNode;
import com.support.log.model.thread.descriptor.LogIdRange;
import com.support.log.model.thread.descriptor.ThreadDescriptor;
import com.support.log.model.thread.info.ThreadEntry;
import com.support.log.model.thread.info.ThreadsInfo;
import com.support.log.storage.util.TestDataLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

@RunWith(JUnitPlatform.class)
class MetaInformationCollectorTest {

    private static MetaInformationCollector metaInformationCollector;
    private static List<LogNode> nodesToTest;

    @BeforeAll
    static void setupTestClass(){
        LogNode[] nodes = TestDataLoader.loadLogNodes("lognodes/parser/saving/LogNodesList.json");
        nodesToTest = Arrays.asList(nodes);
        metaInformationCollector = new MetaInformationCollector();
        nodesToTest.forEach(metaInformationCollector::cacheMetaInf);
    }

    @Test
    void threadDescriptorsCount(){
        List<ThreadDescriptor> descriptors = metaInformationCollector.getThreadDescriptors();
        assertEquals(3, descriptors.size());
    }
    @Test
    void threadDescriptorsName(){
        List<ThreadDescriptor> descriptors = metaInformationCollector.getThreadDescriptors();
        List<String> threadNames = descriptors.stream().map(ThreadDescriptor::getName).collect(Collectors.toList());
        assertAll("All names must match",
                () -> assertTrue(threadNames.contains("http-0.0.0.0-8080-1")),
                () -> assertTrue(threadNames.contains("http-0.0.0.0-8080-2")),
                () -> assertTrue(threadNames.contains("http-0.0.0.0-8080-3"))
        );
    }

    @Test
    void threadDescriptorsRangesSize(){
        List<ThreadDescriptor> descriptors = metaInformationCollector.getThreadDescriptors();
        Map<String, ThreadDescriptor> descriptorsByName = descriptors
                .stream()
                .collect(Collectors.toMap(ThreadDescriptor::getName, element -> element));
        List<LogIdRange> firstLogIdRanges = descriptorsByName.get("http-0.0.0.0-8080-1").getIdRanges();
        List<LogIdRange> secondLogIdRanges = descriptorsByName.get("http-0.0.0.0-8080-2").getIdRanges();
        List<LogIdRange> thirdLogIdRanges = descriptorsByName.get("http-0.0.0.0-8080-3").getIdRanges();
        assertAll("Ranges of all threads must match by size",
                () -> assertEquals(3, firstLogIdRanges.size()),
                () -> assertEquals(1, secondLogIdRanges.size()),
                () -> assertEquals(2, thirdLogIdRanges.size())
        );
    }

    @Test
    void threadDescriptorsRange(){
        List<ThreadDescriptor> descriptors = metaInformationCollector.getThreadDescriptors();
        Map<String, ThreadDescriptor> descriptorsByName = descriptors
                .stream()
                .collect(Collectors.toMap(ThreadDescriptor::getName, element -> element));
        List<LogIdRange> firstLogIdRanges = descriptorsByName.get("http-0.0.0.0-8080-1").getIdRanges();
        List<LogIdRange> secondLogIdRanges = descriptorsByName.get("http-0.0.0.0-8080-2").getIdRanges();
        List<LogIdRange> thirdLogIdRanges = descriptorsByName.get("http-0.0.0.0-8080-3").getIdRanges();
        assertAll("Ranges of http-0.0.0.0-8080-1 must match",
                () -> assertEquals(Long.valueOf(1), firstLogIdRanges.get(0).getFirstId()),
                () -> assertEquals(Long.valueOf(1), firstLogIdRanges.get(0).getLastId()),
                () -> assertEquals(Long.valueOf(4), firstLogIdRanges.get(1).getFirstId()),
                () -> assertEquals(Long.valueOf(4), firstLogIdRanges.get(1).getLastId()),
                () -> assertEquals(Long.valueOf(8), firstLogIdRanges.get(2).getFirstId()),
                () -> assertEquals(Long.valueOf(9), firstLogIdRanges.get(2).getLastId())
        );
        assertAll("Ranges of http-0.0.0.0-8080-2 must match",
                () -> assertEquals(Long.valueOf(2), secondLogIdRanges.get(0).getFirstId()),
                () -> assertEquals(Long.valueOf(2), secondLogIdRanges.get(0).getLastId())
        );
        assertAll("Ranges of http-0.0.0.0-8080-3 must match",
                () -> assertEquals(Long.valueOf(3), thirdLogIdRanges.get(0).getFirstId()),
                () -> assertEquals(Long.valueOf(3), thirdLogIdRanges.get(0).getLastId()),
                () -> assertEquals(Long.valueOf(5), thirdLogIdRanges.get(1).getFirstId()),
                () -> assertEquals(Long.valueOf(7), thirdLogIdRanges.get(1).getLastId())
        );
    }

    @Test
    void threadInfoEntriesSize() {
        ThreadsInfo threadsInfo = metaInformationCollector.getThreadsInfo();
        assertEquals(3, threadsInfo.getThreads().size());
    }

    @Test
    void threadInfoFilling(){
        ThreadsInfo threadsInfo = metaInformationCollector.getThreadsInfo();
        Map<String, ThreadEntry> entriesByThread = threadsInfo.getThreads().stream().collect(Collectors.toMap(ThreadEntry::getThreadName, entry -> entry));
        assertAll("Thread info for http-0.0.0.0-8080-1 must match",
                () -> assertEquals("1", entriesByThread.get("http-0.0.0.0-8080-1").getFileName()),
                () -> assertEquals(1001L, entriesByThread.get("http-0.0.0.0-8080-1").getStartTime()),
                () -> assertEquals(1009L, entriesByThread.get("http-0.0.0.0-8080-1").getEndTime())
        );
        assertAll("Thread info for http-0.0.0.0-8080-2 must match",
                () -> assertEquals("2", entriesByThread.get("http-0.0.0.0-8080-2").getFileName()),
                () -> assertEquals(1002L, entriesByThread.get("http-0.0.0.0-8080-2").getStartTime()),
                () -> assertEquals(1002L, entriesByThread.get("http-0.0.0.0-8080-2").getEndTime())
        );
        assertAll("Thread info for http-0.0.0.0-8080-3 must match",
                () -> assertEquals("3", entriesByThread.get("http-0.0.0.0-8080-3").getFileName()),
                () -> assertEquals(1003L, entriesByThread.get("http-0.0.0.0-8080-3").getStartTime()),
                () -> assertEquals(1007L, entriesByThread.get("http-0.0.0.0-8080-3").getEndTime())
        );
    }


}

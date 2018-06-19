package com.support.log.storage.integration;

import com.support.log.storage.IdentifierParsedLog;
import com.support.log.storage.LogStorageImpl;
import com.support.log.storage.StorageService;
import com.support.log.storage.integration.dummy.TestIdentifierParsedLog;
import com.support.log.storage.integration.dummy.TestStorageContext;
import com.support.log.storage.util.TestDataLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.io.File;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertTimeout;

@RunWith(JUnitPlatform.class)
class LogStorageImplTestIntegration {

    private static StorageService service;
    private static File logFile;
    @BeforeAll
    public static void parseProcessingCoreTest(){
        service = new LogStorageImpl(new TestStorageContext());
        logFile = TestDataLoader.loadFile("integration/matrixtdp.log");
    }

    @Test
    void testLogProcessing() {
        Future<IdentifierParsedLog> future = service.process(new TestIdentifierParsedLog(), Collections.singletonList(logFile));
        assertTimeout(Duration.ofSeconds(10), (ThrowingSupplier<IdentifierParsedLog>) future::get);
    }


}

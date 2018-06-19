package com.support.log.storage.parser.mapping;

import com.support.log.model.node.LogNode;
import com.support.log.storage.util.TestDataLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static com.support.log.storage.util.CommonTestConstants.*;

@RunWith(JUnitPlatform.class)
public class LogNodeToStringMappingTest {
    private static LogNodeToStringMapping mappingToTest;
    @BeforeAll
    private static void setUpTests(){
        mappingToTest = new LogNodeToStringMapping();
    }

    @Test
    public void testLogNodeFullyPopulated(){
        List<String> listToBePopulated = new ArrayList<>();
        LogNode node = TestDataLoader.loadLogNode("lognodes/parser/mapping/CorrectLogNode");
        nodeEntryRegExps.forEach(nodeEntryRegExps -> mappingToTest.populate(nodeEntryRegExps, node, listToBePopulated));
        assertAll("All elements of populated list must be equal",
                () -> assertEquals(node.getDate(), listToBePopulated.get(0)),
                () -> assertEquals(node.getTime(), listToBePopulated.get(1)),
                () -> assertEquals(node.getMillisecond(), listToBePopulated.get(2)),
                () -> assertEquals("DEBUG", listToBePopulated.get(3)),
                () -> assertEquals(node.getName(), listToBePopulated.get(4)),
                () -> assertEquals("com.test.packege.SomeClass", listToBePopulated.get(5))
        );
    }
}

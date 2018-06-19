package com.support.log.storage.util;

import com.support.core.util.JsonUtils;
import com.support.log.model.node.LogNode;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;

public class TestDataLoader {
    public static LogNode loadLogNode(String filename){
        return loadData(filename, LogNode.class);
    }
    public static LogNode[] loadLogNodes(String filename){
        return loadData(filename, LogNode[].class);
    }

    private static <V> V loadData(String filename, Class<V> loadedType){
        URL url = TestDataLoader.class.getClassLoader().getResource(filename);
        try {
            Path path = Paths.get(Objects.requireNonNull(url).toURI());
            String stringLogNode = Files.lines(path).collect(Collectors.joining( "" ));
            return JsonUtils.getObject(stringLogNode, loadedType);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Can't read file " + Objects.requireNonNull(url).toString() + " message: " + e.getMessage());
        }
    }

    public static File loadFile(String path){
        try {
            URL url = TestDataLoader.class.getClassLoader().getResource(path);
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.support.log.storage.parser.mapping;

@FunctionalInterface
public interface BiVoidFunction<K, V>{
    void apply(K k, V v);
}
package com.support.core.profile;

public interface Profiler {

    void before(String operation);
    void after(String operation);
    void clear();
    void clear(String operation);

    void printFullStatistic();
    void printStatistic(String operation);
}

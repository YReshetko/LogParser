package com.support.log.storage;

/**
 *
 */
public interface ParsingProgress
{
    void addTotalSize(long size);
    void setTotalSize(long size);
    void subtractSize(long size);
    double getProgress();
}

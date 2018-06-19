package com.support.log.storage;

import com.support.log.model.node.LogNode;

import java.text.ParseException;

/**
 * Log node parser converts String to LogNode and LogNode back to String according log patterns
 */
public interface Parser
{
    LogNode parse(String value) throws ParseException;
    String parse(LogNode node);
    boolean hasStamp(String logLine);
}

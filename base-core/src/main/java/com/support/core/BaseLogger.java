package com.support.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class BaseLogger
{
    private Logger logger;
    protected boolean isLogging()
    {
        return getOrCreateLoggerIfNotExist().isDebugEnabled();
    }

    protected void log(String msg)
    {
        getOrCreateLoggerIfNotExist().debug(msg);
    }
    protected void error(String msg, Throwable e)
    {
        getOrCreateLoggerIfNotExist().error(msg, e);
    }

    private Logger getOrCreateLoggerIfNotExist() {
        if(logger == null) {
            logger = LogManager.getLogger(this.getClass().getName());
        }
        return logger;
    }
}

package com.support.log.storage;

import com.support.log.model.filedescroptor.LogFilesDescriptor;

/**
 * Simply it's log ID which help to determine log definitely
 */
public interface IdentifierParsedLog
{
    String getKey();
    LogFilesDescriptor getLogDescriptor();
}

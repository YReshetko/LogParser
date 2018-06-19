package com.support.log.storage.integration.dummy;

import com.support.log.model.filedescroptor.LogFilesDescriptor;
import com.support.log.storage.IdentifierParsedLog;

public class TestIdentifierParsedLog implements IdentifierParsedLog {
    @Override
    public String getKey() {
        return "";
    }

    @Override
    public LogFilesDescriptor getLogDescriptor() {
        return null;
    }
}

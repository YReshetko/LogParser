package com.support.log.storage.integration.dummy;

import com.support.log.model.node.LogNode;
import com.support.log.storage.IdentifierParsedLog;
import com.support.log.storage.StorageCommand;

import java.util.List;

public class DummyCommand implements StorageCommand<LogNode> {
    @Override
    public void setData(LogNode... value) {

    }

    @Override
    public void setData(IdentifierParsedLog identifier, LogNode... value) {

    }

    @Override
    public void setData(IdentifierParsedLog identifier) {

    }

    @Override
    public IdentifierParsedLog getIdentifier() {
        return null;
    }

    @Override
    public String sortBy() {
        return null;
    }

    @Override
    public Class<LogNode> getType() {
        return LogNode.class;
    }

    @Override
    public Command getCommandType() {
        return null;
    }

    @Override
    public String getSelector() {
        return null;
    }

    @Override
    public List<String> getSelectors() {
        return null;
    }

    @Override
    public String getOldValue() {
        return null;
    }

    @Override
    public String getNewValue() {
        return null;
    }

    @Override
    public long getSize() {
        return 0;
    }
}

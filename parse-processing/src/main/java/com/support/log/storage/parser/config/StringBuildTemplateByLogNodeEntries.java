package com.support.log.storage.parser.config;

import java.util.List;

public interface StringBuildTemplateByLogNodeEntries {
    String build(List<String> logEntries);
    String[] getTemplate();
}

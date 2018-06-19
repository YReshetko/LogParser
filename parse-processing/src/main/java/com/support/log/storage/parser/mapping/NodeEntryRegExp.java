package com.support.log.storage.parser.mapping;

public class NodeEntryRegExp {
    private final String name;
    private final String regexp;
    private final int positionInLine;


    public NodeEntryRegExp(String name, String regexp, int positionInLine) {
        this.name = name;
        this.regexp = regexp;
        this.positionInLine = positionInLine;
    }

    public String getName() {
        return name;
    }

    public String getRegexp() {
        return regexp;
    }

    public int getPositionInLine() {
        return positionInLine;
    }
}

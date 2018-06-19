package com.support.log.storage;

/**
 * Created with IntelliJ IDEA.
 * User: Yurchik
 * Date: 23.06.17
 * Time: 20:26
 * To change this template use File | Settings | File Templates.
 */
public interface StorageContext
{
    Parser getParser();
    Saver getSaver();
    Retriever getRetriever();
    ParsingProgress getParsingProgress();
}

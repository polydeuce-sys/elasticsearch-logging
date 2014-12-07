package com.polydeucesys.eslogging.core;

/**
 * 
 * @author Kevin McLellan
 *
 * @param <L> - A Log type (for example LogRecord in Log4J). The serializer is responsible for handling the 
 * Serialization of the log from the internal format of the logging framework to a Json String for 
 * submission to ElasticSearch.
 * @param<D> - A document type (for example a Json String) which the log is serialized to before insertion
 */
public interface LogSerializer<L, D> {
	D serializeLog(final L log) throws LogSubmissionException;
}

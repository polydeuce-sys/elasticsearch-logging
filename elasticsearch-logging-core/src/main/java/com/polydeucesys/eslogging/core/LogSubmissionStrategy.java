package com.polydeucesys.eslogging.core;

/**
 * Defines the interface for a submission strategy for documents to ElasticSearch in a log appender context.
 * The strategy defines the queueing of log messages to publish and error handling on publication. Note that
 * in general, as the strategy may queue blocks of messages, abnormal application termination can cause the 
 * logs not to be flushed to ElasticSearch, and as such it may be required to maintain an additional file
 * or similar appender which may provide the missing log information. 
 *  
 * @author Kevin McLellan
 *
 * @param <L> - A serialized Log type (for example Index in Jest).
 * @param <D> - A Document type (for example a Bulk object in Jest) as submitted to the ElasticSearch cluster
 * @param <R> - A Result type (for example a JestResult String) as returned from the ElasticSearch cluster on submission
 */
public interface LogSubmissionStrategy<L, D, R> {
	void setQueueDepth(final int queueDepth);
	int getQueueDepth();
	void setMaxSubmissionIntervalMillisec(final long maxSubmissionInterval);
	long getMaxSubmissionIntervalMillisec();
	void setConnection(final Connection<D, R> connection);
	Connection<D, R> getConnection();
	void setErrorHandler( LogAppenderErrorHandler errorHandler);
	LogAppenderErrorHandler getErrorHandler();
	void submit( final L log ) throws LogSubmissionException;
	void close() throws LogSubmissionException;
}

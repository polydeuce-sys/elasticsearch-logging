package com.polydeucesys.eslogging.core;

/**
 * Defines the interface for a queueing submission strategy for documents to storage in a log appender context.
 * The strategy defines the queueing of log messages to publish and error handling on publication. Note that
 * in general, as the strategy may queue blocks of messages, abnormal application termination can cause the 
 * logs not to be flushed to storage, and as such it may be required to maintain an additional file
 * or similar appender which may provide the missing log information. 
 *  
 * @author Kevin McLellan
 * @version 1.0
 *
 * @param <L> - A serialized Log type (for example Index in Jest).
 * @param <D> - A Document type (for example a Bulk object in Jest) as submitted to the ElasticSearch cluster
 * @param <R> - A Result type (for example a JestResult String) as returned from the ElasticSearch cluster on submission
 */
public interface LogSubmissionQueueingStrategy<L, D, R> extends LogSubmissionStrategy<L, D, R>, LifeCycle{
	/**
	 * Set the number of log objects to queue before submission. Each time this limit is reached, the queued 
	 * log objects will be submitted
	 * @param queueDepth
	 * 				{@code int} representing the queue size which will force submission
	 */
	void setQueueDepth(final int queueDepth);
	
	/**
	 * 
	 * @return {@code int} representing the queue size which will force submission
	 */
	int getQueueDepth();
	
	/**
	 * Sets the interval on which the queue will be flushed if the number of items has not reached teh queue depth.
	 * This is used to ensure application with infrequent logs at some time periods still store them with
	 * reasonable frequency
	 * @param maxSubmissionInterval
	 * 		{@code int} the maximum interval between submissions. Submission will be attempted at half this interval.
	 */
	void setMaxSubmissionIntervalMillisec(final long maxSubmissionInterval);
	
	/**
	 * @return
	 * 	 {@code int} the maximum interval between submissions
	 */
	long getMaxSubmissionIntervalMillisec();
}

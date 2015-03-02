package com.polydeucesys.eslogging.core;

/**
 * Defines a communication mechanism between the logging framework and 
 * the storage system, whereby documents of type {@code D} can be 
 * submitted and responses of type {@code R} are returned.
 * @author Kevin McLellan
 * @version 1.0
 *
 * @param <D> 
 * 			The type of the documents being submitted
 * @param <R>
 * 			The type of the successful response from the storage
 */
public interface Connection<D, R> extends LifeCycle{
	void setConnectionString(final String connectionString);
	String getConnectionString();
	R submit(final D document) throws LogSubmissionException;
	void submitAsync( final D document, AsyncSubmitCallback<R> callback);
}

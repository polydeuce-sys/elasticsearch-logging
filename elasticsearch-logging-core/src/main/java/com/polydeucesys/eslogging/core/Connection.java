package com.polydeucesys.eslogging.core;

public interface Connection<D, R> {
	void setConnectionString(final String connectionString);
	String getConnectionString();
	void connect();
	R submit(final D document) throws LogSubmissionException;
	void submitAsync( final D document, AsyncSubmitCallback<R> callback);
}

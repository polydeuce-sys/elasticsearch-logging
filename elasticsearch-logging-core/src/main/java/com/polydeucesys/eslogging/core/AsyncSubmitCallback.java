package com.polydeucesys.eslogging.core;

/**
 * Provides an abstraction for callbacks from asynchronous submission.
 * An example implementation would wrap a JestResultHander for Jest client
 * based communications. 
 * @author Kevin McLellan
 *
 */
public interface AsyncSubmitCallback<R> {
	void completed( final R result );
	void error( final LogSubmissionException wrappedException);
}

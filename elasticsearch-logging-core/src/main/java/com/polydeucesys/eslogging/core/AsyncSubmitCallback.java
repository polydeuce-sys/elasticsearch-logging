package com.polydeucesys.eslogging.core;

/**
 * Provides an abstraction for callbacks from asynchronous submission.
 * An example implementation would wrap a JestResultHander for Jest client
 * based communications. 
 * @author Kevin McLellan
 * @version 1.0
 *
 */
public interface AsyncSubmitCallback<R> {
	/**
	 * Called when the submission has been successful.
	 * @param result 
	 * 				Instance of the result type used in the comminucation framework (for example, JestResult)
	 */
	void completed( final R result );
	
	/**
	 * Called when the submission has failed. Any errors from the communication framework are wrapped in the {@code wrappedException}
	 * @param wrappedException {@link LogSubmissionException} wrapping error information.
	 */
	void error( final LogSubmissionException wrappedException);
}

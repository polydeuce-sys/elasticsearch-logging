package com.polydeucesys.eslogging.core;

/**
 * An exception classed used for both exceptions thrown in this component, and as a wrapper for 
 * exceptions thrown in frameworks or components used.
 * @author Kevin McLellan
 * @version 1.0
 *
 */
public class LogSubmissionException extends Exception {

	private static final long serialVersionUID = -8041186358570402381L;
	
	/**
	 * Creates an exception with the given message
	 * @param message
	 */
	public LogSubmissionException(final String message){
		super(message);
	}
	
	/**
	 * creates an exception with the given message and wrapped cause
	 * @param message
	 * @param cause
	 */
	public LogSubmissionException(final String message, final Throwable cause){
		super(message, cause);
	}
	
}

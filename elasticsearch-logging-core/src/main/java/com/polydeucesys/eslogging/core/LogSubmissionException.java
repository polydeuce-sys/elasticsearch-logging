package com.polydeucesys.eslogging.core;

/**
 * 
 * @author Kevin McLellan
 *
 */
public class LogSubmissionException extends Exception {

	private static final long serialVersionUID = -8041186358570402381L;
	
	public LogSubmissionException(final String message){
		super(message);
	}
	
	public LogSubmissionException(final String message, final Throwable cause){
		super(message, cause);
	}
	
}

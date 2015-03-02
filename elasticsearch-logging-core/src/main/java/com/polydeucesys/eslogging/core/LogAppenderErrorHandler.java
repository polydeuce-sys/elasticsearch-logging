package com.polydeucesys.eslogging.core;

/**
 * An abstraction of error handling in the logging frameworks. 
 * @author Kevin McLellan
 * @version 1.0
 *
 */
public interface LogAppenderErrorHandler {
	/**
	 * Handle the wrapped error as appropriate
	 * @param ex 
	 * 			{@link LogSubmissionException} wrapping the error be it from this framework or the implementation it uses
	 */
	void error(LogSubmissionException ex);
}

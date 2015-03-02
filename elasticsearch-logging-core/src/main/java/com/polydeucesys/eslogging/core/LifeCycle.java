package com.polydeucesys.eslogging.core;

/**
 * Provides the basic lifecycle points for an appender. Used to abstract
 * similar facilities such as  {@code activateOptions} and {@code stop} in 
 * {@code log4j 1.x}
 * @author Kevin McLellan
 * @version 1.0
 *
 */
public interface LifeCycle {
	/**
	 * Perform any required startup. 
	 * @throws LogSubmissionException wrapping any startup errors
	 */
	void start() throws LogSubmissionException;
	/**
	 * Perform any required shutdown
	 * @throws LogSubmissionException wrapping any shutdown errors
	 */
	void stop() throws LogSubmissionException;
}

package com.polydeucesys.eslogging.core;
/**
 * Copyright (c) 2016 Polydeuce-Sys Ltd
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 **/
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

	/**
	 * Indicate if the LifeCycle has isStarted and not stopped
	 * @return true if isStarted and not stopped.
	 */
	boolean isStarted();
}

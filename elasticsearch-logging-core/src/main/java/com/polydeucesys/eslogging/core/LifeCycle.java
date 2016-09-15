package com.polydeucesys.eslogging.core;
/**
 *  Copyright 2016 Polydeuce-Sys Ltd
 *
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *       Unless required by applicable law or agreed to in writing, software
 *       distributed under the License is distributed on an "AS IS" BASIS,
 *       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *       See the License for the specific language governing permissions and
 *       limitations under the License.
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

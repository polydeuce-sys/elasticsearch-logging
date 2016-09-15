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

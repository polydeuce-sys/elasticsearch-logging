package com.polydeucesys.eslogging.log4j2;

import org.apache.logging.log4j.core.ErrorHandler;

import com.polydeucesys.eslogging.core.LogAppenderErrorHandler;
import com.polydeucesys.eslogging.core.LogSubmissionException;
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
 * Wraps the log4j 2.x {@link ErrorHandler} implementation so it can be accessed from the logging implementation
 * independent L{@link com.polydeucesys.eslogging.core.LogAppenderModule}
 */
public class Log4J2ErrorHandlerWrapper implements LogAppenderErrorHandler {
	private static final String ERROR_HANDLER_FORMAT = "Jest Elasticsearch Logger Error : %s";
	private final ErrorHandler wrapped;

	public Log4J2ErrorHandlerWrapper(final ErrorHandler wrapped){
		this.wrapped = wrapped;
	}
	
	@Override
	public void error(LogSubmissionException ex) {
		wrapped.error( String.format(ERROR_HANDLER_FORMAT, ex.getMessage()), ex);
	}
}

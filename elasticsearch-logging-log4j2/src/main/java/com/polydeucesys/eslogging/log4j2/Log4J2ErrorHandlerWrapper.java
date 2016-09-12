package com.polydeucesys.eslogging.log4j2;

import org.apache.logging.log4j.core.ErrorHandler;

import com.polydeucesys.eslogging.core.LogAppenderErrorHandler;
import com.polydeucesys.eslogging.core.LogSubmissionException;
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

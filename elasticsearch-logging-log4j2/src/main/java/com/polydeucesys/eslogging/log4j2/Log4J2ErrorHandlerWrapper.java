package com.polydeucesys.eslogging.log4j2;

import org.apache.logging.log4j.core.ErrorHandler;

import com.polydeucesys.eslogging.core.LogAppenderErrorHandler;
import com.polydeucesys.eslogging.core.LogSubmissionException;

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

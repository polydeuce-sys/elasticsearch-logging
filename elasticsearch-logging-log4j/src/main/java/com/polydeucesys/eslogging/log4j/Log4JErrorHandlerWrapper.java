package com.polydeucesys.eslogging.log4j;

import com.polydeucesys.eslogging.core.LogAppenderErrorHandler;
import com.polydeucesys.eslogging.core.LogSubmissionException;

import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.ErrorHandler;

public class Log4JErrorHandlerWrapper implements LogAppenderErrorHandler {

	private static final String ERROR_HANDLER_FORMAT = "Jest Elasticsearch Logger Error : %s";
	private final ErrorHandler wrapped;	
	
	public Log4JErrorHandlerWrapper(final ErrorHandler wrapped){
		this.wrapped = wrapped;
	}
	
	
	@Override
	public void error(LogSubmissionException ex) {
		wrapped.error( String.format(ERROR_HANDLER_FORMAT, ex.getMessage()), ex, ErrorCode.GENERIC_FAILURE);
	}

}

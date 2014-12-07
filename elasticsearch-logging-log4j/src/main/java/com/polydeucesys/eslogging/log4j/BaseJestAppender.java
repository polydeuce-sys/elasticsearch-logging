package com.polydeucesys.eslogging.log4j;

import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.polydeucesys.eslogging.core.Connection;
import com.polydeucesys.eslogging.core.LogAppenderErrorHandler;
import com.polydeucesys.eslogging.core.LogAppenderModule;
import com.polydeucesys.eslogging.core.LogSerializer;
import com.polydeucesys.eslogging.core.LogSubmissionStrategy;
import com.polydeucesys.eslogging.core.jest.JestConstants;
import com.polydeucesys.eslogging.core.jest.JestHttpConnection;
import com.polydeucesys.eslogging.core.jest.JestLogSubmissionStrategy;
import com.polydeucesys.eslogging.core.jest.SimpleJestIndexSerializer;

public class BaseJestAppender extends AppenderSkeleton{
	
	// Settable properties
	private String connectionString = "";
	private int queueDepth = JestConstants.DEFAULT_QUEUE_DEPTH;
	private long maxSubmissionInterval = JestConstants.DEFAULT_MAX_SUBMISSION_INTERVAL_MILLISECONDS;
	private boolean isOutputLoggerErrorsToStdErr = true;
	// Internals
	private LogAppenderModule<LoggingEvent, Index, Bulk, JestResult> loggingModule;
	private final Object moduleBuildLock = new Object();
	
	public void setConnectionString(String connectionString){
		this.connectionString = connectionString;
	}
	
	public String getConnectionString(){
		return connectionString;
	}

	public int getQueueDepth() {
		return queueDepth;
	}

	public void setQueueDepth(int queueDepth) {
		this.queueDepth = queueDepth;
	}

	public long getMaxSubmissionInterval() {
		return maxSubmissionInterval;
	}

	public void setMaxSubmissionInterval(long maxSubmissionInterval) {
		this.maxSubmissionInterval = maxSubmissionInterval;
	}

	public boolean isOutputLoggerErrorsToStdErr() {
		return isOutputLoggerErrorsToStdErr;
	}

	public void setOutputLoggerErrorsToStdErr(boolean isOutputLoggerErrorsToStdErr) {
		this.isOutputLoggerErrorsToStdErr = isOutputLoggerErrorsToStdErr;
	}

	@Override
	public void close() {
		
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}
	
	private LogAppenderErrorHandler setupErrorHandler( ){
		org.apache.log4j.spi.ErrorHandler log4j2ErrorHandler = getErrorHandler();
		LogAppenderErrorHandler esJestErrorHandler = isOutputLoggerErrorsToStdErr ?
                											new JestLogSubmissionStrategy.LogSubmissionStdErrErrorHandler() :
                											new JestLogSubmissionStrategy.LogSubmissionNoOpErrorHandler();
		if(log4j2ErrorHandler != null){
			esJestErrorHandler = new Log4JErrorHandlerWrapper(log4j2ErrorHandler);
		}
		return esJestErrorHandler;
	}

	private void buildLoggingModule(){
		LogSerializer<LoggingEvent, Index> logSerializer = new SimpleJestIndexSerializer<LoggingEvent>();
		Connection<Bulk, JestResult> connection = new JestHttpConnection();
		connection.setConnectionString(connectionString);
		connection.connect();
		LogSubmissionStrategy<Index, Bulk, JestResult> logSubmissionStrategy = new JestLogSubmissionStrategy();
		logSubmissionStrategy.setConnection(connection);
		logSubmissionStrategy.setQueueDepth(queueDepth);
		logSubmissionStrategy.setMaxSubmissionIntervalMillisec(maxSubmissionInterval);
		LogAppenderErrorHandler errorHandler = isOutputLoggerErrorsToStdErr ?
				                                    new JestLogSubmissionStrategy.LogSubmissionStdErrErrorHandler() :
				                                    new JestLogSubmissionStrategy.LogSubmissionNoOpErrorHandler();
		loggingModule = new LogAppenderModule<LoggingEvent, Index, Bulk, JestResult>(logSerializer, 
																					 logSubmissionStrategy, 
																					 errorHandler);
		
	}
	
	@Override
	protected void append(LoggingEvent log) {
		if(loggingModule == null){
			synchronized(moduleBuildLock){
				if(loggingModule == null){
					buildLoggingModule();
				}
			}
		}
		loggingModule.append(log);
	}

}

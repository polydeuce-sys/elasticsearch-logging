package com.polydeucesys.eslogging.log4j2;

import java.io.Serializable;

import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;


import com.polydeucesys.eslogging.core.Connection;
import com.polydeucesys.eslogging.core.LogAppenderErrorHandler;
import com.polydeucesys.eslogging.core.LogAppenderModule;
import com.polydeucesys.eslogging.core.LogSerializer;
import com.polydeucesys.eslogging.core.LogSubmissionStrategy;
import com.polydeucesys.eslogging.core.jest.JestConstants;
import com.polydeucesys.eslogging.core.jest.JestHttpConnection;
import com.polydeucesys.eslogging.core.jest.JestLogSubmissionStrategy;
import com.polydeucesys.eslogging.core.jest.SimpleJestIndexSerializer;

public class BaseJestAppender extends AbstractAppender{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8319273193664493254L;
	// Settable properties
	private String connectionString = "";
	private int queueDepth = JestConstants.DEFAULT_QUEUE_DEPTH;
	private long maxSubmissionInterval = JestConstants.DEFAULT_MAX_SUBMISSION_INTERVAL_MILLISECONDS;
	private boolean isOutputLoggerErrorsToStdErr = true;
	// Internals
	private LogAppenderModule<LogEvent, Index, Bulk, JestResult> loggingModule;
	private final Object moduleBuildLock = new Object();
	
	protected BaseJestAppender(String name,
            Filter filter,
            Layout<? extends Serializable> layout){
		super(name, filter, layout);
	}
	
	protected BaseJestAppender(String name,
            Filter filter,
            Layout<? extends Serializable> layout,
            boolean ignoreExceptions){
		super(name, filter, layout, ignoreExceptions);

	}
	
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
	
	private LogAppenderErrorHandler setupErrorHandler( ){
		ErrorHandler log4j2ErrorHandler = getHandler();
		LogAppenderErrorHandler esJestErrorHandler = isOutputLoggerErrorsToStdErr ?
                											new JestLogSubmissionStrategy.LogSubmissionStdErrErrorHandler() :
                											new JestLogSubmissionStrategy.LogSubmissionNoOpErrorHandler();
		if(log4j2ErrorHandler != null){
			esJestErrorHandler = new Log4J2ErrorHandlerWrapper(log4j2ErrorHandler);
		}
		return esJestErrorHandler;
	}

	private void buildLoggingModule(){
		LogSerializer<LogEvent, Index> logSerializer = new SimpleJestIndexSerializer<LogEvent>();
		Connection<Bulk, JestResult> connection = new JestHttpConnection();
		connection.setConnectionString(connectionString);
		connection.connect();
		LogSubmissionStrategy<Index, Bulk, JestResult> logSubmissionStrategy = new JestLogSubmissionStrategy();
		logSubmissionStrategy.setConnection(connection);
		logSubmissionStrategy.setQueueDepth(queueDepth);
		logSubmissionStrategy.setMaxSubmissionIntervalMillisec(maxSubmissionInterval);
		LogAppenderErrorHandler errorHandler = setupErrorHandler();

		loggingModule = new LogAppenderModule<LogEvent, Index, Bulk, JestResult>(logSerializer, 
																					 logSubmissionStrategy, 
																					 errorHandler);
		
	}
	
	@Override
	public void append(LogEvent log) {
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

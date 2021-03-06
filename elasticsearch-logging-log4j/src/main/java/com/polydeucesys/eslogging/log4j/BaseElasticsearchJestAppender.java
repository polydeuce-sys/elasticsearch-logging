package com.polydeucesys.eslogging.log4j;

import java.util.HashMap;
import java.util.Map;

import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

import com.google.gson.Gson;
import com.polydeucesys.eslogging.core.Connection;
import com.polydeucesys.eslogging.core.Constants;
import com.polydeucesys.eslogging.core.LogAppenderErrorHandler;
import com.polydeucesys.eslogging.core.LogAppenderModule;
import com.polydeucesys.eslogging.core.LogSubmissionException;
import com.polydeucesys.eslogging.core.LogSubmissionQueueingStrategy;
import com.polydeucesys.eslogging.core.LogTransform;
import com.polydeucesys.eslogging.core.SimpleDateStampedLogMapper;
import com.polydeucesys.eslogging.core.gson.SimpleGsonLogSerializer;
import com.polydeucesys.eslogging.core.jest.JestConstants;
import com.polydeucesys.eslogging.core.jest.JestHttpConnection;
import com.polydeucesys.eslogging.core.jest.JestIndexStringSerializerWrapper;
import com.polydeucesys.eslogging.core.jest.JestLogSubmissionStrategy;
import com.polydeucesys.eslogging.core.jest.LogMapper;
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
 * Provides the Appender implementation for log4j version 1. The appender wraps the
 * {@link LogAppenderModule} functionality and provides a means of setting the configuration
 * of the module.
 *
 * @author Kevin McLellan
 * @version 1.0
 */
public class BaseElasticsearchJestAppender extends AppenderSkeleton{
	private static final String FAILED_STOP_MSG = "Exception closing BaseElasticsearchJestAppender appender module";
	private static final String FAILED_START_MSG = "Exception starting BaseElasticsearchJestAppender appender module";
	
	// Settable properties
	private String connectionString = "";
	private String logIndexPrefix = "";
	private String logIndexDateFormat = Constants.ELASTICSEARCH_INDEX_DATE_STAMP_FORMAT;
	private String logDocType = "";
	private final Map<String, String> logContextPropertiesMap = new HashMap<String,String>();
	
	private int queueDepth = JestConstants.DEFAULT_QUEUE_DEPTH;
	private long maxSubmissionInterval = JestConstants.DEFAULT_MAX_SUBMISSION_INTERVAL_MILLISECONDS;
	private boolean isOutputLoggerErrorsToStdErr = true;
	// Internals
	private LogAppenderModule<LoggingEvent, LoggingEvent, Index, Bulk, JestResult> loggingModule;
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

	public String getLogIndexPrefix() {
		return logIndexPrefix;
	}

	public void setLogIndexPrefix(String logIndexPrefix) {
		this.logIndexPrefix = logIndexPrefix;
	}

	public String getLogIndexDateFormat() {
		return logIndexDateFormat;
	}

	public void setLogIndexDateFormat(String logIndexDateFormat) {
		this.logIndexDateFormat = logIndexDateFormat;
	}

	public String getLogDocType() {
		return logDocType;
	}

	public void setLogDocType(String logDocType) {
		this.logDocType = logDocType;
	}
	
	public String getLogContextPropertiesJson(){
		Gson gson = new Gson();
		return gson.toJson(logContextPropertiesMap);
	}
	
	@SuppressWarnings("unchecked")
	public void setLogContextPropertiesJson(String propertiesJson ){
		Gson gson = new Gson();
		logContextPropertiesMap.putAll((Map<String,String>)gson.fromJson(propertiesJson, 
				                                                         logContextPropertiesMap.getClass()));
	}

	@Override
	public void close() {
		synchronized(moduleBuildLock){
			try {
				loggingModule.stop();
			} catch (LogSubmissionException e) {
				getErrorHandler().error(FAILED_STOP_MSG, e, ErrorCode.CLOSE_FAILURE);
			}
		}
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

	@Override
	public void activateOptions(){
		synchronized(moduleBuildLock){
			LogTransform<LoggingEvent, LoggingEvent> logTransform = new HostAndCwdLogTransform(logContextPropertiesMap);
		
			LogMapper<LoggingEvent> dateStampedLogMapper = new SimpleDateStampedLogMapper<LoggingEvent>(getLogIndexPrefix(),
																						  getLogIndexDateFormat(),
																						  getLogDocType());
			SimpleGsonLogSerializer<LoggingEvent> wrappedJsonStringSerializer = new SimpleGsonLogSerializer<LoggingEvent>();
			wrappedJsonStringSerializer.setTypeAdapter(LoggingEvent.class, new LoggingEventTypeAdapter());
			JestIndexStringSerializerWrapper<LoggingEvent> logSerializer = new JestIndexStringSerializerWrapper<LoggingEvent>();
			logSerializer.setWrappedJsonStringSerializer(wrappedJsonStringSerializer);
			logSerializer.setIndexMapper(dateStampedLogMapper);
			Connection<Bulk, JestResult> connection = new JestHttpConnection();
			connection.setConnectionString(connectionString);
			LogSubmissionQueueingStrategy<Index, Bulk, JestResult> logSubmissionStrategy = new JestLogSubmissionStrategy();
			logSubmissionStrategy.setConnection(connection);
			logSubmissionStrategy.setQueueDepth(queueDepth);
			logSubmissionStrategy.setMaxSubmissionIntervalMillisec(maxSubmissionInterval);
			LogAppenderErrorHandler errorHandler = setupErrorHandler();
			loggingModule = new LogAppenderModule<LoggingEvent, LoggingEvent, Index, Bulk, JestResult>(	logTransform,
																										logSerializer, 
																										logSubmissionStrategy, 
																										errorHandler);
			try {
				loggingModule.start();
			} catch (LogSubmissionException e) {
				getErrorHandler().error(FAILED_START_MSG, e, ErrorCode.GENERIC_FAILURE);
			}
		}
	}
	
	@Override
	protected void append(LoggingEvent log) {
		// make sure we don't lose our MDC 
		// if the module is asynchronous
		log.getMDCCopy();
		loggingModule.append(log);
	}

}

package com.polydeucesys.eslogging.log4j2;

import com.google.gson.Gson;
import com.polydeucesys.eslogging.core.*;
import com.polydeucesys.eslogging.core.gson.SimpleGsonLogSerializer;
import com.polydeucesys.eslogging.core.jest.*;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
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
 * Provides the Appender implementation for log4j version 2. The appender wraps the
 * {@link LogAppenderModule} functionality and provides a means of setting the configuration
 * of the module.
 *
 * @author Kevin McLellan
 * @version 1.0
 */
@Plugin(name = "BaseElasticsearchJestAppender", category = "Core", elementType = "appender", printObject = true)
public class BaseElasticsearchJestAppender extends AbstractAppender {

	@PluginFactory
	public static BaseElasticsearchJestAppender createAppender(@PluginAttribute("name") String name,
											  @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
											  @PluginElement("Layout") Layout layout,
											  @PluginElement("Filters") Filter filter,
                                              @PluginAttribute("connectionString") String connectionString,
                                              @PluginAttribute("queueDepth") int queueDepth,
                                              @PluginAttribute("maxSubmissionInterval") int maxSubmissionInterval,
                                              @PluginAttribute("logIndexPrefix") String logIndexPrefix,
                                              @PluginAttribute("logDocType") String logDocType) {

		if (name == null) {
			LOGGER.error("No name provided for StubAppender");
			return null;
		}

		if (layout == null) {
			layout = PatternLayout.createDefaultLayout();
		}
        BaseElasticsearchJestAppender appender =  new BaseElasticsearchJestAppender(name, filter, layout, ignoreExceptions);
	    appender.setConnectionString(connectionString);
        appender.setQueueDepth(queueDepth);
        appender.setMaxSubmissionInterval(maxSubmissionInterval);
        appender.setLogIndexPrefix(logIndexPrefix);
        appender.setLogDocType(logDocType);
        return appender;
	}

	private static final String FAILED_STOP_MSG = "Exception closing BaseElasticsearchJestAppender appender module";
	private static final String FAILED_START_MSG = "Exception starting BaseElasticsearchJestAppender appender module";

	private static final long serialVersionUID = 8319273193664493254L;
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
	private LogAppenderModule<LogEvent, PropertyCarryLogEventWrapper, Index, Bulk, JestResult> loggingModule;
	private final Object moduleBuildLock = new Object();
	
	protected BaseElasticsearchJestAppender(String name,
            Filter filter,
            Layout<? extends Serializable> layout){
		super(name, filter, layout);
	}
	
	protected BaseElasticsearchJestAppender(String name,
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
		LogTransform<LogEvent, PropertyCarryLogEventWrapper> logTransform = new HostAndCwdLogTransform(logContextPropertiesMap);
		
		LogMapper<PropertyCarryLogEventWrapper> dateStampedLogMapper = new SimpleDateStampedLogMapper<PropertyCarryLogEventWrapper>(getLogIndexPrefix(),
																					  getLogIndexDateFormat(),
																					  getLogDocType());
		SimpleGsonLogSerializer<PropertyCarryLogEventWrapper> wrappedJsonStringSerializer = new SimpleGsonLogSerializer<PropertyCarryLogEventWrapper>();
		wrappedJsonStringSerializer.setTypeAdapter(PropertyCarryLogEventWrapper.class, new PropertyCarryLogEventWrapperTypeAdapter());
		JestIndexStringSerializerWrapper<PropertyCarryLogEventWrapper> logSerializer = new JestIndexStringSerializerWrapper<PropertyCarryLogEventWrapper>();
		logSerializer.setWrappedJsonStringSerializer(wrappedJsonStringSerializer);
		logSerializer.setIndexMapper(dateStampedLogMapper);
		Connection<Bulk, JestResult> connection = new JestHttpConnection();
		connection.setConnectionString(connectionString);
		LogSubmissionQueueingStrategy<Index, Bulk, JestResult> logSubmissionStrategy = new JestLogSubmissionStrategy();
		logSubmissionStrategy.setConnection(connection);
		logSubmissionStrategy.setQueueDepth(queueDepth);
		logSubmissionStrategy.setMaxSubmissionIntervalMillisec(maxSubmissionInterval);
		LogAppenderErrorHandler errorHandler = setupErrorHandler();

		loggingModule = new LogAppenderModule<LogEvent,PropertyCarryLogEventWrapper, Index, Bulk, JestResult>
                (logTransform, logSerializer, logSubmissionStrategy, errorHandler);
		try {
			loggingModule.start();
		} catch (LogSubmissionException e) {
			error(FAILED_START_MSG, e);
		}
}
	
	@Override 
	public void start(){
		synchronized(moduleBuildLock){
			buildLoggingModule();
		}
		super.start();
	}
	
	@Override
	public void stop(){
		super.stop();
		synchronized(moduleBuildLock){
			try {
				loggingModule.stop();
			} catch (LogSubmissionException e) {
				error(FAILED_STOP_MSG, e);
			}		
		}
	}
	
	@Override
	public void append(LogEvent log) {
		loggingModule.append(log);
	}

}

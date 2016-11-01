package com.polydeucesys.eslogging.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.google.gson.Gson;
import com.polydeucesys.eslogging.core.*;
import com.polydeucesys.eslogging.core.gson.SimpleGsonLogSerializer;
import com.polydeucesys.eslogging.core.jest.*;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import org.slf4j.MDC;

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
 * Provides the Appender implementation for logback. The appender wraps the
 * {@link LogAppenderModule} functionality and provides a means of setting the configuration
 * of the module.
 *
 * @author Kevin McLellan
 * @version 1.0
 */
public class BaseElasticsearchJestAppender extends AppenderBase<ILoggingEvent> {

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
    private LogAppenderModule<ILoggingEvent, ILoggingEvent, Index, Bulk, JestResult> loggingModule;
    private final Object moduleBuildLock = new Object();
    // for logback lifecycle management purposes
    private final ILoggingEventTypeAdapter iLoggingEventTypeAdapter = new ILoggingEventTypeAdapter();

    private final LogbackStatusManagerErrorHandler errorHandler = new LogbackStatusManagerErrorHandler();

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
        LogAppenderErrorHandler esJestErrorHandler = isOutputLoggerErrorsToStdErr ?
                new JestLogSubmissionStrategy.LogSubmissionStdErrErrorHandler() :
                new LogbackStatusManagerErrorHandler();
        return esJestErrorHandler;
    }

    public LogAppenderErrorHandler getErrorHandler() {
        return this.errorHandler;
    }


    @Override
    public void start(){
        synchronized (moduleBuildLock){
            super.start();
            LogTransform<ILoggingEvent, ILoggingEvent> logTransform = (LogTransform<ILoggingEvent, ILoggingEvent>) new HostAndCwdLogTransform(logContextPropertiesMap);

            LogMapper<ILoggingEvent> dateStampedLogMapper = new SimpleDateStampedLogMapper<ILoggingEvent>(getLogIndexPrefix(),
                    getLogIndexDateFormat(),
                    getLogDocType());
            iLoggingEventTypeAdapter.start();
            SimpleGsonLogSerializer<ILoggingEvent> wrappedJsonStringSerializer = new SimpleGsonLogSerializer<ILoggingEvent>();
            wrappedJsonStringSerializer.setTypeAdapter(ILoggingEvent.class, iLoggingEventTypeAdapter);
            wrappedJsonStringSerializer.setTypeAdapter(LoggingEvent.class, iLoggingEventTypeAdapter);
            JestIndexStringSerializerWrapper<ILoggingEvent> logSerializer = new JestIndexStringSerializerWrapper<ILoggingEvent>();
            logSerializer.setWrappedJsonStringSerializer(wrappedJsonStringSerializer);
            logSerializer.setIndexMapper(dateStampedLogMapper);
            Connection<Bulk, JestResult> connection = new JestHttpConnection();
            connection.setConnectionString(connectionString);
            LogSubmissionQueueingStrategy<Index, Bulk, JestResult> logSubmissionStrategy = new JestLogSubmissionStrategy();
            logSubmissionStrategy.setConnection(connection);
            logSubmissionStrategy.setQueueDepth(queueDepth);
            logSubmissionStrategy.setMaxSubmissionIntervalMillisec(maxSubmissionInterval);
            loggingModule = new LogAppenderModule<ILoggingEvent, ILoggingEvent, Index, Bulk, JestResult>(	logTransform,
                    logSerializer,
                    logSubmissionStrategy,
                    errorHandler);
            try {
                loggingModule.start();
            } catch (LogSubmissionException e) {
                errorHandler.error(FAILED_START_MSG, e);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        synchronized (moduleBuildLock) {
            try {
                loggingModule.stop();
            } catch (LogSubmissionException e) {
                errorHandler.error(FAILED_STOP_MSG, e);
            }
        }
    }

    @Override
    protected void append(ILoggingEvent e) {
        loggingModule.append(e);
    }
}

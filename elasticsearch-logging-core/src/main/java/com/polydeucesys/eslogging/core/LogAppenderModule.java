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
 * Provides an independent logging implementation which can be wrapped as appropriate for
 * any supported logging framework. The basic workflow is that logs are submitted from the
 * framework and passed to the {@link LogTransform} to be enriched as desired. They are then
 * 'serialized' by the {@code LogSerializer} (note that in the Jest implementations for example,
 * this serialization simply wraps the submitted object, and actual serialization is done 
 * asynchronously at submission.)  The serialized log is then passed to the strategy for
 * queuing or further processing before the strategy determines it will submit the 
 * logs via its' {@link Connection}.
 * 
 * @author Kevin McLellan
 * @version 1.0
 *
 * @param <L> The type of log receive from framework
 * @param <T> The transformed log type
 * @param <S> The serialized form of the single transformed instance
 * @param <B> The bulk submission form
 * @param <R> The result type returned from the Elasticsearch store on submission
 */
public class LogAppenderModule<L,T, S, B, R> implements LifeCycle{
	private final LogTransform<L, T> logTransform;
	private final LogSerializer<T,S> logSerializer;
	private final LogSubmissionQueueingStrategy<S,B,R> logSubmissionStrategy;
	private final LogAppenderErrorHandler errorHandler;
	private volatile boolean guard = false;
	/**
	 * Constructs a {@code LogAppenderModule} from the given transform, serialize, strategy and error handler components.
	 * @param logTransform 
	 * 			{@link LogTransform<L, T>} instance for enriching the logging framework supplied objects.
	 * @param logSerializer
	 * 			{@link LogSerializer<T,S>} instance for conversion of logging framework supplied objects to 
	 *          instance of the type {@code S} supported by the submission strategy 
	 * @param logSubmissionStrategy
	 * 			{@link LogSubmissionStrategy<S,B,R>} used to determine the queuing or additional processing of the 
	 *          submitted logs before submission to storage
	 * @param errorHandler
	 * 			{@link LogAppenderErrorHandler} used to pass errors to the appropriate handling mechanism in the 
	 *          logging framework (or to handle them itself)
	 */
	public LogAppenderModule(final LogTransform<L, T> logTransform,
			                 final LogSerializer<T,S> logSerializer, 
			                 final LogSubmissionQueueingStrategy<S,B,R> logSubmissionStrategy,
			                 final LogAppenderErrorHandler errorHandler){
		this.logTransform = logTransform;
		this.logSerializer = logSerializer;
		this.logSubmissionStrategy = logSubmissionStrategy;
		this.errorHandler = errorHandler;
	}
	
	/**
	 * Append a log
	 * @param log
	 * 			A log of type {@code L} for processing
	 */
	public void append(L log){
	    if(!guard) {
            try {
                guard = true;
                T transformed = logTransform.transform(log);
                S serialized = logSerializer.serializeLog(transformed);
                logSubmissionStrategy.submit(serialized);
            } catch (LogSubmissionException lse) {
                errorHandler.error(lse);
            } finally {
                guard = false;
            }
        }
	}

    /**
     * Inform the {@code LogSubmissionStrategy} that it must perform any required start up operations.
     * @see LifeCycle
     */
    @Override
    public void start() throws LogSubmissionException {
    	logSerializer.start();
        logSubmissionStrategy.start();
    }

    /**
     * Return the isStarted value from the {@code LogSubmissionStrategy}
     * @see LifeCycle
     * @return
     */
    @Override
    public boolean isStarted(){
        return (logSubmissionStrategy.isStarted() && logSerializer.isStarted());
    }

	/**
	 * Inform the {@code LogSubmissionStrategy} that it must shut down.
	 * @see LifeCycle
	 */
	@Override
	public void stop() throws LogSubmissionException{
		try{
			logSerializer.stop();
			logSubmissionStrategy.stop();
		}catch(LogSubmissionException lse){
			errorHandler.error(lse);
		}
	}


}

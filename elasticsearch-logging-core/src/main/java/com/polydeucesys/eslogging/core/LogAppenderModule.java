package com.polydeucesys.eslogging.core;

/**
 * 
 * @author Kevin McLellan
 *
 * @param <L> - The type of the Log object 
 */
public class LogAppenderModule<L,O, I, D, R> {
	private final LogTransform<L, O> logTransform;
	private final LogSerializer<O,I> logSerializer;
	private final LogSubmissionStrategy<I,D,R> logSubmissionStrategy;
	private final LogAppenderErrorHandler errorHandler;
	
	public LogAppenderModule(final LogTransform<L, O> logTransform,
			                 final LogSerializer<O,I> logSerializer, 
			                 final LogSubmissionStrategy<I,D,R> logSubmissionStrategy,
			                 final LogAppenderErrorHandler errorHandler){
		this.logTransform = logTransform;
		this.logSerializer = logSerializer;
		this.logSubmissionStrategy = logSubmissionStrategy;
		this.errorHandler = errorHandler;
	}
	
	
	public void append(L log){
		try{
			O transformed = logTransform.transform(log);
			I serialized = logSerializer.serializeLog(transformed);
			logSubmissionStrategy.submit(serialized);
		}catch(LogSubmissionException lse){
			errorHandler.error(lse);
		}
	}
}

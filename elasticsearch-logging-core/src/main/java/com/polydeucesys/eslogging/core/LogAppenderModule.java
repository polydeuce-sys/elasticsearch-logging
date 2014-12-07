package com.polydeucesys.eslogging.core;

/**
 * 
 * @author Kevin McLellan
 *
 * @param <L> - The type of the Log object 
 */
public class LogAppenderModule<L, I, D, R> {
	
	private final LogSerializer<L,I> logSerializer;
	private final LogSubmissionStrategy<I,D,R> logSubmissionStrategy;
	private final LogAppenderErrorHandler errorHandler;
	
	public LogAppenderModule(LogSerializer<L,I> logSerializer, 
			                 LogSubmissionStrategy<I,D,R> logSubmissionStrategy,
			                 LogAppenderErrorHandler errorHandler){
		this.logSerializer = logSerializer;
		this.logSubmissionStrategy = logSubmissionStrategy;
		this.errorHandler = errorHandler;
	}
	
	
	public void append(L log){
		try{
			logSubmissionStrategy.submit(logSerializer.serializeLog(log));
		}catch(LogSubmissionException lse){
			errorHandler.error(lse);
		}
	}
}

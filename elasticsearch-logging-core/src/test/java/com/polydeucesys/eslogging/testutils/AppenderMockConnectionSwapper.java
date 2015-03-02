package com.polydeucesys.eslogging.testutils;

import java.lang.reflect.Field;

import com.polydeucesys.eslogging.core.Connection;
import com.polydeucesys.eslogging.core.LogAppenderModule;
import com.polydeucesys.eslogging.core.LogSubmissionQueueingStrategy;

public class AppenderMockConnectionSwapper {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void swapAppenderToConnection( Object appender, Connection<?,?> connection) throws NoSuchFieldException, 
	                                                                                                 SecurityException, 
	                                                                                                 IllegalArgumentException, 
	                                                                                                 IllegalAccessException{
		Field moduleField = appender.getClass().getDeclaredField("loggingModule");
		moduleField.setAccessible(true);
		LogAppenderModule loggingModule = (LogAppenderModule) moduleField.get(appender);
		Field strategyField = loggingModule.getClass().getDeclaredField("logSubmissionStrategy");
		strategyField.setAccessible(true);
		LogSubmissionQueueingStrategy strategy = (LogSubmissionQueueingStrategy) strategyField.get(loggingModule); 
		strategy.setConnection(connection);
	}
	
	private AppenderMockConnectionSwapper(){}
}

package com.polydeucesys.eslogging.testutils;

import java.lang.reflect.Field;

import com.polydeucesys.eslogging.core.Connection;
import com.polydeucesys.eslogging.core.LogAppenderModule;
import com.polydeucesys.eslogging.core.LogSubmissionQueueingStrategy;
/**
 * Copyright (c) 2016 Polydeuce-Sys Ltd
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 **/
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

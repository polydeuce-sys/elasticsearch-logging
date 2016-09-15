package com.polydeucesys.eslogging.log4j2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext.ContextStack;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.Message;
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
 * Provides a wrapper for {@link LogEvent} instances which can carry an additional property map.
 * This is for example used to when adding host and working directory info to the log
 */
public class PropertyCarryLogEventWrapper implements LogEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3769654056243664476L;
	
	private final LogEvent wrappedEvent;
	private final Map<String, String> carriedProperties = new HashMap<String, String>();
	
	public PropertyCarryLogEventWrapper(final LogEvent wrappedEvent){
		this.wrappedEvent = wrappedEvent;
	}

	@Override
	public Map<String, String> getContextMap() {
		return wrappedEvent.getContextMap();
	}

	@Override
	public ContextStack getContextStack() {
		return wrappedEvent.getContextStack();
	}

	@Override
	public Level getLevel() {
		return wrappedEvent.getLevel();
	}

	@Override
	public String getLoggerFqcn() {
		return wrappedEvent.getLoggerFqcn();
	}

	@Override
	public String getLoggerName() {
		return wrappedEvent.getLoggerName();
	}

	@Override
	public Marker getMarker() {
		return wrappedEvent.getMarker();
	}

	@Override
	public Message getMessage() {
		return wrappedEvent.getMessage();
	}

	@Override
	public StackTraceElement getSource() {
		return wrappedEvent.getSource();
	}

	@Override
	public String getThreadName() {
		return wrappedEvent.getThreadName();
	}

	@Override
	public long getThreadId() {
		return wrappedEvent.getThreadId();
	}

	@Override
	public int getThreadPriority() {
		return wrappedEvent.getThreadPriority();
	}

	@Override
	public Throwable getThrown() {
		return wrappedEvent.getThrown();
	}

	@Override
	public ThrowableProxy getThrownProxy() {
		return wrappedEvent.getThrownProxy();
	}

	@Override
	public long getTimeMillis() {
		return wrappedEvent.getTimeMillis();
	}

	@Override
	public boolean isEndOfBatch() {
		return wrappedEvent.isEndOfBatch();
	}

	@Override
	public boolean isIncludeLocation() {
		return wrappedEvent.isIncludeLocation();
	}

	@Override
	public void setEndOfBatch(boolean flag) {
		wrappedEvent.setEndOfBatch(flag);
	}

	@Override
	public void setIncludeLocation(boolean flag) {
		wrappedEvent.setIncludeLocation(flag);
	}

	@Override
	public long getNanoTime() {
		return wrappedEvent.getNanoTime();
	}

	public void setCarriedProperty(String key, String value){
		carriedProperties.put(key, value);
	}
	
	public Map<String, String> getCarriedProperties(){
		return Collections.unmodifiableMap(carriedProperties);
	}

}

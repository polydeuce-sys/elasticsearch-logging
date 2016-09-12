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

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
	
	public void setCarriedProperty(String key, String value){
		carriedProperties.put(key, value);
	}
	
	public Map<String, String> getCarriedProperties(){
		return Collections.unmodifiableMap(carriedProperties);
	}

}

package com.polydeucesys.eslogging.log4j2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.core.LogEvent;

import com.polydeucesys.eslogging.core.LogTransform;

public class HostAndCwdLogTransform implements LogTransform<LogEvent, PropertyCarryLogEventWrapper> {
	private static final String HOSTNAME_PROPERTY = "hostname";
	private static String hostname;
	
	private static final String CWD_PROPERTY = "cwd";
	private static String cwd = System.getProperty("user.dir");
	private final Map<String, String> contextProperties = new HashMap<String, String>();
	
	static {
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			hostname = "Unknown";
		}
	}
	
	public HostAndCwdLogTransform(final Map<String, String> contextProperties){
		this.contextProperties.putAll(contextProperties);
		this.contextProperties.put(HOSTNAME_PROPERTY, hostname);
		this.contextProperties.put(CWD_PROPERTY, cwd);
	}
	
	@Override
	public PropertyCarryLogEventWrapper transform(LogEvent log) {
		PropertyCarryLogEventWrapper wrappedLog = new PropertyCarryLogEventWrapper(log);
		for(String key : contextProperties.keySet()){
			wrappedLog.setCarriedProperty(key, contextProperties.get(key));
		}
		return wrappedLog;
	}

}

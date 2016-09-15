package com.polydeucesys.eslogging.log4j2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.core.LogEvent;

import com.polydeucesys.eslogging.core.LogTransform;
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
 * Adds host and current working directory information to the given {@link LogEvent}, in addition
 * to any other properties provided in the constructor.
 */
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

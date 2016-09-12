package com.polydeucesys.eslogging.log4j2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.core.LogEvent;

import com.polydeucesys.eslogging.core.LogTransform;
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

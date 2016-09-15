package com.polydeucesys.eslogging.log4j;

import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.slf4j.MDC;
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
 * Simple helper class for writing log messages with a specific set of MDC values.
 */
public class LogMDCHelper {
	public static void logWithContextMap( Logger log, Level level, Object message, Throwable exception, Map<String, String> context){
		Map<String, String> restore = MDC.getCopyOfContextMap();
		for(String contextKey : context.keySet()){
			MDC.put(contextKey, context.get(contextKey));
		}
		if(exception == null){
			log.log(level, message);
		}else{
			log.log(level, message, exception);
		}
		for(String contextKey : context.keySet()){
			MDC.remove(contextKey);
		}
		for(String mdcKey : restore.keySet()){
			MDC.put(mdcKey, restore.get(mdcKey));
		}
	}
	
	public static void logWithContextMap( Logger log, Level level, Object message,  Map<String, String> context){
		logWithContextMap(log, level, message, null, context);
	}
	
	private LogMDCHelper(){}	
}

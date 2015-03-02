package com.polydeucesys.eslogging.log4j;

import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.slf4j.MDC;

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

package com.polydeucesys.eslogging.log4j2;

import java.io.IOException;
import java.util.Set;

import org.apache.logging.log4j.core.impl.ThrowableProxy;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class PropertyCarryLogEventWrapperTypeAdapter extends TypeAdapter<PropertyCarryLogEventWrapper> {
	
	private static final String WRITE_ONLY_ADAPTER = "Only write operations should be used in the LogAppender";

	private static final String TIMESTAMP_KEY = "timestamp";
	private static final String THREAD_NAME_KEY = "threadName";
	private static final String LOCATION_KEY = "locationInfo";
	
	private static final String LEVEL_KEY = "level";
	private static final String NDC_KEY = "ndc";
	private static final String MESSAGE_KEY = "message";
	private static final String THROWABLE_KEY = "throwable";
	private static final String PROPERTIES_KEY = "properties";

	@Override
	public PropertyCarryLogEventWrapper read(JsonReader arg0) throws IOException {
		throw new IllegalStateException(WRITE_ONLY_ADAPTER);
	}

	@Override
	public void write(JsonWriter writer, PropertyCarryLogEventWrapper log) throws IOException {
		if (log == null) {
			writer.nullValue();
			return;
	    }
		writer.beginObject();
		writer.name(TIMESTAMP_KEY).value(log.getTimeMillis());
		writer.name(THREAD_NAME_KEY).value(log.getThreadName());
		writer.name(LOCATION_KEY).value(log.getSource().toString());
		writer.name(LEVEL_KEY).value(log.getLevel().toString());
		writer.name(NDC_KEY).value(log.getContextStack().toString());
		writer.name(MESSAGE_KEY).value(log.getMessage().getFormattedMessage());
		if(log.getThrownProxy() != null){
			writer.name(THROWABLE_KEY);
			writer.beginArray();
			ThrowableProxy tp = log.getThrownProxy();
			while(tp!=null){
				writer.value(tp.getExtendedStackTraceAsString());
				tp = tp.getCauseProxy();
			}
			writer.endArray();
		}else{
			writer.name(THROWABLE_KEY).nullValue();
		}
		@SuppressWarnings("unchecked")
		Set<String> propKeys = log.getCarriedProperties().keySet();
		Set<String> mdcKeys = log.getContextMap().keySet();
		if(propKeys.size() > 0 ||mdcKeys.size() > 0){
			writer.name(PROPERTIES_KEY);
			writer.beginObject();
			for(String key : propKeys){
				writer.name(key).value(log.getCarriedProperties().get(key));
			}
			for(String key : mdcKeys){
				writer.name(key).value(log.getContextMap().get(key));
			}
			writer.endObject();
		}else{
			writer.name(PROPERTIES_KEY).nullValue();
		}
		writer.endObject();
	}

}

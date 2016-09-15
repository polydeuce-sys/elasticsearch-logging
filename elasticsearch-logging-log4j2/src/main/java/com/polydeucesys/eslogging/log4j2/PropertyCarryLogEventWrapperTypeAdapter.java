package com.polydeucesys.eslogging.log4j2;

import java.io.IOException;
import java.util.Set;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.config.plugins.*;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.logging.log4j.core.impl.ThrowableProxy;

import static com.polydeucesys.eslogging.core.JsonDocumentElementKeys.*;

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
 * A write only Gson {@link TypeAdapter} for serializing {@link PropertyCarryLogEventWrapper} instances
 */
public class PropertyCarryLogEventWrapperTypeAdapter extends TypeAdapter<PropertyCarryLogEventWrapper> {
	
	private static final String WRITE_ONLY_ADAPTER = "Only write operations should be used in the LogAppender";

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
			writer.name(MDC_KEY);
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

package com.polydeucesys.eslogging.log4j;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.spi.LoggingEvent;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

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
 * Write only Gson type adapter for serialization of {@link LoggingEvent} instances.
 */
public class LoggingEventTypeAdapter extends TypeAdapter<LoggingEvent> {

	@Override
	public LoggingEvent read(JsonReader arg0) throws IOException {
		throw new IllegalStateException(WRITE_ONLY_ADAPTER);
	}

	@Override
	public void write(JsonWriter writer, LoggingEvent log) throws IOException {
		if (log == null) {
			writer.nullValue();
			return;
	    }
		writer.beginObject();
		writer.name(LOGGER_NAME_KEY).value(log.getLoggerName());
		writer.name(TIMESTAMP_KEY).value(log.getTimeStamp());
		writer.name(START_TIME_KEY).value(LoggingEvent.getStartTime());
		writer.name(THREAD_NAME_KEY).value(log.getThreadName());
		writer.name(LOCATION_KEY).value(log.getLocationInformation().fullInfo);
		writer.name(LEVEL_KEY).value(log.getLevel().toString());
		writer.name(NDC_KEY).value(log.getNDC());
        Map mdc = log.getProperties();
        if(!mdc.isEmpty()) {
            writer.name(MDC_KEY);
			writer.beginObject();
            for(Map.Entry entry : (Set<Map.Entry>)mdc.entrySet()){
                writer.name(entry.getKey().toString())
                        .value(entry.getValue().toString());
            }
            writer.endObject();
        }else{
            writer.name(MDC_KEY);
			writer.nullValue();
        }
		writer.name(MESSAGE_KEY).value(log.getRenderedMessage());
		if(log.getThrowableInformation() != null){
			writer.name(THROWABLE_KEY);
			writer.beginArray();
			for(String t : log.getThrowableStrRep()){
				writer.value(t);
			}
			writer.endArray();
		}else{
			writer.name(THROWABLE_KEY).nullValue();
		}
		writer.endObject();
	}

}

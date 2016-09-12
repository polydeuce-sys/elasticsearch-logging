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
            writer.name(MDC_KEY).beginObject();
            for(Map.Entry entry : (Set<Map.Entry>)mdc.entrySet()){
                writer.name(MDC_KEY).name(entry.getKey().toString())
                        .value(entry.getValue().toString());
            }
            writer.name(MDC_KEY).endObject();
        }else{
            writer.name(MDC_KEY).nullValue();
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

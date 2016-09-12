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

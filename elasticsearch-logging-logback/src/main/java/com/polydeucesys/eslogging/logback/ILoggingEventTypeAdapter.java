package com.polydeucesys.eslogging.logback;

import ch.qos.logback.classic.pattern.RootCauseFirstThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.spi.LifeCycle;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.slf4j.MDC;

import static com.polydeucesys.eslogging.core.JsonDocumentElementKeys.*;


import java.io.IOException;
import java.util.Map;
import java.util.Set;
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
 * A write only Gson {@link TypeAdapter} for serializing {@link ILoggingEvent} instances
 */
public class ILoggingEventTypeAdapter extends TypeAdapter<ILoggingEvent> implements LifeCycle {
    private final RootCauseFirstThrowableProxyConverter throwableProxyConverter =
            new RootCauseFirstThrowableProxyConverter();

    @Override
    public void write(JsonWriter writer, ILoggingEvent log) throws IOException {
        if (log == null) {
            writer.nullValue();
            return;
        }
        writer.beginObject();
        writer.name(LOGGER_NAME_KEY).value(log.getLoggerName());
        writer.name(TIMESTAMP_KEY).value(log.getTimeStamp());
        writer.name(START_TIME_KEY).value(log.getLoggerContextVO().getBirthTime());
        writer.name(THREAD_NAME_KEY).value(log.getThreadName());
        writer.name(LOCATION_KEY).value(log.getCallerData().toString());
        writer.name(LEVEL_KEY).value(log.getLevel().toString());
        Map mdc = log.getMDCPropertyMap();
        if(!mdc.isEmpty()) {
            writer.name(MDC_KEY);
            writer.beginObject();
            for(Map.Entry<String, String> entry : (Set<Map.Entry>)mdc.entrySet()){
                writer.name(entry.getKey().toString())
                        .value(entry.getValue().toString());
            }
            writer.endObject();
        }else{
            writer.name(MDC_KEY).nullValue();
        }
        writer.name(MESSAGE_KEY).value(log.getFormattedMessage());
        if(log.getThrowableProxy() != null){
            IThrowableProxy tp = log.getThrowableProxy();
            writer.name(THROWABLE_KEY);
            writer.beginArray();
            if(tp.getCause() != null) writer.value("Cause : " + tp.getCause().toString());
            writer.value(tp.getMessage());
            for(StackTraceElementProxy t : tp.getStackTraceElementProxyArray()){
                writer.value(t.getSTEAsString());
            }
            writer.endArray();
        }else{
            writer.name(THROWABLE_KEY).nullValue();
        }
        writer.endObject();
    }

    @Override
    public ILoggingEvent read(JsonReader jsonReader) throws IOException {
        throw new IllegalStateException(WRITE_ONLY_ADAPTER);
    }

    @Override
    public void start() {
        throwableProxyConverter.start();
    }

    @Override
    public void stop() {
        throwableProxyConverter.stop();
    }

    @Override
    public boolean isStarted() {
        return throwableProxyConverter.isStarted();
    }
}

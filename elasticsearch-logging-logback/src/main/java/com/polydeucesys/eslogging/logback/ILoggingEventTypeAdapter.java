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

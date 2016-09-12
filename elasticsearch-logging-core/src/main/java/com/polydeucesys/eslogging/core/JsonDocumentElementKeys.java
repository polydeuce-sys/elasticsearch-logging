package com.polydeucesys.eslogging.core;
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
 * Keys used in the Json documents submitted to Elasticsearch by the various implementations of the
 * log appenders.
 * Created by kevinmclellan on 31/08/2016.
 */
public class JsonDocumentElementKeys {
    public static final String WRITE_ONLY_ADAPTER = "Only write operations should be used in the LogAppender";

    public static final String TIMESTAMP_KEY = "timestamp";
    public static final String START_TIME_KEY = "startTime";
    public static final String LOGGER_NAME_KEY = "logger";
    public static final String THREAD_NAME_KEY = "threadName";
    public static final String LOCATION_KEY = "locationInfo";

    public static final String LEVEL_KEY = "level";
    public static final String NDC_KEY = "ndc";
    public static final String MDC_KEY = "mdc";
    public static final String MESSAGE_KEY = "message";
    public static final String THROWABLE_KEY = "throwable";
    public static final String PROPERTIES_KEY = "properties";

    private JsonDocumentElementKeys(){}
}

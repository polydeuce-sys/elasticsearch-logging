package com.polydeucesys.eslogging.core;
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

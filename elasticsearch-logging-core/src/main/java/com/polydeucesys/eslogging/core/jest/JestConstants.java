package com.polydeucesys.eslogging.core.jest;
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
 * The JestContants class provides the constant values which relate solely to the Jest client based implementations
 * of the various interfaces.
 * @author Kevin McLellan
 *
 */
public final class JestConstants {
	private JestConstants(){}
	
	public static final int DEFAULT_QUEUE_DEPTH = 50;
	public static final long DEFAULT_MAX_SUBMISSION_INTERVAL_MILLISECONDS = 2000;
	public static final String DEFAULT_LOG_INDEX_PREFIX = "logs";
	public static final String DEFAULT_LOG_DOC_TYPE = "logs";

	
	public static final String  JEST_EXCEPTION_FORMAT = "Exception logging via Jest with config : %s";
	public static final String  JEST_BAD_STATUS_EXCEPTION_FORMAT = "Bad status error :  %s, async logging via Jest";
	public static final String  JEST_ASYNC_EXCEPTION_FORMAT = "Error in async logging via Jest : %s";
	
	public static final boolean CLIENT_IS_MULTITHREADED_DEFAULT = true;
	public static final int     CLIENT_MAX_TOTAL_HTTP_CONNECTIONS = 10;
	public static final int     CLIENT_DEFAULT_MAX_HTTP_CONNECTIONS_PER_ROUTE = 5;
	public static final boolean CLIENT_IS_NODE_DISCOVERY_ENABLED_DEFAULT = false;
	public static final long    CLIENT_NODE_DISCOVERY_INTERVAL_MILLIS = 10000;
	public static final long    MINIMUM_CLIENT_NODE_DISCOVERY_INTERVAL_MILLIS = 1000;
	public static final int 	CLIENT_CONNECTION_TIMEOUT_MILLIS = 30000;
	public static final int     CLIENT_READ_TIMEOUT_MILLIS = 30000;
	public static final long    CLIENT_MAX_CONNECTION_IDLE_TIME_MILLIS = 0;

	public static final int     CLIENT_DEFAULT_MINIMUM_CONNECTION_TIMEOUT_MILLIS = 1000;	
	public static final int     CLIENT_DEFAULT_MINIMUM_READ_TIMEOUT_MILLIS = 1000;
	public static final long    CLIENT_DEFAULT_MAX_ASYNC_COMPLETEION_TIME_FOR_SHUTDOWN = 1000;

}

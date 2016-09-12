package com.polydeucesys.eslogging.core.jest;
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

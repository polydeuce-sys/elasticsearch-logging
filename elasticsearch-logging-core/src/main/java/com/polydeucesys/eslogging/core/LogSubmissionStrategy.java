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
 * Defines the base interface for a strategy for log submission. This requires a connection, an error handler and
 * a call point for submission of a log object from the logging framework.
 * @author Kevin McLellan
 * @version 1.0
 *
 * @param <L> 
 * 			Type of log object submitted by the logging framework
 * @param <D>
 * 			Type of document expected by the log storage
 * @param <R>
 * 			Type of response given by storage on successfulcsubmission
 */
public interface LogSubmissionStrategy<L,D,R> {
	void setConnection(final Connection<D, R> connection);
	Connection<D, R> getConnection();
	void setErrorHandler( LogAppenderErrorHandler errorHandler);
	LogAppenderErrorHandler getErrorHandler();
	void submit( final L log ) throws LogSubmissionException;
}

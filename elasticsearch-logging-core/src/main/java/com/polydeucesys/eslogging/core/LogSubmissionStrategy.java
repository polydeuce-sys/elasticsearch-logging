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

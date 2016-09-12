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
 * 
 * @author Kevin McLellan
 * @version 1.0
 *
 * @param <L> - A Log type (for example LogRecord in Log4J). The serializer is responsible for handling the 
 * Serialization of the log from the internal format of the logging framework to a Json String for 
 * submission to ElasticSearch.
 * @param<D> - A document type (for example a Json String) which the log is serialized to before insertion
 */
public interface LogSerializer<L, D> extends LifeCycle {
	/**
	 * Given a logging framework log object of type {@code L} serialize it to type {@code D} for submission
	 * @param log
	 * @return instance of type {@code D} representing the serialized log
	 * @throws LogSubmissionException
	 */
	D serializeLog(final L log) throws LogSubmissionException;
}

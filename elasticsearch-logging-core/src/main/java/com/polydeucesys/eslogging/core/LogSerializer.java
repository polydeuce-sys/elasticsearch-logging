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

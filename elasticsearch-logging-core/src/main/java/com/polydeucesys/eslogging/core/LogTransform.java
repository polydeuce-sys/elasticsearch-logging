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
 * The LogTransform interface defines an object which is used to enrich or otherwise transform the
 * incoming log object prior to serialization and submission. An example may add properties to the 
 * object indicating the hostname and logging application when the appender is writing to an 
 * ElasticSearch index shared by many other processes/hosts.
 * @author kmac
 *
 */
public interface LogTransform<I,O> {
	/**
	 * Trasmform the log event instance into the form used for serialization when submitting to
     * Elasticsearch.
	 * @param log
	 * @return
	 */
	O transform(I log);
}

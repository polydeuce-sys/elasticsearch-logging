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
 * The LogMapper interface provides a means of creating a mapping between an
 * ElasticSearch Index and DocumentType and a given log entry.
 * @param <L> Type of log entry
 */
public interface LogMapper<L> {
	String getIndexNameForLog( final L log );
	String getDocumentTypeForLog( final L log );
}

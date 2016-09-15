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
 * Defined the constants used in the Appender code.
 * @author Kevin McLellan
 * @version 1.0
 *
 */
public class Constants {
	
	public static final String CONFIGURATION_ERROR_FORMAT = "Property %s in class %s is not configured correctly";
	public static final String SERIALIZE_NULL_ERROR_FORMAT = "%s cannot serialize null log entry";
	
	public static final String ELASTICSEARCH_INDEX_DATE_STAMP_FORMAT = "yyyy.MM.dd";

	private Constants(){}
}

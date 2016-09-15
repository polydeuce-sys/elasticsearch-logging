package com.polydeucesys.eslogging.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.polydeucesys.eslogging.core.jest.LogMapper;
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
 * Provides a simple LogMapper implementation which writes log to a timestmped index based on the current
 * Date.
 *
 * @param <L>
 */
public class SimpleDateStampedLogMapper<L> implements LogMapper<L> {
	private final ThreadLocal<SimpleDateFormat> indexDateFormat;
	private final String logIndexPrefix;
	private final String logDocType;	
	
	public SimpleDateStampedLogMapper( final String logIndexPrefix, 
			                           final String logIndexDateFormat, 
			                           final String logDocType){
	    if(logDocType == null || logIndexDateFormat == null || logIndexPrefix == null){
	        throw new IllegalStateException("Invalid " + this.getClass() + " configuration.");
        }
		String workingPrefix = logIndexPrefix;
		if(!workingPrefix.endsWith("-")){
			workingPrefix = workingPrefix + "-";
		}
		this.logIndexPrefix = workingPrefix;

		this.logDocType = logDocType;
		// test that the date format is valid. Better to fail now than later
		new SimpleDateFormat(logIndexDateFormat);
		
		indexDateFormat = new ThreadLocal<SimpleDateFormat>(){
			@Override
			protected SimpleDateFormat initialValue(){
				return new SimpleDateFormat(logIndexDateFormat);
			}
		};
		
	}
	
	@Override
    /**
     * Give a log event of type L, return the index name from the configured prefix and current
     * Date.
     */
	public String getIndexNameForLog(L log) {
		StringBuilder logIndexNameBuilder = new StringBuilder(logIndexPrefix);
		return logIndexNameBuilder.append(indexDateFormat.get().format(new Date())).toString();
	}

	@Override
    /**
     * Return the configured log document type.
     */
	public String getDocumentTypeForLog(L log) {
		return logDocType;
	}

}

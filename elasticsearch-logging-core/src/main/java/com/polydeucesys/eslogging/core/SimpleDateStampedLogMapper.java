package com.polydeucesys.eslogging.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.polydeucesys.eslogging.core.jest.LogMapper;
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

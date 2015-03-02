package com.polydeucesys.eslogging.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.polydeucesys.eslogging.core.jest.LogMapper;

//YYYY.MM.DD
public class SimpleDateStampedLogMapper<L> implements LogMapper<L> {
	private final ThreadLocal<SimpleDateFormat> indexDateFormat;
	private final String logIndexPrefix;
	private final String logDocType;	
	
	public SimpleDateStampedLogMapper( final String logIndexPrefix, 
			                           final String logIndexDateFormat, 
			                           final String logDocType){
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
	public String getIndexNameForLog(L log) {
		StringBuilder logIndexNameBuilder = new StringBuilder(logIndexPrefix);
		return logIndexNameBuilder.append(indexDateFormat.get().format(new Date())).toString();
	}

	@Override
	public String getDocumentTypeForLog(L log) {
		return logDocType;
	}

}

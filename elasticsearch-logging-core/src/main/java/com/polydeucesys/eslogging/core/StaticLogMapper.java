package com.polydeucesys.eslogging.core;

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
 * Provides an implementation of the LogMapper interface which returns a fixed
 * Index name and Document Type for all logs. 
 * @author Kevin McLellan
 *
 * @param <L>
 */
public final class StaticLogMapper<L> implements LogMapper<L> {
	private String indexName;
	private String documentType; 

	public void setIndexName(final String indexName){
		this.indexName = indexName;
	}
	
	public void setDocumentType(final String documentType){
		this.documentType = documentType;
	}
	
	@Override
	public String getIndexNameForLog(final L log) {
		if(indexName == null){
			throw new IllegalArgumentException(String.format(
					Constants.CONFIGURATION_ERROR_FORMAT, "indexName", this.getClass().getName()));
		}
		return indexName;
	}

	@Override
	public String getDocumentTypeForLog(final L log) {
		if(documentType == null){
			throw new IllegalArgumentException(String.format(
					Constants.CONFIGURATION_ERROR_FORMAT, "documentType", this.getClass().getName()));
		}
		return documentType;
	}

}

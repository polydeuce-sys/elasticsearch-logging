package com.polydeucesys.eslogging.core;

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

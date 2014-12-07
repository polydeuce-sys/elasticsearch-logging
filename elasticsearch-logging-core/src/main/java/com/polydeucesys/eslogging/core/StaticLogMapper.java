package com.polydeucesys.eslogging.core;

import com.polydeucesys.eslogging.core.jest.LogMapper;

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

package com.polydeucesys.eslogging.core.jest;

public interface LogMapper<L> {
	String getIndexNameForLog( final L log );
	String getDocumentTypeForLog( final L log );
}

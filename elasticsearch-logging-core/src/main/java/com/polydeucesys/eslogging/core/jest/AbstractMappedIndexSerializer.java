package com.polydeucesys.eslogging.core.jest;

import io.searchbox.core.Index;

import com.polydeucesys.eslogging.core.Constants;
import com.polydeucesys.eslogging.core.LogSerializer;
import com.polydeucesys.eslogging.core.LogSubmissionException;

public abstract class AbstractMappedIndexSerializer<L> implements LogSerializer<L, Index>{
	private LogMapper<L> indexMapper;
	private boolean hasValidated = false;
	
	public void setIndexMapper(final LogMapper<L> indexMapper){
		this.indexMapper = indexMapper;
	}
	
	public LogMapper<L> getIndexMapper(){
		return indexMapper;
	}

	protected abstract Index internalSerializeLog(final L log) throws LogSubmissionException;
	
	protected abstract boolean doHasValidated();
	
	private boolean hasValidated(){
		return hasValidated && doHasValidated();
	}
	
	protected abstract void doValidate();
	
	private void validate(){
		if(indexMapper == null){
			throw new IllegalArgumentException(String.format(
					Constants.CONFIGURATION_ERROR_FORMAT, "indexMapper", this.getClass().getName()));
		}
		doValidate();
		hasValidated = true;
	}
	
	@Override
	public Index serializeLog(final L log) throws LogSubmissionException {
		if(!hasValidated()){
			validate();
		}
		return internalSerializeLog(log);
	}
}

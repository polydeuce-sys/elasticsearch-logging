package com.polydeucesys.eslogging.core.jest;

import io.searchbox.core.Index;

import com.polydeucesys.eslogging.core.Constants;
import com.polydeucesys.eslogging.core.LogSubmissionException;

/**
 * A serializer which uses the Jest support for POJOs to serialize the log object to a 
 * Jest Index object for publication
 * 
 * @author Kevin McLellan
 * @param <L>
 *
 */
public final class SimpleJestIndexSerializer<L> extends AbstractMappedIndexSerializer<L>{
	
	@Override
	protected boolean doHasValidated() {
		return true;
	}

	@Override
	protected void doValidate() {
		// NOOP - No additional config is required below the level of the
		// abstract superclass
	}

	@Override
	public Index internalSerializeLog(final L log) throws LogSubmissionException {
		if(log == null){
			throw new LogSubmissionException(String.format(Constants.SERIALIZE_NULL_ERROR_FORMAT,
					this.getClass().getName()));
		}
		final Index.Builder lb = new Index.Builder(log)
											.index(getIndexMapper().getIndexNameForLog(log))
											.type(getIndexMapper().getDocumentTypeForLog(log));
		return lb.build();
	}

	


}

package com.polydeucesys.eslogging.log4j;

import org.apache.log4j.spi.LoggingEvent;

import io.searchbox.core.Index;

import com.polydeucesys.eslogging.core.LogSubmissionException;
import com.polydeucesys.eslogging.core.jest.AbstractMappedIndexSerializer;

public class LoggingEventJestIndexSerializer extends
		AbstractMappedIndexSerializer<LoggingEvent> {

	@Override
	protected Index internalSerializeLog(LoggingEvent log) throws LogSubmissionException {
		return null;
	}

	@Override
	protected boolean doHasValidated() {
		return true;
	}

	@Override
	protected void doValidate() {
		//NOOP - No additional validation needed
	}

}

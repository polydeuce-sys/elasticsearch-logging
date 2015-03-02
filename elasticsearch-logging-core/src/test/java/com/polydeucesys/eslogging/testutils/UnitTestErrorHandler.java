package com.polydeucesys.eslogging.testutils;

import java.util.ArrayList;
import java.util.List;

import com.polydeucesys.eslogging.core.LogAppenderErrorHandler;
import com.polydeucesys.eslogging.core.LogSubmissionException;

public class UnitTestErrorHandler implements LogAppenderErrorHandler {

	private final ArrayList<LogSubmissionException> exceptions = new ArrayList<LogSubmissionException>();
	
	public List<LogSubmissionException> getExceptions(){
		return exceptions;
	}
	
	@Override
	public void error(LogSubmissionException ex) {
		exceptions.add(ex);
	}
	

}

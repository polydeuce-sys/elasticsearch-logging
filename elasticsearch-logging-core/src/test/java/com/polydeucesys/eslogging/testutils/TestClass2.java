package com.polydeucesys.eslogging.testutils;

import com.polydeucesys.eslogging.core.Constants;
import com.polydeucesys.eslogging.core.LogSerializer;
import com.polydeucesys.eslogging.core.LogSubmissionException;

public class TestClass2 {
	
	public static class TestClass2Serializer implements LogSerializer<TestClass2, String>{

		@Override
		public String serializeLog(TestClass2 log)
				throws LogSubmissionException {
			if(log == null){
				throw new LogSubmissionException(String.format(Constants.SERIALIZE_NULL_ERROR_FORMAT,
						this.getClass().getName()));
			}
			return log.toJsonString();
		}

		@Override
		public void start() throws LogSubmissionException {

		}

		@Override
		public void stop() throws LogSubmissionException {

		}

		@Override
		public boolean isStarted() {
			return true;
		}
	}
	
	private final String s1;
	private final int i1;
	public TestClass2(final String s1, final int i1){
		this.s1 = s1;
		this.i1 = i1;
	}
	
	public String getS1(){
		return s1;
	}
	
	public int getI1(){
		return i1;
	}
	
	public String toJsonString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{ \"s1\" : \"");
		sb.append(s1);
		sb.append("\", \"i1\" : ");
		sb.append(i1);
		sb.append("}");
		return sb.toString();
	}
}

package com.polydeucesys.eslogging.testutils;

import java.util.Random;

import com.polydeucesys.eslogging.core.Constants;
import com.polydeucesys.eslogging.core.LogSerializer;
import com.polydeucesys.eslogging.core.LogSubmissionException;

public class TestClass3 {
	private final static Random random = new Random();

	public static class RandomString {

		  private static final char[] symbols;

		  static {
		    StringBuilder tmp = new StringBuilder();
		    for (char ch = '0'; ch <= '9'; ++ch)
		      tmp.append(ch);
		    for (char ch = 'a'; ch <= 'z'; ++ch)
		      tmp.append(ch);
		    symbols = tmp.toString().toCharArray();
		  }   


		  private final char[] buf;

		  public RandomString(int length) {
		    if (length < 1)
		      throw new IllegalArgumentException("length < 1: " + length);
		    buf = new char[length];
		  }

		  public String nextString() {
		    for (int idx = 0; idx < buf.length; ++idx) 
		      buf[idx] = symbols[random.nextInt(symbols.length)];
		    return new String(buf);
		  }
		}
	
	public static class TestClass3Serializer implements LogSerializer<TestClass3, String>{

		@Override
		public String serializeLog(TestClass3 log)
				throws LogSubmissionException {
			if(log == null){
				throw new LogSubmissionException(String.format(Constants.SERIALIZE_NULL_ERROR_FORMAT,
						this.getClass().getName()));
			}
			return log.toJsonString();
		}
		
	}
	
	private static RandomString rs = new RandomString(64);
	
	private String s1;
	private String s2;
	private String s3;
	private String s4;
	private String s5;
	private String s6;
	private String s7;
	
	private int i1;
	
	public TestClass3(){
		s1 = rs.nextString();
		s2 = rs.nextString();
		s3 = rs.nextString();
		s4 = rs.nextString();
		s5 = rs.nextString();
		s6 = rs.nextString();
		s7 = rs.nextString();

		i1 = random.nextInt();
	}
	
	public String toJsonString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{ \"s1\" : \"");
		sb.append(s1);
		sb.append("\",");
		sb.append("\"s2\" : \"");
		sb.append(s2);
		sb.append("\",");
		sb.append("\"s3\" : \"");
		sb.append(s3);
		sb.append("\",");
		sb.append("\"s4\" : \"");
		sb.append(s4);
		sb.append("\",");
		sb.append("\"s5\" : \"");
		sb.append(s5);
		sb.append("\",");
		sb.append("\"s6\" : \"");
		sb.append(s6);
		sb.append("\",");
		sb.append("\"s7\" : \"");
		sb.append(s7);
		sb.append("\", \"i1\" : ");
		sb.append(i1);
		sb.append("}");
		return sb.toString();
	}
}

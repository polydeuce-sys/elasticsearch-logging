package com.polydeucesys.eslogging.core.gson;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.polydeucesys.eslogging.core.Constants;
import com.polydeucesys.eslogging.core.LogSubmissionException;
import com.polydeucesys.eslogging.testutils.TestClass1;

/**
 * 
 * @author kmac
 *
 */
public class SimpleGsonLogSerializerTest {
	
	@Rule 
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testSerializes() throws Exception{
		TestClass1 t1 = new TestClass1("hello", 1);
		String expectedt1 = "\\{\\s*\"s1\"\\s*\\:\\s*\"hello\"\\s*,\\s*\"i1\"\\:\\s*1\\s*\\}";
		SimpleGsonLogSerializer<TestClass1> s1 = new SimpleGsonLogSerializer<TestClass1>();
		assertTrue("Expected string matching '" +expectedt1+ "'. Got: "+s1.serializeLog(t1), 
				s1.serializeLog(t1).matches(expectedt1));
		
	}
	
	@Test
	public void testNullerError() throws Exception{
		SimpleGsonLogSerializer<TestClass1> s1 = new SimpleGsonLogSerializer<TestClass1>();
		thrown.expect(LogSubmissionException.class);
		s1.serializeLog(null);
	}

}

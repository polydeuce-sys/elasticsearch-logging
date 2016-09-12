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
 * Copyright (c) 2016 Polydeuce-Sys Ltd
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 **/
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
		s1.start();
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

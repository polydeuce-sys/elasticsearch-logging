package com.polydeucesys.eslogging.core.jest;

import static org.junit.Assert.*;
import io.searchbox.client.http.JestHttpClient;
import io.searchbox.core.Index;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.polydeucesys.eslogging.core.Constants;
import com.polydeucesys.eslogging.core.LogSubmissionException;
import com.polydeucesys.eslogging.core.StaticLogMapper;
import com.polydeucesys.eslogging.core.gson.SimpleGsonLogSerializer;
import com.polydeucesys.eslogging.testutils.MockCloseableHttpClient;
import com.polydeucesys.eslogging.testutils.TestClass1;
import com.polydeucesys.eslogging.testutils.TestClass2;

public class JestIndexStringSerializerWrapperTest {

	
	private LogMapper<TestClass1> getStaticMapper(){
		final StaticLogMapper<TestClass1> m1 = new StaticLogMapper<TestClass1>();
		m1.setIndexName("testIndex");
		m1.setDocumentType("testDocument");
		return m1;
	}
	
	private LogMapper<TestClass2> getStaticMapper2(){
		final StaticLogMapper<TestClass2> m1 = new StaticLogMapper<TestClass2>();
		m1.setIndexName("testIndex");
		m1.setDocumentType("testDocument");
		return m1;
	}
	
	@Test
	public void testSerializes() throws Exception{
		final TestClass1 t1 = new TestClass1("hello", 1);
		final String expected = "\\{\\s*\"s1\"\\s*\\:\\s*\"hello\"\\s*,\\s*\"i1\"\\s*\\:\\s*1\\s*\\}";
		final SimpleGsonLogSerializer<TestClass1> s1 = new SimpleGsonLogSerializer<TestClass1>();
		final JestIndexStringSerializerWrapper<TestClass1> w1 = new JestIndexStringSerializerWrapper<TestClass1>();
		w1.setIndexMapper(getStaticMapper());
		w1.setWrappedJsonStringSerializer(s1);
		JestHttpClient jestClient = MockCloseableHttpClient.testableJestClient();
		MockCloseableHttpClient mockc = (MockCloseableHttpClient) jestClient.getHttpClient();
		mockc.setNextResponse(MockCloseableHttpClient.responseWithBody(200, 
				"{\"_index\" : \"" + getStaticMapper().getIndexNameForLog(t1) + "\", " + 
				 "\"_type\"  : \"" + getStaticMapper().getDocumentTypeForLog(t1) + "\", " + 
				 "\"_id\" : \"1\", \"_version\" : \"1\", \"_created\" : true }"));
		jestClient.execute(w1.serializeLog(t1));
		assertTrue("Expected string matching '" +expected+"'. Got: "+mockc.getLastRequestContent(), 
				mockc.getLastRequestContent().matches(expected));
	}
	
	@Test
	public void testSerializesAlternateStringSerializer() throws Exception{
		final TestClass2 t1 = new TestClass2("hello", 1);
		final String expected = "\\{\\s*\"s1\"\\s*\\:\\s*\"hello\"\\s*,\\s*\"i1\"\\s*\\:\\s*1\\s*\\}";
		final TestClass2.TestClass2Serializer s2 = new TestClass2.TestClass2Serializer();
		final JestIndexStringSerializerWrapper<TestClass2> w1 = new JestIndexStringSerializerWrapper<TestClass2>();
		w1.setIndexMapper(getStaticMapper2());
		w1.setWrappedJsonStringSerializer(s2);
		JestHttpClient jestClient = MockCloseableHttpClient.testableJestClient();
		MockCloseableHttpClient mockc = (MockCloseableHttpClient) jestClient.getHttpClient();
		mockc.setNextResponse(MockCloseableHttpClient.responseWithBody(200, 
				"{\"_index\" : \"" + getStaticMapper2().getIndexNameForLog(t1) + "\", " + 
				 "\"_type\"  : \"" + getStaticMapper2().getDocumentTypeForLog(t1) + "\", " + 
				 "\"_id\" : \"1\", \"_version\" : \"1\", \"_created\" : true }"));
		jestClient.execute(w1.serializeLog(t1));
		assertTrue("Expected string matching '" +expected+"'. Got: "+mockc.getLastRequestContent(), 
				mockc.getLastRequestContent().matches(expected));
	}
	
	@Test
	public void testConfigError() throws Exception{
		final TestClass1 t1 = new TestClass1("hello", 1);
		final SimpleGsonLogSerializer<TestClass1> s1 = new SimpleGsonLogSerializer<TestClass1>();
		final JestIndexStringSerializerWrapper<TestClass1> w1 = new JestIndexStringSerializerWrapper<TestClass1>();
		try{
			w1.serializeLog(t1);
			fail("Expected exception");

		}catch(IllegalArgumentException ex){
			assertEquals(String.format(Constants.CONFIGURATION_ERROR_FORMAT,
				"indexMapper", w1.getClass().getName()),
				ex.getMessage());
		}
		// new serializer 
		final SimpleGsonLogSerializer<TestClass1> s2 = new SimpleGsonLogSerializer<TestClass1>();
		final JestIndexStringSerializerWrapper<TestClass1> w2 = new JestIndexStringSerializerWrapper<TestClass1>();
		w2.setIndexMapper(getStaticMapper());
		try{
			w2.serializeLog(t1);
			fail("Expected exception");
		}catch(IllegalArgumentException ex){
			assertEquals(String.format(Constants.CONFIGURATION_ERROR_FORMAT,
				"wrappedJsonStringSerializer", w2.getClass().getName()),
				ex.getMessage());
		}

	}
	
	@Test
	public void testNullError() throws Exception{
		final SimpleGsonLogSerializer<TestClass1> s1 = new SimpleGsonLogSerializer<TestClass1>();
		final JestIndexStringSerializerWrapper<TestClass1> w1 = new JestIndexStringSerializerWrapper<TestClass1>();
		w1.setIndexMapper(getStaticMapper());
		w1.setWrappedJsonStringSerializer(s1);	
		try{
			w1.serializeLog(null);
			fail("Expected exception");
		}catch(LogSubmissionException ex){
			
		}
			
	}

}

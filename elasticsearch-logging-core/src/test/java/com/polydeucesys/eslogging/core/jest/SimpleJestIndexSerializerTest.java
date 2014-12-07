package com.polydeucesys.eslogging.core.jest;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.searchbox.client.http.JestHttpClient;

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
import com.polydeucesys.eslogging.testutils.TestClass3;

public class SimpleJestIndexSerializerTest {

	@Rule 
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void clearExpectation(){
		thrown = ExpectedException.none();
	}
	
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
	
	private LogMapper<TestClass3> getStaticMapper3(){
		final StaticLogMapper<TestClass3> m1 = new StaticLogMapper<TestClass3>();
		m1.setIndexName("testIndex");
		m1.setDocumentType("testDocument");
		return m1;
	}

	
	@Test
	public void testSerializes() throws Exception{
		final TestClass1 t1 = new TestClass1("hello", 1);
		final String expected = "\\{\\s*\"s1\"\\s*\\:\\s*\"hello\"\\s*,\\s*\"i1\"\\s*\\:\\s*1\\s*\\}";
		final SimpleJestIndexSerializer<TestClass1> w1 = new SimpleJestIndexSerializer<TestClass1>();
		w1.setIndexMapper(getStaticMapper());
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
	public void testConfigError() throws Exception{
		final TestClass1 t1 = new TestClass1("hello", 1);
		final SimpleJestIndexSerializer<TestClass1> w1 = new SimpleJestIndexSerializer<TestClass1>();
		try{
			w1.serializeLog(t1);
		}catch(IllegalArgumentException ex){
			assertEquals(String.format(Constants.CONFIGURATION_ERROR_FORMAT,
					"indexMapper", w1.getClass().getName()),
					ex.getMessage());
		}
	}
	
	@Test
	public void testNullError() throws Exception{
		final SimpleJestIndexSerializer<TestClass1> w1 = new SimpleJestIndexSerializer<TestClass1>();
		w1.setIndexMapper(getStaticMapper());
		thrown.expect(LogSubmissionException.class);
		try{
			w1.serializeLog(null);
			fail("Expected exception");
		}catch(LogSubmissionException ex){
			
		}	
	}

	/**
	@Test
	public void perfTest() throws Exception{
		final SimpleJestIndexSerializer<TestClass3> w1 = new SimpleJestIndexSerializer<TestClass3>();
		w1.setIndexMapper(getStaticMapper3());
		long start = System.currentTimeMillis();
		for(int i = 0; i < 1000000; i++){
			TestClass3 t1 = new TestClass3();
			try{
				w1.serializeLog(t1);
			}catch(LogSubmissionException ex){
				
			}	
		}
		System.out.println("Took " + (System.currentTimeMillis() - start) + " milliseconds with Jest serializer");
		
		final TestClass3.TestClass3Serializer s2 = new TestClass3.TestClass3Serializer();
		final JestIndexStringSerializerWrapper<TestClass3> w2 = new JestIndexStringSerializerWrapper<TestClass3>();
		w2.setIndexMapper(getStaticMapper3());
		w2.setWrappedJsonStringSerializer(s2);
		start = System.currentTimeMillis();
		for(int i = 0; i < 1000000; i++){
			TestClass3 t2 = new TestClass3();
			try{
				w2.serializeLog(t2);
			}catch(LogSubmissionException ex){
				
			}	
		}
		System.out.println("Took " + (System.currentTimeMillis() - start) + " milliseconds with coded json + jest wrapper serializer");

		final SimpleGsonLogSerializer<TestClass3> s1 = new SimpleGsonLogSerializer<TestClass3>();
		final JestIndexStringSerializerWrapper<TestClass3> w3 = new JestIndexStringSerializerWrapper<TestClass3>();
		w3.setIndexMapper(getStaticMapper3());
		w3.setWrappedJsonStringSerializer(s2);
		start = System.currentTimeMillis();
		for(int i = 0; i < 1000000; i++){
			TestClass3 t3 = new TestClass3();
			try{
				w3.serializeLog(t3);
			}catch(LogSubmissionException ex){
				
			}	
		}
		System.out.println("Took " + (System.currentTimeMillis() - start) + " milliseconds with gson json + jest wrapper serializer");

	}
    **/
}

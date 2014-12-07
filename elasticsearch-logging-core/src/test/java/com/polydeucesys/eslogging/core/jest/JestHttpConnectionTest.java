package com.polydeucesys.eslogging.core.jest;

import static org.junit.Assert.*;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import com.polydeucesys.eslogging.core.AsyncSubmitCallback;
import com.polydeucesys.eslogging.core.LogSubmissionException;
import com.polydeucesys.eslogging.testutils.MockClosableHttpAsyncClient;
import com.polydeucesys.eslogging.testutils.MockCloseableHttpClient;
import com.polydeucesys.test.utils.JestClientFactoryMockNode;

public class JestHttpConnectionTest {
	
	@Test
	public void testDefaultConfigConnection() {
		JestHttpConnection jhc = new JestHttpConnection();
		jhc.setConnectionString("http://testhost.testdomain.com");
		try{	
			jhc.connect();
		}catch(IllegalArgumentException ex){
			fail("Default configuration fails validation");
		}
	}
	
	@Test
	public void testBadConfigThrows() {
		JestHttpConnection jhc = new JestHttpConnection();
		try{
			jhc.connect();
			fail("Should throw exception when not given valid connectionString");
		}catch(IllegalArgumentException iex){
			//noop
		}
		jhc = new JestHttpConnection();
		jhc.setConnectionString("http://testhost.testdomain.com");
		jhc.setClientConnectionTimeoutMillis(1);
		try{
			jhc.connect();
			fail("Should throw exception with invalid connect timeout");
		}catch(IllegalArgumentException iex){
			//noop
		}
		jhc = new JestHttpConnection();
		jhc.setConnectionString("testhost.testdomain.com");
		jhc.setClientReadTimeoutMillis(1);
		try{
			jhc.connect();
			fail("Should throw exception with invalid read timeout");
		}catch(IllegalArgumentException iex){
			//noop
		}	
		
		jhc = new JestHttpConnection();
		jhc.setConnectionString("testhost.testdomain.com");
		jhc.setMultithreaded(true);
		jhc.setMaxTotalHttpConnections(1);
		try{
			jhc.connect();
			fail("Should throw exception with invalid multithreading config");
		}catch(IllegalArgumentException iex){
			//noop
		}
	}

	@Test 
	public void testBadStatusThrows() throws LogSubmissionException{
		MockCloseableHttpClient mockClient = new MockCloseableHttpClient();
		JestClientFactoryMockNode factory = new JestClientFactoryMockNode(mockClient);
		JestHttpConnection jhc = new JestHttpConnection();
		jhc.setJestClientFactory(factory);
		jhc.setConnectionString("testhost.testdomain.com");
		jhc.connect();
		mockClient.setNextResponse(MockCloseableHttpClient.responseWithBody(401,
				"Unauthorized"));
		Index act = new Index.Builder("{ test: test }").index("testi")
                .type("testt")
                .build();
		Bulk b = new Bulk.Builder().addAction(act).build();
		try{
			JestResult res = jhc.submit(b);
			fail("Status other than 2xx should result in exception");
		}catch(LogSubmissionException lex){
			// no-op
		}
		mockClient.setNextResponse(MockCloseableHttpClient.responseWithBody(500,
				"Bad Server"));
		try{
			JestResult res = jhc.submit(b);
			fail("Status other than 2xx should result in exception");
		}catch(LogSubmissionException lex){
			// no-op
		}
	}
	
	@Test 
	public void testHttpExceptionRethrown() throws LogSubmissionException{
		MockCloseableHttpClient mockClient = new MockCloseableHttpClient();
		JestClientFactoryMockNode factory = new JestClientFactoryMockNode(mockClient);
		JestHttpConnection jhc = new JestHttpConnection();
		jhc.setJestClientFactory(factory);
		jhc.setConnectionString("http://testhost.testdomain.com");
		jhc.connect();
		mockClient.setNextResponse(MockCloseableHttpClient.responseWithBody(200, 
				"{\"_index\" : \"testi\", " + 
						 "\"_type\"  : \"testt\", " + 
						 "\"_id\" : \"1\", \"_version\" : \"1\", \"_created\" : true }"));
		mockClient.setNextResponseException(new IOException("Unit test exception"));
		Index act = new Index.Builder("{ test: test }").index("testi")
                .type("testt")
                .build();
		Bulk b = new Bulk.Builder().addAction(act).build();
		try{
			JestResult res = jhc.submit(b);
			fail("Status other than 2xx should result in exception");
		}catch(LogSubmissionException lex){
			assertTrue(lex.getCause() instanceof IOException);
		}
	}
	
	@Test 
	public void testAsyncBadStatusThrows() throws LogSubmissionException{
		MockClosableHttpAsyncClient mockClient = new MockClosableHttpAsyncClient();
		JestClientFactoryMockNode factory = new JestClientFactoryMockNode(null, mockClient);
		final AtomicBoolean didRun = new AtomicBoolean(false);
		JestHttpConnection jhc = new JestHttpConnection();
		jhc.setJestClientFactory(factory);
		jhc.setConnectionString("http://testhost.testdomain.com");
		jhc.connect();
		mockClient.setNextResponse(MockCloseableHttpClient.responseWithBody(401,
				"Unauthorized"));
		Index act = new Index.Builder("{ test: test }").index("testi")
                .type("testt")
                .build();
		Bulk b = new Bulk.Builder().addAction(act).build();
		AsyncSubmitCallback<JestResult> callback = new AsyncSubmitCallback<JestResult>(){

			@Override
			public void completed(JestResult result) {
				didRun.set(true);
				fail("Should not complete with bad status");
			}

			@Override
			public void error(LogSubmissionException wrappedException) {
				didRun.set(true);
				assertNotNull(wrappedException);
			}
			
		};
		jhc.submitAsync(b, callback);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			
		}
		assertTrue(didRun.get());
	}
	
	@Test 
	public void testAsyncHttpExceptionRethrown() throws LogSubmissionException{
		MockClosableHttpAsyncClient mockClient = new MockClosableHttpAsyncClient();
		JestClientFactoryMockNode factory = new JestClientFactoryMockNode(null, mockClient);
		final AtomicBoolean didRun = new AtomicBoolean(false);

		JestHttpConnection jhc = new JestHttpConnection();
		jhc.setJestClientFactory(factory);
		jhc.setConnectionString("http://testhost.testdomain.com");
		jhc.connect();
		mockClient.setNextResponse(MockCloseableHttpClient.responseWithBody(200, 
				"{\"_index\" : \"testi\", " + 
						 "\"_type\"  : \"testt\", " + 
						 "\"_id\" : \"1\", \"_version\" : \"1\", \"_created\" : true }"));
		mockClient.setNextResponseException(new IOException("Unit test exception"));
		Index act = new Index.Builder("{ test: test }").index("testi")
                .type("testt")
                .build();
		Bulk b = new Bulk.Builder().addAction(act).build();
		AsyncSubmitCallback<JestResult> callback = new AsyncSubmitCallback<JestResult>(){

			@Override
			public void completed(JestResult result) {
				didRun.set(true);
			}

			@Override
			public void error(LogSubmissionException wrappedException) {
				didRun.set(true);
				assertNotNull(wrappedException);
				assertTrue(wrappedException.getCause() instanceof IOException);
			}
			
		};
		jhc.submitAsync(b, callback);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			
		}
		assertTrue(didRun.get());
	}
}

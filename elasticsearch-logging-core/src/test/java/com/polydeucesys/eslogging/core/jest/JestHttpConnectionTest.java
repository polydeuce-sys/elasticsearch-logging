package com.polydeucesys.eslogging.core.jest;

import static org.junit.Assert.*;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import com.polydeucesys.eslogging.core.AsyncSubmitCallback;
import com.polydeucesys.eslogging.core.LogSubmissionException;
import com.polydeucesys.eslogging.testutils.JestClientFactoryMockNode;
import com.polydeucesys.eslogging.testutils.MockClosableHttpAsyncClient;
import com.polydeucesys.eslogging.testutils.MockCloseableHttpClient;
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
public class JestHttpConnectionTest {
	
	@Test
	public void testDefaultConfigConnection() {
		JestHttpConnection jhc = new JestHttpConnection();
		jhc.setConnectionString("http://testhost.testdomain.com");
		try{	
			jhc.start();
		}catch(IllegalArgumentException ex){
			fail("Default configuration fails validation");
		}catch(LogSubmissionException ex){
			fail("Unexpected exception in test");
		}
		
		try {
			jhc.stop();
		} catch (LogSubmissionException e) {
			fail("Unexpected exception in test");
		}
	}
	
	@Test
	public void testBadConfigThrows() {
		JestHttpConnection jhc = new JestHttpConnection();
		try{
			jhc.start();
			fail("Should throw exception when not given valid connectionString");
		}catch(IllegalArgumentException iex){
			//noop
		}catch(LogSubmissionException ex){
			fail("Unexpected exception in test");
		}
		jhc = new JestHttpConnection();
		jhc.setConnectionString("http://testhost.testdomain.com");
		jhc.setClientConnectionTimeoutMillis(1);
		try{
			jhc.start();
			fail("Should throw exception with invalid connect timeout");
		}catch(IllegalArgumentException iex){
			//noop
		}catch(LogSubmissionException ex){
			fail("Unexpected exception in test");
		}
		jhc = new JestHttpConnection();
		jhc.setConnectionString("testhost.testdomain.com");
		jhc.setClientReadTimeoutMillis(1);
		try{
			jhc.start();
			fail("Should throw exception with invalid read timeout");
		}catch(IllegalArgumentException iex){
			//noop
		}catch(LogSubmissionException ex){
			fail("Unexpected exception in test");
		}	
		
		jhc = new JestHttpConnection();
		jhc.setConnectionString("testhost.testdomain.com");
		jhc.setMultithreaded(true);
		jhc.setMaxTotalHttpConnections(1);
		try{
			jhc.start();
			fail("Should throw exception with invalid multithreading config");
		}catch(IllegalArgumentException iex){
			//noop
		}catch(LogSubmissionException ex){
			fail("Unexpected exception in test");
		}
	}
	
	@Test
	public void testStopFlushesRequsts() throws LogSubmissionException{
		final AtomicBoolean didRun = new AtomicBoolean(false);
		MockClosableHttpAsyncClient mockClient = new MockClosableHttpAsyncClient();
		mockClient.setRequestTime(1500L);
		JestClientFactoryMockNode factory = new JestClientFactoryMockNode(null, mockClient);

		JestHttpConnection jhc = new JestHttpConnection();
		jhc.setJestClientFactory(factory);
		jhc.setConnectionString("http://testhost.testdomain.com");
		jhc.setMaxAsyncCompletionTimeForShutdownMillis(2500);
		jhc.start();
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
				fail("Should have run properly");
			}
			
		};
		jhc.submitAsync(b, callback);
		assertFalse(didRun.get());
		jhc.stop();
		assertTrue(didRun.get());
	}

	@Test 
	public void testBadStatusThrows() throws LogSubmissionException{
		MockCloseableHttpClient mockClient = new MockCloseableHttpClient();
		JestClientFactoryMockNode factory = new JestClientFactoryMockNode(mockClient);
		JestHttpConnection jhc = new JestHttpConnection();
		jhc.setJestClientFactory(factory);
		jhc.setConnectionString("testhost.testdomain.com");
		jhc.start();
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
		jhc.start();
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
		jhc.stop();
	}
	
	@Test 
	public void testAsyncBadStatusThrows() throws LogSubmissionException{
		MockClosableHttpAsyncClient mockClient = new MockClosableHttpAsyncClient();
		JestClientFactoryMockNode factory = new JestClientFactoryMockNode(null, mockClient);
		final AtomicBoolean didRun = new AtomicBoolean(false);
		final AtomicBoolean didError = new AtomicBoolean(false);
		
		JestHttpConnection jhc = new JestHttpConnection();
		jhc.setJestClientFactory(factory);
		jhc.setConnectionString("http://testhost.testdomain.com");
		jhc.start();
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
			}

			@Override
			public void error(LogSubmissionException wrappedException) {
				didRun.set(true);
				assertNotNull(wrappedException);
				didError.set(true);
			}
			
		};
		jhc.submitAsync(b, callback);
		// should flush
		jhc.stop();
		assertTrue(didRun.get());
		assertTrue(didError.get());

	}
	
	@Test 
	public void testAsyncHttpInterruptedException() throws LogSubmissionException{
		MockClosableHttpAsyncClient mockClient = new MockClosableHttpAsyncClient();
		JestClientFactoryMockNode factory = new JestClientFactoryMockNode(null, mockClient);
		final AtomicBoolean didRun = new AtomicBoolean(false);
		final AtomicBoolean didError = new AtomicBoolean(false);

		JestHttpConnection jhc = new JestHttpConnection();
		jhc.setJestClientFactory(factory);
		jhc.setConnectionString("http://testhost.testdomain.com");
		jhc.start();
		mockClient.setNextResponse(MockCloseableHttpClient.responseWithBody(200, 
				"{\"_index\" : \"testi\", " + 
						 "\"_type\"  : \"testt\", " + 
						 "\"_id\" : \"1\", \"_version\" : \"1\", \"_created\" : true }"));
		mockClient.setNextResponseException(new InterruptedException("Unit test exception"));
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
				assertNotNull(wrappedException);
				assertTrue(wrappedException.getCause() instanceof InterruptedException);
				didRun.set(true);
				didError.set(true);
			}
			
		};
		jhc.submitAsync(b, callback);
		jhc.stop();
		assertTrue(didRun.get());
		assertTrue(didError.get());
	}
	
	@Test 
	public void testAsyncHttpSocketException() throws LogSubmissionException{
		MockClosableHttpAsyncClient mockClient = new MockClosableHttpAsyncClient();
		JestClientFactoryMockNode factory = new JestClientFactoryMockNode(null, mockClient);
		final AtomicBoolean didRun = new AtomicBoolean(false);
		final AtomicBoolean didError = new AtomicBoolean(false);

		JestHttpConnection jhc = new JestHttpConnection();
		jhc.setJestClientFactory(factory);
		jhc.setConnectionString("http://testhost.testdomain.com");
		jhc.start();
		mockClient.setNextResponse(MockCloseableHttpClient.responseWithBody(200, 
				"{\"_index\" : \"testi\", " + 
						 "\"_type\"  : \"testt\", " + 
						 "\"_id\" : \"1\", \"_version\" : \"1\", \"_created\" : true }"));
		mockClient.setNextResponseException(new SocketException("Unit test exception"));
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
				assertNotNull(wrappedException);
				assertTrue(wrappedException.getCause() instanceof SocketException);
				didRun.set(true);
				didError.set(true);
			}
			
		};
		jhc.submitAsync(b, callback);
		jhc.stop();
		assertTrue(didRun.get());
		assertTrue(didError.get());
	}
	@Test 
	public void testAsyncHttpExceptionRethrown() throws LogSubmissionException{
		MockClosableHttpAsyncClient mockClient = new MockClosableHttpAsyncClient();
		JestClientFactoryMockNode factory = new JestClientFactoryMockNode(null, mockClient);
		final AtomicBoolean didRun = new AtomicBoolean(false);
		final AtomicBoolean didError = new AtomicBoolean(false);


		JestHttpConnection jhc = new JestHttpConnection();
		jhc.setJestClientFactory(factory);
		jhc.setConnectionString("http://testhost.testdomain.com");
		jhc.start();
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
				assertNotNull(wrappedException);
				assertTrue(wrappedException.getCause() instanceof IOException);
				didRun.set(true);
				didError.set(true);
			}
			
		};
		jhc.submitAsync(b, callback);
		jhc.stop();
		assertTrue(didRun.get());
		assertTrue(didError.get());

	}
	
}

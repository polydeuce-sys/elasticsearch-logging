package com.polydeucesys.eslogging.core.jest;

import static org.junit.Assert.*;

import io.searchbox.client.JestResult;
import io.searchbox.core.Index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.polydeucesys.eslogging.core.LogAppenderErrorHandler;
import com.polydeucesys.eslogging.core.LogSubmissionException;
import com.polydeucesys.eslogging.testutils.MockClosableHttpAsyncClient;
import com.polydeucesys.eslogging.testutils.MockCloseableHttpClient;
import com.polydeucesys.test.utils.JestClientFactoryMockNode;

public class JestLogSubmissionStrategyTest {
	private static class UnitTestErrorhandler implements LogAppenderErrorHandler{
		private final ArrayList<LogSubmissionException> exceptions = new ArrayList<LogSubmissionException>();
		
		public List<LogSubmissionException> getExceptions(){
			return exceptions;
		}
		
		@Override
		public void error(LogSubmissionException ex) {
			exceptions.add(ex);
		}
		
	}

	@Test
	public void testSubmitCorrectBulkAndTimed() {
		UnitTestErrorhandler errHandler = new UnitTestErrorhandler();
		MockClosableHttpAsyncClient mockClient = new MockClosableHttpAsyncClient();
		final AtomicInteger callCount = new AtomicInteger(0);
		final AtomicInteger errorCount = new AtomicInteger(0);
		MockClosableHttpAsyncClient.TestCallback<Object> countCallback = new MockClosableHttpAsyncClient.TestCallback<Object>(){

			@Override
			public void callbackResult(Object result) {
				callCount.incrementAndGet();
			}

			@Override
			public void callbackError(Exception error) {
			}
			
		};
		mockClient.setTestCallback(countCallback);
		JestClientFactoryMockNode factory = new JestClientFactoryMockNode(null, mockClient);
		JestHttpConnection jhc = new JestHttpConnection();
		jhc.setJestClientFactory(factory);
		jhc.setConnectionString("http://testhost.testdomain.com");

		JestLogSubmissionStrategy strategy = new JestLogSubmissionStrategy();
		strategy.setConnection(jhc);
		strategy.setErrorHandler(errHandler);
		strategy.setQueueDepth(10);
		strategy.setMaxSubmissionIntervalMillisec(2000);
		for(int i = 0; i < 35; i ++){
			Index idx = new Index.Builder("{ text : \"doc " + i + "\"}").index("testi").type("testd").build();
			try {
				strategy.submit(idx);
			} catch (LogSubmissionException e) {
				fail("Unexpected exception : " + e);
			}
		}
		assertEquals(3, callCount.get());
		assertEquals(0, errHandler.exceptions.size());
		try {
			Thread.sleep(3500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(4, callCount.get());
		assertEquals(0, errHandler.exceptions.size());
	}
	
	@Test
	public void testSubmitWithErrorsAndBadStatus(){
		UnitTestErrorhandler errHandler = new UnitTestErrorhandler();
		MockClosableHttpAsyncClient mockClient = new MockClosableHttpAsyncClient();
		final AtomicInteger callCount = new AtomicInteger(0);
		final AtomicInteger errorCount = new AtomicInteger(0);
		MockClosableHttpAsyncClient.TestCallback<Object> countCallback = new MockClosableHttpAsyncClient.TestCallback<Object>(){

			@Override
			public void callbackResult(Object result) {
				callCount.incrementAndGet();
			}

			@Override
			public void callbackError(Exception error) {
			}
			
		};
		mockClient.setTestCallback(countCallback);
		JestClientFactoryMockNode factory = new JestClientFactoryMockNode(null, mockClient);
		JestHttpConnection jhc = new JestHttpConnection();
		jhc.setJestClientFactory(factory);
		jhc.setConnectionString("http://testhost.testdomain.com");

		JestLogSubmissionStrategy strategy = new JestLogSubmissionStrategy();
		strategy.setConnection(jhc);
		strategy.setErrorHandler(errHandler);
		strategy.setQueueDepth(1);
		strategy.setMaxSubmissionIntervalMillisec(200000);
		mockClient.setNextResponseException(new IOException("Unit test exception"));
		Index idx = new Index.Builder("{ text : \"doc 1\"}").index("testi").type("testd").build();
		try {
			strategy.submit(idx);
		} catch (LogSubmissionException e) {
			fail("Unexpected exception : " + e);
		}
		assertEquals(1, errHandler.exceptions.size());
		try {
			strategy.submit(idx);
		} catch (LogSubmissionException e) {
			fail("Unexpected exception : " + e);
		}
		assertEquals(1, errHandler.exceptions.size());
		mockClient.setNextResponse(MockCloseableHttpClient.responseWithBody(401,
				"Unauthorized"));
		try {
			strategy.submit(idx);
		} catch (LogSubmissionException e) {
			fail("Unexpected exception : " + e);
		}
		assertEquals(2, errHandler.exceptions.size());
		try {
			strategy.submit(idx);
		} catch (LogSubmissionException e) {
			fail("Unexpected exception : " + e);
		}
		assertEquals(2, errHandler.exceptions.size());
	}
}

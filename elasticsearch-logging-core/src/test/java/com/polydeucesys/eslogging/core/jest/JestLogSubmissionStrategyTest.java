package com.polydeucesys.eslogging.core.jest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.searchbox.core.Index;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.polydeucesys.eslogging.core.LogSubmissionException;
import com.polydeucesys.eslogging.testutils.JestClientFactoryMockNode;
import com.polydeucesys.eslogging.testutils.MockClosableHttpAsyncClient;
import com.polydeucesys.eslogging.testutils.MockCloseableHttpClient;
import com.polydeucesys.eslogging.testutils.UnitTestErrorHandler;

public class JestLogSubmissionStrategyTest {

	@Test
	public void testSubmitCorrectBulkAndTimed() {
		UnitTestErrorHandler errHandler = new UnitTestErrorHandler();
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
		jhc.setConnectionString("http://localhost");

		JestLogSubmissionStrategy strategy = new JestLogSubmissionStrategy();
		strategy.setConnection(jhc);
		strategy.setErrorHandler(errHandler);
		strategy.setQueueDepth(10);
		strategy.setMaxSubmissionIntervalMillisec(2000);
		try{
			strategy.start();
		}catch (LogSubmissionException e) {
				fail("Unexpected exception in start : " + e);
		}
		for(int i = 0; i < 35; i ++){
			Index idx = new Index.Builder("{ text : \"doc " + i + "\"}").index("testi").type("testd").build();
			try {
				strategy.submit(idx);
			} catch (LogSubmissionException e) {
				fail("Unexpected exception : " + e);
			}
		}
		assertTrue("Call count was " + callCount.get(), callCount.get() >= 3);
		assertEquals(0, errHandler.getExceptions().size());
		try {
			Thread.sleep(3500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(4 <= callCount.get());
		assertEquals(0, errHandler.getExceptions().size());
	}
	
	@Test
	public void testStopFlushesQueuedSubmits(){
		UnitTestErrorHandler errHandler = new UnitTestErrorHandler();
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
		jhc.setConnectionString("http://localhost");

		JestLogSubmissionStrategy strategy = new JestLogSubmissionStrategy();
		strategy.setConnection(jhc);
		strategy.setErrorHandler(errHandler);
		strategy.setQueueDepth(10);
		strategy.setMaxSubmissionIntervalMillisec(200000);
		try{
			strategy.start();
		}catch (LogSubmissionException e) {
				fail("Unexpected exception in start : " + e);
		}
		for(int i = 0; i < 35; i ++){
			Index idx = new Index.Builder("{ text : \"doc " + i + "\"}").index("testi").type("testd").build();
			try {
				strategy.submit(idx);
			} catch (LogSubmissionException e) {
				fail("Unexpected exception : " + e);
			}
		}
		assertTrue("Call count was " + callCount.get(), callCount.get() == 3);
		assertEquals(0, errHandler.getExceptions().size());
		try {
			strategy.stop();
		} catch (Exception e) {
			fail("Unexpected exception in stop" + e);
		}
		assertEquals(4, callCount.get());
		assertEquals(0, errHandler.getExceptions().size());
	}
	
	@Test
	public void testSubmitWithErrorsAndBadStatus(){
		UnitTestErrorHandler errHandler = new UnitTestErrorHandler();
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
		try{
			strategy.start();
		}catch (LogSubmissionException e) {
				fail("Unexpected exception in start : " + e);
		}
		try {
			strategy.submit(idx);
		} catch (LogSubmissionException e) {
			fail("Unexpected exception in submit : " + e);
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
		}
		assertEquals(1, errHandler.getExceptions().size());
		try {
			strategy.submit(idx);
		} catch (LogSubmissionException e) {
			fail("Unexpected exception : " + e);
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
		}
		assertEquals(1, errHandler.getExceptions().size());
		mockClient.setNextResponse(MockCloseableHttpClient.responseWithBody(401,
				"Unauthorized"));
		try {
			strategy.submit(idx);
		} catch (LogSubmissionException e) {
			fail("Unexpected exception : " + e);
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
		}
		assertEquals(2, errHandler.getExceptions().size());
		try {
			strategy.submit(idx);
		} catch (LogSubmissionException e) {
			fail("Unexpected exception : " + e);
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
		}
		assertEquals(2, errHandler.getExceptions().size());
		try {
			strategy.stop();
		} catch (LogSubmissionException e) {
			fail("Unexpected Exception in stop");
		}
	}
	
	@Test
	public void testLogsQueuedForStart(){
		UnitTestErrorHandler errHandler = new UnitTestErrorHandler();
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
		assertEquals(0, callCount.get());
		assertEquals(0, errHandler.getExceptions().size());
		try{
			strategy.start();
		}catch (LogSubmissionException e) {
				fail("Unexpected exception in start : " + e);
		}
		Index idx = new Index.Builder("{ text : \"doc next\"}").index("testi").type("testd").build();
		try {
			strategy.submit(idx);
		} catch (LogSubmissionException e) {
			fail("Unexpected exception : " + e);
		}
		try {
			strategy.stop();
		} catch (LogSubmissionException e) {
			fail("unexpected exception in stop");
		}
		assertEquals(1, callCount.get());
	}
}

package com.polydeucesys.eslogging.log4j;

import static org.junit.Assert.*;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;

import com.github.restdriver.clientdriver.ClientDriverRule;

public class BaseElasticsearchJestAppenderTest {

	private static final int TEST_PORT = 9200;

	
	@Test
	public void test() {
		String connectionString = "http://localhost:" + TEST_PORT;
		BaseElasticsearchJestAppender appender = new BaseElasticsearchJestAppender();
		appender.setConnectionString(connectionString);
		appender.setQueueDepth(1);
		appender.setMaxSubmissionInterval(100);
		appender.setLogDocType("appLog");
		appender.setLogIndexPrefix("test-logs");
		
		Logger testLogger = LogManager.getLogger("UnitTest");
		testLogger.addAppender(appender);
		testLogger.info("A Test message");
		testLogger.warn("A Test warn message");
		testLogger.error("A test of an error", new IllegalArgumentException("Test Arg"));
		LogManager.shutdown();
	}
	
	@Test
	public void perfTest() {
		String connectionString = "http://localhost:" + TEST_PORT;
		BaseElasticsearchJestAppender appender = new BaseElasticsearchJestAppender();
		appender.setConnectionString(connectionString);
		appender.setQueueDepth(50);
		appender.setMaxSubmissionInterval(100);
		appender.setLogDocType("appLog");
		appender.setLogIndexPrefix("test-logs");
		appender.setLogContextPropertiesJson("{ \"application\" : \"MyTestApp\" }");
		Logger testLogger = LogManager.getLogger("UnitTest");
		testLogger.addAppender(appender);
		long start = System.currentTimeMillis();
		long count = 0;
		while(System.currentTimeMillis() - start < 2000){
			testLogger.log(Level.toPriority( (int)count++ % 9 ), "A log message at " + System.currentTimeMillis() + " is the " + count + " message");
		}
		LogManager.shutdown();

		System.out.println("Wrote " + count + " logs in 2 seconds");
	}

}

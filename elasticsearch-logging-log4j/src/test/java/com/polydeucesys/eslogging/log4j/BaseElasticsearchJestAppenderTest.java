package com.polydeucesys.eslogging.log4j;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.indices.DeleteIndex;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

public class BaseElasticsearchJestAppenderTest {
	private static final String MAVEN_SERVER_URL_PROPERTY = "it-elasticsearch-url";
	private static final String MAVEN_TEST_INDEX_PROPERTY = "it-elasticsearch-index";
	private static final SimpleDateFormat todayFormat = new SimpleDateFormat("yyyy.MM.dd");
	
	private static String getDateString(){
		return todayFormat.format(new Date());
	}

	
	@After
	private void tearDownIntegrationTestIndex() throws Exception{
		String DateString = getDateString();
		JestClientFactory factory = new JestClientFactory();
		HttpClientConfig.Builder builder = new HttpClientConfig.Builder(System.getProperty("it-elasticsearch-url"));
		factory.setHttpClientConfig(builder.build());
		JestClient client = factory.getObject();
		DeleteIndex deleteAction = new DeleteIndex.Builder(System.getProperty(MAVEN_TEST_INDEX_PROPERTY) + DateString).build();
		client.execute( deleteAction );
	}
	
	@Test
	public void testBasicLoggingIT() {
		String connectionString = System.getProperty("it-elasticsearch-url");
		String testIndex = System.getProperty("it-elasticsearch-url");
		String testDocType = System.getProperty("it-elasticsearch-doctype");
		
		BaseElasticsearchJestAppender appender = new BaseElasticsearchJestAppender();
		appender.setConnectionString(connectionString);
		appender.setQueueDepth(1);
		appender.setMaxSubmissionInterval(100);
		appender.setLogDocType(testDocType);
		appender.setLogIndexPrefix(testIndex);
		appender.activateOptions();
		Logger testLogger = LogManager.getLogger("UnitTest");
		testLogger.addAppender(appender);
		testLogger.info("A Test message");
		testLogger.warn("A Test warn message");
		testLogger.error("A test of an error", new IllegalArgumentException("Test Arg"));
		LogManager.shutdown();
		// this is all async and networked, so for simplicity, we will 
		// just use a sleep
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void perfTest() {
		BaseElasticsearchJestAppender appender = new BaseElasticsearchJestAppender();
		appender.setConnectionString("");
		appender.setQueueDepth(50);
		appender.setMaxSubmissionInterval(100);
		appender.setLogDocType("appLog");
		appender.setLogIndexPrefix("test-logs");
		appender.setLogContextPropertiesJson("{ \"application\" : \"MyTestApp\" }");
		appender.activateOptions();
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

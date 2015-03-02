package com.polydeucesys.eslogging.log4j;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.indices.DeleteIndex;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

public class BaseElasticsearchJestAppenderIT {
	private static final String MAVEN_SERVER_URL_PROPERTY = "it-elasticsearch-url";
	private static final String MAVEN_TEST_INDEX_PROPERTY = "it-elasticsearch-index";
	private static final SimpleDateFormat todayFormat = new SimpleDateFormat("yyyy.MM.dd");
	
	private static String getDateString(){
		return todayFormat.format(new Date());
	}

	
	@After
	public void tearDownIntegrationTestIndex() throws Exception{
		JestClientFactory factory = new JestClientFactory();
		HttpClientConfig.Builder builder = new HttpClientConfig.Builder(System.getProperty(MAVEN_SERVER_URL_PROPERTY));
		factory.setHttpClientConfig(builder.build());
		JestClient client = factory.getObject();
		DeleteIndex deleteAction = new DeleteIndex.Builder(System.getProperty(MAVEN_TEST_INDEX_PROPERTY) + "-" + getDateString()).build();
		JestResult jr = client.execute( deleteAction );
		if(!jr.isSucceeded()){
			throw new RuntimeException(jr.getErrorMessage());
		}
		Thread.sleep(1000);
	}
	
	@Test
	public void testBasicLoggingIT() throws Exception{
		String connectionString = System.getProperty("it-elasticsearch-url");
		String testIndex = System.getProperty("it-elasticsearch-index");
		String testDocType = System.getProperty("it-elasticsearch-doctype");
		System.out.println("****** PROPERTIES ******" + connectionString);
		BaseElasticsearchJestAppender appender = new BaseElasticsearchJestAppender();
		appender.setConnectionString(connectionString);
		appender.setQueueDepth(1);
		appender.setMaxSubmissionInterval(100);
		appender.setLogDocType(testDocType);
		appender.setLogIndexPrefix(testIndex);
		appender.activateOptions();
		Logger testLogger = LogManager.getLogger("UnitTest");
		testLogger.addAppender(appender);
		testLogger.setLevel(Level.INFO);
		testLogger.info("A Test message");
		testLogger.warn("A Test warn message");
		testLogger.error("A test of an error", new IllegalArgumentException("Test Arg"));
		LogManager.shutdown();
		// this is all async and networked, so for simplicity, we will 
		// just use a sleep
		Thread.sleep(1500);
		// prove that there are 3 records in the index
		JestClientFactory factory = new JestClientFactory();
		HttpClientConfig.Builder builder = new HttpClientConfig.Builder(System.getProperty(MAVEN_SERVER_URL_PROPERTY));
		factory.setHttpClientConfig(builder.build());
		JestClient client = factory.getObject();
		Search search = new Search.Builder("{ \"query\" : { \"match_all\" : {} } }").addIndex(System.getProperty(MAVEN_TEST_INDEX_PROPERTY) + "-" + getDateString()).build();
		JestResult res = client.execute(search);
		
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

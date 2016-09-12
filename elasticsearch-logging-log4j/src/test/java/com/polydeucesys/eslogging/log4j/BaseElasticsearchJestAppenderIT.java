package com.polydeucesys.eslogging.log4j;

import static com.polydeucesys.utils.TestConstants.*;
import static org.junit.Assert.assertEquals;

import com.google.gson.JsonObject;
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
import org.junit.Test;

public class BaseElasticsearchJestAppenderIT {

	private static final SimpleDateFormat todayFormat = new SimpleDateFormat("yyyy.MM.dd");
	
	private static String getDateString(){
		return todayFormat.format(new Date());
	}

    // The appender deals with checking if we have a "-" on our name. Test code does not.
    private String getTestIndexName( String testIndex ){
        if(!testIndex.endsWith("-")){
            testIndex = testIndex + "_";
        }
        return testIndex + getDateString();
    }

	public void tearDownIntegrationTestIndex(String testIndex) throws Exception{
		JestClientFactory factory = new JestClientFactory();
		HttpClientConfig.Builder builder = new HttpClientConfig.Builder(System.getProperty(MAVEN_SERVER_URL_PROPERTY,MAVEN_SERVER_URL_PROPERTY_DEFAULT));
		factory.setHttpClientConfig(builder.build());
		JestClient client = factory.getObject();

		DeleteIndex deleteAction = new DeleteIndex.Builder( getTestIndexName(testIndex) ).build();
		JestResult jr = client.execute( deleteAction );
		if(!jr.isSucceeded()){
			throw new RuntimeException(jr.getErrorMessage());
		}
	}
	
	@Test
	public void testBasicLoggingIT() throws Exception{
		String connectionString = System.getProperty(MAVEN_SERVER_URL_PROPERTY, MAVEN_SERVER_URL_PROPERTY_DEFAULT);
		String testIndex = System.getProperty(MAVEN_TEST_INDEX_PROPERTY, MAVEN_TEST_INDEX_PROPERTY_DEFAULT);
		String testDocType = System.getProperty(MAVEN_TEST_DOCTYPE_PROPERTY, MAVEN_TEST_DOCTYPE_PROPERTY_DEFAULT);
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
		HttpClientConfig.Builder builder = new HttpClientConfig.Builder(System.getProperty(MAVEN_SERVER_URL_PROPERTY, MAVEN_SERVER_URL_PROPERTY_DEFAULT));
		factory.setHttpClientConfig(builder.build());
		JestClient client = factory.getObject();
		Search search = new Search.Builder("{ \"query\" : { \"match_all\" : {} } }").addIndex(getTestIndexName(testIndex)).build();
		JestResult res = client.execute(search);
		JsonObject resObj = res.getJsonObject();
		int hits = resObj.getAsJsonObject("hits").get("total").getAsInt();
		assertEquals("Should have e hits " + res.getJsonString(), hits, 3);

		// this is all async and networked, so for simplicity, we will
        // just use a sleep
        Thread.sleep(1500);
        tearDownIntegrationTestIndex(testIndex);
	}

	@Test
	public void perfTest() throws Exception{
        String testIndex = "perf_" + System.getProperty(MAVEN_TEST_INDEX_PROPERTY, MAVEN_TEST_INDEX_PROPERTY_DEFAULT);
        BaseElasticsearchJestAppender appender = new BaseElasticsearchJestAppender();
		appender.setConnectionString("http://localhost:9200");
		appender.setQueueDepth(50);
		appender.setMaxSubmissionInterval(100);
		appender.setLogDocType("appLog");
		appender.setLogIndexPrefix(testIndex);
		appender.setLogContextPropertiesJson("{ \"application\" : \"MyTestApp\" }");
		appender.activateOptions();
		Logger testLogger = LogManager.getLogger("UnitTest");
		testLogger.addAppender(appender);
		long start = System.currentTimeMillis();
		long count = 0;
		while(System.currentTimeMillis() - start < 2000){
			testLogger.log(Level.toPriority( (int)count++ % 9 ), "A log message at " + System.currentTimeMillis() +
                    " is the " + count + " message");
		}
		LogManager.shutdown();

		System.out.println("Wrote " + count + " logs in 2 seconds");
        // this is all async and networked, so for simplicity, we will
        // just use a sleep
        Thread.sleep(2500);
        //tearDownIntegrationTestIndex(testIndex);
	}

}

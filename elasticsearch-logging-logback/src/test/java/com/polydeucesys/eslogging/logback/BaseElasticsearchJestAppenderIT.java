package com.polydeucesys.eslogging.logback;

import static com.polydeucesys.utils.TestConstants.*;
import static org.junit.Assert.*;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.google.gson.JsonObject;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.indices.DeleteIndex;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kevinmclellan on 01/09/2016.
 */
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
        String testConfigFile = "logback-test.xml";
        System.out.println("****** PROPERTIES ******" + connectionString);
        System.setProperty("connection.string", connectionString);
        System.setProperty("log.doc.type", testDocType);
        System.setProperty("log.index.prefix", testIndex);
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.start();

        Logger testLogger = LoggerFactory.getLogger("UnitTest");
        testLogger.info("A Test message");
        testLogger.warn("A Test warn message");
        testLogger.error("A test of an error", new IllegalArgumentException("Test Arg"));
        Thread.sleep(1500);

        context.stop();
        // this is all async and networked, so for simplicity, we will
        // just use a sleep
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
        String connectionString = System.getProperty(MAVEN_SERVER_URL_PROPERTY, MAVEN_SERVER_URL_PROPERTY_DEFAULT);
        String testIndex = System.getProperty(MAVEN_TEST_INDEX_PROPERTY, MAVEN_TEST_INDEX_PROPERTY_DEFAULT);
        String testDocType = System.getProperty(MAVEN_TEST_DOCTYPE_PROPERTY, MAVEN_TEST_DOCTYPE_PROPERTY_DEFAULT);
        String testConfigFile = "logback-perf-test.xml";
        System.out.println("****** PROPERTIES ******" + connectionString);
        System.setProperty("connection.string", connectionString);
        System.setProperty("log.doc.type", testDocType);
        System.setProperty("log.index.prefix", testIndex);
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        // each test can use a separate config file
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            // Call context.reset() to clear any previous configuration, e.g. default
            // configuration. For multi-step configuration, omit calling context.reset().
            context.reset();
            configurator.doConfigure(testConfigFile);
        } catch (JoranException je) {
            // StatusPrinter will handle this
        }
        long start = System.currentTimeMillis();
        long count = 0;
        Logger testLogger = LoggerFactory.getLogger("PerfTest");
        context.start();

        while(System.currentTimeMillis() - start < 2000){
            testLogger.info( "A log message at " + System.currentTimeMillis() +
                    " is the " + count + " message");
        }
        context.stop();

        System.out.println("Wrote " + count + " logs in 2 seconds");
        // this is all async and networked, so for simplicity, we will
        // just use a sleep
        Thread.sleep(2500);
        //tearDownIntegrationTestIndex(testIndex);
    }

}

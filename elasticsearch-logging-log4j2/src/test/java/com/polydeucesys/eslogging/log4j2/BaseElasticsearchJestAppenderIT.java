package com.polydeucesys.eslogging.log4j2;

import static com.polydeucesys.utils.TestConstants.*;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.*;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.indices.DeleteIndex;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.spi.StandardLevel;
import org.junit.*;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseElasticsearchJestAppenderIT {

    private static final SimpleDateFormat todayFormat = new SimpleDateFormat("yyyy.MM.dd");

    private static String getDateString(){
        return todayFormat.format(new Date());
    }

    // The appender deals with checking if we have a "-" on our name. Test code does not.
    private String getTestIndexName(){
        String testIndex = System.getProperty(MAVEN_TEST_INDEX_PROPERTY, MAVEN_TEST_INDEX_PROPERTY_DEFAULT);
        if(!testIndex.endsWith("-")){
            testIndex = testIndex + "_";
        }
        return testIndex + getDateString();
    }

    @After
    public void tearDownIntegrationTestIndex() throws Exception{
        JestClientFactory factory = new JestClientFactory();
        HttpClientConfig.Builder builder = new HttpClientConfig.Builder(System.getProperty(MAVEN_SERVER_URL_PROPERTY,MAVEN_SERVER_URL_PROPERTY_DEFAULT));
        factory.setHttpClientConfig(builder.build());
        JestClient client = factory.getObject();

        DeleteIndex deleteAction = new DeleteIndex.Builder( getTestIndexName() ).build();
        JestResult jr = client.execute( deleteAction );
        if(!jr.isSucceeded()){
            throw new RuntimeException(jr.getErrorMessage());
        }
        Thread.sleep(1000);
    }

    @Test
    public void testBasicLoggingIT() throws Exception{
        String connectionString = System.getProperty(MAVEN_SERVER_URL_PROPERTY, MAVEN_SERVER_URL_PROPERTY_DEFAULT);
        String testIndex = System.getProperty(MAVEN_TEST_INDEX_PROPERTY, MAVEN_TEST_INDEX_PROPERTY_DEFAULT);
        String testDocType = System.getProperty(MAVEN_TEST_DOCTYPE_PROPERTY, MAVEN_TEST_DOCTYPE_PROPERTY_DEFAULT);
        System.out.println("****** PROPERTIES ******" + connectionString);
        System.setProperty("connection.string", connectionString);
        System.setProperty("log.doc.type", testDocType);
        System.setProperty("log.index.prefix", testIndex);
        Logger testLogger = LogManager.getLogger("UnitTest");
        testLogger.info("A Test message");
        testLogger.warn("A Test warn message");
        testLogger.error("A test of an error", new IllegalArgumentException("Test Arg"));
        // this is all async and networked, so for simplicity, we will
        // just use a sleep
        Thread.sleep(1500);
        // prove that there are 3 records in the index
        JestClientFactory factory = new JestClientFactory();
        HttpClientConfig.Builder builder = new HttpClientConfig.Builder(System.getProperty(MAVEN_SERVER_URL_PROPERTY, MAVEN_SERVER_URL_PROPERTY_DEFAULT));
        factory.setHttpClientConfig(builder.build());
        JestClient client = factory.getObject();
        Search search = new Search.Builder("{ \"query\" : { \"match_all\" : {} } }").addIndex(getTestIndexName()).build();
        JestResult res = client.execute(search);

    }

    @Ignore
    @Test
    public void perfTest() {

        Logger testLogger = LogManager.getLogger("UnitTest");
        long start = System.currentTimeMillis();
        long count = 0;
        while(System.currentTimeMillis() - start < 2000){
            testLogger.log(Level.getLevel(StandardLevel.getStandardLevel((int) count++ % 9).name()), "A log message at " + System.currentTimeMillis() + " is the " + count + " message");
        }

        System.out.println("Wrote " + count + " logs in 2 seconds");
    }

}
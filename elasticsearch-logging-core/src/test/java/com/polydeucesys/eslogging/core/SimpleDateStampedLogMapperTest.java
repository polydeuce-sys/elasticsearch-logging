package com.polydeucesys.eslogging.core;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class SimpleDateStampedLogMapperTest {

	@Test
	public void testMappingsForLog() {
		String testFormat = "yyyy-MM-dd";
		String testPrefix = "unitTest";
		SimpleDateFormat tFormat = new SimpleDateFormat(testFormat);
		SimpleDateStampedLogMapper<String> mapper = new SimpleDateStampedLogMapper<String>(testPrefix,testFormat , "logs");
		assertEquals(mapper.getDocumentTypeForLog("ALog"), "logs");
		assertEquals(mapper.getIndexNameForLog("MyLog"), testPrefix + "-" + tFormat.format(new Date()));
	}

	@Test
	public void testConstructorFailsForBadFormat() {
		try{
			String testFormat = "yyfy-MM-dd";
			String testPrefix = "unitTest";
			SimpleDateFormat tFormat = new SimpleDateFormat(testFormat);
			SimpleDateStampedLogMapper<String> mapper = new SimpleDateStampedLogMapper<String>(testPrefix,testFormat , "logs");
			fail("Constructor should fail");
		}catch(IllegalArgumentException iaex){
			assertTrue(true);
		}
	}

}

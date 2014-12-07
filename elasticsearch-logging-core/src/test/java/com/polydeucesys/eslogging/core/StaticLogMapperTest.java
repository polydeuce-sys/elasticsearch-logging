package com.polydeucesys.eslogging.core;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * 
 * @author Kevin McLellan
 *
 */
public class StaticLogMapperTest {
	
	@Rule 
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testStaticMapper() {
		final String testLog = "This test log";
		final StaticLogMapper<String> testMapper = new StaticLogMapper<String>();
		thrown.expectMessage(String.format(Constants.CONFIGURATION_ERROR_FORMAT, 
				"indexName", "com.polydeucesys.eslogging.core.StaticLogMapper"));
		testMapper.getIndexNameForLog(testLog);
		thrown.expectMessage(String.format(Constants.CONFIGURATION_ERROR_FORMAT, 
				"documentType", "com.polydeucesys.eslogging.core.StaticLogMapper"));
		testMapper.getDocumentTypeForLog("This test log");
		final String testIndex = "testIndex";
		final String testDocumentType = "testDocument";
		testMapper.setIndexName(testIndex);
		testMapper.setDocumentType(testDocumentType);
		assertEquals(testIndex, testMapper.getIndexNameForLog(testLog));
		assertEquals(testDocumentType, testMapper.getDocumentTypeForLog(testLog));
		assertEquals(testIndex, testMapper.getIndexNameForLog(null));
		assertEquals(testDocumentType, testMapper.getDocumentTypeForLog(null));
		assertEquals(testIndex, testMapper.getIndexNameForLog(""));
		assertEquals(testDocumentType, testMapper.getDocumentTypeForLog(""));
	}

}

package com.polydeucesys.eslogging.core;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
/**
 * Copyright (c) 2016 Polydeuce-Sys Ltd
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 **/
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

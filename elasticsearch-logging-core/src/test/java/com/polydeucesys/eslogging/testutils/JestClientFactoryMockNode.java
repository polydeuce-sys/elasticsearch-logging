package com.polydeucesys.eslogging.testutils;


import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.http.JestHttpClient;
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

public class JestClientFactoryMockNode extends JestClientFactory {
	private final MockCloseableHttpClient mockHttpClient;
	private final MockClosableHttpAsyncClient mockAsyncHttpClient;
	
	public JestClientFactoryMockNode(MockCloseableHttpClient mockHttpClient){
		this.mockHttpClient = mockHttpClient;
		this.mockAsyncHttpClient = new MockClosableHttpAsyncClient();
	}
	
	public JestClientFactoryMockNode(MockCloseableHttpClient mockHttpClient,
			                         MockClosableHttpAsyncClient mockAsyncHttpClient){
		this.mockHttpClient = (mockHttpClient == null? new MockCloseableHttpClient() : mockHttpClient);
		this.mockAsyncHttpClient = (mockAsyncHttpClient == null ? new MockClosableHttpAsyncClient(): mockAsyncHttpClient);
	}
	 
	@Override
	public JestClient getObject(){
		JestHttpClient client = (JestHttpClient)super.getObject();
		client.setHttpClient(mockHttpClient);
		client.setAsyncClient(mockAsyncHttpClient);
		return client;
	}
}

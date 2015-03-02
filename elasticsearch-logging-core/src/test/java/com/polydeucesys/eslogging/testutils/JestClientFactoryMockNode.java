package com.polydeucesys.eslogging.testutils;


import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.http.JestHttpClient;

public class JestClientFactoryMockNode extends JestClientFactory {
	private final MockCloseableHttpClient mockHttpClient;
	private final MockClosableHttpAsyncClient mockAsyncHttpClient;
	
	public JestClientFactoryMockNode(MockCloseableHttpClient mockHttpClient){
		this.mockHttpClient = mockHttpClient;
		this.mockAsyncHttpClient = null;
	}
	
	public JestClientFactoryMockNode(MockCloseableHttpClient mockHttpClient,
			                         MockClosableHttpAsyncClient mockAsyncHttpClient){
		this.mockHttpClient = mockHttpClient;
		this.mockAsyncHttpClient = mockAsyncHttpClient;
	}
	 
	@Override
	public JestClient getObject(){
		JestHttpClient client = (JestHttpClient)super.getObject();
		client.setHttpClient(mockHttpClient);
		client.setAsyncClient(mockAsyncHttpClient);
		return client;
	}
}

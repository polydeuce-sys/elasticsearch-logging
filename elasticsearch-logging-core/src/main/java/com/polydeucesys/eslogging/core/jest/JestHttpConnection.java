package com.polydeucesys.eslogging.core.jest;

import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.JestResultHandler;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.http.JestHttpClient;
import io.searchbox.core.Bulk;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.auth.AuthScope;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import com.polydeucesys.eslogging.core.AsyncSubmitCallback;
import com.polydeucesys.eslogging.core.Connection;
import com.polydeucesys.eslogging.core.Constants;
import com.polydeucesys.eslogging.core.LogSubmissionException;
import com.polydeucesys.utils.StringUtils;
/**
 *  Copyright 2016 Polydeuce-Sys Ltd
 *
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *       Unless required by applicable law or agreed to in writing, software
 *       distributed under the License is distributed on an "AS IS" BASIS,
 *       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *       See the License for the specific language governing permissions and
 *       limitations under the License.
 **/

/**
 * Provides a Connection implementation based on the Jest REST client. 
 * 
 * @author Kevin McLellan
 *
 */
public class JestHttpConnection implements Connection<Bulk, JestResult> {

	private static class JestResultWrapper implements JestResultHandler<JestResult>{
		private final AsyncSubmitCallback<JestResult> callback;
		private final AtomicInteger activeAsyncRequests;
		
		public JestResultWrapper(final AsyncSubmitCallback<JestResult> callback, 
				                 final AtomicInteger activeAsyncRequests){
			this.callback = callback;
			this.activeAsyncRequests = activeAsyncRequests;
		}

		@Override
		public void completed(JestResult result) {
			try {
                if (result.isSucceeded()) {
                    callback.completed(result);
                } else {
                    callback.error(new LogSubmissionException(
                            String.format(JestConstants.JEST_BAD_STATUS_EXCEPTION_FORMAT,
                                    result.getErrorMessage())));
                }
            }catch( IllegalStateException e){
                callback.error(new LogSubmissionException(
                        String.format(JestConstants.JEST_BAD_STATUS_EXCEPTION_FORMAT,
                                e.getMessage())));
			}finally{
				activeAsyncRequests.decrementAndGet();
			}
		}

		@Override
		public void failed(Exception ex) {
			try{
				callback.error(new LogSubmissionException(
						 				String.format(JestConstants.JEST_ASYNC_EXCEPTION_FORMAT, 
						 							  ex),
								       ex));
			}finally{
				activeAsyncRequests.decrementAndGet();
			}
		}
		
	}
	private Set<String> esHosts = new TreeSet<String>();
	private JestClientFactory jestClientFactory;
	private JestHttpClient jestClient;
	private HttpClientCredentialConfig clientCredentialConfig;
	private final AtomicInteger activeAsyncRequests = new AtomicInteger(0);
	
	// http config values
	private boolean isMultithreaded = JestConstants.CLIENT_IS_MULTITHREADED_DEFAULT;  
	private int     maxTotalHttpConnections = JestConstants.CLIENT_MAX_TOTAL_HTTP_CONNECTIONS;
	private int 	defaultMaxConnectionsPerRoute = JestConstants.CLIENT_DEFAULT_MAX_HTTP_CONNECTIONS_PER_ROUTE;
	private boolean isDiscoveryEnabled = JestConstants.CLIENT_IS_NODE_DISCOVERY_ENABLED_DEFAULT;
	private long    nodeDiscoveryIntervalMillis = JestConstants.CLIENT_NODE_DISCOVERY_INTERVAL_MILLIS;
	private int     clientConnectionTimeoutMillis = JestConstants.CLIENT_CONNECTION_TIMEOUT_MILLIS;
	private int     minimumAllowedConnectionTimeoutMillis = JestConstants.CLIENT_DEFAULT_MINIMUM_CONNECTION_TIMEOUT_MILLIS;
	private int     clientReadTimeoutMillis = JestConstants.CLIENT_READ_TIMEOUT_MILLIS;
	private int     minimumAllowedReadTimeoutMillis = JestConstants.CLIENT_DEFAULT_MINIMUM_READ_TIMEOUT_MILLIS;

	private long    clientMaxConnectionIdleTimeMillis = JestConstants.CLIENT_MAX_CONNECTION_IDLE_TIME_MILLIS;
	
	private long    maxAsyncCompletionTimeForShutdownMillis = JestConstants.CLIENT_DEFAULT_MAX_ASYNC_COMPLETEION_TIME_FOR_SHUTDOWN;

	private volatile boolean started = false;
	@Override
	public void setConnectionString(String connectionString) {
		StringUtils.splitToCollection(connectionString, esHosts);
		if(esHosts.size() == 0){
			throw new IllegalArgumentException(String.format(
					Constants.CONFIGURATION_ERROR_FORMAT, "connectionString", this.getClass().getName()));
		}
	}

	@Override
	public String getConnectionString() {
		return StringUtils.join(esHosts);
	}

	public JestClientFactory getJestClientFactory() {
		return jestClientFactory;
	}

	/**
	 * Set the instance of {@link JestClientFactory} used to return Jest clients for use with
	 * the server specified in the connection string. This allows for clients which can connect to
	 * secured servers for example. See {@link com.polydeucesys.eslogging.core.jest.ex.kerberos.KerberosJestClientFactory}
	 * @param jestClientFactory
	 */
	public void setJestClientFactory(final JestClientFactory jestClientFactory) {
		this.jestClientFactory = jestClientFactory;
	}

	public boolean isMultithreaded() {
		return isMultithreaded;
	}

	public void setMultithreaded(boolean isMultithreaded) {
		this.isMultithreaded = isMultithreaded;
	}

	public int getMaxTotalHttpConnections() {
		return maxTotalHttpConnections;
	}

	public void setMaxTotalHttpConnections(int maxTotalHttpConnections) {
		this.maxTotalHttpConnections = maxTotalHttpConnections;
	}

	public int getDefaultMaxConnectionsPerRoute() {
		return defaultMaxConnectionsPerRoute;
	}

	public void setDefaultMaxConnectionsPerRoute(int defaultMaxConnectionsPerRoute) {
		this.defaultMaxConnectionsPerRoute = defaultMaxConnectionsPerRoute;
	}

	public boolean isDiscoveryEnabled() {
		return isDiscoveryEnabled;
	}

	public void setDiscoveryEnabled(boolean isDiscoveryEnabled) {
		this.isDiscoveryEnabled = isDiscoveryEnabled;
	}

	public long getNodeDiscoveryIntervalMillis() {
		return nodeDiscoveryIntervalMillis;
	}

	public void setNodeDiscoveryIntervalMillis(long nodeDiscoveryIntervalMillis) {
		this.nodeDiscoveryIntervalMillis = nodeDiscoveryIntervalMillis;
	}

	public int getClientConnectionTimeoutMillis() {
		return clientConnectionTimeoutMillis;
	}

	public int getMinimumAllowedConnectionTimeoutMillis() {
		return minimumAllowedConnectionTimeoutMillis;
	}

	public void setMinimumAllowedConnectionTimeoutMillis(
			int minimumAllowedConnectionTimeoutMillis) {
		this.minimumAllowedConnectionTimeoutMillis = minimumAllowedConnectionTimeoutMillis;
	}

	public void setClientConnectionTimeoutMillis(int clientConnectionTimeoutMillis) {
		this.clientConnectionTimeoutMillis = clientConnectionTimeoutMillis;
	}

	public int getClientReadTimeoutMillis() {
		return clientReadTimeoutMillis;
	}

	public void setClientReadTimeoutMillis(int clientReadTimeoutMillis) {
		this.clientReadTimeoutMillis = clientReadTimeoutMillis;
	}

	public int getMinimumAllowedReadTimeoutMillis() {
		return minimumAllowedReadTimeoutMillis;
	}

	public void setMinimumAllowedReadTimeoutMillis(
			int minimumAllowedReadTimeoutMillis) {
		this.minimumAllowedReadTimeoutMillis = minimumAllowedReadTimeoutMillis;
	}

	public long getClientMaxConnectionIdleTimeMillis() {
		return clientMaxConnectionIdleTimeMillis;
	}

	public void setClientMaxConnectionIdleTimeMillis(
			long clientMaxConnectionIdleTimeMillis) {
		this.clientMaxConnectionIdleTimeMillis = clientMaxConnectionIdleTimeMillis;
	}
	
	public long getMaxAsyncCompletionTimeForShutdownMillis() {
		return maxAsyncCompletionTimeForShutdownMillis;
	}

	public void setMaxAsyncCompletionTimeForShutdownMillis(
			long maxAsyncCompletionTimeForShutdownMillis) {
		this.maxAsyncCompletionTimeForShutdownMillis = maxAsyncCompletionTimeForShutdownMillis;
	}

	public HttpClientCredentialConfig getClientCredentialConfig() {
		return clientCredentialConfig;
	}

	public void setClientCredentialConfig(HttpClientCredentialConfig clientCredentialConfig) {
		this.clientCredentialConfig = clientCredentialConfig;
	}

	@Override
	public JestResult submit(Bulk document) throws LogSubmissionException {
		try {
			JestResult result = jestClient.execute(document);
			if(!result.isSucceeded()){
				throw new LogSubmissionException(
						 String.format(JestConstants.JEST_BAD_STATUS_EXCEPTION_FORMAT, 
								       result.getErrorMessage(), 
								       toString())); 
			}
			return result;
		} catch (IOException e) {
			throw new LogSubmissionException(
					 String.format(JestConstants.JEST_EXCEPTION_FORMAT, toString()),
					 e);
		} catch (IllegalStateException e) {
		    // Apparently this is thrown for invalid response bodies rather than
            // checking status codes
            throw new LogSubmissionException(
                    String.format(JestConstants.JEST_EXCEPTION_FORMAT, toString()),
                    e);
        }
	}

	@Override
	public void submitAsync(Bulk document,
			AsyncSubmitCallback<JestResult> callback) {
        activeAsyncRequests.incrementAndGet();
        jestClient.executeAsync(document, new JestResultWrapper(callback, activeAsyncRequests));
	}
	
	// ensure max connections are not 0 if multi-threaded
	private void validateConnectionParameters(){
		if(esHosts.size() == 0){
			throw new IllegalArgumentException(String.format(
					Constants.CONFIGURATION_ERROR_FORMAT, 
					"connectionString", 
					this.getClass().getName()));
		}
		if(getClientConnectionTimeoutMillis() < getMinimumAllowedConnectionTimeoutMillis()){
			throw new IllegalArgumentException(String.format(
					Constants.CONFIGURATION_ERROR_FORMAT, "clientConnectionTimeoutMillis", this.getClass().getName()));
		}
		if(getClientReadTimeoutMillis() < getMinimumAllowedReadTimeoutMillis()){
			throw new IllegalArgumentException(String.format(
					Constants.CONFIGURATION_ERROR_FORMAT, "clientReadTimeoutMillis", this.getClass().getName()));
		}
		if(isDiscoveryEnabled() && 
				getNodeDiscoveryIntervalMillis() < JestConstants.MINIMUM_CLIENT_NODE_DISCOVERY_INTERVAL_MILLIS){
			throw new IllegalArgumentException(String.format(
					Constants.CONFIGURATION_ERROR_FORMAT, "nodeDiscoveryIntervalMillis", this.getClass().getName()));
		}
		if(isMultithreaded() && 
				getMaxTotalHttpConnections() < 2 ||
				getDefaultMaxConnectionsPerRoute() < 2){
			throw new IllegalArgumentException(String.format(
					Constants.CONFIGURATION_ERROR_FORMAT, 
					"maxTotalHttpConnections or defaultMaxConnectionsPerRoute", 
					this.getClass().getName()));
		}
	}

	private HttpClientConfig setupHttpClientParameters(){
		HttpClientConfig httpClientConfig = new HttpClientConfig.Builder(esHosts)
		.addServer(esHosts)
		.discoveryEnabled(isDiscoveryEnabled())
		.discoveryFrequency(getNodeDiscoveryIntervalMillis(),TimeUnit.MILLISECONDS )
		.maxTotalConnection(getMaxTotalHttpConnections())
		.defaultMaxTotalConnectionPerRoute(getDefaultMaxConnectionsPerRoute())
		.connTimeout(getClientConnectionTimeoutMillis())
		.readTimeout(getClientReadTimeoutMillis())
		.maxConnectionIdleTime(getClientMaxConnectionIdleTimeMillis(), TimeUnit.MILLISECONDS)
		.build();
		return httpClientConfig;
	}
	

	private JestClientFactory setupDefaultClientFactory(){
		JestClientFactory factory = new JestClientFactory();
		if(getClientCredentialConfig() != null){
			factory =  new JestClientFactory() {
				@Override
			    protected HttpClientBuilder configureHttpClient(HttpClientBuilder builder) {
					HttpClientCredentialConfig credConfig = getClientCredentialConfig();
					CredentialsProvider baseProvider = new BasicCredentialsProvider();
					AuthScope scope = new AuthScope(credConfig.getAuthScopeHost(), 
							                        credConfig.getAuthScopePort(),
							                        credConfig.getAuthScopeRealm());
					
					baseProvider.setCredentials(scope, credConfig.getCredentials());
					builder.setDefaultCredentialsProvider(baseProvider);
					return builder;
			    }
			};
		}
		return factory;
	}
	
	private JestClientFactory setupJestClientFactory( ){
		if(jestClientFactory == null){
			jestClientFactory = setupDefaultClientFactory();
		}
		return jestClientFactory;
	}
	
	private JestHttpClient buildJestClient(){
		HttpClientConfig httpClientConfig = setupHttpClientParameters();
		JestClientFactory factory = setupJestClientFactory( );
		factory.setHttpClientConfig(httpClientConfig);
		JestHttpClient client = (JestHttpClient)factory.getObject();
		return client;
	}
	
	@Override
	public void start() throws LogSubmissionException{
		validateConnectionParameters();
		jestClient = buildJestClient();
        started = true;
	}

	@Override
    public boolean isStarted(){
	    return started;
    }

	@Override
	public void stop() throws LogSubmissionException{
	    started = false;
		long start = System.currentTimeMillis();
		while(activeAsyncRequests.get() > 0 &&
				System.currentTimeMillis() - start < maxAsyncCompletionTimeForShutdownMillis){
			try{
				Thread.sleep(100);
			}catch(InterruptedException iex){
				
			}
		}
		jestClient.shutdownClient();
	}



}

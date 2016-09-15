package com.polydeucesys.eslogging.core.jest.ex.kerberos;

import io.searchbox.client.JestClientFactory;

import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

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
 * An example of a client factory which returns Kerberos enabled Jest clients for connection to
 * Elasticsearch.
 */
public class KerberosJestClientFactory extends JestClientFactory { 
	private KerberosHttpClientCredentialConfig clientCredentialConfig;
	
	
	public KerberosHttpClientCredentialConfig getClientCredentialConfig() {
		return clientCredentialConfig;
	}


	public void setClientCredentialConfig(KerberosHttpClientCredentialConfig clientCredentialConfig) {
		this.clientCredentialConfig = clientCredentialConfig;
	}


	@Override
    protected HttpClientBuilder configureHttpClient(HttpClientBuilder builder) {
		KerberosHttpClientCredentialConfig credConfig = getClientCredentialConfig();
		CredentialsProvider baseProvider = new BasicCredentialsProvider();
		AuthScope scope = new AuthScope(credConfig.getAuthScopeHost(), 
				                        credConfig.getAuthScopePort(),
				                        credConfig.getAuthScopeRealm());
		Lookup<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create()
	            .register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory(credConfig.isStripPortForSpnLookup()))
	            .build();
		baseProvider.setCredentials(scope, credConfig.getCredentials());
		builder.setDefaultCredentialsProvider(baseProvider)
		       .setDefaultAuthSchemeRegistry(authSchemeRegistry);
		return builder;
    }
}

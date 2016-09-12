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

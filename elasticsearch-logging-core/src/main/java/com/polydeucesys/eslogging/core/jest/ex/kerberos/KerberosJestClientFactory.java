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

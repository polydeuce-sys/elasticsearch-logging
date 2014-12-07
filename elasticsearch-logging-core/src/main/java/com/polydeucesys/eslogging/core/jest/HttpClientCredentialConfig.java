package com.polydeucesys.eslogging.core.jest;

import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.Credentials;

import com.polydeucesys.eslogging.core.CredentialConfig;

public interface HttpClientCredentialConfig extends CredentialConfig{
	// Bean properties
	String getAuthScopeHost();
	int    getAuthScopePort();
	String getAuthScopeRealm();
	// Likely to be fixed by implementation
	String getAuthPolicy();
	AuthSchemeProvider getAuthSchemeProvider();
	Credentials getCredentials();
}

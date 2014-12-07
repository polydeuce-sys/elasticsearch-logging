package com.polydeucesys.eslogging.core.jest.ex.kerberos;

import java.security.Principal;

import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.Credentials;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.impl.auth.SPNegoSchemeFactory;

import com.polydeucesys.eslogging.core.jest.HttpClientCredentialConfig;

public class KerberosHttpClientCredentialConfig implements
		HttpClientCredentialConfig {
	
	private String host = null;
	private int port = -1;
	private String realm = null;
	private boolean stripPortForSpnLookup;
	
	public boolean isStripPortForSpnLookup() {
		return stripPortForSpnLookup;
	}

	public void setStripPortForSpnLookup(boolean stripPortForSpnLookup) {
		this.stripPortForSpnLookup = stripPortForSpnLookup;
	}

	@Override
	public String getAuthPolicy() {
		return AuthSchemes.SPNEGO;
	}

	@Override
	public AuthSchemeProvider getAuthSchemeProvider() {
		return new SPNegoSchemeFactory();
	}
	
	@Override
	public Credentials getCredentials() {
		return new Credentials(){

			@Override
			public Principal getUserPrincipal() {
				return null;
			}

			@Override
			public String getPassword() {
				return null;
			}
			
		};
	}

	@Override
	public String username() {
		return null;
	}

	@Override
	public String password() {
		return null;
	}
	
	public void setAuthScopeHost(String host) {
		this.host = host;
	}

	@Override
	public String getAuthScopeHost() {
		return host;
	}

	public void setAuthScopePort(int port) {
		this.port = port;
	}

	@Override
	public int getAuthScopePort() {
		return port;
	}
	
	public void setAuthScopeRealm(String realm) {
		this.realm = realm;
	}

	@Override
	public String getAuthScopeRealm() {
		return realm;
	}
}

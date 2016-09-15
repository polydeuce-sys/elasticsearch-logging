package com.polydeucesys.eslogging.core.jest.ex.kerberos;

import java.security.Principal;

import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.Credentials;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.impl.auth.SPNegoSchemeFactory;

import com.polydeucesys.eslogging.core.jest.HttpClientCredentialConfig;

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
 * An implementation of the {@link HttpClientCredentialConfig} supporting Kerberos authentication
 * via SPNEGO.
 */
public class KerberosHttpClientCredentialConfig implements
		HttpClientCredentialConfig {
	
	private String host = null;
	private int port = -1;
	private String realm = null;
	private boolean stripPortForSpnLookup;

    /**
     * Boolean indicating if the port number should be incuded in the SPN.
     * @return true if port is removed from SPN
     */
	public boolean isStripPortForSpnLookup() {
		return stripPortForSpnLookup;
	}

    /**
     * Set flag indicating weather port should be included or stripped in SPN
     * @param stripPortForSpnLookup
     */
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

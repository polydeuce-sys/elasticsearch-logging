package com.polydeucesys.eslogging.core.jest.ex.kerberos;

import java.security.Principal;

import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.Credentials;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.impl.auth.SPNegoSchemeFactory;

import com.polydeucesys.eslogging.core.jest.HttpClientCredentialConfig;

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

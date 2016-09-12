package com.polydeucesys.eslogging.core.jest;

import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.Credentials;

import com.polydeucesys.eslogging.core.CredentialConfig;

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
 * An extension to the {@link CredentialConfig} interface for use with HTTP connections.
 */
public interface HttpClientCredentialConfig extends CredentialConfig{
	// Bean properties

    /**
     * Gets the hostname for the Authroization Scope (OAuth or Kerberos)
     * @return hostname
     */
	String getAuthScopeHost();

    /**
     * Port for Authorization Scope
     * @return port
     */
	int    getAuthScopePort();

    /**
     * Return the Realm for the Authorization Scope
     * @return
     */
	String getAuthScopeRealm();
	// Likely to be fixed by implementation
	String getAuthPolicy();
	AuthSchemeProvider getAuthSchemeProvider();
	Credentials getCredentials();
}

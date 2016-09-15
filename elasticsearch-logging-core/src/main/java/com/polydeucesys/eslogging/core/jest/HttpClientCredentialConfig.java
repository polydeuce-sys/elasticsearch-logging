package com.polydeucesys.eslogging.core.jest;

import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.Credentials;

import com.polydeucesys.eslogging.core.CredentialConfig;

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

package com.polydeucesys.eslogging.core;
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
 * This interface defines an object which can be used to supply credential information for a connection.
 * An implementation could for example support encryption, allowing the logging configuration file
 * to provide a Base64 + encrypted version of the username and password information rather than
 * maintaining secure information in a text file.
 * @author Kevin McLellan
 * @version 1.0
 *
 */
public interface CredentialConfig {
	/**
	 * Returns a string representing an identity credential
	 * @return {@code String} identifier
	 */
	String username();
	/**
	 * Returns a string representing a authentication credential
	 * @return {@code String} authentication token
	 */
	String password();
}

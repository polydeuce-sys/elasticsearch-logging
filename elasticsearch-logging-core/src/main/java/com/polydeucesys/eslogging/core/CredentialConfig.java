package com.polydeucesys.eslogging.core;

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

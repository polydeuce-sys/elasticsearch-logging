package com.polydeucesys.eslogging.core;

/**
 * Defined the constants used in the Appender code.
 * @author Kevin McLellan
 * @version 1.0
 *
 */
public class Constants {
	
	public static final String CONFIGURATION_ERROR_FORMAT = "Property %s in class %s is not configured correctly";
	public static final String SERIALIZE_NULL_ERROR_FORMAT = "%s cannot serialize null log entry";
	
	public static final String ELASTICSEARCH_INDEX_DATE_STAMP_FORMAT = "yyyy.MM.dd";

	private Constants(){}
}

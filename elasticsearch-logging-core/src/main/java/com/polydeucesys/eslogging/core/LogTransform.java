package com.polydeucesys.eslogging.core;

/**
 * The LogTransform interface defines an object which is used to enrich or otehrwise transform the 
 * incoming log object prior to serialization and submission. An example may add properties to the 
 * object indicating the hostname and logging application when the appender is writing to an 
 * ElasticSearch index shared by many other processes/hosts.
 * @author kmac
 *
 */
public interface LogTransform<I,O> {
	O transform(I log);
}

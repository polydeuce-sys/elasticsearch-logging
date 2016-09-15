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
 * Defines a communication mechanism between the logging framework and 
 * the storage system, whereby documents of type {@code D} can be 
 * submitted and responses of type {@code R} are returned.
 * @author Kevin McLellan
 * @version 1.0
 *
 * @param <D> 
 * 			The type of the documents being submitted
 * @param <R>
 * 			The type of the successful response from the storage
 */
public interface Connection<D, R> extends LifeCycle{
	void setConnectionString(final String connectionString);
	String getConnectionString();
	R submit(final D document) throws LogSubmissionException;
	void submitAsync( final D document, AsyncSubmitCallback<R> callback);
}

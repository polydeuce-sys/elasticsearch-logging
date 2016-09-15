package com.polydeucesys.eslogging.core.jest;

import io.searchbox.core.Index;

import com.polydeucesys.eslogging.core.Constants;
import com.polydeucesys.eslogging.core.LogSerializer;
import com.polydeucesys.eslogging.core.LogSubmissionException;

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
 * An abstract implementation of the {@link LogSerializer} interface which provides some basic validation
 * of the configuration. Subclasses implementing doValidate should throw an exception
 * in the event of validation failure. Subclasses have access to the {@link LogMapper}
 * to determine the appropriate index and doc type for a given log message. For example
 * logging error messsages to a separate index.
 * @param <L> The type of the log event (possibly transformed) in the logging framework
 */
public abstract class AbstractMappedIndexSerializer<L> implements LogSerializer<L, Index>{
	private LogMapper<L> indexMapper;
	private boolean hasValidated = false;

    /**
     * Set the LogMapper used in this serializer. Note this must be set before the
     * serializer can be used, or validation will throw an exception.
     * @param indexMapper  Concrete implementation of the LogMapper interface
     */
	public void setIndexMapper(final LogMapper<L> indexMapper){
		this.indexMapper = indexMapper;
	}

    /**
     * Gets the current LogMapper
     * @return the LogMapper instance for this serializer
     */
	public LogMapper<L> getIndexMapper(){
		return indexMapper;
	}

	protected abstract Index internalSerializeLog(final L log) throws LogSubmissionException;

	protected abstract void doValidate() throws LogSubmissionException;
	
	private void validate() throws LogSubmissionException{
		if(indexMapper == null){
			throw new LogSubmissionException(String.format(
					Constants.CONFIGURATION_ERROR_FORMAT, "indexMapper", this.getClass().getName()));
		}
		doValidate();
		hasValidated = true;
	}
	
	@Override
	public Index serializeLog(final L log) throws LogSubmissionException {
		return internalSerializeLog(log);
	}

	public void start() throws LogSubmissionException{
        validate();
    }

    public void stop() throws LogSubmissionException{

    }

    public boolean isStarted(){
        return hasValidated;
    }


}

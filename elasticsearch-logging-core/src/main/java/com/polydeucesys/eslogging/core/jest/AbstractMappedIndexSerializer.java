package com.polydeucesys.eslogging.core.jest;

import io.searchbox.core.Index;

import com.polydeucesys.eslogging.core.Constants;
import com.polydeucesys.eslogging.core.LogSerializer;
import com.polydeucesys.eslogging.core.LogSubmissionException;

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

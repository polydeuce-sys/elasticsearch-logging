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
 * Provides a wrapper class around a Json String serializer to create a suitable object for 
 * submission via the Jest client. The option is provided to allow for custom serialisation
 * implementations which may be desirable for high throughput applications where reflections
 * based serialisations may not be fast enough.
 * @author Kevin McLellan
 *
 * @param <L> - Log entry type
 */
public final class JestIndexStringSerializerWrapper<L> extends AbstractMappedIndexSerializer<L>{
	
	private LogSerializer<L, String> wrappedJsonStringSerializer;

	public void setWrappedJsonStringSerializer(final LogSerializer<L, String> wrappedJsonStringSerializer){
		this.wrappedJsonStringSerializer = wrappedJsonStringSerializer;
	}
	
	public LogSerializer<L, String> getWrappedJsonStringSerializer(){
		return wrappedJsonStringSerializer;
	}


	@Override
	protected void doValidate() throws LogSubmissionException{
		if(wrappedJsonStringSerializer == null){
			throw new LogSubmissionException(String.format(
					Constants.CONFIGURATION_ERROR_FORMAT, "wrappedJsonStringSerializer", this.getClass().getName()));
		}
	}

	@Override
	public Index internalSerializeLog(final L log) throws LogSubmissionException {
		Index.Builder ib = new Index.Builder(wrappedJsonStringSerializer.serializeLog(log))
		                   .index(getIndexMapper().getIndexNameForLog(log))
		                   .type(getIndexMapper().getDocumentTypeForLog(log));
		return ib.build();
	}

	@Override
	public void start() throws LogSubmissionException
	{
        super.start();
		wrappedJsonStringSerializer.start();
	}

	@Override
	public void stop() throws LogSubmissionException
	{
		wrappedJsonStringSerializer.stop();
	}
}

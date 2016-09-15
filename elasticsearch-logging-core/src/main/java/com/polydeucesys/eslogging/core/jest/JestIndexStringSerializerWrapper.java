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

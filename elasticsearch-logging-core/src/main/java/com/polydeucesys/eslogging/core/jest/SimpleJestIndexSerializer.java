package com.polydeucesys.eslogging.core.jest;

import io.searchbox.core.Index;

import com.polydeucesys.eslogging.core.Constants;
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
 * A serializer which uses the Jest support for POJOs to serialize the log object to a 
 * Jest Index object for publication
 * 
 * @author Kevin McLellan
 * @param <L>
 *
 */
public final class SimpleJestIndexSerializer<L> extends AbstractMappedIndexSerializer<L>{

	@Override
	protected void doValidate() {
		// NOOP - No additional config is required below the level of the
		// abstract superclass
	}

	@Override
	public Index internalSerializeLog(final L log) throws LogSubmissionException {
		if(log == null){
			throw new LogSubmissionException(String.format(Constants.SERIALIZE_NULL_ERROR_FORMAT,
					this.getClass().getName()));
		}
		final Index.Builder lb = new Index.Builder(log)
											.index(getIndexMapper().getIndexNameForLog(log))
											.type(getIndexMapper().getDocumentTypeForLog(log));
		return lb.build();
	}
}

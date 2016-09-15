package com.polydeucesys.eslogging.core.gson;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
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
 * Provides a simple Gson based serializer. Note that Gson is documented as being ThreadSafe so a single
 * Gson instance in used to concurrently serialize all objects.
 * @author Kevin McLellan
 *
 * @param <L>
 */
public final class SimpleGsonLogSerializer<L> implements LogSerializer<L, String> {
	private Gson gson;

	private final GsonBuilder builder = new GsonBuilder();
	

	public <T> void setTypeAdapter(Type type, TypeAdapter<T> logAdapter) {
		builder.registerTypeAdapter(type, logAdapter);
	}

	@Override
	public String serializeLog(final L log) throws LogSubmissionException{
		if(log == null){
			throw new LogSubmissionException(String.format(Constants.SERIALIZE_NULL_ERROR_FORMAT,
					this.getClass().getName()));
		}
		return gson.toJson(log);
	}

    @Override
    public void start() throws LogSubmissionException {
        gson = builder.create();
    }

    @Override
    public void stop() throws LogSubmissionException {

    }

    @Override
    public boolean isStarted() {
        return !(gson == null);
    }
}

package com.polydeucesys.eslogging.core.gson;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
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

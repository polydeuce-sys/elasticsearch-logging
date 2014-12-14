package com.polydeucesys.eslogging.core.gson;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.polydeucesys.eslogging.core.Constants;
import com.polydeucesys.eslogging.core.LogSerializer;
import com.polydeucesys.eslogging.core.LogSubmissionException;

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
		if(gson == null){
			gson = builder.create();
		}
		return gson.toJson(log);
	}

}

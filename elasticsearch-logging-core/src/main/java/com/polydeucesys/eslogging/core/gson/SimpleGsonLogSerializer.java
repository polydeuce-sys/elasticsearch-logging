package com.polydeucesys.eslogging.core.gson;

import com.google.gson.Gson;
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
	private final Gson gson = new Gson();

	@Override
	public String serializeLog(final L log) throws LogSubmissionException{
		if(log == null){
			throw new LogSubmissionException(String.format(Constants.SERIALIZE_NULL_ERROR_FORMAT,
					this.getClass().getName()));
		}
		return gson.toJson(log);
	}

}

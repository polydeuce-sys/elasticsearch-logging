package com.polydeucesys.eslogging.core.jest;

import io.searchbox.core.Index;

import com.polydeucesys.eslogging.core.Constants;
import com.polydeucesys.eslogging.core.LogSerializer;
import com.polydeucesys.eslogging.core.LogSubmissionException;

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
	private boolean validated = false;
	
	public void setWrappedJsonStringSerializer(final LogSerializer<L, String> wrappedJsonStringSerializer){
		this.wrappedJsonStringSerializer = wrappedJsonStringSerializer;
	}
	
	public LogSerializer<L, String> getWrappedJsonStringSerializer(){
		return wrappedJsonStringSerializer;
	}

	@Override 
	public boolean doHasValidated(){
		return validated;
	}
	
	@Override
	protected void doValidate(){
		if(wrappedJsonStringSerializer == null){
			throw new IllegalArgumentException(String.format(
					Constants.CONFIGURATION_ERROR_FORMAT, "wrappedJsonStringSerializer", this.getClass().getName()));
		}
		validated = true;
	}

	@Override
	public Index internalSerializeLog(final L log) throws LogSubmissionException {
		Index.Builder ib = new Index.Builder(wrappedJsonStringSerializer.serializeLog(log))
		                   .index(getIndexMapper().getIndexNameForLog(log))
		                   .type(getIndexMapper().getDocumentTypeForLog(log));
		return ib.build();
	}

}

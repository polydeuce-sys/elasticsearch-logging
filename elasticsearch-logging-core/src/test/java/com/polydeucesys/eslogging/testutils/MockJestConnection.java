package com.polydeucesys.eslogging.testutils;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;

import com.google.gson.Gson;
import com.polydeucesys.eslogging.core.AsyncSubmitCallback;
import com.polydeucesys.eslogging.core.Connection;
import com.polydeucesys.eslogging.core.LogSubmissionException;

/**
 * Used for tests at a level above the actual connection. 
 * i.e. for testing Appenders via a logger or similar.
 * @author kmac
 *
 */
public class MockJestConnection implements Connection<Bulk, JestResult> {
	private boolean started = false;
	private String connectionString = null;
	
	private LogSubmissionException toThrow;
	
	private List<Bulk> submissions = new ArrayList<Bulk>();
	
	private JestResult defaultResult = new JestResult(new Gson());
	
	public MockJestConnection(){
		defaultResult.setSucceeded(true);
		defaultResult.setJsonString("{ \"status\" : \"ok\"}");
	}
	
	public void setToThrow( LogSubmissionException lse){
		toThrow = lse;
	}
	@Override
	public void start() throws LogSubmissionException {
		if(connectionString == null){
			throw new LogSubmissionException("Invalid Config. No Connection String");
		}
		started = true;
	}

	@Override
	public void stop() throws LogSubmissionException {
		started = false;
	}

	@Override
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	@Override
	public String getConnectionString() {
		return connectionString;
	}

	@Override
	public JestResult submit(Bulk document) throws LogSubmissionException {
		submissions.add(document);
		return defaultResult;
	}

	@Override
	public void submitAsync(Bulk document,
			AsyncSubmitCallback<JestResult> callback) {
		submissions.add(document);
		callback.completed(defaultResult);
	}

	public List<Bulk> getSubmissions(){
		return submissions;
	}
}

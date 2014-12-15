package com.polydeucesys.eslogging.core.jest;

import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.polydeucesys.eslogging.core.AsyncSubmitCallback;
import com.polydeucesys.eslogging.core.Connection;
import com.polydeucesys.eslogging.core.LogAppenderErrorHandler;
import com.polydeucesys.eslogging.core.LogSubmissionException;
import com.polydeucesys.eslogging.core.LogSubmissionStrategy;

public class JestLogSubmissionStrategy implements
		LogSubmissionStrategy<Index, Bulk, JestResult> {

	private Bulk.Builder submissionsBuilder = new Bulk.Builder();
	private int submissionCount = 0;
	private final Object submissionLock = new Object();
	private long lastSubmissionTime = System.currentTimeMillis();
	private boolean hasConnected = false;
	private LogAppenderErrorHandler errorHandler = new LogSubmissionStdErrErrorHandler();
	private JestSubmissionCallback callback = new JestSubmissionCallback(errorHandler);
	private final ScheduledExecutorService intervalScheduler = Executors.newSingleThreadScheduledExecutor();
	private int queueDepth;
	private long maxSubmissionInterval;
	private long submissionInterval;
	private Connection<Bulk, JestResult> connection; 
	
	public static class LogSubmissionStdErrErrorHandler implements LogAppenderErrorHandler{
		private static final String ERR_MSG_FORMAT = "Jest Log Submission Failed due to error \"%s\"";
		@Override
		public void error(LogSubmissionException ex) {
			System.err.println(String.format(ERR_MSG_FORMAT, ex.getMessage()));
		}
	}
	
	public static class LogSubmissionNoOpErrorHandler implements LogAppenderErrorHandler{
		@Override
		public void error(LogSubmissionException ex) {
		}
	}
	
	private static class JestSubmissionCallback implements AsyncSubmitCallback<JestResult>{
		
		private final LogAppenderErrorHandler errorHandler;
		
		public JestSubmissionCallback(LogAppenderErrorHandler errorHandler ){
			this.errorHandler = errorHandler;
		}
		
		@Override
		public void completed(JestResult result) {
			// no-op on success
		}

		@Override
		public void error(LogSubmissionException wrappedException) {
			errorHandler.error(wrappedException);
		}
		
	}
	
	private class IntervalRunnable implements Runnable {

		@Override
		public void run() {
			try{
				Bulk toSubmit = null;
				synchronized(submissionLock){
					toSubmit = checkIfSubmitRequired();
				}
				submitIfRequired(toSubmit);
			}catch(RuntimeException ex){
				Thread runner = Thread.currentThread();
				runner.getUncaughtExceptionHandler().uncaughtException(runner, ex);
			}
		}
		
	}
	@Override
	public void setQueueDepth(int queueDepth) {
		this.queueDepth = queueDepth;
	}

	@Override
	public int getQueueDepth() {
		return queueDepth;
	}

	@Override
	public void setMaxSubmissionIntervalMillisec(long maxSubmissionInterval) {
		this.maxSubmissionInterval = maxSubmissionInterval;
		this.submissionInterval = maxSubmissionInterval / 2;
	}

	@Override
	public long getMaxSubmissionIntervalMillisec() {
		return maxSubmissionInterval;
	}
	
	@Override
	public void setErrorHandler(LogAppenderErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
		this.callback = new JestSubmissionCallback(errorHandler);
	}

	@Override
	public LogAppenderErrorHandler getErrorHandler() {
		return errorHandler;
	}

	@Override
	public void setConnection(Connection<Bulk, JestResult> connection) {
		this.connection = connection;
	}
	
	@Override
	public Connection<Bulk, JestResult> getConnection() {
		return connection;
	}

	private Bulk checkIfSubmitRequired(){
		Bulk toSubmit = null;

		long now = System.currentTimeMillis();
		if((now - lastSubmissionTime > submissionInterval && submissionCount > 0) ||
				submissionCount >= queueDepth){
			toSubmit = submissionsBuilder.build();
			submissionsBuilder = new Bulk.Builder();
			lastSubmissionTime = System.currentTimeMillis();
			submissionCount = 0;
		}
		return toSubmit;
	}
	
	private void submitIfRequired( Bulk toSubmit){
		if(toSubmit != null){
			connection.submitAsync(toSubmit, 
					callback);
		}
	}
	
	@Override
	public void submit(Index log) throws LogSubmissionException {
		Bulk toSubmit = null;
		synchronized(submissionLock){
			if(!hasConnected){
				// if this was more heavyweight we might make it async
				connection.connect();
				intervalScheduler.scheduleAtFixedRate(new IntervalRunnable(), 
														submissionInterval, 
														submissionInterval, 
														TimeUnit.MILLISECONDS);
				hasConnected = true;
			}
			// add to Bulk. if the Bulk now has the full set of docs, 
			// then create a new Bulk, and assign the current on to the local
			// var
			submissionsBuilder.addAction(log);
			submissionCount++;
			toSubmit = checkIfSubmitRequired();
		}
		submitIfRequired(toSubmit);
	}

	@Override
	public void close() throws LogSubmissionException {
		synchronized(submissionLock){
			if(submissionCount > 0){
				Bulk toSubmit = submissionsBuilder.build();
				connection.submit(toSubmit);
			}
			connection.close();
		}
	}

}

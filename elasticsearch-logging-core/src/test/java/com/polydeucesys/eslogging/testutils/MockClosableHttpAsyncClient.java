package com.polydeucesys.eslogging.testutils;

import io.searchbox.client.JestResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpRequest;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class MockClosableHttpAsyncClient extends CloseableHttpAsyncClient{
	
	public interface TestCallback<T>{
		void callbackResult( T result);
		void callbackError(Exception error);
	}
	
	private static class MockHttpFuture<T> implements Future<T>{
		private  final T nextResponse;
	    private  final Exception nextResponseException;
	    private  final FutureCallback<T> callback;
	    private final  TestCallback<T> testCallback;
		public MockHttpFuture(T nextResponse, Exception nextResponseException, FutureCallback<T> callback, 
				TestCallback<T> testCallback){
			this.nextResponse = nextResponse;
			this.nextResponseException = nextResponseException;
			this.callback = callback;
			this.testCallback = testCallback;
		}

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return true;
		}

		@Override
		public T get()
				throws InterruptedException, ExecutionException {
			if(nextResponseException != null){
				if(nextResponseException instanceof InterruptedException){
					throw (InterruptedException)nextResponseException;
				}else{
					testCallback.callbackError(nextResponseException);
					callback.failed(nextResponseException);
				}
			}else{
				testCallback.callbackResult(nextResponse);
				callback.completed(nextResponse);
			}
			return nextResponse;
		}

		@Override
		public T get(long timeout,
				TimeUnit unit) throws InterruptedException, ExecutionException,
				TimeoutException {
			if(nextResponseException != null){
				if(nextResponseException instanceof InterruptedException){
					throw (InterruptedException)nextResponseException;
				}else{
					callback.failed(nextResponseException);
				}
			}
			callback.completed(nextResponse);
			return nextResponse;
		}

		@Override
		public boolean isCancelled() {
			return false;
		}

		@Override
		public boolean isDone() {
			return true;
		}
		
	}
	// so we can inspect it
    private  HttpRequest savedRequest;
    private  CloseableHttpResponse defaultResponse = MockCloseableHttpClient.responseWithBody(200, "{}");

    private  CloseableHttpResponse nextResponse;
    private  Exception nextResponseException;
    private  TestCallback<JestResult> testCallback;
    
    
    public MockClosableHttpAsyncClient(){
    	testCallback = new TestCallback(){

			@Override
			public void callbackResult(Object result) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void callbackError(Exception error) {
				// TODO Auto-generated method stub
				
			}
    		
    	};
    }

    public HttpRequest getLastRequest(){
    	return savedRequest;
    }
    
    public String getLastRequestContent(){
    	String retval = "";
    	if(savedRequest instanceof HttpEntityEnclosingRequestBase){
    		HttpEntityEnclosingRequestBase lastPost = (HttpEntityEnclosingRequestBase) savedRequest;
    		try {
    			retval =  EntityUtils.toString(lastPost.getEntity(), StandardCharsets.UTF_8);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}else{
    		HttpRequestBase rb = (HttpRequestBase) savedRequest;
    		retval =  rb.getURI().toASCIIString();
    	}
    	return retval;
    }
    
    public void setNextResponse(CloseableHttpResponse nextResponse){
    	this.nextResponse = nextResponse;
    }
    
    public void setNextResponseException(Exception nextResponseException){
    	this.nextResponseException = nextResponseException;
    }
    
    public void setTestCallback( TestCallback testCallback){
    	this.testCallback = testCallback;
    }

	@Override
	public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer,
			HttpAsyncResponseConsumer<T> responseConsumer, HttpContext context,
			FutureCallback<T> callback) {
		try{
			MockHttpFuture future =  new MockHttpFuture(nextResponse == null? defaultResponse:nextResponse, nextResponseException, callback, testCallback);
			try {
				future.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return future;
		}finally{
			nextResponse = null;
			nextResponseException = null;
		}
	}

	@Override
	public void close() throws IOException {
		
	}

	@Override
	public boolean isRunning() {
		return true;
	}

	@Override
	public void start() {
		
	}

}

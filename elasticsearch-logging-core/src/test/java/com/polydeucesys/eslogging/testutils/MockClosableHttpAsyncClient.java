package com.polydeucesys.eslogging.testutils;

import io.searchbox.client.JestResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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
public class MockClosableHttpAsyncClient extends CloseableHttpAsyncClient{
	
	public interface TestCallback<T>{
		void callbackResult( T result);
		void callbackError(Exception error);
	}
	
	private static class MockHttpFuture<T> implements Future<T>{
		private  final T nextResponse;
	    private  final Exception nextResponseException;
	    private final boolean    throwExceptionInsteadOfCallbackError;
	    private  final FutureCallback<T> callback;
	    private final  TestCallback<T> testCallback;
	    private final long requestTime;
	    
	    
		public MockHttpFuture(T nextResponse, Exception nextResponseException, 
				boolean    throwExceptionInsteadOfCallbackError,
				FutureCallback<T> callback, 
				TestCallback<T> testCallback, long requestTime){
			this.nextResponse = nextResponse;
			this.nextResponseException = nextResponseException;
			this.throwExceptionInsteadOfCallbackError = throwExceptionInsteadOfCallbackError;
			this.callback = callback;
			this.testCallback = testCallback;
			this.requestTime = requestTime;
		}

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return true;
		}

		@Override
		public T get()
				throws InterruptedException, ExecutionException {
			if(requestTime > 0){
				try {
					Thread.sleep(requestTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(nextResponseException != null){
				if(nextResponseException instanceof InterruptedException){
					throw (InterruptedException)nextResponseException;
				}else{
					testCallback.callbackError(nextResponseException);
					callback.failed(nextResponseException);
				}
			}else{
                try {
                    callback.completed(nextResponse);
                    testCallback.callbackResult(nextResponse);
                }catch (IllegalStateException badRead){
                    testCallback.callbackError(badRead);
                    callback.failed(badRead);
                }
			}
			return nextResponse;
		}

		@Override
		public T get(long timeout,
				TimeUnit unit) throws InterruptedException, ExecutionException,
				TimeoutException {
			if(requestTime > 0){
				try {
					Thread.sleep(requestTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(nextResponseException != null){
				if(nextResponseException instanceof InterruptedException){
					throw (InterruptedException)nextResponseException;
				}else{
					if(throwExceptionInsteadOfCallbackError){
						throw new ExecutionException(nextResponseException);
					}else{
						callback.failed(nextResponseException);
					}
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
    private boolean throwExceptionInsteadOfCallbackError = false;
    private  TestCallback<JestResult> testCallback;
    private  long requestTime = 0L;
    private  Executor fakesecutor = Executors.newSingleThreadExecutor(); 
    
    
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
    
    public void setThrowExceptionInsteadOfCallbackError(boolean throwExceptionInsteadOfCallbackError){
    	this.throwExceptionInsteadOfCallbackError = throwExceptionInsteadOfCallbackError;
    }
    
    public long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}

	public void setTestCallback( TestCallback testCallback){
    	this.testCallback = testCallback;
    }

	@Override
	public <T> Future<T> execute(final HttpAsyncRequestProducer requestProducer,
			final HttpAsyncResponseConsumer<T> responseConsumer, final HttpContext context,
			final FutureCallback<T> callback) {
		try{
			@SuppressWarnings({ "rawtypes", "unchecked" })
			final MockHttpFuture future =  new MockHttpFuture(nextResponse == null? defaultResponse:nextResponse, 
					                                    nextResponseException, throwExceptionInsteadOfCallbackError,
					                                    callback, 
					                                    testCallback, requestTime);
			fakesecutor.execute(new Runnable(){

				@Override
				public void run() {
					try {
						future.get();
					} catch (InterruptedException e) {
						callback.failed(e);
					} catch (ExecutionException e) {
						callback.failed(e);
					}
				}
				
			});
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

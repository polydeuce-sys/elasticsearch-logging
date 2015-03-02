package com.polydeucesys.eslogging.testutils;

import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.http.JestHttpClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * Borrowed/adapted from the Jest Test classes to allow checking at the top level what is getting 
 * pushed down to the server
 * @author Dogukan Sonmez, extracted by Kevin McLellan
 *
 */
@SuppressWarnings("deprecation")
public class MockCloseableHttpClient extends CloseableHttpClient {
	
	public static JestHttpClient testableJestClient(){
		JestClientFactory factory = new JestClientFactory();
        HttpClientConfig httpClientConfig = new HttpClientConfig.Builder("http://localhost:9200").build();
        factory.setHttpClientConfig(httpClientConfig);
        JestHttpClient clientWithMockedHttpClient = (JestHttpClient) factory
                .getObject();

        clientWithMockedHttpClient.setHttpClient(new MockCloseableHttpClient());
        return clientWithMockedHttpClient;
	}
	
	private static class BasicCloseableHttpResponse extends BasicHttpResponse implements CloseableHttpResponse {

        BasicCloseableHttpResponse(StatusLine sl) {
            super(sl);
        }

        @Override
        public void close() throws IOException {
            EntityUtils.consume(getEntity());
        }
    }
	
	public static CloseableHttpResponse responseWithBody( final int statusCode, final String body ){
		CloseableHttpResponse resp = new BasicCloseableHttpResponse(new StatusLine() {
            @Override
            public int getStatusCode() {
                // TODO Auto-generated method stub
                return statusCode;
            }

            @Override
            public ProtocolVersion getProtocolVersion() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getReasonPhrase() {
                // TODO Auto-generated method stub
                return null;
            }
        });
        resp.setEntity(new AbstractHttpEntity() {

            @Override
            public boolean isRepeatable() {
                // TODO Auto-generated method stub
                return true;
            }

            @Override
            public long getContentLength() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public InputStream getContent() throws IOException,
                    IllegalStateException {
                return new ByteArrayInputStream(body.getBytes());
            }

            @Override
            public void writeTo(OutputStream outstream)
                    throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean isStreaming() {
                // TODO Auto-generated method stub
                return false;
            }


        });
        return resp;
	}
	
	// so we can inspect it
    private  HttpRequest savedRequest;
    private  CloseableHttpResponse defaultResponse = responseWithBody(200, "{}");
    private  CloseableHttpResponse nextResponse;
    private  IOException nextResponseException;
    private long requestTime = 0L;
    
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
    
    public void setNextResponseException(IOException nextResponseException){
    	this.nextResponseException = nextResponseException;
    }
    
	public long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}

	@Override
	public ClientConnectionManager getConnectionManager() {
		return null;
	}

	@Override
	public HttpParams getParams() {
		return null;
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	protected CloseableHttpResponse doExecute(HttpHost arg0, HttpRequest arg1,
			HttpContext arg2) throws IOException, ClientProtocolException {
		savedRequest = arg1;
		IOException toThrow = nextResponseException;
		try{
			if(toThrow != null){
				throw toThrow;
			}
			
			return nextResponse == null? defaultResponse:nextResponse;
		}finally{
			nextResponseException = null;
			nextResponse = null;
		}
	}

}

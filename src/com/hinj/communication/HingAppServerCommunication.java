package com.hinj.communication;

import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import android.util.Log;

import com.hinj.app.utils.AppUtils;
import com.loopj.android.http.MySSLSocketFactory;

public class HingAppServerCommunication
{
	public static JSONObject callJson(String url, JSONObject parameters) throws Exception
	{
		Log.d("Url",url);
		Log.d("Request",parameters.toString());
				
		KeyStore trustStore = KeyStore.getInstance(KeyStore .getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
        
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
	   
        HttpClient client = new DefaultHttpClient(ccm, params);
        
		InputStream in = null;
		
		HttpPost post = new HttpPost(url);
		
		String myUserAgent = "Mozilla/5.0 (Windows NT 6.1; rv:26.0) Gecko/20100101 Firefox/26.0";
		post.setHeader("User-Agent", myUserAgent);
        
		StringEntity se = null;
		HttpResponse response = null;
		JSONObject json = null;
				
		se = new StringEntity(parameters.toString(),"UTF-8");
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
		post.setEntity(se);
		response = client.execute(post);
		
		if (response != null) 
		{
			in = response.getEntity().getContent();
		}

		String data = AppUtils.inputSteamToString(in);
					
		Log.d("Response", data);
		
		json = new JSONObject(data);	

		return json;
	}	
	
	public static JSONObject post(String url, List<NameValuePair> nameValuePairs) throws Exception
	{
		Log.d("Request", nameValuePairs.toString());
		
	    HttpClient httpClient = getNewHttpClient();
	    InputStream in = null;
	    HttpContext localContext = new BasicHttpContext();
	    //localContext.setAttribute(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; rv:26.0) Gecko/20100101 Firefox/26.0");
	    HttpPost httpPost = new HttpPost(url);
	 	    
		String myUserAgent ="Mozilla/5.0 (Windows NT 6.1; rv:26.0) Gecko/20100101 Firefox/26.0";
		httpPost.setHeader("User-Agent", myUserAgent);
		
	    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	    for(int index=0; index < nameValuePairs.size(); index++) 
	    {
	    	if(nameValuePairs.get(index).getName().equalsIgnoreCase("image")) 
	    	{
	    		entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File(nameValuePairs.get(index).getValue())));
	    	}
	    	else 
	    	{
	    		entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
	    	}
	    }

	    httpPost.setEntity(entity);

	    HttpResponse response = httpClient.execute(httpPost, localContext);
	    if (response != null) 
	    {
	    	in = response.getEntity().getContent();
	    }

	    String data = AppUtils.inputSteamToString(in);
				
	    Log.d("Response", data);
						
	    return new JSONObject(data);	 	   
	}	
	
	public static JSONObject post2(String url, List<NameValuePair> nameValuePairs) throws Exception
	{
		Log.d("Request", nameValuePairs.toString());
		
	    HttpClient httpClient = new DefaultHttpClient();
	    InputStream in = null;
	    HttpContext localContext = new BasicHttpContext();
	    //localContext.setAttribute(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; rv:26.0) Gecko/20100101 Firefox/26.0");
	    HttpPost httpPost = new HttpPost(url);
	 	    
		String myUserAgent ="Mozilla/5.0 (Windows NT 6.1; rv:26.0) Gecko/20100101 Firefox/26.0";
		httpPost.setHeader("User-Agent", myUserAgent);
		
	    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	    for(int index=0; index < nameValuePairs.size(); index++) 
	    {
	    	if(nameValuePairs.get(index).getName().equalsIgnoreCase("image")) 
	    	{
	    		entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File(nameValuePairs.get(index).getValue())));
	    	}
	    	else 
	    	{
	    		entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
	    	}
	    }

	    httpPost.setEntity(entity);

	    HttpResponse response = httpClient.execute(httpPost, localContext);
	    if (response != null) 
	    {
	    	in = response.getEntity().getContent();
	    }

	    String data = AppUtils.inputSteamToString(in);
				
	    Log.d("Response", data);
						
	    return new JSONObject(data);	 	   
	}	
	
	
	public static HttpClient getNewHttpClient() {
	    try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));

	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        return new DefaultHttpClient(ccm, params);
	    } catch (Exception e) {
	        return new DefaultHttpClient();
	    }
	}
}




















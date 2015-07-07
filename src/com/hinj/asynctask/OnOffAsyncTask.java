package com.hinj.asynctask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;

import com.hinj.app.utils.ConnectUrl;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.SpyCallSharedPrefrence;
import com.hinj.secure.smsgps.DeviceInterface;


public class OnOffAsyncTask extends AsyncTask<Void, Void, Void>{

	private static final String TAG = "OnOffAsyncTask"; 
	private Context ctx;
	private String cellPoliceParentControlCompleteJson=null;
	private DeviceInterface onComplete;


	BroadcastReceiver broadcastReceiver;
	
	String currentTimeString=null;
	CountDownTimer countDown;

	String flagValue="beforeBlock";

	public OnOffAsyncTask(Context ctx,DeviceInterface onComplete) {
		// TODO Auto-generated constructor stub

		this.ctx=ctx;
		this.onComplete=onComplete;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try{

			cellPoliceParentControlCompleteJson=getDeviceLogOnOffStatus();
			Log.i("cellPoliceParentControlCompleteJson*****************",cellPoliceParentControlCompleteJson);
			if(cellPoliceParentControlCompleteJson!=null && (!cellPoliceParentControlCompleteJson.equals("")))
			{
				SpyCallSharedPrefrence.saveLoggingStatus(ctx, cellPoliceParentControlCompleteJson);
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private String getDeviceLogOnOffStatus()
	{
		try {
			JSONObject parameters = new JSONObject();
			parameters.put("user_id",SpyCallSharedPrefrence.getsaveUSER_ID(ctx));
			return RequestPost.callJson(ConnectUrl.URL_Logging_Status, parameters);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}

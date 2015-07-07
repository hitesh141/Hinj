package com.hinj.secure.smsgps;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.hinj.app.utils.Utility;
import com.hinj.asynctask.OnOffAsyncTask;


public class CallOnOffAnsycTaskService extends Service{
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i("service restart", "service restart");
		try{
			if(Utility.isNetworkAvailable(CallOnOffAnsycTaskService.this)){	
				new OnOffAsyncTask(CallOnOffAnsycTaskService.this,null).execute();									
			}else{
				Log.i("", "No Internet Found");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Announcement about stopping
		//Toast.makeText(this, "Stopping the Demo Service", Toast.LENGTH_SHORT).show();
	}

}

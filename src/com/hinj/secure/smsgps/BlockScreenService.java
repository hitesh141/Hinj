package com.hinj.secure.smsgps;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.hinj.app.utils.SpyCallSharedPrefrence;

public class BlockScreenService extends Service{
	TimerTask task;
	Timer activityTimer;
	Handler handler;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate(){
		Log.e("v", "onCreate");
		handler = new Handler();
	}

	@Override
	public void onDestroy() {
		Log.e("c", "onDestroy");
		activityTimer.cancel();
		SpyCallSharedPrefrence.saveTimeRestrictPhoneBlockedStatus(this,0);
		CallBlockActivity.CallBlockActivityInstance.finish();
	}

	@Override
	public int onStartCommand(Intent intent,int flags,int startId) {
		// TODO Auto-generated method stub

		SpyCallSharedPrefrence.saveTimeRestrictPhoneBlockedStatus(this,1);
		task = new TimerTask() {

			public void run() {
				handler.post(new Runnable() {

					@Override
					public void run() {	
						if(SpyCallSharedPrefrence.getTimeRestrictPhoneBlockedStatus(BlockScreenService.this)==1)
						{
							getCallBlockScreen();
						}
						else
						{
							CallBlockActivity.CallBlockActivityInstance.finish();
						}
					}
				});
			}
		};

		if(activityTimer!=null)
		{
			activityTimer.cancel();
		}
		activityTimer = new Timer();
		activityTimer.scheduleAtFixedRate(task,1,1000);

		return START_STICKY;

	}


	private void getCallBlockScreen() {
		try{
			Intent intent = new Intent(BlockScreenService.this, CallBlockActivity.class);		
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_FROM_BACKGROUND);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			BlockScreenService.this.startActivity(intent);		    
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}

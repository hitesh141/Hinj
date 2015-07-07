package com.hinj.app.activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HingAppConstants;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.PushNotificationUtils;
import com.hinj.parsing.HingAppParsing;

public class MyPushRegister 
{
	private String mDeviceName ="android";
	private String mDeviceToken;
	private String mUserID;
	private Context context;

	public MyPushRegister(Activity currentActivity)
	{
		context			= currentActivity;
		mUserID 		= HinjAppPreferenceUtils.getRaId(currentActivity);
		mDeviceToken 	= GCMRegistrar.getRegistrationId(currentActivity);

		if (mDeviceToken.equals(HingAppConstants.EMPTY_STRING)) {

			GCMRegistrar.register(currentActivity, AppUtils.GCM_SENDERID);

		} else {

			Log.v("", "Already registered:  "+mDeviceToken);
		}

		HinjAppPreferenceUtils.setRegisterDeviceId(currentActivity, mDeviceToken);
		
		//if(!mUserID.equals("nothing"))
		{
			GCMRegistrar.checkDevice(currentActivity);
			GCMRegistrar.checkManifest(currentActivity);

			if(PushNotificationUtils.registrationId.equals(HingAppConstants.EMPTY_STRING)){   


			}else{

				mDeviceToken =PushNotificationUtils.registrationId;
			}

			if (PushNotificationUtils.notificationReceived) {

				onNotification();
			}
		}

		ConnectivityManager connectivityManager = (ConnectivityManager) currentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		connectivityManager = (ConnectivityManager) currentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo isNetworkDown = connectivityManager.getActiveNetworkInfo();

		if(isNetworkDown != null)
		{
			Log.d("isConnectivityAvailable", isNetworkDown.getState().toString());

			if(isNetworkDown.getState() == NetworkInfo.State.CONNECTED)
			{
				//new YourAsyncAutoLoginForPN().execute();
			}
		}
		else
		{
			Log.d("isConnectivityAvailable", "No Connection Available");
		}
	}

	public class YourAsyncAutoLoginForPN extends AsyncTask<String, String,Object[]> {
		//ProgressDialog progressDialog ;
		String response = "";
		String emailStr;
		
		@Override
		public void onPreExecute() {
			//progressDialog = ProgressDialog.show(LoginActivity.this,"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.autoLoginForPN(context,mDeviceName, mDeviceToken,mUserID); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			
			//progressDialog.cancel();
			try
			{
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
				}
				else
				{
					response = (String) result[1];
					//AppUtils.showDialog(context, response);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void onNotification(){

		PushNotificationUtils.notificationReceived = false;

	}
}

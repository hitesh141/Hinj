package com.hinj.app.model;

import java.util.ArrayList;

public class ContedChilDeviceDetailBean {

	String  photoCount = "", musicCount = "", videoCount = "",
			appCount = "", child_ra_id = "", contactCount = "",
			messageCount = "", model = "", child_upload_devide_id = "", child_register_device_id = "";

	public String getChild_upload_devide_id() {
		return child_upload_devide_id;
	}

	public void setChild_upload_devide_id(String child_upload_devide_id) {
		this.child_upload_devide_id = child_upload_devide_id;
	}

	ArrayList<String> login_device_name = new ArrayList<String>();
	
//	public ContedChilDeviceDetailBean(String photos, String music,
//			String videos, String apps, String messages, String contacts,
//			String child_model, String child_ra_id, String child_deviceId) {
//		
//				this.photoCount = photos;
//				this.musicCount = music;
//				this.videoCount = videos;
//				this.appCount = apps;
//				this.child_ra_id = child_ra_id;
//				this.contactCount = contacts;
//				this.messageCount = messages;
//				this.model = child_model;
//				this.child_devide_id = child_deviceId;
//	}

	public String getChild_register_device_id() {
		
		return child_register_device_id;
	}

	public void setChild_register_device_id(String Child_register_device_id) {
		this.child_register_device_id = Child_register_device_id;
	}
	public ContedChilDeviceDetailBean() {
		// TODO Auto-generated constructor stub
	}

	public String getChild_ra_id() {
		return child_ra_id;
	}

	public void setChild_ra_id(String child_ra_id) {
		this.child_ra_id = child_ra_id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(String messageCount) {
		this.messageCount = messageCount;
	}

	public String getPhotoCount() {
		return photoCount;
	}

	public void setPhotoCount(String photoCount) {
		this.photoCount = photoCount;
	}

	public String getMusicCount() {
		return musicCount;
	}

	public void setMusicCount(String musicCount) {
		this.musicCount = musicCount;
	}

	public String getVideoCount() {
		return videoCount;
	}

	public void setVideoCount(String videoCount) {
		this.videoCount = videoCount;
	}

	public String getAppCount() {
		return appCount;
	}

	public void setAppCount(String appCount) {
		this.appCount = appCount;
	}

	public String getContactCount() {
		return contactCount;
	}

	public void setContactCount(String contactCount) {
		this.contactCount = contactCount;
	}

	public ArrayList<String> getLogin_device_name() {
		return login_device_name;
	}

	public void setLogin_device_name(ArrayList<String> login_device_name) {
		this.login_device_name = login_device_name;
	}

}

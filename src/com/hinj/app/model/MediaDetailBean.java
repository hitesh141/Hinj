package com.hinj.app.model;

public class MediaDetailBean {

	String id = "", phone_time = "", file_size = "", type = "", created = "",
			file = "",album="",original="", file2 = "",duration="",album_image="";

	public MediaDetailBean(String id2) {
		id = id2;
	}
	
	public MediaDetailBean() {
	}

	public MediaDetailBean(String original2, String file2) {
		original = original2;
		this.file2 = file2;
	}

	public MediaDetailBean(String original2, String file3, String file22) {
		original = original2;
		file2 = file22;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhone_time() {
		return phone_time;
	}

	public void setPhone_time(String phone_time) {
		this.phone_time = phone_time;
	}

	public String getFile_size() {
		return file_size;
	}

	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	public String getFile2() {
		return file2;
	}

	public void setFile2(String file2) {
		this.file2 = file2;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getAlbum_image() {
		return album_image;
	}

	public void setAlbum_image(String album_image) {
		this.album_image = album_image;
	}
	
	
}

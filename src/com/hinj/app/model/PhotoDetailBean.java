package com.hinj.app.model;

public class PhotoDetailBean {

	String thumb = "", fullview = "";

	public PhotoDetailBean(String thumb, String fullview) {
		this.thumb = thumb;
		this.fullview  = fullview;
	}

	public String getThumb() {
		return thumb;
	}

	public void setFullview(String thumb) {
		this.thumb = thumb;
	}
	
	public String getFullview() {
		return fullview;
	}

	public void setThumb(String fullview) {
		this.fullview = fullview;
	}
}

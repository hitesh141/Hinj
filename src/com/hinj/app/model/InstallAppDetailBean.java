package com.hinj.app.model;

public class InstallAppDetailBean {

	String package_name = "", id = "", created = "", app_name = "", install_date = "", version = "", apk_file="", download_link="",apk_icon_file="";

	public String getDownload_link() {
		return download_link;
	}

	public void setDownload_link(String download_link) {
		this.download_link = download_link;
	}

	public String getApk_file() {
		return apk_file;
	}

	public void setApk_file(String apk_file) {
		this.apk_file = apk_file;
	}

	public String getPackage_name() {
		return package_name;
	}

	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getInstall_date() {
		return install_date;
	}

	public void setInstall_date(String install_date) {
		this.install_date = install_date;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getApk_icon_file() {
		return apk_icon_file;
	}

	public void setApk_icon_file(String apk_icon_file) {
		this.apk_icon_file = apk_icon_file;
	}

}

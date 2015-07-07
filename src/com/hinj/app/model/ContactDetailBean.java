package com.hinj.app.model;

public class ContactDetailBean {

	String orgStr = "", phone = "", compose_name = "", image = "",
			firstname = "", ZipCode = "", dateStr = "", jobTitleStr = "",
			id = "", created = "", Address = "", last_name = "",
			longitude = "", latitude = "", emailAdd = "", mobile = "";

	public ContactDetailBean(String id2) {
		this.id = id2;
	}
	
	
	public ContactDetailBean() {
		// TODO Auto-generated constructor stub
	}


	public ContactDetailBean(String id2, String mobile2) {
		this.id = id2;
		this.mobile = mobile2;
	}


	public String getOrgStr() {
		return orgStr;
	}

	public void setOrgStr(String orgStr) {
		this.orgStr = orgStr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompose_name() {
		return compose_name;
	}

	public void setCompose_name(String compose_name) {
		this.compose_name = compose_name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getZipCode() {
		return ZipCode;
	}

	public void setZipCode(String zipCode) {
		ZipCode = zipCode;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getJobTitleStr() {
		return jobTitleStr;
	}

	public void setJobTitleStr(String jobTitleStr) {
		this.jobTitleStr = jobTitleStr;
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

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getEmailAdd() {
		return emailAdd;
	}

	public void setEmailAdd(String emailAdd) {
		this.emailAdd = emailAdd;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}

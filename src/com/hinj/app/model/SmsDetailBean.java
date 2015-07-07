package com.hinj.app.model;

public class SmsDetailBean {

	String id = "", sms_type = "", text_message = "", created = "",
				recipient_number = "", modified="",sender_number="",
				sms_unique_id="";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSms_type() {
		return sms_type;
	}

	public void setSms_type(String sms_type) {
		this.sms_type = sms_type;
	}

	public String getText_message() {
		return text_message;
	}

	public void setText_message(String text_message) {
		this.text_message = text_message;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getRecipient_number() {
		return recipient_number;
	}

	public void setRecipient_number(String recipient_number) {
		this.recipient_number = recipient_number;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getSender_number() {
		return sender_number;
	}

	public void setSender_number(String sender_number) {
		this.sender_number = sender_number;
	}

	public String getSms_unique_id() {
		return sms_unique_id;
	}

	public void setSms_unique_id(String sms_unique_id) {
		this.sms_unique_id = sms_unique_id;
	}
	
	

}

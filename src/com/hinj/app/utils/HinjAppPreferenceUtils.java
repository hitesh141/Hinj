/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD. Nov 7, 2013 5:40:54 PM
 */
package com.hinj.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class HinjAppPreferenceUtils 
{	
	public static boolean setOk(Context context,String ok)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.OK_KEY,ok).commit();
	}
		
	public static String getOk(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.OK_KEY,"");
	}
	
	/**
	 * Set set RaId 
	 */
	public static boolean setRaId(Context context,String userId)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.RA_ID_KEY,userId).commit();
	}
	/**
	 * Get set RaId	 
	 */
	public static String getRaId(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.RA_ID_KEY,"");
	}
	
	
	
	/**
	 * Set Login Device ID
	 */
	public static boolean setLoginDeviceId(Context context,String userId)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.LOGIN_DEVICE_ID_KEY,userId).commit();
	}
	/**
	 * Get Login Device ID
	 */
	public static String getLoginDeviceId(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.LOGIN_DEVICE_ID_KEY,"");
	}
	
	
	
	/**
	 * Save last name 
	 */
	public static boolean setLastName(Context context,String googleId)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.LAST_NAME_KEY,googleId).commit();
	}
	/**
	 * Get last name 
	 */
	public static String getLastName(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.LAST_NAME_KEY,"");
	}
	
	
	/**
	 * Save first name 
	 */
	public static boolean setFirstName(Context context,String googleId)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.FIRST_NAME_KEY,googleId).commit();
		
	}
	/**
	 * Get first name 
	 */
	public static String getFirstName(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.FIRST_NAME_KEY,"");
	}
	
	/**
	 * Save email Id	 
	 */
	public static boolean setEmail(Context context,String googleId)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.EMAIL_ID_KEY,googleId).commit();
	}
	
	/**
	 * Get email Id	 
	 */
	public static String getEmail(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.EMAIL_ID_KEY,"");
	}
	
	/**
	 * Save password 
	 */
	public static boolean setPassword(Context context,String googleId)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.PASSWORD_KEY,googleId).commit();
	}
	
	/**
	 * Get password	 
	 */
	public static String getPassword(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.PASSWORD_KEY,"");
	}
	
	
	
	
	/**
	 * Save preference url 
	 */
	public static boolean setPreferenceUrl(Context context,String preferenceurl)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.PREFERENCE_URL_KEY,preferenceurl).commit();
	}
	
	/**
	 * Get password	 
	 */
	public static String getPreferenceUrl(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.PREFERENCE_URL_KEY,"");
	}
	
	
	/**
	 * Save preference username
	 */
	public static boolean setPreferenceUsername(Context context,String username)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.USER_NAME_KEY,username).commit();
	}
	
	/**
	 * Get preference username	 
	 */
	public static String getPreferenceUsername(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.USER_NAME_KEY,"");
	}
	
	
	/**
	 * Save preference voice retention
	 */
	public static boolean setPreferenceVoiceRetention(Context context,String voiceretentiondays)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.VOICE_RETENTION_KEY,voiceretentiondays).commit();
	}
	
	/**
	 * Get preference voice retention	 
	 */
	public static String getPreferenceVoiceRetention(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.VOICE_RETENTION_KEY,"");
	}
	
	
	/**
	 * Save preference auto login status
	 */
	public static boolean setPreferenceLoginStatus(Context context,String loginstatus)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.AUTO_LOGIN_KEY,loginstatus).commit();
	}
	
	/**
	 * Get preference auto login status	 
	 */
	public static String getPreferenceLoginStatus(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.AUTO_LOGIN_KEY,"");
	}
	
	
	/**
	 * Save preference UploadDeviceID
	 */
	public static boolean setUploadDeviceID(Context context,String loginstatus)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.UPLOAD_DEVICE_ID_KEY,loginstatus).commit();
	}
	
	/**
	 * Get preference UploadDeviceID
	 */
	public static String getUploadDeviceID(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.UPLOAD_DEVICE_ID_KEY,"");
	}
	
	
	/**
	 * Save preference set date
	 */
	public static boolean setPreferenceExpirationDate(Context context,String date)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.PREFERENCE_DATE_SAVE_KEY,date).commit();
	}
	
	/**
	 * Get preference auto set date
	 */
	public static String getPreferenceExpirationDate(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.PREFERENCE_DATE_SAVE_KEY,"");
	}
	
     //PhotoCount
     /**
 	 * Save PhotoCount
 	 */
 	public static boolean setPhotoCount(Context context,String date)
 	{
 		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
 		return sp.edit().putString(HingAppConstants.PHOTO_COUNT_KEY,date).commit();
 	}
 	
 	/**
 	 * Get PhotoCount
 	 */
 	public static String getPhotoCount(Context context)
 	{
 		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
 		return sp.getString(HingAppConstants.PHOTO_COUNT_KEY,"");
 	}
 	
 	 /**
 	 * Save musicCount
 	 */
 	public static boolean setMusicCount(Context context,String date)
 	{
 		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
 		return sp.edit().putString(HingAppConstants.MUSIC_COUNT_KEY,date).commit();
 	}
 	
 	/**
 	 * Get music Count
 	 */
 	public static String getMusicCount(Context context)
 	{
 		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
 		return sp.getString(HingAppConstants.MUSIC_COUNT_KEY,"");
 	}
 	//videos
	 /**
 	 * Save videos Count
 	 */
 	public static boolean setvideosCount(Context context,String videos)
 	{
 		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
 		return sp.edit().putString(HingAppConstants.VIDEO_COUNT_KEY,videos).commit();
 	}
 	
 	/**
 	 * Get videos Count
 	 */
 	public static String getvideosCount(Context context)
 	{
 		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
 		return sp.getString(HingAppConstants.VIDEO_COUNT_KEY,"");
 	}
 	
 	 /**
 	 * Save appsCount
 	 */
 	public static boolean setAppsCount(Context context,String apps)
 	{
 		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
 		return sp.edit().putString(HingAppConstants.APPS_COUNT_KEY,apps).commit();
 	}
 	
 	/**
 	 * Get appsCount
 	 */
 	public static String getAppsCount(Context context)
 	{
 		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
 		return sp.getString(HingAppConstants.APPS_COUNT_KEY,"");
 	}
 	//contacts

	 /**
	 * Save contactsCount
	 */
	public static boolean setContactsCount(Context context,String apps)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.CONTACTS_COUNT_KEY,apps).commit();
	}
	
	/**
	 * Get contactsCount
	 */
	
	
	public static String getContactsCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.CONTACTS_COUNT_KEY,"");
	}
	//messages
	 /**
	 * Save messagesCount
	 */
	public static boolean setMessagesCount(Context context,String messages)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.MESSAGES_COUNT_KEY,messages).commit();
	}
	
	/**
	 * Get messagesCount
	 */
	public static String getMessagesCount(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.MESSAGES_COUNT_KEY,"");
	}
	
	
	 /**
		 * Save REGISTER_DEVICE_ID_KEY deviceID
		 */
		public static boolean setRegisterDeviceId(Context context,String deviceID)
		{
			SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
			return sp.edit().putString(HingAppConstants.REGISTER_DEVICE_ID_KEY,deviceID).commit();
		}
		
		/**
		 * Get REGISTER_DEVICE_ID_KEY deviceID
		 */
		public static String getRegisterDeviceId(Context context)
		{
			SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
			return sp.getString(HingAppConstants.REGISTER_DEVICE_ID_KEY,"");
		}
		

		
		

		 /**
			 * Save getChildRegisterDeviceId deviceID
			 */
			public static boolean setChildRegisterDeviceId(Context context,String deviceID)
			{
				SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
				return sp.edit().putString(HingAppConstants.CHILD_REGISTER_DEVICE_ID_KEY,deviceID).commit();
			}
			
			/**
			 * Get getChildRegisterDeviceId deviceID
			 */
			public static String getChildRegisterDeviceId(Context context)
			{
				SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
				return sp.getString(HingAppConstants.CHILD_REGISTER_DEVICE_ID_KEY,"");
			}
		
		
			 /**
			 * Save url
			 */
			public static boolean setUrl(Context context,String deviceID)
			{
				SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
				return sp.edit().putString(HingAppConstants.URL_KRY,deviceID).commit();
			}
			
			/**
			 * Get url
			 */
			public static String getUrl(Context context)
			{
				SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
				return sp.getString(HingAppConstants.URL_KRY,"");
			}
		
			
			
			
			
			 /**
			 * Save Delete CallLog id
			 */
			public static boolean setCallLogDeleteId(Context context,String deviceID)
			{
				SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
				return sp.edit().putString(HingAppConstants.CALL_LOG_DELETE_ID,deviceID).commit();
			}
			
			/**
			 * Get Delete CallLog id
			 */
			public static String getCallLogDeleteId(Context context)
			{
				SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
				return sp.getString(HingAppConstants.CALL_LOG_DELETE_ID,"");
			}
			
			
			 /**
			 * Save Message Delete id
			 */
			public static boolean setSMSDeleteId(Context context,String deviceID)
			{
				SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
				return sp.edit().putString(HingAppConstants.SMS_DELETE_ID,deviceID).commit();
			}
			
			/**
			 * Get Message Delete id
			 */
			public static String getSMSLogDeleteId(Context context)
			{
				SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
				return sp.getString(HingAppConstants.SMS_DELETE_ID,"");
			}
			
			
			 /**
			 * Save AppDownloadLink
			 */
			public static boolean setAppDownloadLink(Context context,String deviceID)
			{
				SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
				return sp.edit().putString(HingAppConstants.APP_DOWNLOAD_LINK,deviceID).commit();
			}
			
			/**
			 * Get AppDownloadLink
			 */
			public static String getAppDownloadLink(Context context)
			{
				SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
				return sp.getString(HingAppConstants.APP_DOWNLOAD_LINK,"");
			}
			
			
			
			 /**
			 * Save DownloadAppName
			 */
			public static boolean setDownloadAppName(Context context,String deviceID)
			{
				SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
				return sp.edit().putString(HingAppConstants.DOWNLOAD_APP_NAME,deviceID).commit();
			}
			
			/**
			 * Get DownloadAppName
			 */
			public static String getDownloadAppName(Context context)
			{
				SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
				return sp.getString(HingAppConstants.DOWNLOAD_APP_NAME,"");
			}
			
			
			
		
	 /**
	 * Save ClipboardStr
	 */
	public static boolean setClipboardStr(Context context,String deviceID)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.CLIPBOARD_KEY,deviceID).commit();
	}
	
	/**
	 * Get ClipboardStr
	 */
	public static String getClipboardStr(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.CLIPBOARD_KEY,"");
	}
	
	
	 /**
	 * Save modelname
	 */
	public static boolean setModelName(Context context,String modelname)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.ChildDevicedModel_KEY,modelname).commit();
	}
	
	/**
	 * Get modelname
	 */
	public static String getModelName(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.ChildDevicedModel_KEY,"");
	}
	
	
	 /**
	 * Save child ra id
	 */
	public static boolean setChildRaId(Context context,String modelname)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.CHILD_RA_ID_KEY,modelname).commit();
	}
	
	/**
	 * Get child ra id
	 */
	public static String getChildRaId(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.CHILD_RA_ID_KEY,"");
	}
	
	
	 /**
	 * Save child upload device id
	 */
	public static boolean setChildUploadDeviceId(Context context,String childUploadDeviceId)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.CHILD_UPLOAD_DEVICE_ID_KEY,childUploadDeviceId).commit();
	}
	
	/**
	 * Get child upload device id
	 */
	public static String getChildUploadDeviceId(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.CHILD_UPLOAD_DEVICE_ID_KEY,"");
	}
	
	

	 /**
	 * Save parent model name
	 */
	public static boolean setParentModelName(Context context,String modelname)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.PARENT_MODEL_NAME_KEY,modelname).commit();
	}
	
	/**
	 * Get ParentModelName
	 */
	public static String getParentModelName(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.PARENT_MODEL_NAME_KEY,"");
	}
	
	
	 /**
	 * Save parent parentDeviceId
	 */
	public static boolean setParentDeviceId(Context context,String parentDeviceId)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.PARENT_DEVICE_ID_KEY,parentDeviceId).commit();
	}
	
	/**
	 * Get parentDeviceId
	 */
	public static String getParentDeviceId(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.PARENT_DEVICE_ID_KEY,"");
	}
	
	/**
	 * Set User name 
	 */
	public static boolean setUserName(Context context,String UserName)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.USER_NAME_KEY,UserName).commit();
	}
	/**
	 * Get User name	 
	 */
	public static String getUserName(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.USER_NAME_KEY,"");
	}
	
	/**
	 * Set User email	 
	 */
	public static boolean setUserEmailId(Context context,String email)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.EMAIL_ID_KEY,email).commit();
	}
	/**
	 * Get User email	 
	 */
	public static String getUserEmailId(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.EMAIL_ID_KEY,"");
	}
	
	 /**
	 * Save message chat recipient number
	 */
	public static boolean setMessageChatRecipientNumber(Context context,String recipient)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.MESSAGE_CHAT_RECIPIENT_NUMBER_NAME_KEY,recipient).commit();
	}
	
	/**
	 * Get message chat recipient number
	 */
	public static String getMessageChatRecipientNumber(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.MESSAGE_CHAT_RECIPIENT_NUMBER_NAME_KEY,"");
	}
	
	
	 /**
	 * Save call recipient number
	 */
	public static boolean setCallRecipientNumber(Context context,String recipient)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.CALL_RECIPIENT_NUMBER_NAME_KEY,recipient).commit();
	}
	
	/**
	 * Get call recipient number
	 */
	public static String getCallRecipientNumber(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.CALL_RECIPIENT_NUMBER_NAME_KEY,"");
	}	
	
	
	
	
	
	/**
	 * Save KeepScreenAwake
	 */
	public static boolean setKeepScreenAwake(Context context,String recipient)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.KEEP_SCREEN_AWAKE,recipient).commit();
	}
	
	/**
	 * Get KeepScreenAwake
	 */
	public static String getKeepScreenAwake(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.KEEP_SCREEN_AWAKE,"ON");
	}	
	
	/**
	 * Save NotifcationOnOff
	 */
	public static boolean setNotifcationIcon(Context context,String recipient)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.NOTIFICATION_ON_OFF,recipient).commit();
	}
	
	/**
	 * Get NotifcationOnOff
	 */
	public static String getNotifcationIcon(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.NOTIFICATION_ON_OFF,"ON");
	}	
	
	/**
	 * Save NotifcationSound
	 */
	public static boolean setNotifcationSound(Context context,String recipient)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.edit().putString(HingAppConstants.NOTIFICATION_SOUND,recipient).commit();
	}
	
	/**
	 * Get NotifcationSound
	 */
	public static String getNotifcationSound(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences(HingAppConstants.HINJ_APP_SHARED_PREFERENCE,Activity.MODE_PRIVATE);
		return sp.getString(HingAppConstants.NOTIFICATION_SOUND,"ON");
	}	
	
	
}
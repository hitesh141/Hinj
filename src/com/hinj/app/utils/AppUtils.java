package com.hinj.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

/**
 * Name :- Hiteshm Date :- 06/11/2014 Purpose :- Purpose of this Utility Class
 * is to use declare global methods which can use anywhere in our projects
 * */

public class AppUtils {
	private final static String UTF8 = "UTF-8";
	
	public static String GCM_SENDERID = "991780076634" ;
		//"86689480042"; anzys id
	//"991780076634" ;my id
	public static long currentTime = System.currentTimeMillis();
	public static String currentDate = AppUtils.getSimpleDateFormat(
			currentTime, "ddMMyyyyhhmmss");

	// Twitter Keys
//	public final static String TWITTER_CONSUMER_KEY = "nPYIJFW17PNkVKK9E58tfPsmu"; // Twitter consumer key
//	public final static String TWITTER_CONSUMER_SECRET = "pX6KD594bKkhEXFzvveR9WrHf8uSEj9AYC4N3ntOTp7Ta5yZ4m";
	
//	public final static String TWITTER_CONSUMER_KEY = "m5X4lWm9F4lXGyDh28uW0zqdv"; // Twitter consumer key
//	public final static String TWITTER_CONSUMER_SECRET = "vSgF3kO3YRSvOHnnVJjQ5K4NpaHK3Tgv0Dt0vUnxF2M10nY2JG";
	
	public final static String TWITTER_CONSUMER_KEY = "tkLpMCakECUUNT94Qbn5K2daZ"; // Twitter consumer key
	public final static String TWITTER_CONSUMER_SECRET = "CiPPSwZLNQbns7RojKgpPAHgkJS3YryV105QLfqos340SJNSt3";
	
	// Twitter consumer secret key
	public static ArrayList<Fragment> myFragList;
	
	/**
	 * A utility method to determine whether network available or not
	 */
	public static boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager connectivityManager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/**
	 * A utility method to determine internet connectivity this is invokedbefore
	 * every web request
	 */
	public static boolean isConnectivityAvailable(Context ctx) {
		boolean isNetworkConnected = false;

		ConnectivityManager manager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();

		if (info != null) {

			Log.d("isConnectivityAvailable", info.getState().toString());
			if (info.getState() == NetworkInfo.State.CONNECTED) {
				isNetworkConnected = true;
			}
		} else {
			Log.d("isConnectivityAvailable", "No Connection Available");
		}
		return isNetworkConnected;
	}

	public static String inputSteamToString(InputStream is) {
		String response;
		StringBuffer responseInBuffer = new StringBuffer();
		byte[] b = new byte[4028];
		try {
			for (int n; (n = is.read(b)) != -1;) {
				responseInBuffer.append(new String(b, 0, n));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		response = new String(responseInBuffer.toString());
		return response;
	}

	/**
	 * Checking for empty string if it is equal to null, length is zero or
	 * containing "null" string
	 */
	public static boolean isEmptyString(String text) {
		return (text == null || text.trim().equals("null") || text.trim()
				.length() <= 0);
	}

	/**
	 * Converts an input stream to string. Used in ServerCommunication class.
	 */

	public static boolean hasSpecialCharacter(String text) {
		if (isEmptyString(text)) {
			return false;
		} else {
			if (text.contains("<") || text.contains(">") || text.contains(";")
					|| text.contains("%") || text.contains("\"")
					|| text.contains("\'") || text.contains(",")
					|| text.contains("#") || text.contains("&")
					|| text.contains(":") || text.contains("?")
					|| text.contains("!") || text.contains("+")
					|| text.contains("-") || text.contains("=")
					|| text.contains("@") || text.contains("^")
					|| text.contains("*") || text.contains("(")
					|| text.contains(")") || text.contains("{")
					|| text.contains("}") || text.contains("[")
					|| text.contains("]")) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static void showDialog(Context activity, String title) {
		String message = AppUtils.isEmptyString(HinjAppPreferenceUtils
				.getOk(activity)) ? "Ok" : HinjAppPreferenceUtils
				.getOk(activity);

		AlertDialog.Builder alert = new Builder(activity);
		alert.setMessage(title).setPositiveButton(message,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});

		alert.show();
	}

	public static void showDialog(Activity activity, String title,
			final OnClickListener onClickListener) {
		String message = AppUtils.isEmptyString(HinjAppPreferenceUtils
				.getOk(activity)) ? "Ok" : HinjAppPreferenceUtils
				.getOk(activity);

		AlertDialog.Builder alert = new Builder(activity);
		alert.setMessage(title).setPositiveButton(message,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						onClickListener.onClick(null);
					}
				});

		alert.show();
	}

	public static double roundToDecimalPlaces(String text) {
		double value = Double.parseDouble(text);

		DecimalFormat df = new DecimalFormat("#.##");
		String formate = df.format(value);
		try {
			double finalValue = (Double) df.parse(formate);
			return finalValue;
		} catch (ClassCastException e) {
			e.printStackTrace();

			try {
				double finalValue = (Long) df.parse(formate);
				return finalValue;
			} catch (ParseException e1) {
				e1.printStackTrace();
				return value;
			} catch (Throwable t) {
				t.printStackTrace();
				return value;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return value;
		} catch (Throwable t) {
			t.printStackTrace();
			return value;
		}
	}

	/**
	 * Checking if a String matches desired Regex pattern. Used for validating
	 * emails.
	 */

	public static boolean isMatch(String text, String pattern) {
		try {
			Pattern patt = Pattern.compile(pattern);
			Matcher matcher = patt.matcher(text);
			return matcher.matches();
		} catch (RuntimeException e) {
			return false;
		}
	}

	/**
	 * Checking if a string contains another string ignoring their case.
	 */

	@SuppressLint("DefaultLocale")
	public static boolean containsIgnoreCaseString(String original,
			String tobeChecked) {
		return original.toLowerCase().contains(tobeChecked.toLowerCase());
	}

	/**
	 * Converts dp to pixels
	 */

	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	/**
	 * Converts pixels to dp
	 */

	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	/**
	 * Return a string array containing each chracter of the given string.
	 */

	public static String[] breakStringIntoCharacters(String data) {
		if (!AppUtils.isEmptyString(data)) {
			String output[] = new String[data.length()];
			for (int i = 0; i < data.length(); i++) {
				output[i] = String.valueOf(data.charAt(i));
			}
			return output;
		} else {
			return new String[] {};
		}
	}

	/**
	 * Get device registered email.
	 */

	public static String getEmail(Context context) {
		AccountManager accountManager = AccountManager.get(context);
		Account account = getAccount(accountManager);
		if (account == null) {
			return "";
		} else {
			return account.name;
		}
	}

	/**
	 * Get device account. Used by getEmail()
	 */

	private static Account getAccount(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];
		} else {
			account = null;
		}
		return account;
	}

	/**
	 * Utility method to hide soft keyboard in an activity
	 */

	public static void hideSoftKeyboard(Activity activity) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity
					.getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Utility method to get activity running on top of activity stack.
	 */

	public static String getCurrentTopActivity(Context context) {
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager
				.getRunningTasks(1);
		ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
		return ar.topActivity.getClassName().toString();
	}

	/**
	 * Utility method to get First letter capital
	 */

	public static String capitalizeFirstLetter(String original) {
		try {
			if (isEmptyString(original)) {
				return original;
			}

			String[] tokens = original.split("\\s");
			original = "";

			for (int i = 0; i < tokens.length; i++) {
				char capLetter = Character.toUpperCase(tokens[i].charAt(0));
				original += " " + capLetter
						+ tokens[i].substring(1, tokens[i].length());
			}

			return original.trim();
		} catch (Exception e) {
			if (isEmptyString(original)) {
				return original;
			}
			return original;
		}
	}

	public static String getCurrentTimeStamp(Context ctx, long longTimeMiliSec) {

		String returnTime = "";

		final long MILISEC_ONE_DAY = (long) (1 * 24 * 60 * 60 * 1000);

		Calendar cal = Calendar.getInstance();
		int currentHour = cal.get(Calendar.HOUR_OF_DAY);
		int currentMin = cal.get(Calendar.MINUTE);

		int remainingHour = 23 - currentHour;
		int remainingMin = 60 - currentMin;

		long miliSec_at_12 = (System.currentTimeMillis() + (remainingHour * 60 * 60 * 1000) + (remainingMin * 60 * 1000));

		long difference = miliSec_at_12 - longTimeMiliSec;
		int numDays = (int) (difference / MILISEC_ONE_DAY);

		if (numDays == 0) {
			if (android.text.format.DateFormat.is24HourFormat(ctx)) {
				returnTime = getSimpleDateFormat(longTimeMiliSec, "HH:mm").toString();
			} else {
				returnTime = getSimpleDateFormat(longTimeMiliSec, "hh:mm aa").toString();
			}
		} else if (numDays == 1) {
			returnTime = "Yesterday";
		} else  if(numDays == 2) {
			returnTime = "1 Day ago";
		} else if(numDays == 3) {
			returnTime = "2 Days ago";
		}
		else {
			returnTime = getSimpleDateFormat(longTimeMiliSec, "dd MMM yyyy").toString();
		}

		return returnTime;

	}

	@SuppressLint("SimpleDateFormat")
	public static String getSimpleDateFormat(long longStampTime, String pattern) {
		SimpleDateFormat mdyFormat = new SimpleDateFormat(pattern, Locale.US);
		TimeZone utcZone = TimeZone.getTimeZone("UTC");
		mdyFormat.setTimeZone(utcZone);

		// System.out.println(currentHour1%12 + ":" + currentMin1 + " " +
		// ((currentHour1>=12) ? "PM" : "AM"));
		return mdyFormat.format(new Date(longStampTime*1000));
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

	@SuppressLint("SimpleDateFormat")
	public static String changeDateFormat(String matchDate, String format2) {
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS");

		Date d1 = null;
		try {
			d1 = format.parse(matchDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long matchTimeMillis = d1.getTime();

		String formatedDate = getSimpleDateFormat(matchTimeMillis, format2)
				.toString();
		return formatedDate;
	}

	@SuppressLint("SimpleDateFormat")
	public static String changeDateFormat2(String matchDate, String format2) {
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyhhmmss");

		Date d1 = null;
		try {
			d1 = format.parse(matchDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long matchTimeMillis = d1.getTime();

		String formatedDate = getSimpleDateFormat(matchTimeMillis, format2)
				.toString();
		return formatedDate;
	}

	@Deprecated
	public static String encodeUrl(Bundle parameters) {
		if (parameters == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String key : parameters.keySet()) {
			Object parameter = parameters.get(key);
			if (!(parameter instanceof String)) {
				continue;
			}

			if (first)
				first = false;
			else
				sb.append("&");
			sb.append(URLEncoder.encode(key) + "="
					+ URLEncoder.encode(parameters.getString(key)));
		}
		return sb.toString();
	}

	@Deprecated
	public static Bundle decodeUrl(String s) {
		Bundle params = new Bundle();
		if (s != null) {
			String array[] = s.split("&");
			for (String parameter : array) {
				String v[] = parameter.split("=");

				try {
					if (v.length == 2) {
						params.putString(URLDecoder.decode(v[0], UTF8),
								URLDecoder.decode(v[1], UTF8));
					} else if (v.length == 1) {
						params.putString(URLDecoder.decode(v[0], UTF8), "");
					}
				} catch (UnsupportedEncodingException e) {
					// shouldn't happen
				}
			}
		}
		return params;
	}

	public static void convertXml(String xml) {
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(xml));

			// normalize text representation
			doc.getDocumentElement().normalize();
			System.out.println("Root element of the doc is "
					+ doc.getDocumentElement().getNodeName());

			NodeList listOfPersons = doc.getElementsByTagName("person");
			int totalPersons = listOfPersons.getLength();
			System.out.println("Total no of people : " + totalPersons);

			for (int s = 0; s < listOfPersons.getLength(); s++) {

				Node firstPersonNode = listOfPersons.item(s);
				if (firstPersonNode.getNodeType() == Node.ELEMENT_NODE) {

					Element firstPersonElement = (Element) firstPersonNode;

					// -------
					NodeList firstNameList = firstPersonElement
							.getElementsByTagName("first");
					Element firstNameElement = (Element) firstNameList.item(0);

					NodeList textFNList = firstNameElement.getChildNodes();
					System.out
							.println("First Name : "
									+ ((Node) textFNList.item(0))
											.getNodeValue().trim());

					// -------
					NodeList lastNameList = firstPersonElement
							.getElementsByTagName("last");
					Element lastNameElement = (Element) lastNameList.item(0);

					NodeList textLNList = lastNameElement.getChildNodes();
					System.out
							.println("Last Name : "
									+ ((Node) textLNList.item(0))
											.getNodeValue().trim());

					// ----
					NodeList ageList = firstPersonElement
							.getElementsByTagName("age");
					Element ageElement = (Element) ageList.item(0);

					NodeList textAgeList = ageElement.getChildNodes();
					System.out.println("Age : "
							+ ((Node) textAgeList.item(0)).getNodeValue()
									.trim());

					// ------

				}// end of if clause

			}// end of for loop with s var

		} catch (SAXParseException err) {
			System.out.println("** Parsing error" + ", line "
					+ err.getLineNumber() + ", uri " + err.getSystemId());
			System.out.println(" " + err.getMessage());

		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	// to see database on SD Card
	public static void writeToSD(Context mContext, String DATABASE_NAME)
			throws IOException {

		String DB_PATH = "";

		DB_PATH = mContext.getDatabasePath(DATABASE_NAME).toString();

		File sd = new File(Environment.getExternalStorageDirectory().toString());

		if (sd.canWrite()) {

			String backupDBName = "INTRA_SCRIPT_BACK_DB.db";

			File currentDB = new File(DB_PATH);
			File backupDB = new File(sd, backupDBName);

			if (backupDB.exists()) {
				backupDB.delete();
			}

			if (currentDB.exists()) {

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());

				src.close();
				dst.close();
			}

			System.out.println("IN BACKUP DB WRITING");
		}
	}
	
}

/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.  11:02:04 AM  Dec 17, 2013
 */
package com.hinj.secure.smsgps;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class ChildOnOFFStatusAlarm {
	private static PendingIntent resetAlarm;
	private static String TAG="ChildGPSAlarm";
	private static AlarmManager am;
	public static void start(Context context) {
		try {
			// We want the alarm to go off 30 seconds from now.
			long firstTime = SystemClock.elapsedRealtime();

			// Create an IntentSender that will launch our service, to be scheduled with the alarm manager.
			resetAlarm = PendingIntent.getService(context, 0, new Intent(context, CallOnOffAnsycTaskService.class), 0);
			// Schedule the alarm!
			am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			//am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 2 * 60 * 1000, resetAlarm);
			Log.i(TAG, firstTime+"");
			am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 4*1000*60, resetAlarm);
		} 
		catch (Exception e) {
			Log.v("CellInfo", "Exception while start the ChildGPSAlarm at: " + e.getMessage());
		}
	}
	
	public static void stop(Context context) {
		try {
			// When interval going to change from web services
			am.cancel(resetAlarm);
		} 
		catch (Exception e) {
			Log.v("CellInfo", "Exception while start the ChildGPSAlarm at: " + e.getMessage());
		}
	}
}

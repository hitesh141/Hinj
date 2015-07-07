package com.hinj.app.activity;

import android.content.Context;

public class GCMBroadcastReceiver extends	com.google.android.gcm.GCMBroadcastReceiver {

	@Override
	protected String getGCMIntentServiceClassName(Context context) {

		return "com.hinj.app.activity.GCMIntentService";
	}
}
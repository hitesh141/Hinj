/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
package com.hinj.asynctask;

import java.util.List;

import android.os.AsyncTask;
import eu.chainfire.libsuperuser.Shell;

/**
 * @author jitendrav
 *
 */
public class PermissionAsyncTask extends AsyncTask<String, Void, String> {
	private boolean suAvailable = true;
	private String suVersion = null;
	private String suVersionInternal = null;
	private List<String> suResult = null;

	@Override
	public void onPreExecute() {
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		// 06-26 17:25:59.132: I/ChildLoginActivity(23782):
		// {"register":[{"error":"License key is not exit in our database."}]}
		if (suAvailable) {
			suVersion = Shell.SU.version(false);
			suVersionInternal = Shell.SU.version(true);
			suResult = Shell.SU.run(new String[] {
				/*	"mount -o rw,remount /system  /",
					"chmod -R 777 /data/data/com.whatsapp  /",
					"chmod -R 777 /data/data/com.facebook.orca /",
					"chmod -R 777 /data/data/com.skype.raider /",
					"chmod -R 777 /data/data/com.google.android.gm  /",
					"chmod -R 777 /data/data/com.instagram.android /",
					"chmod -R 777 /data/data/com.viber.voip  /",
					"chmod -R 777 /data/data/com.bbm/",
					"chmod -R 777 /data/data/com.bbm/files/bbmcore/",
					"chmod -R 777 /data/data/com.twitter.android/"*/});
			/*
			"chmod -R 777 /data/data/com.bbm/",
			"chmod -R 777 /data/data/com.bbm/files/bbmcore/"*/
		}

		return null;
	}

	@Override
	public void onPostExecute(String serverResponse) {
		

	}
}
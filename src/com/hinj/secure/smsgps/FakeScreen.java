/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
/**
 * @author jitendrav
 *
 */
package com.hinj.secure.smsgps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.hinj.app.activity.R;

public class FakeScreen extends Activity {

	static Context ctx=null;
	public static Activity moveBack=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.fake_screen);
		ctx=this;
		
		moveBack=this;
       // moveTaskToBack(true);
	}

	protected static Activity getInstance()
	{
		return (Activity)ctx;
	}

}

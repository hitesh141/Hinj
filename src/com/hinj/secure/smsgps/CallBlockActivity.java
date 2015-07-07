package com.hinj.secure.smsgps;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.hinj.app.activity.R;


public class CallBlockActivity extends Activity {
	
	public static Activity CallBlockActivityInstance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock_main);		
		CallBlockActivityInstance=this;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}

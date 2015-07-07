package com.hinj.secure.smsgps;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.hinj.app.activity.R;

public class Splashscreen extends Activity{

	TimerTask task;
	Timer timer;
	Handler handler;
	View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blackscreen);
		
		task=new TimerTask() 
		{ 	
			public void run() 
			{
				/*if(!SpyCallSharedPrefrence.getSubmitStatus(Splashscreen.this).equalsIgnoreCase("CLICKED")) 
				{
					Intent intent = new Intent(Splashscreen.this,D.class);
					startActivity(intent);
					finish();
				}
				else
				{
					Intent intent = new Intent(Splashscreen.this,WhiteScreen.class);
					startActivity(intent);
					finish();
				}*/
			}
		};

		timer=new Timer();
		timer.schedule(task,1000);
	}
}
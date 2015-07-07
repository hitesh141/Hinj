package com.hinj.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.hinj.app.utils.HinjAppPreferenceUtils;

public class SettingsActivity extends Activity{

	private ImageView imag_keep_screen_awake, imag_notif_on_off, imag_notif_sound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.fragment_settings);
        
        imag_keep_screen_awake = (ImageView) findViewById(R.id.imag_keep_screen_awake);
        imag_notif_on_off = (ImageView) findViewById(R.id.imag_notif_on_off);
        imag_notif_sound = (ImageView) findViewById(R.id.imag_notif_sound);
        
        if (HinjAppPreferenceUtils.getKeepScreenAwake(SettingsActivity.this).equalsIgnoreCase("ON")) {
        	imag_keep_screen_awake.setBackgroundResource(R.drawable.toggle_on);
		}else {
			imag_keep_screen_awake.setBackgroundResource(R.drawable.toggle_off);
		}if (HinjAppPreferenceUtils.getNotifcationIcon(SettingsActivity.this).equalsIgnoreCase("ON")) {
			imag_notif_on_off.setBackgroundResource(R.drawable.toggle_on);
		}else {
			imag_notif_on_off.setBackgroundResource(R.drawable.toggle_off);
		}if (HinjAppPreferenceUtils.getNotifcationSound(SettingsActivity.this).equalsIgnoreCase("ON")) {
			imag_notif_sound.setBackgroundResource(R.drawable.toggle_on);
		}else {
			imag_notif_sound.setBackgroundResource(R.drawable.toggle_off);
		}
        
		imag_keep_screen_awake.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (HinjAppPreferenceUtils.getKeepScreenAwake(SettingsActivity.this).equalsIgnoreCase("ON")) {
		        	imag_keep_screen_awake.setBackgroundResource(R.drawable.toggle_off);
		        	HinjAppPreferenceUtils.setKeepScreenAwake(SettingsActivity.this, "OFF");
				}else {
					imag_keep_screen_awake.setBackgroundResource(R.drawable.toggle_on);
					HinjAppPreferenceUtils.setKeepScreenAwake(SettingsActivity.this, "ON");
				}
			}
		});
		
		imag_notif_on_off.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (HinjAppPreferenceUtils.getNotifcationIcon(SettingsActivity.this).equalsIgnoreCase("ON")) {
					imag_notif_on_off.setBackgroundResource(R.drawable.toggle_off);
					HinjAppPreferenceUtils.setNotifcationIcon(SettingsActivity.this, "OFF");
				}else {
					imag_notif_on_off.setBackgroundResource(R.drawable.toggle_on);
					HinjAppPreferenceUtils.setNotifcationIcon(SettingsActivity.this, "ON");
				}
			}
		});
		
		imag_notif_sound.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (HinjAppPreferenceUtils.getNotifcationSound(SettingsActivity.this).equalsIgnoreCase("ON")) {
					imag_notif_sound.setBackgroundResource(R.drawable.toggle_off);
					HinjAppPreferenceUtils.setNotifcationSound(SettingsActivity.this, "OFF");
				}else {
					imag_notif_sound.setBackgroundResource(R.drawable.toggle_on);
					HinjAppPreferenceUtils.setNotifcationSound(SettingsActivity.this, "ON");
				}
			}
		});
	}
}

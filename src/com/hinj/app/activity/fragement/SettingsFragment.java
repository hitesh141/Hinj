package com.hinj.app.activity.fragement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hinj.app.activity.R;
import com.hinj.app.utils.HinjAppPreferenceUtils;

public class SettingsFragment extends Fragment{

	private ImageView imag_keep_screen_awake, imag_notif_on_off, imag_notif_sound;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        
        imag_keep_screen_awake = (ImageView) rootView.findViewById(R.id.imag_keep_screen_awake);
        imag_notif_on_off = (ImageView) rootView.findViewById(R.id.imag_notif_on_off);
        imag_notif_sound = (ImageView) rootView.findViewById(R.id.imag_notif_sound);
        
        
        if (HinjAppPreferenceUtils.getKeepScreenAwake(getActivity()).equalsIgnoreCase("ON")) {
        	imag_keep_screen_awake.setBackgroundResource(R.drawable.toggle_on);
		}else {
			imag_keep_screen_awake.setBackgroundResource(R.drawable.toggle_off);
		}if (HinjAppPreferenceUtils.getNotifcationIcon(getActivity()).equalsIgnoreCase("ON")) {
			imag_notif_on_off.setBackgroundResource(R.drawable.toggle_on);
		}else {
			imag_notif_on_off.setBackgroundResource(R.drawable.toggle_off);
		}if (HinjAppPreferenceUtils.getNotifcationSound(getActivity()).equalsIgnoreCase("ON")) {
			imag_notif_sound.setBackgroundResource(R.drawable.toggle_on);
		}else {
			imag_notif_sound.setBackgroundResource(R.drawable.toggle_off);
		}
        
		imag_keep_screen_awake.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (HinjAppPreferenceUtils.getKeepScreenAwake(getActivity()).equalsIgnoreCase("ON")) {
		        	imag_keep_screen_awake.setBackgroundResource(R.drawable.toggle_off);
		        	HinjAppPreferenceUtils.setKeepScreenAwake(getActivity(), "OFF");
				}else {
					imag_keep_screen_awake.setBackgroundResource(R.drawable.toggle_on);
					HinjAppPreferenceUtils.setKeepScreenAwake(getActivity(), "ON");
				}
			}
		});
		
		imag_notif_on_off.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (HinjAppPreferenceUtils.getNotifcationIcon(getActivity()).equalsIgnoreCase("ON")) {
					imag_notif_on_off.setBackgroundResource(R.drawable.toggle_off);
					HinjAppPreferenceUtils.setNotifcationIcon(getActivity(), "OFF");
				}else {
					imag_notif_on_off.setBackgroundResource(R.drawable.toggle_on);
					HinjAppPreferenceUtils.setNotifcationIcon(getActivity(), "ON");
				}
			}
		});
		
		imag_notif_sound.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (HinjAppPreferenceUtils.getNotifcationSound(getActivity()).equalsIgnoreCase("ON")) {
					imag_notif_sound.setBackgroundResource(R.drawable.toggle_off);
					HinjAppPreferenceUtils.setNotifcationSound(getActivity(), "OFF");
				}else {
					imag_notif_sound.setBackgroundResource(R.drawable.toggle_on);
					HinjAppPreferenceUtils.setNotifcationSound(getActivity(), "ON");
				}
			}
		});
		
        return rootView;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}

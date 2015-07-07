package com.hinj.globalData;

import java.util.ArrayList;

import com.hinj.app.model.MediaDetailBean;

public class GlobalData {
	private static GlobalData _instance = null;
	
	public ArrayList<MediaDetailBean> mMediaDetailBean = new ArrayList<MediaDetailBean>(0);
	
	public String[] name = new String[30];
	public String[] url = new String[30];
	
	public GlobalData(){
		
	}

	public static GlobalData getInstance(){
		if(_instance==null){
			_instance = new GlobalData();
		}
		return _instance;
	}
}

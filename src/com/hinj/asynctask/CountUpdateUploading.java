package com.hinj.asynctask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.hinj.parsing.HingAppParsing;

@SuppressLint("SimpleDateFormat")
public class CountUpdateUploading extends AsyncTask<String, String,Object[]> {
	private String response = "";
	private String deviceId,userId,model;
	private Context context;
	
	public CountUpdateUploading(Context ctx, String deviceId,String userId,String model) {
		this.deviceId = deviceId;
		this.userId = userId;
		this.model = model;
		this.context = ctx;
	}

	@Override
	public Object[] doInBackground(String... params) {
		
		return HingAppParsing.postScanResultService(context, deviceId,userId,model); 
	}  

	@Override
	public void onPostExecute(Object[] result) {
		try{
			System.out.println("result "+result);
			boolean status = (Boolean) result[0];
			response = (String) result[1];
			
			if(status)
			{
//				Toast.makeText(ConnectDeviceActivity.this, response, Toast.LENGTH_SHORT).show();
				
			}
			else
			{
				response = (String) result[1];
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
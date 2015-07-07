package com.hinj.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HingAppConstants;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.asynctask.DeviceInfoAsyncTask;
import com.hinj.parsing.HingAppParsing;

public class RegisterActivity extends Activity implements OnClickListener {

	EditText email_editText, username_editText, password_editText;
	LinearLayout register_LL;
	Button submit_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		email_editText = (EditText) findViewById(R.id.email_editText);
		username_editText = (EditText) findViewById(R.id.username_editText);
		password_editText = (EditText) findViewById(R.id.password_editText);

		register_LL = (LinearLayout) findViewById(R.id.register_LL);
		
		submit_btn =  (Button) findViewById(R.id.submit_btn);
		
		register_LL.setOnClickListener(this);
		submit_btn.setOnClickListener(this);
		
		/**
		 * Register For PushNotification
		 * */
		new MyPushRegister(RegisterActivity.this);

	}

	@Override
	public void onClick(View v) {
		if (v == register_LL) {
			String email = email_editText.getText().toString().trim();
			String username = username_editText.getText().toString().trim();
			String password = password_editText.getText().toString().trim();

			if (AppUtils.isEmptyString(email)) {
				String message = "Please enter email";
				AppUtils.showDialog(RegisterActivity.this, "" + message);
			} else if (AppUtils.isEmptyString(username)) {
				String message = "Please enter username";
				AppUtils.showDialog(RegisterActivity.this, "" + message);
			} else if (!AppUtils.isMatch(email, HingAppConstants.EMAIL_PATTERN)) {
				String message = "Please Enter A Valid E-mail";
				AppUtils.showDialog(RegisterActivity.this, "" + message);
			} else if (AppUtils.isEmptyString(password)) {
				String message = "Please enter password";
				AppUtils.showDialog(RegisterActivity.this, "" + message);
			} else if (password.length() < 6) {
				String message = "Password must be atleast of 6 characters";
				AppUtils.showDialog(RegisterActivity.this, "" + message);
			} else {
				RegisterAsynctask task = new RegisterAsynctask(email,username, password);
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

				Intent i = new Intent(RegisterActivity.this, DashBoardActivity.class);
				startActivity(i);
			}
		}
		if(v == submit_btn)
		{
			String email = email_editText.getText().toString().trim();
			String username = username_editText.getText().toString().trim();
			String password = password_editText.getText().toString().trim();

			if (AppUtils.isEmptyString(email)) {
				String message = "Please enter email";
				AppUtils.showDialog(RegisterActivity.this, "" + message);
			} else if (!AppUtils.isMatch(email, HingAppConstants.EMAIL_PATTERN)) {
				String message = "Please Enter A Valid E-mail";
				AppUtils.showDialog(RegisterActivity.this, "" + message);
			}else if (AppUtils.isEmptyString(username)) {
				String message = "Please enter username";
				AppUtils.showDialog(RegisterActivity.this, "" + message);
			}  else if (AppUtils.isEmptyString(password)) {
				String message = "Please enter password";
				AppUtils.showDialog(RegisterActivity.this, "" + message);
			} else if (password.length() < 6) {
				String message = "Password must be atleast of 6 characters";
				AppUtils.showDialog(RegisterActivity.this, "" + message);
			} else {
				RegisterAsynctask task = new RegisterAsynctask(email,username, password);
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		}
	}
	
	public class RegisterAsynctask extends AsyncTask<String, String,Object[]> {
		ProgressDialog progressDialog ;
		String response = "";
		String emailStr,username,passwordStr;
		String retrieveSchedule;
		
		public RegisterAsynctask(String emailStr,String username,String passwordStr) {
			this.emailStr = emailStr;
			this.username = username;
			this.passwordStr = passwordStr;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(RegisterActivity.this,"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.userRegister(RegisterActivity.this, emailStr, username,passwordStr,""); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try{
				System.out.println("result "+result);
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
					builder.setMessage(response)
					       .setCancelable(false)
					       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	   dialog.cancel();
					        	   
					        	   new LoginAsynctask(emailStr,passwordStr).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					        	 
	                              /*  Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
	                                startActivity(intent);
	                                finish();*/
					           }
					       });
					AlertDialog alert = builder.create();
					alert.setCancelable(false);
					alert.show();
					
				}
				else
				{
					response = (String) result[1];
					AppUtils.showDialog(RegisterActivity.this, response);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public class LoginAsynctask extends AsyncTask<String, String,Object[]> {
		ProgressDialog progressDialog ;
		String response = "";
		String emailStr,passwordStr;
		
		public LoginAsynctask(String emailStr,String passwordStr) {
			this.emailStr = emailStr;
			this.passwordStr = passwordStr;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(RegisterActivity.this,"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.userLogin(RegisterActivity.this, emailStr, passwordStr); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try{
				System.out.println("result "+result);
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
					
					HinjAppPreferenceUtils.setPreferenceLoginStatus(RegisterActivity.this, "true");
					
					/**
					 * Register For PushNotification
					 * */
					new MyPushRegister(RegisterActivity.this);
					
					//new AfterOK().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					
					Intent intent = new Intent(RegisterActivity.this,BarcodeScanActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
				
				}
				else
				{
					response = (String) result[1];
					AppUtils.showDialog(RegisterActivity.this, response);
				}	
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void childDeviceStorage() {
	    String grtAvailableInternalMemory, getTotalInternalMemorySize, getAvailableExternalMemorySize, getTotalExternalMemorySize;
		grtAvailableInternalMemory= DeviceInfoAsyncTask.getAvailableInternalMemorySize();
		getTotalInternalMemorySize= DeviceInfoAsyncTask.getTotalInternalMemorySize();
		getAvailableExternalMemorySize= DeviceInfoAsyncTask.getAvailableExternalMemorySize();
		getTotalExternalMemorySize= DeviceInfoAsyncTask.getTotalExternalMemorySize();
		
		String totalInternalMemorySize = getTotalInternalMemorySize;
		String[] totalInternalMemorySize_splited = totalInternalMemorySize.split(" ");
		
		String availableInternalMemory = grtAvailableInternalMemory;
		String[] InternalMemorySize_splited = availableInternalMemory.split(" ");
        
		int internal = (Integer.parseInt(InternalMemorySize_splited[0])*100)/Integer.parseInt(totalInternalMemorySize_splited[0]);
		
		String totalExternalMemorySize = getTotalExternalMemorySize;
		String[] totalExternalMemorySize_splited = totalExternalMemorySize.split(" ");
		
		String availableExternalMemorySize = getAvailableExternalMemorySize;
		String[] availableExternalMemorySize_splited = availableExternalMemorySize.split(" ");
		
		int external = (Integer.parseInt(availableExternalMemorySize_splited[0])*100)/Integer.parseInt(totalExternalMemorySize_splited[0]);
		
		
		new StorageAsynctask(String.valueOf(internal), String.valueOf(external)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	public class StorageAsynctask extends AsyncTask<String, String,Object[]> {
		ProgressDialog progressDialog ;
		String response = "";
		String in,ex;

		public StorageAsynctask(String internal, String external) {
			in = internal;
			ex = external;
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.userStorage(RegisterActivity.this, HinjAppPreferenceUtils.getRaId(RegisterActivity.this)
					,HinjAppPreferenceUtils.getUploadDeviceID(RegisterActivity.this),in , ex); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			try{
				System.out.println("result "+result);
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

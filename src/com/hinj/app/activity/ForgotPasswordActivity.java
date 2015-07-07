package com.hinj.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HingAppConstants;
import com.hinj.parsing.HingAppParsing;

public class ForgotPasswordActivity extends Activity implements OnClickListener {

	private EditText email_editText;
	private Button submit_btn,cancel_btn;
	String email ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_forgot_password);

		email_editText = (EditText) findViewById(R.id.email_editText);
		
		submit_btn = (Button) findViewById(R.id.submit_btn);
		cancel_btn = (Button) findViewById(R.id.cancel_btn);
		
		submit_btn.setOnClickListener(this);
		cancel_btn.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {

		if(v == submit_btn)
		{
			String email = email_editText.getText().toString().trim();

			if(AppUtils.isEmptyString(email))
			{
				String message=	"Please enter email";
				AppUtils.showDialog(ForgotPasswordActivity.this, ""+message);
			}
			else if(!AppUtils.isMatch(email, HingAppConstants.EMAIL_PATTERN))
			{
				String message = "Please Enter A Valid E-mail";
				AppUtils.showDialog(ForgotPasswordActivity.this, ""+message);
			//	email_editText.setText("");
			}
			else
			{
				ForgotPasswordAsynctask forgotPasswordAsynctask = new ForgotPasswordAsynctask(email);
				forgotPasswordAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		}
		
		if(v == cancel_btn)
		{
			finish();
		}
	}
	

	public class ForgotPasswordAsynctask extends AsyncTask<String, String,Object[]> {
		ProgressDialog progressDialog ;
		String response = "";
		String emailStr;
		
		public ForgotPasswordAsynctask(String emailStr) {
			this.emailStr = emailStr;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(ForgotPasswordActivity.this,"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.forgotPassword(ForgotPasswordActivity.this, emailStr); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			
			progressDialog.cancel();
			try{
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					Toast.makeText(ForgotPasswordActivity.this, response, Toast.LENGTH_SHORT).show();
					ForgotPasswordActivity.this.finish();
					hideKeyboard();
					//onBackPressed();
					//ForgotPasswordActivity.this.finish();
					
					
					
				}
				else
				{
					response = (String) result[1];
					AppUtils.showDialog(ForgotPasswordActivity.this, response);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	private void hideKeyboard() {   
	    // Check if no view has focus:
	    View view = this.getCurrentFocus();
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}

}

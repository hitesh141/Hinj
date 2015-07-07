package com.hinj.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.facebook.UiLifecycleHelper;
import com.hinj.app.social.FacebookSignInHelper;
import com.hinj.app.social.FacebookSignInReceiver;
import com.hinj.app.social.GooglePlusSignInHelper;
import com.hinj.app.social.GooglePlusSignInReceiver;
import com.hinj.app.twitter.TwitterApp;
import com.hinj.app.twitter.TwitterApp.TwDialogListener;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.RequestPost;
import com.hinj.asynctask.DeviceInfoAsyncTask;
import com.hinj.parsing.HingAppParsing;

public class LoginActivity extends Activity implements OnClickListener,FacebookSignInReceiver,GooglePlusSignInReceiver   {

	private TextView forgetPassword_textView;
	private LinearLayout login_LL,google_login_btn,facebook_login_btn,twitter_login_btn;
	private EditText email_editText, password_editText;
	private LinearLayout skip_LL, register_LL;

	//	twitter
	public TwitterApp mTwitter;
	public String twresult = "fail";
	
	private static final String TAG = "SignInTestActivity";
	String email ;

	// A progress dialog to display when the user is connecting in
	// case there is a delay in any of the dialogs being ready.
	private ProgressDialog mConnectionProgressDialog;
	
	// Facebook Declarations
	private UiLifecycleHelper uiHelper;
	String facebookEmailId ,facebookID ;

	private static final int RC_SIGN_IN = 0;

    private GooglePlusSignInHelper googlePlusSignInHelper;
    
    RequestPost RequestPostClass;
    private String getUpdateResponce;
    AlertDialog.Builder alertbox1;
    
    private static final int EX_FILE_PICKER_RESULT = 0;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		forgetPassword_textView = (TextView) findViewById(R.id.forgetPassword_textView);
		skip_LL = (LinearLayout) findViewById(R.id.skip_LL);
		register_LL = (LinearLayout) findViewById(R.id.register_LL);
		
		login_LL = (LinearLayout) findViewById(R.id.login_LL);
		google_login_btn = (LinearLayout) findViewById(R.id.google_login_btn);
		facebook_login_btn = (LinearLayout) findViewById(R.id.facebook_login_btn);
		twitter_login_btn = (LinearLayout) findViewById(R.id.twitter_login_btn);
		
		email_editText = (EditText) findViewById(R.id.email_editText);
		password_editText = (EditText) findViewById(R.id.password_editText);
		
		login_LL.setOnClickListener(this);
		google_login_btn.setOnClickListener(this);
		facebook_login_btn.setOnClickListener(this);
		twitter_login_btn.setOnClickListener(this);
		
		forgetPassword_textView.setOnClickListener(this);
		skip_LL.setOnClickListener(this);
		register_LL.setOnClickListener(this);

		/**
		 * Facebook UI class
		 */
		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);

		mConnectionProgressDialog = new ProgressDialog(this);
	    mConnectionProgressDialog.setTitle("Login");
	    mConnectionProgressDialog.setMessage("Please wait...");

	    googlePlusSignInHelper = new GooglePlusSignInHelper(this, this);
	    
	    password_editText.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					
					new MyPushRegister(LoginActivity.this);

					String email=email_editText.getText().toString().trim();
					String password=password_editText.getText().toString().trim();

					if(AppUtils.isEmptyString(email))
					{
						String message=	"Please enter email";
						AppUtils.showDialog(LoginActivity.this, ""+ message);
					}
					/*else if(!AppUtils.isMatch(email, HingAppConstants.EMAIL_PATTERN))
					{
						String message = "Please enter a valid e-mail";
						AppUtils.showDialog(LoginActivity.this, ""+message);
					//	email_editText.setText("");
					}*/
					else if(AppUtils.isEmptyString(password))
					{
						String message=	"Please enter password";
						AppUtils.showDialog(LoginActivity.this, ""+message);
					}
					else if (password.length() < 6) {
						String message = "Password must be atleast of 6 characters";
						AppUtils.showDialog(LoginActivity.this, "" + message);
					}
					else
					{
						LoginAsynctask task=new LoginAsynctask(email,password);
						task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}	
				} 
				return false;    
			}
	    });
	}

	@Override
	public void onClick(View v) {

		if (v == login_LL) {
			new MyPushRegister(LoginActivity.this);
			String email=email_editText.getText().toString().trim();
			String password=password_editText.getText().toString().trim();

			if(AppUtils.isEmptyString(email))
			{
				String message=	"Please enter email";
				AppUtils.showDialog(LoginActivity.this, ""+message);
			}
		/*	else if(!AppUtils.isMatch(email, HingAppConstants.EMAIL_PATTERN))
			{
				String message = "Please enter a valid e-mail";
				AppUtils.showDialog(LoginActivity.this, ""+message);
			//	email_editText.setText("");
			}*/
			else if(AppUtils.isEmptyString(password))
			{
				String message=	"Please enter password";
				AppUtils.showDialog(LoginActivity.this, ""+message);
			}
			else if (password.length() < 6) {
				String message = "Password must be atleast of 6 characters";
				AppUtils.showDialog(LoginActivity.this, "" + message);
			}
			else
			{
				LoginAsynctask task=new LoginAsynctask(email,password);
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}	
		}
		
		if (v == google_login_btn) {
			new MyPushRegister(LoginActivity.this);
			 mConnectionProgressDialog.show();
	            if (!AppUtils.isNetworkAvailable(LoginActivity.this)) {
	            	AppUtils.showDialog(LoginActivity.this, "No internet connection available.");
	            } else {
	                googlePlusSignInHelper.signInWithGplus();
	            }
		}
		
		if (v == facebook_login_btn) {
			new MyPushRegister(LoginActivity.this);
			 FacebookSignInHelper facebookSignInHelper = new FacebookSignInHelper(this, this);
	         facebookSignInHelper.startLogin();
		}
		
		if (v == twitter_login_btn) {
			new MyPushRegister(LoginActivity.this);
			mTwitter = new TwitterApp(LoginActivity.this, AppUtils.TWITTER_CONSUMER_KEY, AppUtils.TWITTER_CONSUMER_SECRET);
			   mTwitter.setListener(mTwLoginDialogListenerforPost);
		        mTwitter.resetAccessToken();
		        if (mTwitter.hasAccessToken() == true) {
		        	//System.out.println("mTwitter user id  "+  mTwitter.getUserid());
		            mTwitter.resetAccessToken();
		        } else {
		            mTwitter.authorize();
		        }
		}
		
		if (v == forgetPassword_textView) {
			
			Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
			startActivity(intent);
			
			/*String email = email_editText.getText().toString().trim();

			if(AppUtils.isEmptyString(email))
			{
				String message=	"Please enter email";
				AppUtils.showDialog(LoginActivity.this, ""+message);
			}
			else if(!AppUtils.isMatch(email, HingAppConstants.EMAIL_PATTERN))
			{
				String message = "Please Enter A Valid E-mail";
				AppUtils.showDialog(LoginActivity.this, ""+message);
			//	email_editText.setText("");
			}
			else
			{
				ForgotPasswordAsynctask forgotPasswordAsynctask = new ForgotPasswordAsynctask(email);
				forgotPasswordAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}*/
		}
		
		if (v == skip_LL) {
			
			Intent i = new Intent(LoginActivity.this, BarcodeScanActivity.class);
			startActivity(i);
			finish();
		}
		
		if(v == register_LL)
		{
			Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(i);
			finish();
		}
	}
	
	public void finishFacebookLogin(String FB_Id, String FB_EmailId, String fbName, String first_name, String last_name, String stateName) {
	      
	//	Toast.makeText(LoginActivity.this, "FB_Id user id   is  :"+ FB_Id   +"FB_EmailId     :     "+ FB_EmailId, Toast.LENGTH_SHORT).show();
		
		//facebook aync task
		FacebookLoginAsynctask asynctask = new FacebookLoginAsynctask(FB_Id,FB_EmailId);
		asynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	
	}
	 
	@Override
	public void finishGoogleLogin(String id, String email, String personName) {

	//	Toast.makeText(LoginActivity.this, "google plus user id   is  :"+ id + "email   :"+email, Toast.LENGTH_SHORT).show();
		GoogleLoginAsynctask asynctask = new GoogleLoginAsynctask(id,email);
		asynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	public TwDialogListener mTwLoginDialogListenerforPost = new TwDialogListener() {
		@Override
		public void onError(String value) {
			//Log.e("TWITTER", value);
			mTwitter.resetAccessToken();
		}

		@Override
		public void onComplete(String value) {
			//System.out.println("mTwitter user id  "+  mTwitter.getUserid());

			//Toast.makeText(LoginActivity.this, "mTwitter user id   is  :"+ mTwitter.getUserid(), Toast.LENGTH_SHORT).show();
			
			//twitter aync task
			TwitterLoginAsynctask asynctask = new TwitterLoginAsynctask(mTwitter.getUserid());
			asynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// facebook
		uiHelper.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == RC_SIGN_IN) {
            googlePlusSignInHelper.handleActivityResult(requestCode, resultCode, intent);
        }
		
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
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
			progressDialog = ProgressDialog.show(LoginActivity.this,"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.userLogin(LoginActivity.this, emailStr, passwordStr); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try
			{
				System.out.println("result "+result);
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
					
					HinjAppPreferenceUtils.setPreferenceLoginStatus(LoginActivity.this, "true");
					
					/**
					 * Register For PushNotification
					 * */
					new MyPushRegister(LoginActivity.this);
					
					//new AfterOK().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					
					Intent intent = new Intent(LoginActivity.this,BarcodeScanActivity.class);
                    startActivity(intent);
                    finish();
                    childDeviceStorage();
				
				}
				else
				{
					response = (String) result[1];
					AppUtils.showDialog(LoginActivity.this, response);
				}	
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class FacebookLoginAsynctask extends AsyncTask<String, String,Object[]> {
		ProgressDialog progressDialog ;
		String response = "";
		String facebook_id,facebookEmail;
		
		public FacebookLoginAsynctask(String facebook_id,String facebookEmail) {
			this.facebook_id = facebook_id;
			this.facebookEmail = facebookEmail;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(LoginActivity.this,"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.userFacebookLogin(LoginActivity.this, facebookEmail, facebook_id); 
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
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
					
					HinjAppPreferenceUtils.setPreferenceLoginStatus(LoginActivity.this, "true");
					
					/**
					 * Register For PushNotification
					 * */
					new MyPushRegister(LoginActivity.this);
					
					//new AfterOK().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					
					Intent intent = new Intent(LoginActivity.this,BarcodeScanActivity.class);
                    startActivity(intent);
                    finish();
				}
				else
				{
					response = (String) result[1];
					AppUtils.showDialog(LoginActivity.this, response);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public class GoogleLoginAsynctask extends AsyncTask<String, String,Object[]> {
		ProgressDialog progressDialog ;
		String response = "";
		String emailStr,googleId;
		
		public GoogleLoginAsynctask(String googleId,String emailStr) {
			this.emailStr = emailStr;
			this.googleId = googleId;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(LoginActivity.this,"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.userGoogleLogin(LoginActivity.this, emailStr, googleId); 
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
					Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
					
					HinjAppPreferenceUtils.setPreferenceLoginStatus(LoginActivity.this, "true");
					
					/**
					 * Register For PushNotification
					 * */
					new MyPushRegister(LoginActivity.this);
					
					//new AfterOK().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					
					Intent intent = new Intent(LoginActivity.this,BarcodeScanActivity.class);
                    startActivity(intent);
                    finish();
				}
				else
				{
					response = (String) result[1];
					AppUtils.showDialog(LoginActivity.this, response);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public class TwitterLoginAsynctask extends AsyncTask<String, String,Object[]> {
		ProgressDialog progressDialog ;
		String response = "";
		String twitterId;
		
		public TwitterLoginAsynctask(String twitterId) {
			this.twitterId = twitterId;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(LoginActivity.this,"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.userTwitterLogin(LoginActivity.this, twitterId); 
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
					Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
					
					HinjAppPreferenceUtils.setPreferenceLoginStatus(LoginActivity.this, "true");
					
					/**
					 * Register For PushNotification
					 * */
					new MyPushRegister(LoginActivity.this);
					
					//new AfterOK().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					
					Intent intent = new Intent(LoginActivity.this,BarcodeScanActivity.class);
                    startActivity(intent);
                    finish();
					
				}
				else
				{
					response = (String) result[1];
					AppUtils.showDialog(LoginActivity.this, response);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
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
			progressDialog = ProgressDialog.show(LoginActivity.this,"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.forgotPassword(LoginActivity.this, emailStr); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			
			progressDialog.cancel();
			try{
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
				}
				else
				{
					response = (String) result[1];
					AppUtils.showDialog(LoginActivity.this, response);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		googlePlusSignInHelper.handleOnStart();
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
			
			return HingAppParsing.userStorage(LoginActivity.this, HinjAppPreferenceUtils.getRaId(LoginActivity.this)
					,HinjAppPreferenceUtils.getUploadDeviceID(LoginActivity.this),in , ex); 
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
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "Stop");
		
		googlePlusSignInHelper.handleOnStop();

	    mConnectionProgressDialog.dismiss();
	    uiHelper.onDestroy();
	}
}

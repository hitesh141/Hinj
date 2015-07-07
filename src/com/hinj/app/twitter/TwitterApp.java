/** 
* Copyright OCTAL INFO SOLUTIONS PVT LTD.
 * 
 * May 16 6:15:24 PM
 * 
 * @author kamalkant
 * 
 * Name : TwitterApp
 * 
 * Description : This class use for the twitter, this class download from twitter.
 * 
 * Limitations : Mobile phone should have Internet.
 * 
 */

package com.hinj.app.twitter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import twitter4j.IDs;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;


public class TwitterApp {
	private Twitter mTwitter;
//	private FriendsFollowersResources mTwitterFollowers;

	private TwitterSession mSession;
	private AccessToken mAccessToken;
	private CommonsHttpOAuthConsumer mHttpOauthConsumer;
	private OAuthProvider mHttpOauthprovider;
	private String mConsumerKey;
	private String mSecretKey;
	private ProgressDialog mProgressDlg;
	private TwDialogListener mListener;
	private Activity context;
	

	public static final String  CALLBACK_URL = "twitterapp://connect";
	private static final String TWITTER_ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
	private static final String TWITTER_AUTHORZE_URL = "https://api.twitter.com/oauth/authorize";
	private static final String TWITTER_REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	public static final String MESSAGE = "Hello Everyone....";
	SharedPreferences myPrefs;

    public static String twitterId = "";

	public TwitterApp(Activity context, String consumerKey, String secretKey)
	{
		this.context = context;

		/**
		 * Get data from preference 
		 */
		myPrefs = PreferenceManager.getDefaultSharedPreferences(this.context);
		
		mTwitter = new TwitterFactory().getInstance();
		mSession = new TwitterSession(context);
		mProgressDlg = new ProgressDialog(context);
		mProgressDlg.setCancelable(false);

		
		mProgressDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mConsumerKey = consumerKey;
		mSecretKey = secretKey;

		mHttpOauthConsumer = new CommonsHttpOAuthConsumer(mConsumerKey,mSecretKey);
		 
		String request_url=TWITTER_REQUEST_URL;
		String access_token_url=TWITTER_ACCESS_TOKEN_URL;
		String authorize_url=TWITTER_AUTHORZE_URL;
		
		mHttpOauthprovider = new DefaultOAuthProvider(
				request_url,
				access_token_url,
				authorize_url);
		mAccessToken = mSession.getAccessToken();
		

		configureToken();
	}

	public void setListener(TwDialogListener listener) {
		mListener = listener;
	}

	@SuppressWarnings("deprecation")
	private void configureToken() {
		if (mAccessToken != null) {
			mTwitter.setOAuthConsumer(mConsumerKey, mSecretKey);
			mTwitter.setOAuthAccessToken(mAccessToken);
		}
	}

	public boolean hasAccessToken()
	{
		return (mAccessToken == null) ? false : true;
	}

	public void resetAccessToken() {
		if (mAccessToken != null) {
			mSession.resetAccessToken();
			mAccessToken = null;
		}
	}

	public String getUsername()
	{
		return mSession.getUsername();

	}
    public String getUserid()
    {
        return mSession.getUserid();

    }
	public int getFollowersCount()
	{
		try 
		{
			IDs arr=mTwitter.getFollowersIDs(100);
			long[] array=arr.getIDs();
			Log.i("the count", array.length+"");
			return array.length;
			
		}
		catch (TwitterException e) 
		{
			return 0;
		}
		
	}

	public void updateStatus(String status,String xx) throws Exception {
		try 
		{
			System.out.println("Image url: "+xx);
			
			File Imagedirectory = new File(xx);
			StatusUpdate status1 = new StatusUpdate(status);
			status1.setMedia(Imagedirectory);	       
			mTwitter.updateStatus(status1);
		}
		 catch (TwitterException e) 
		{
			e.printStackTrace();
		}
	}

	public void authorize() 
	{
		
		if(!mProgressDlg.isShowing())
		{
		
		mProgressDlg.setMessage("Loading..");
		mProgressDlg.show();
		}

		new Thread() {
			@Override
			public void run() {
				String authUrl = "";
				int what = 1;

				try {
					authUrl = mHttpOauthprovider.retrieveRequestToken(mHttpOauthConsumer, CALLBACK_URL);
					what = 0;
				} catch (Exception e) {
					e.printStackTrace();
				}
				mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0, authUrl));
			}
		}.start();
	}

	public void processToken(String callbackUrl)
	{
		
		if(!mProgressDlg.isShowing())
		{
		mProgressDlg.setMessage("Loading..");
		mProgressDlg.show();
		}

		final String verifier = getVerifier(callbackUrl);

		new Thread() {
			@Override
			public void run() {
				int what = 1;

				try {
					mHttpOauthprovider.retrieveAccessToken(mHttpOauthConsumer,verifier);
					mAccessToken = new AccessToken(
							mHttpOauthConsumer.getToken(),
							mHttpOauthConsumer.getTokenSecret());

					configureToken();

					User user = mTwitter.verifyCredentials();
					System.out.println("user twitter "+ user.toString());
					
					mSession.storeAccessToken(mAccessToken, user.getName(), ""+user.getId());

					Log.d("twitterId",""+user.getId());
                    twitterId =""+ user.getId();
					what = 0;
				} catch (Exception e) {
					e.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			}
		}.start();
	}

	
	private String getVerifier(String callbackUrl) {
		String verifier = "";

		try {
			callbackUrl = callbackUrl.replace("twitterapp", "http");

			URL url = new URL(callbackUrl);
			String query = url.getQuery();

			String array[] = query.split("&");

			for(String parameter:array) 
			{
				String v[] = parameter.split("=");

				if (URLDecoder.decode(v[0]).equals(oauth.signpost.OAuth.OAUTH_VERIFIER)) 
				{
					verifier = URLDecoder.decode(v[1]);
					break;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return verifier;
	}

	private void showLoginDialog(String url) {
		final TwDialogListener listener = new TwDialogListener() {

			public void onComplete(String value) {
				processToken(value);
			}

			public void onError(String value) {
				mListener.onError("Failed opening authorization page");
			}
		};

		new TwitterDialog(context, url, listener).show();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) 
		{
			mProgressDlg.dismiss();

			if (msg.what == 1) {
				if (msg.arg1 == 1)
					mListener.onError("Error getting request token");
				else
					mListener.onError("Error getting access token");
			} else {
				if (msg.arg1 == 1)
					showLoginDialog((String) msg.obj);
				else
					mListener.onComplete("");
			}
		}
	};

	public interface TwDialogListener {
		public void onComplete(String value);

		public void onError(String value);
	}
}
/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD. 
 */

package com.hinj.app.activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.PushNotificationUtils;

@SuppressLint("Wakelock")
public class GCMIntentService extends GCMBaseIntentService{

	public static final String ACTION_NOTIFICATION_PLAY_PAUSE = "action_notification_play_pause";
	public static final String ACTION_NOTIFICATION_FAST_FORWARD = "action_notification_fast_forward";
	public static final String ACTION_NOTIFICATION_REWIND = "action_notification_rewind";
	private boolean mIsPlaying = false;
	
	public Context context;
	
	public GCMIntentService(){
		super(PushNotificationUtils.GCM_SENDERID);
	}

	@Override
	protected void onError(Context context, String regId) {
		Log.e("", "error registration id : "+regId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		handleMessage(context, intent);
		//such();
		//startNotification();
		//showNotification(true);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		//		Log.e("", "registration id : "+regId);
		this.context = context;
		handleRegistration(context, regId);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {

	}

	@SuppressWarnings({ "deprecation", "deprecation" })
	private void handleMessage(final Context context, Intent intent) {

		PushNotificationUtils.notiMsg 	= 	intent.getStringExtra("message");
		PushNotificationUtils.notiTitle = 	intent.getStringExtra("title");
		PushNotificationUtils.notiType 	= 	intent.getStringExtra("type");
		PushNotificationUtils.notiUrl 	= 	intent.getStringExtra("url");
		PushNotificationUtils.parent_device_id 	= 	intent.getStringExtra("parent_device_id");
		PushNotificationUtils.notiTitle	=	getResources().getString(R.string.app_name);
		
		PushNotificationUtils.getRegistrDeviceName = intent.getStringExtra("registerDeviceName");

		int icon = 0;
		long notificationTime 		= 0;
		CharSequence tickerText 	= "";
		CharSequence contentTitle 	= "";

		
		
		if (HinjAppPreferenceUtils.getNotifcationIcon(context).equalsIgnoreCase("ON")) {
			icon		= R.drawable.logo;  
		}
		tickerText 	= PushNotificationUtils.notiTitle;   

		notificationTime	= System.currentTimeMillis(); 
		contentTitle 		= PushNotificationUtils.notiTitle; 

		NotificationManager notificationManager =	(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification;
		
		if (HinjAppPreferenceUtils.getNotifcationIcon(context).equalsIgnoreCase("ON")) {
			notification 				= new Notification(icon, tickerText, notificationTime);
		}else {
			notification 				= new Notification(0, tickerText, notificationTime);
		}
		
		Intent notificationIntent = new Intent(context, DashBoardActivity.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, PushNotificationUtils.notiMsg, pendingIntent);
		notification.flags	|=	notification.FLAG_AUTO_CANCEL;

		/**
		 * sound on off by web service 
		 * */
		
		if (HinjAppPreferenceUtils.getNotifcationSound(context).equalsIgnoreCase("ON")) {
			notification.defaults |= Notification.DEFAULT_SOUND;
		}

		/**
		 * vibration 
		 * */
		notification.vibrate=new long[] {100L, 100L, 200L, 500L};
		notificationManager.notify(1, notification);

		if(PushNotificationUtils.notiMsg.contains("wants authorization"))
		{
			Intent intent2 = new Intent(getApplicationContext(),ShowConnectedDevice.class);
			//intent2.putExtra("message", PushNotificationUtils.notiMsg);
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent2);
		}
		/*else if(PushNotificationUtils.notiMsg.contains("Yes"))
		{
			Intent intent3 = new Intent(context,DashBoardActivity.class);
			intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent3);
			
			//showMessage("You are successfully authorised.");
			new Handler(Looper.getMainLooper()).post(new Runnable(){

				@Override
				public void run() {
					 Toast.makeText(context, "You are successfully authorised." , Toast.LENGTH_SHORT).show();
				}
			});
		}
		else if(PushNotificationUtils.notiMsg.contains("No"))
		{
			//showMessage("You are not successfully authorised.");
			new Handler(Looper.getMainLooper()).post(new Runnable(){

				@Override
				public void run() {
					 Toast.makeText(context, "You are not successfully authorised." , Toast.LENGTH_SHORT).show();
				}
			});
		}*/
		
		PushNotificationUtils.notificationReceived=true;
		PowerManager pm 	= (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		WakeLock wl 		= pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
		wl.acquire();
	}
	
	private void showMessage(final String msg) {
		new Handler(Looper.getMainLooper()).post(new Runnable(){

			@Override
			public void run() {
				 Toast.makeText(context, msg , Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void startNotification(){
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = 
                (NotificationManager) getSystemService(ns);

        Notification notification = new Notification(R.drawable.ic_launcher, null, 
                System.currentTimeMillis());

        RemoteViews notificationView = new RemoteViews(getPackageName(),
                R.layout.custom_notofication);

        //the intent that is started when the notification is clicked (works)
        Intent notificationIntent = new Intent(this, DashBoardActivity.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, 
                notificationIntent, 0);

        notification.contentView = notificationView;
        notification.contentIntent = pendingNotificationIntent;
        notification.flags |= Notification.FLAG_NO_CLEAR;

        //this is the intent that is supposed to be called when the 
        //button is clicked
        Intent switchIntent = new Intent(this, LoginActivity.class);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 0,switchIntent, 0);

        notificationView.setOnClickPendingIntent(R.id.disconnect_button,pendingSwitchIntent);

        notificationManager.notify(1, notification);
    }
    
    private void showNotification( boolean isPlaying ) {
		Notification notification = new NotificationCompat.Builder( getApplicationContext() )
				.setAutoCancel( true )
				.setSmallIcon( R.drawable.logo )
				.setContentTitle( getString( R.string.app_name ) )
				.build();

		if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
			notification.bigContentView = getExpandedView( isPlaying );

		NotificationManager manager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
		manager.notify( 1, notification );
	}
    
	private RemoteViews getExpandedView( boolean isPlaying ) {
		RemoteViews customView = new RemoteViews( getPackageName(), R.layout.custom_notofication );

		customView.setImageViewResource( R.id.button_yes, R.drawable.ic_launcher );
		customView.setImageViewResource( R.id.button_no, R.drawable.ic_launcher );

		if( isPlaying )
			customView.setImageViewResource( R.id.button_yes, R.drawable.logo );
		else
			customView.setImageViewResource( R.id.button_no, R.drawable.logo );

		customView.setImageViewResource( R.id.button_yes, R.drawable.logo );

		Intent intent = new Intent( getApplicationContext(), DashBoardActivity.class );
		
		intent.setAction( ACTION_NOTIFICATION_PLAY_PAUSE );
		PendingIntent pendingIntent = PendingIntent.getService( getApplicationContext(), 1, intent, 0 );
		customView.setOnClickPendingIntent( R.id.button_yes, pendingIntent );

		intent.setAction( ACTION_NOTIFICATION_FAST_FORWARD );
		pendingIntent = PendingIntent.getService( getApplicationContext(), 1, intent, 0 );
		customView.setOnClickPendingIntent( R.id.button_no, pendingIntent );

		return customView;
	}
	
	private void handleRegistration(Context context, String regId) {
		
		PushNotificationUtils.registrationId = regId;
		Log.e("", "registration id : "+regId);
	}
}

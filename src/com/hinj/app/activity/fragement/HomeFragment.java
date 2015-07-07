package com.hinj.app.activity.fragement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hinj.app.activity.ConnectDeviceActivity;
import com.hinj.app.activity.DashBoardActivity;
import com.hinj.app.activity.R;
import com.hinj.app.model.ContedChilDeviceDetailBean;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.asynctask.DeviceInfoAsyncTask;
import com.hinj.parsing.HingAppParsing;

public class HomeFragment extends Fragment {
	
	public HomeFragment(){}
	
	private TextView contactsCount_textView,appCount_textView,musicCount_textView, internal_storage, external_storage,
					 videoCount_textView, photoCount_textView,messagesCount_textView,device_model_textView;
	private ImageView refresh;
	private String grtAvailableInternalMemory, getTotalInternalMemorySize, getAvailableExternalMemorySize, getTotalExternalMemorySize;
	private ProgressBar mprogressBar, progressBar2;
	private LinearLayout contact_lay, app_lay, music_lay, videos_lay, photos_lay, messages_lay;
	
	ContedChilDeviceDetailBean mContedChilDeviceDetailBean;
	private ArrayList<ContedChilDeviceDetailBean> mmContedChilDeviceDetailBean = new ArrayList<ContedChilDeviceDetailBean>();
	
	Timer timer;
	MyTimerTask myTimerTask;
	 
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.activity_device_details, container, false);
        
        contactsCount_textView 	= (TextView) rootView.findViewById(R.id.contactsCount_textView);
        appCount_textView 		= (TextView) rootView.findViewById(R.id.appCount_textView);
        musicCount_textView 	= (TextView) rootView.findViewById(R.id.musicCount_textView);
        videoCount_textView 	= (TextView) rootView.findViewById(R.id.videoCount_textView);
        photoCount_textView 	= (TextView) rootView.findViewById(R.id.photoCount_textView);
        messagesCount_textView 	= (TextView) rootView.findViewById(R.id.messagesCount_textView);
        
        contact_lay 	= (LinearLayout) rootView.findViewById(R.id.contact_lay);
        app_lay 	= (LinearLayout) rootView.findViewById(R.id.app_lay);
        music_lay 	= (LinearLayout) rootView.findViewById(R.id.music_lay);
        videos_lay 	= (LinearLayout) rootView.findViewById(R.id.videos_lay);
        photos_lay 	= (LinearLayout) rootView.findViewById(R.id.photos_lay);
        messages_lay 	= (LinearLayout) rootView.findViewById(R.id.messages_lay);
        
        device_model_textView	= (TextView) rootView.findViewById(R.id.device_model_textView);
        
        internal_storage		= (TextView) rootView.findViewById(R.id.internal_storage);
        external_storage		= (TextView) rootView.findViewById(R.id.external_storage);
        refresh					= (ImageView) rootView.findViewById(R.id.refresh);
        mprogressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        
        setOnClickListener();
        
    	grtAvailableInternalMemory= DeviceInfoAsyncTask.getAvailableInternalMemorySize();
		getTotalInternalMemorySize= DeviceInfoAsyncTask.getTotalInternalMemorySize();
		getAvailableExternalMemorySize= DeviceInfoAsyncTask.getAvailableExternalMemorySize();
		getTotalExternalMemorySize= DeviceInfoAsyncTask.getTotalExternalMemorySize();
		
		internal_storage.setText("Internal Storage (Available "+grtAvailableInternalMemory+"/"+getTotalInternalMemorySize+")");
		external_storage.setText("SD Card (Available "+getAvailableExternalMemorySize+"/"+getTotalExternalMemorySize+")");
		
		String totalInternalMemorySize = getTotalInternalMemorySize;
		String[] totalInternalMemorySize_splited = totalInternalMemorySize.split(" ");
		
		String availableInternalMemory = grtAvailableInternalMemory;
		String[] InternalMemorySize_splited = availableInternalMemory.split(" ");
        
		mprogressBar.setProgress(ConnectDeviceActivity.internal);
		
		String totalExternalMemorySize = getTotalExternalMemorySize;
		String[] totalExternalMemorySize_splited = totalExternalMemorySize.split(" ");
		
		String availableExternalMemorySize = getAvailableExternalMemorySize;
		String[] availableExternalMemorySize_splited = availableExternalMemorySize.split(" ");
		
		progressBar2.setProgress(ConnectDeviceActivity.external);
		
		timer = new Timer();
	    myTimerTask = new MyTimerTask();
	    
        return rootView;
    }
	
	@Override
	public void onResume() {
		super.onResume();
		
		initGUI();
		
		getActivity().registerReceiver(updateCounts, new IntentFilter("com.hinj.app.activity.fragement.UPDATECOUNTINTENT")); 
	}
	
	@Override
	public void onPause() {

		super.onPause();
		getActivity().unregisterReceiver(updateCounts);
		
		 if (timer!=null){
		     timer.cancel();
		     timer = null;
		    }
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		
		
	        //singleshot delay 1000 ms
	      //  timer.schedule(myTimerTask, 1000);
	        //delay 1000ms, repeat in 5000ms
	        timer.schedule(myTimerTask, 1000, 30 * 1000);
	    
	}

	private void setOnClickListener() {
		refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				initGUI();
			}
		});
		contact_lay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ContactsFragment mContactsFragment = new ContactsFragment();
				Bundle bunldeObj = new Bundle();
				bunldeObj.putString("position", "4");
				DashBoardActivity.replaceFragementsClick(mContactsFragment, bunldeObj,"Contacts");
			}
		});
		
		app_lay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				InstallAppsFragment mInstallAppsFragment = new InstallAppsFragment();
				Bundle bunldeObj = new Bundle();
				bunldeObj.putString("position", "4");
				DashBoardActivity.replaceFragementsClick(mInstallAppsFragment, bunldeObj,"Apps");
			}
		});
		
		music_lay.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
				MusicFragment mMusicFragment = new MusicFragment();
				Bundle bunldeObj = new Bundle();
				bunldeObj.putString("position", "4");
				DashBoardActivity.replaceFragementsClick(mMusicFragment, bunldeObj,"Music");
			}
		});
		
		videos_lay.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
				VideosFragment mVideosFragment = new VideosFragment();
				Bundle bunldeObj = new Bundle();
				bunldeObj.putString("position", "4");
				DashBoardActivity.replaceFragementsClick(mVideosFragment, bunldeObj,"Videos");
			}
		});
		
		photos_lay.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
				PhotosFragment mPhotosFragment = new PhotosFragment();
				Bundle bunldeObj = new Bundle();
				bunldeObj.putString("position", "4");
				DashBoardActivity.replaceFragementsClick(mPhotosFragment, bunldeObj,"Photos");
			}
		});
		
		messages_lay.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
				MessageFragment mMessageFragment = new MessageFragment();
				Bundle bunldeObj = new Bundle();
				bunldeObj.putString("position", "4");
				DashBoardActivity.replaceFragementsClick(mMessageFragment, bunldeObj,"Messages");
			}
		});
	}

	private void initGUI() {

		contactsCount_textView.setText(HinjAppPreferenceUtils.getContactsCount(getActivity()));
		appCount_textView.setText(HinjAppPreferenceUtils.getAppsCount(getActivity()));
		musicCount_textView.setText(HinjAppPreferenceUtils.getMusicCount(getActivity()));
		videoCount_textView.setText(HinjAppPreferenceUtils.getvideosCount(getActivity()));
		photoCount_textView.setText(HinjAppPreferenceUtils.getPhotoCount(getActivity()));
		messagesCount_textView.setText(HinjAppPreferenceUtils.getMessagesCount(getActivity()));
		device_model_textView.setText(HinjAppPreferenceUtils.getModelName(getActivity()));
		
	}
	
	// handler for received Intents for the "my-event" event
	public BroadcastReceiver updateCounts = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			initGUI();
			// Extract data included in the Intent
			/*String first_name = intent.getStringExtra("first_name");
			String last_name = intent.getStringExtra("last_name");
			appDelegate.getmLoginData().setFirst_name(first_name);
			appDelegate.getmLoginData().setLast_name(last_name);
			
			txtviewUserName.setText(""+appDelegate.getmLoginData().getFirst_name()+" "+appDelegate.getmLoginData().getLast_name());*/
			/*
			contactsCount_textView.setText(HinjAppPreferenceUtils.getContactsCount(getActivity()));
			appCount_textView.setText(HinjAppPreferenceUtils.getAppsCount(getActivity()));
			musicCount_textView.setText(HinjAppPreferenceUtils.getMusicCount(getActivity()));
			videoCount_textView.setText(HinjAppPreferenceUtils.getvideosCount(getActivity()));
			photoCount_textView.setText(HinjAppPreferenceUtils.getPhotoCount(getActivity()));
			messagesCount_textView.setText(HinjAppPreferenceUtils.getMessagesCount(getActivity()));
			device_model_textView.setText(HinjAppPreferenceUtils.getModelName(getActivity()));*/
			
		}
	};
	
	
	  /*public class PostBarcodeScanResultWithouScanAsynctask extends AsyncTask<String, String,Object[]> {
			//ProgressDialog progressDialog ;
			String response = "";
			String childRaId;
			int pos_click;
			String device_id1;
			
			@Override
			public void onPreExecute() {
				//mProgressDialog = ProgressDialog.show(ConnectDeviceActivity.this,"Loading...", "Please Wait");
			}

			@Override
			public Object[] doInBackground(String... params) {
				
				return HingAppParsing.postScanResultWthoutScan(getActivity(),HinjAppPreferenceUtils.getRaId(getActivity()),
								HinjAppPreferenceUtils.getLoginDeviceId(getActivity()));  
			}  
	 
			@Override
			public void onPostExecute(Object[] result) {
				try{
					System.out.println("result "+result);
					boolean status = (Boolean) result[0];
					response = (String) result[1];
					
					if(status)
					{
						mContedChilDeviceDetailBean = new ContedChilDeviceDetailBean();
						mmContedChilDeviceDetailBean  = (ArrayList<ContedChilDeviceDetailBean>) result[2]; 
						System.out.println(mmContedChilDeviceDetailBean); 
						
						//HinjAppPreferenceUtils.setChildUploadDeviceId(ConnectDeviceActivity.this, mmContedChilDeviceDetailBean.get(pos_click).getChild_upload_devide_id());
						
						HinjAppPreferenceUtils.setPhotoCount(getActivity(),  mmContedChilDeviceDetailBean.get(pos_click).getPhotoCount());
						HinjAppPreferenceUtils.setMusicCount(getActivity(),  mmContedChilDeviceDetailBean.get(pos_click).getMusicCount());
						HinjAppPreferenceUtils.setvideosCount(getActivity(), mmContedChilDeviceDetailBean.get(pos_click).getVideoCount());
						HinjAppPreferenceUtils.setAppsCount(getActivity(),  mmContedChilDeviceDetailBean.get(pos_click).getAppCount());
						HinjAppPreferenceUtils.setMessagesCount(getActivity(),  mmContedChilDeviceDetailBean.get(pos_click).getMessageCount());
						HinjAppPreferenceUtils.setContactsCount(getActivity(),  mmContedChilDeviceDetailBean.get(pos_click).getContactCount());
						HinjAppPreferenceUtils.setModelName(getActivity(),  mmContedChilDeviceDetailBean.get(pos_click).getModel());
						
						Intent countUpdateIntent = new Intent();
						countUpdateIntent.setAction("com.hinj.app.activity.fragement.UPDATECOUNTINTENT");
						getActivity().sendBroadcast(countUpdateIntent);

					}
					else
					{
						response = (String) result[1];
						//AppUtils.showDialog(getActivity(), response);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				//mProgressDialog.dismiss();
			}
		}*/
	
	
	  class MyTimerTask extends TimerTask {

		  @Override
		  public void run() {
		   Calendar calendar = Calendar.getInstance();
		   SimpleDateFormat simpleDateFormat = 
		     new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
		   final String strDate = simpleDateFormat.format(calendar.getTime());
		   
		   getActivity().runOnUiThread(new Runnable(){

		    @Override
		    public void run() {
		    	//new PostBarcodeScanResultWithouScanAsynctask().execute();
		    	initGUI();
		    }});
		  }
		 }
	  
}

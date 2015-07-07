package com.hinj.app.activity.fragement;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hinj.app.activity.LoginActivity;
import com.hinj.app.activity.R;
import com.hinj.app.model.ConnectedDevicesBean;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.parsing.HingAppParsing;
import com.hinj.secure.smsgps.DataCountUpdateService;

public class AccountFragment extends Fragment {
	
	private ArrayList<ConnectedDevicesBean> connectedDevicesBeans = new ArrayList<ConnectedDevicesBean>();
	public AccountFragment(){}
	
	private Button logout_btn;
	private ListView device_list_view;
	private TextView user_name_text_view,user_email_text_view,hinjeddevice_text_view;
	private LinearLayout hinjeddecvice_text_seperator_LL;
	private Button add_device_btn;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        
        user_name_text_view = (TextView) rootView.findViewById(R.id.user_name_text_view);
        user_email_text_view = (TextView) rootView.findViewById(R.id.user_email_text_view);
        hinjeddevice_text_view = (TextView) rootView.findViewById(R.id.hinjeddevice_text_view);
        
        hinjeddecvice_text_seperator_LL = (LinearLayout) rootView.findViewById(R.id.hinjeddecvice_text_seperator_LL);
        
        device_list_view = (ListView) rootView.findViewById(R.id.device_list_view);
        
        add_device_btn = (Button) rootView.findViewById(R.id.add_device_btn);
        
        logout_btn = (Button) rootView.findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			    builder.setTitle("Confirm");
			    builder.setMessage("Are you sure?");

			    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			        public void onClick(DialogInterface dialog, int which) {

			        	new LogoutAsynctask(HinjAppPreferenceUtils.getRaId(getActivity()),
								HinjAppPreferenceUtils.getUploadDeviceID(getActivity())).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			            dialog.dismiss();
			        }
			    });

			    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

			        @Override
			        public void onClick(DialogInterface dialog, int which) {
			            dialog.dismiss();
			        }
			    });

			    AlertDialog alert = builder.create();
			    alert.show();
			}
		});
         
        add_device_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
        
        new AccountAsynctask(HinjAppPreferenceUtils.getRaId(getActivity()), HinjAppPreferenceUtils.getUploadDeviceID(getActivity())).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        
		
        return rootView;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	//	user_name_text_view.setText(HinjAppPreferenceUtils.getUserName(getActivity()));
	//	user_email_text_view.setText(HinjAppPreferenceUtils.getUserEmailId(getActivity()));
	}
	
	private class AccountAdapter extends ArrayAdapter<ConnectedDevicesBean> {

			ArrayList<ConnectedDevicesBean> deviceArray;
			Context ctx;

			public AccountAdapter(Context context, int textViewResourceId,ArrayList<ConnectedDevicesBean> objects) {
				super(context, textViewResourceId, objects);

				this.ctx = context;
				this.deviceArray = objects;
				notifyDataSetChanged();
			}
			
			@Override
			public ConnectedDevicesBean getItem(int position) {
				return super.getItem(position);
			}
			
			@Override
	        public int getCount() {
	            return deviceArray.size();
	        }

			@Override
			public View getView(int position, View convertView, ViewGroup parent) 
			{
				ViewHolder holder = null;
				if (convertView == null) 
				{
					holder = new ViewHolder();
					convertView = LayoutInflater.from(ctx).inflate(R.layout.list_item_account, parent, false);
					
					holder.device_name_text_view = (TextView) convertView.findViewById(R.id.device_name_text_view);
					holder.contact_imageView = (ImageView) convertView.findViewById(R.id.contact_imageView);
					holder.unhinj_ll = (LinearLayout) convertView.findViewById(R.id.unhinj_ll);
					
					convertView.setTag(holder);
				}
				else 
				{
	                holder = (ViewHolder)convertView.getTag();
	            }
				
				holder.device_name_text_view.setText(deviceArray.get(position).getDeviceName());
			//	holder.contact_imageView.setImageResource(deviceArray.get(position).getFile());
				
				holder.unhinj_ll.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						getActivity().finish();
					}
				});
		        
				return convertView;
			}
		}
	
	public static class ViewHolder {
	        public TextView device_name_text_view;
	        public ImageView contact_imageView;
	        public LinearLayout unhinj_ll;
	}
	 
	public class LogoutAsynctask extends AsyncTask<String, String,Object[]> {
		
		ProgressDialog progressDialog ;
		String response = "";
		String userId,deviceId;
		
		public LogoutAsynctask(String userId,String deviceId) {
			this.userId = userId;
			this.deviceId = deviceId;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.logout(getActivity(), userId,deviceId); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			
			progressDialog.cancel();
			try
			{
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					try {
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								DataCountUpdateService.stop(getActivity());
							}
						});
						
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					HinjAppPreferenceUtils.setOk(getActivity(), "");
					HinjAppPreferenceUtils.setRaId(getActivity(), "");
					HinjAppPreferenceUtils.setLastName(getActivity(), "");
					HinjAppPreferenceUtils.setFirstName(getActivity(), "");
					HinjAppPreferenceUtils.setEmail(getActivity(), "");
					HinjAppPreferenceUtils.setPassword(getActivity(), "");
					HinjAppPreferenceUtils.setPreferenceUrl(getActivity(), "");
					HinjAppPreferenceUtils.setPreferenceUsername(getActivity(), "");
					HinjAppPreferenceUtils.setPreferenceVoiceRetention(getActivity(), "");
					HinjAppPreferenceUtils.setPreferenceLoginStatus(getActivity(), "");
					HinjAppPreferenceUtils.setUploadDeviceID(getActivity(), "");
					HinjAppPreferenceUtils.setPreferenceExpirationDate(getActivity(), "");
				 	HinjAppPreferenceUtils.setPhotoCount(getActivity(), "");
				 	HinjAppPreferenceUtils.setMusicCount(getActivity(), "");
				 	HinjAppPreferenceUtils.setvideosCount(getActivity(), "");
				 	HinjAppPreferenceUtils.setAppsCount(getActivity(), "");
					HinjAppPreferenceUtils.setContactsCount(getActivity(), "");
					HinjAppPreferenceUtils.setMessagesCount(getActivity(), "");
					HinjAppPreferenceUtils.setRegisterDeviceId(getActivity(), "");
					HinjAppPreferenceUtils.setChildRegisterDeviceId(getActivity(), "");
					HinjAppPreferenceUtils.setClipboardStr(getActivity(), "");
					HinjAppPreferenceUtils.setModelName(getActivity(), "");
					HinjAppPreferenceUtils.setChildRaId(getActivity(), "");
					HinjAppPreferenceUtils.setChildUploadDeviceId(getActivity(), "");
					HinjAppPreferenceUtils.setParentModelName(getActivity(), "");
					HinjAppPreferenceUtils.setUserName(getActivity(), "");
					HinjAppPreferenceUtils.setUserEmailId(getActivity(), "");
					HinjAppPreferenceUtils.setMessageChatRecipientNumber(getActivity(), "");
					HinjAppPreferenceUtils.setCallRecipientNumber(getActivity(), "");
					
					
					Intent intent = new Intent(getActivity(),LoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().finish();
					
				}
				else
				{
					response = (String) result[1];
					AppUtils.showDialog(getActivity(), response);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public class AccountAsynctask extends AsyncTask<String, String,Object[]> {
		
		ProgressDialog progressDialog ;
		String response = "";
		String userId,deviceId;
		
		public AccountAsynctask(String userId,String deviceId) {
			this.userId = userId;
			this.deviceId = deviceId;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.getAccountDetails(getActivity(), userId,deviceId);  
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			
			progressDialog.cancel();
			try
			{
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					user_name_text_view.setText(HinjAppPreferenceUtils.getUserName(getActivity()));
					user_email_text_view.setText(HinjAppPreferenceUtils.getUserEmailId(getActivity()));
					
					connectedDevicesBeans = (ArrayList<ConnectedDevicesBean>) result[2] ; 
					
					if(connectedDevicesBeans.size() > 0 )
					{
						hinjeddevice_text_view.setVisibility(View.VISIBLE);
						hinjeddecvice_text_seperator_LL.setVisibility(View.VISIBLE);
					}
					
					AccountAdapter accountAdapter = new AccountAdapter(getActivity(), 0, connectedDevicesBeans);
					device_list_view.setAdapter(accountAdapter);
					
//					Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
				}
				else
				{
					response = (String) result[1];
					AppUtils.showDialog(getActivity(), response);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

package com.hinj.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hinj.app.utils.PushNotificationUtils;
import com.hinj.parsing.HingAppParsing;

public class ShowConnectedDevice extends Activity{

	TextView deviceName_text_view,disconnectTxtView;
	Button disconnect_button;
	private ImageView showConnectedDevice_option_icon_imageView;
	
	String popUpContents[];
    PopupWindow popupWindowDogs;  
    public ViewHolder holder = null;
    
    public LinearLayout barcode_menu_LL;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_connected);
        
        deviceName_text_view = (TextView) findViewById(R.id.deviceName_text_view);
        disconnectTxtView = (TextView) findViewById(R.id.disconnectTxtView);
        showConnectedDevice_option_icon_imageView = (ImageView) findViewById(R.id.showConnectedDevice_option_icon_imageView);
        barcode_menu_LL = (LinearLayout) findViewById(R.id.barcode_menu_LL);
        
        deviceName_text_view.setText(PushNotificationUtils.getRegistrDeviceName);
        
        disconnect_button = (Button) findViewById(R.id.disconnect_button);
        disconnect_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ShowConnectedDevice.this.finish();
				
			}
		});
        
        disconnectTxtView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ShowConnectedDevice.this.finish();
				
			}
		});
        
        showConnectedDevice_option_icon_imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				//setPopupMenu();
				 /*
		         * initialize pop up window
		         */
		        popupWindowDogs = showPopUpWindow();
		        popupWindowDogs.showAsDropDown(barcode_menu_LL, -5, 0);
				
			}
		});
        
        List<String> optionsList = new ArrayList<String>();
        optionsList.add("Settings");
        optionsList.add("About");
        optionsList.add("Share");
        optionsList.add("Exit");

        // convert to simple array
        popUpContents = new String[optionsList.size()];
        optionsList.toArray(popUpContents);
    }
	
	 PopupWindow showPopUpWindow()
	    {
	        // initialize a pop up window type
	        PopupWindow popupWindow = new PopupWindow(this);

	        // the drop down list is a list view
	        ListView listViewDogs = new ListView(this);
	      
	        // set our adapter and pass our pop up window contents
	       // listViewDogs.setAdapter(dogsAdapter(popUpContents));
	        listViewDogs.setAdapter(new OptionsAdapter(ShowConnectedDevice.this, 0, popUpContents)); 
	      
	        // set the item click listener
	        listViewDogs.setOnItemClickListener(new DogsDropdownOnItemClickListener());

	        // some other visual settings
	        popupWindow.setFocusable(true);
	        popupWindow.setWidth(250);
	        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
	      
	        // set the list view as pop up window content
	        popupWindow.setContentView(listViewDogs);

	        return popupWindow;
	    }

	    private class OptionsAdapter extends ArrayAdapter<String[]> {

	    	String[] deviceArray;
			Context ctx;

			public OptionsAdapter(Context context, int textViewResourceId,String[] objects) {
				super(context, textViewResourceId);

				this.ctx = context;
				this.deviceArray = objects;
				notifyDataSetChanged();
			}
			
			@Override
			public String[] getItem(int position) {
				return super.getItem(position);
			}
			
			@Override
	        public int getCount() {
	            return deviceArray.length;
	        }

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) 
			{
				if (convertView == null) 
				{
					holder = new ViewHolder();
					convertView = LayoutInflater.from(ctx).inflate(R.layout.list_item_options_list, parent, false);
					
					holder.options_text_view = (TextView) convertView.findViewById(R.id.options_text_view);
					holder.options_ll = (LinearLayout) convertView.findViewById(R.id.options_ll);
					
					convertView.setTag(holder);
				}
				else 
				{
	                holder = (ViewHolder)convertView.getTag();
	            }
				
				holder.options_text_view.setText(deviceArray[position]);
		        
				holder.options_ll.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						  // get the context and main activity to access variables
			            Context mContext = v.getContext();
			            ShowConnectedDevice mainActivity = ((ShowConnectedDevice) mContext);
			           
			            // add some animation when a list item was clicked
			            Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
			            fadeInAnimation.setDuration(10);
			            v.startAnimation(fadeInAnimation);
			           
			            // dismiss the pop up
			            mainActivity.popupWindowDogs.dismiss();
			           
			            // get the text and set it as the button text
			           if(position == 0)
			           {
			        	   Intent i = new Intent(ShowConnectedDevice.this, SettingsActivity.class);
			        	   startActivity(i);
			           }
			           else if(position == 1)
			           {
			        	   openDialog();
			           }
			           else if(position == 2)
			           {
							Intent intent = new Intent(Intent.ACTION_SEND);
							intent.setType("text/plain");
							intent.putExtra(Intent.EXTRA_TEXT, "www.google.com");
							startActivity(Intent.createChooser(intent, "Share with"));
						}
			           else if(position == 3)
			           {
			        	   cancelNotification(ShowConnectedDevice.this);
			        	   onBackPressed();
			           }
					}
				});
				
				return convertView;
			}
		}
		
		public static class ViewHolder {
		        public TextView options_text_view;
		        public LinearLayout options_ll;
		}
		
		
		
	    public class DogsDropdownOnItemClickListener implements OnItemClickListener {
	    	   
	        @Override
	        public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {

	            // get the context and main activity to access variables
	            Context mContext = v.getContext();
	            BarcodeScanActivity mainActivity = ((BarcodeScanActivity) mContext);
	           
	            // add some animation when a list item was clicked
	            Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
	            fadeInAnimation.setDuration(10);
	            v.startAnimation(fadeInAnimation);
	           
	            // dismiss the pop up
	            mainActivity.popupWindowDogs.dismiss();
	           
	            // get the text and set it as the button text
	           if(pos == 0)
	           {
	        	   Intent i = new Intent(ShowConnectedDevice.this, SettingsActivity.class);
	        	   startActivity(i);
	           }
	           else if(pos == 1)
	           {
	        	   openDialog();
	           }
	           else if(pos == 2)
	           {
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_TEXT, "www.google.com");
					startActivity(Intent.createChooser(intent, "Share with"));
				}
	           else if(pos == 3)
	           {
	        	   cancelNotification(ShowConnectedDevice.this);
	        	   onBackPressed();
	           }

	           Toast.makeText(mContext, "Selected Positon is: " + pos, 100).show();
	          /* optionsList.add("Settings");
	           optionsList.add("About");
	           optionsList.add("Share");
	           optionsList.add("Exit");*/

	        }
	    }
	    
		public static void cancelNotification(Context ctx) {
		    String ns = Context.NOTIFICATION_SERVICE;
		    NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
		    nMgr.cancelAll();
		}
		
		public void openDialog() {
			Dialog dialog = new Dialog(ShowConnectedDevice.this);
			dialog.setContentView(R.layout.dialogbrand_layout);
			dialog.setTitle("About");
			dialog.show();
			}
		
	public class SendConfirmAsynctask extends AsyncTask<String, String,Object[]> {
		ProgressDialog progressDialog ;
		String response = "";
		String parent_device_id,confirm;
		
		public SendConfirmAsynctask(String parent_device_id,String confirm) {
			this.parent_device_id = parent_device_id;
			this.confirm = confirm;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(ShowConnectedDevice.this,"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.getConfirmation(ShowConnectedDevice.this, parent_device_id, confirm); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try{
				System.out.println("result "+result);
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				/*if(status)
				{
					Toast.makeText(ShowConnectedDevice.this, response, Toast.LENGTH_SHORT).show();
					
					HinjAppPreferenceUtils.setPreferenceLoginStatus(ShowConnectedDevice.this, "true");
					
					*//**
					 * Register For PushNotification
					 * *//*
					new MyPushRegister(ShowConnectedDevice.this);
					
					//new AfterOK().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					
					Intent intent = new Intent(ShowConnectedDevice.this,BarcodeScanActivity.class);
                    startActivity(intent);
                    finish();
				
				}
				else
				{
					response = (String) result[1];
					AppUtils.showDialog(ShowConnectedDevice.this, response);
				}	*/
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

package com.hinj.app.activity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.zbar.Symbol;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.hinj.app.utils.HinjAppPreferenceUtils;

public class BarcodeScanActivity extends Activity {

    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;
    private String barcodescanresultStr="";
    private TextView connectTxtView, barCodetxt;
    private WebView barCodeWebView;
    public static String modal_send = "";
    
    public LinearLayout barcode_menu_LL;
    private ImageView option_icon_imageView;
    
    String popUpContents[];
    PopupWindow popupWindowDogs;  
    public ViewHolder holder = null;
    String webData;
    LinearLayout option_LLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        
        connectTxtView = (TextView) findViewById(R.id.connectTxtView);
        barCodetxt = (TextView) findViewById(R.id.barCodetxt);
        barCodeWebView = (WebView) findViewById(R.id.webView_barcode);
        webData = "<!DOCTYPE html><html style='backround : #FFFFFF;'><body></body></html>";
        
        
        option_icon_imageView = (ImageView) findViewById(R.id.option_icon_imageView);
        barcode_menu_LL = (LinearLayout) findViewById(R.id.barcode_menu_LL);
        
        option_LLayout = (LinearLayout) findViewById(R.id.option_LL);
        
        barCodetxt.setText(HinjAppPreferenceUtils.getRaId(BarcodeScanActivity.this));
        
        connectTxtView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i = new Intent(BarcodeScanActivity.this,ConnectDeviceActivity.class);
				startActivity(i);
			}
		});
        
        option_LLayout.setOnClickListener(new OnClickListener() {
			
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
        
		//String android_id = Secure.getString(BarcodeScanActivity.this.getContentResolver(),Secure.ANDROID_ID); 
        String android_id = Secure.getString(BarcodeScanActivity.this.getContentResolver(),Secure.ANDROID_ID);
		
		String uploadDeviceID = HinjAppPreferenceUtils.getUploadDeviceID(BarcodeScanActivity.this);
        String raId = HinjAppPreferenceUtils.getRaId(BarcodeScanActivity.this);
        String model= android.os.Build.MODEL;
        
        modal_send= android.os.Build.MODEL;
        
        if (!HinjAppPreferenceUtils.getRaId(BarcodeScanActivity.this).equalsIgnoreCase("")) {
        	barCodeWebView.setBackgroundColor(Color.WHITE);
        	barCodeWebView.loadUrl("http://chart.apis.google.com/chart?cht=qr&chs=300x300&chl="+android_id+"/"+raId+"/"+model+"&chld=H|3");
		}else {
			barCodeWebView.setVisibility(View.INVISIBLE);
		}
        
        // add items on the array dynamically
        // format is Company Name:: ID
        List<String> optionsList = new ArrayList<String>();
        optionsList.add("Settings");
        optionsList.add("Share");
        optionsList.add("About");
        optionsList.add("Exit");

        // convert to simple array
        popUpContents = new String[optionsList.size()];
        optionsList.toArray(popUpContents);

    }

    public void launchScanner(View v) {
        if (isCameraAvailable()) {
            Intent intent = new Intent(this, ZBarScannerActivity.class);
            startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
        } else {
            Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public void launchQRScanner(View v) {
        if (isCameraAvailable()) {
            Intent intent = new Intent(this, ZBarScannerActivity.class);
            intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
            startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
        } else {
            Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ZBAR_SCANNER_REQUEST:
            case ZBAR_QR_SCANNER_REQUEST:
                if (resultCode == RESULT_OK) {
                    barcodescanresultStr = data.getStringExtra(ZBarConstants.SCAN_RESULT);
                    Toast.makeText(this, "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT), Toast.LENGTH_SHORT).show();
                    
                    String [] seperateid = barcodescanresultStr.split("/");
					System.out.println(seperateid[0]+"    "+ seperateid[1]); 
                    
                } else if(resultCode == RESULT_CANCELED && data != null) {
                    String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
                    if(!TextUtils.isEmpty(error)) {
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    
    private void setPopupMenu() {

		PopupMenu popup = new PopupMenu(BarcodeScanActivity.this, barcode_menu_LL);  
		//Inflating the Popup using xml file  
		popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());  

		//registering popup with OnMenuItemClickListener  
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
			public boolean onMenuItemClick(MenuItem item) { 
				System.out.println(item); 
				
				return true;  
			}  
		});  

		popup.show();//showing popup menu  
	}  
    
    PopupWindow showPopUpWindow()
    {
        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(this);

        // the drop down list is a list view
        ListView listViewDogs = new ListView(this);
      
        // set our adapter and pass our pop up window contents
       // listViewDogs.setAdapter(dogsAdapter(popUpContents));
        listViewDogs.setAdapter(new OptionsAdapter(BarcodeScanActivity.this, 0, popUpContents)); 
      
        // set the item click listener
        listViewDogs.setOnItemClickListener(new DogsDropdownOnItemClickListener());

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(300);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
      
        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }

    private ArrayAdapter<String> dogsAdapter(String dogsArray[]) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dogsArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                // setting the ID and text for every items in the list
                          
                String text = getItem(position);              

                // visual settings for the list item
                TextView listItem = new TextView(BarcodeScanActivity.this);

                listItem.setText(text);
                listItem.setTag(position);
                listItem.setTextSize(22);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.WHITE);
              
                return listItem;
            }
        };
      
        return adapter;
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
				holder.options_icon_imageView = (ImageView) convertView.findViewById(R.id.options_icon_imageView);
				
				convertView.setTag(holder);
			}
			else 
			{
                holder = (ViewHolder)convertView.getTag();
            }
			
			holder.options_text_view.setText(deviceArray[position]);
			
			if(position == 0)
			{
				holder.options_icon_imageView.setBackgroundResource(R.drawable.icon_setting);
			}
			else if(position == 1)
			{
				holder.options_icon_imageView.setBackgroundResource(R.drawable.icon_share);
			}
			else if(position == 2)
			{
				holder.options_icon_imageView.setBackgroundResource(R.drawable.icon_about);
			}
			else if(position == 3)
			{
				holder.options_icon_imageView.setBackgroundResource(R.drawable.icon_quit);
			}
	        
			holder.options_ll.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
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
		           if(position == 0)
		           {
		        	   Intent i = new Intent(BarcodeScanActivity.this, SettingsActivity.class);
		        	   startActivity(i);
		           }
		           else if(position == 1)
		           {
		        	   Intent intent = new Intent(Intent.ACTION_SEND);
					   intent.setType("text/plain");
					   intent.putExtra(Intent.EXTRA_TEXT, "www.google.com");
					   startActivity(Intent.createChooser(intent, "Share with"));
		           }
		           else if(position == 2)
		           {
		        	   openDialog();
				}
		           else if(position == 3)
		           {
		        	   cancelNotification(BarcodeScanActivity.this);
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
	        public ImageView options_icon_imageView;
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
        	   Intent i = new Intent(BarcodeScanActivity.this, SettingsActivity.class);
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
        	   cancelNotification(BarcodeScanActivity.this);
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
		Dialog dialog = new Dialog(BarcodeScanActivity.this);
		dialog.setContentView(R.layout.dialogbrand_layout);
		//dialog.setTitle("About");
		dialog.show();
		}
	
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    	finish();
    }
}

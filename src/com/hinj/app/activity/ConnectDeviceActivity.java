package com.hinj.app.activity;

import java.util.ArrayList;

import net.sourceforge.zbar.Symbol;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.hinj.app.model.ContedChilDeviceDetailBean;
import com.hinj.app.model.DeviceDetailBean;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.parsing.HingAppParsing;

public class ConnectDeviceActivity extends Activity implements OnClickListener{

	private ListView device_list_view;
	private ImageView scan_qr_code_imageView;
	
	private static final int ZBAR_SCANNER_REQUEST = 0;
	private static final int ZBAR_QR_SCANNER_REQUEST = 1;
	    
	private String barcodescanresultStr="";
	
	private EditText child_key_editText;
	private ImageView connect_imgView;
	private DeviceDetailBean detailBean;
	private ContedChilDeviceDetailBean mContedChilDeviceDetailBean;
	private ProgressDialog mProgressDialog;
	private String [] seperateid;
	private boolean click = false;
	private boolean click_button = false;
	private ArrayList<ContedChilDeviceDetailBean> mmContedChilDeviceDetailBean = new ArrayList<ContedChilDeviceDetailBean>();
	private ArrayList<DeviceDetailBean> mDeviceDetailBean = new ArrayList<DeviceDetailBean>();
	private boolean click_scan = false;
	public static int internal= 0, external=0;
	public Button buttonBack;
	private LinearLayout back_LL;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_child_connect);
        
        device_list_view = (ListView) findViewById(R.id.device_list_view);
        scan_qr_code_imageView = (ImageView) findViewById(R.id.scan_qr_code_imageView);
        child_key_editText = (EditText) findViewById(R.id.child_key_editText);
        connect_imgView = (ImageView) findViewById(R.id.connect_imgView);
        
        buttonBack = (Button) findViewById(R.id.buttonBack);
        back_LL = (LinearLayout) findViewById(R.id.back_LL);
        // connect_imgView.setEnabled(false);
        
       /* ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Device 1 Name");
        arrayList.add("Device 2 Name");
        arrayList.add("Device 3 Name");
        arrayList.add("Device 4 Name");
        
        DeviceAdapter adapter = new DeviceAdapter(ConnectDeviceActivity.this, arrayList);
        device_list_view.setAdapter(adapter);*/
        
        scan_qr_code_imageView.setOnClickListener(this);
        connect_imgView.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        back_LL.setOnClickListener(this);
        
    	click_button = true;
		
    	new PostBarcodeScanResultAsynctask(Secure.getString(ConnectDeviceActivity.this.getContentResolver(),Secure.ANDROID_ID),
				HinjAppPreferenceUtils.getRaId(ConnectDeviceActivity.this),
				android.os.Build.MODEL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		
    }
    
	@Override
	public void onClick(View v) {
		if(v == scan_qr_code_imageView)
		{
			if (isCameraAvailable()) {
	            Intent intent = new Intent(ConnectDeviceActivity.this, ZBarScannerActivity.class);
	            intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
	            startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
	        } else {
//	            Toast.makeText(ConnectDeviceActivity.this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
	        }
		}
		
		if(v == connect_imgView)
		{
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(child_key_editText.getWindowToken(),0);
			if (detailBean==null) {
				if (!child_key_editText.getText().toString().equalsIgnoreCase("")) {
					click_button = true;
					new PostBarcodeScanResultAsynctask(HinjAppPreferenceUtils.getChildUploadDeviceId(ConnectDeviceActivity.this),child_key_editText.getText().toString(),"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}else {
				/*	click_button = true;
					new PostBarcodeScanResultAsynctask(Secure.getString(ConnectDeviceActivity.this.getContentResolver(),Secure.ANDROID_ID),
							HinjAppPreferenceUtils.getRaId(ConnectDeviceActivity.this),
							android.os.Build.MODEL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
					
					// Toast.makeText(ConnectDeviceActivity.this, "Please enter user id", Toast.LENGTH_SHORT).show();
				}
			}else {
				click = true;
				mDeviceDetailBean.add(detailBean);
				
				HinjAppPreferenceUtils.setRaId(ConnectDeviceActivity.this, mDeviceDetailBean.get(0).getChild_Ra_id());
				HinjAppPreferenceUtils.setLoginDeviceId(ConnectDeviceActivity.this, mDeviceDetailBean.get(0).getLogin_device_id().get(0).toString());
				
				new PostBarcodeScanResultWithouScanAsynctask(mDeviceDetailBean.get(0).getChild_Ra_id(), 0, mDeviceDetailBean.get(0).getLogin_device_id().get(0).toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		}
		
		device_list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
					mDeviceDetailBean.add(detailBean);
					click = true;
					HinjAppPreferenceUtils.setRaId(ConnectDeviceActivity.this, mDeviceDetailBean.get(0).getChild_Ra_id());
					HinjAppPreferenceUtils.setLoginDeviceId(ConnectDeviceActivity.this, mDeviceDetailBean.get(0).getLogin_device_id().get(0).toString());
					
					new PostBarcodeScanResultWithouScanAsynctask(mDeviceDetailBean.get(0).getChild_Ra_id(), arg2, mDeviceDetailBean.get(0).getLogin_device_id().get(arg2).toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		});
		
		if(v == buttonBack)
		{
			finish();
		}
		
		if(v ==  back_LL)
		{
			finish();
		}
		
	}
	
    public class DeviceAdapter extends ArrayAdapter<String> {
    	
    	ArrayList<String> deviceArray;
    	
        public DeviceAdapter(Context context, ArrayList<String> users) {
           super(context, 0, users);
           this.deviceArray = users;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
           // Get the data item for this position
        	String user = getItem(position);    
           // Check if an existing view is being reused, otherwise inflate the view
           if (convertView == null) {
              convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
           }
           // Lookup view for data population
           LinearLayout main_LL = (LinearLayout) convertView.findViewById(R.id.main_LL);
           TextView device_name_text_view = (TextView) convertView.findViewById(R.id.device_name_text_view);
           
           device_name_text_view.setText(deviceArray.get(position));
           
           main_LL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDeviceDetailBean.add(detailBean);
				click = true;
				HinjAppPreferenceUtils.setRaId(ConnectDeviceActivity.this, mDeviceDetailBean.get(0).getChild_Ra_id());
				HinjAppPreferenceUtils.setLoginDeviceId(ConnectDeviceActivity.this, mDeviceDetailBean.get(0).getLogin_device_id().get(0).toString());
				
				new PostBarcodeScanResultWithouScanAsynctask(mDeviceDetailBean.get(0).getChild_Ra_id(), position, mDeviceDetailBean.get(0).getLogin_device_id().get(position).toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

				}
           });
           // Return the completed view to render on screen 
           
           return convertView;
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
               //   Toast.makeText(this, "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT), Toast.LENGTH_SHORT).show();
                    
                    seperateid = barcodescanresultStr.split("/");
					//System.out.println(seperateid[0]+"    "+ seperateid[1]); 
                    
                    HinjAppPreferenceUtils.setChildUploadDeviceId(ConnectDeviceActivity.this, seperateid[0]);
                    HinjAppPreferenceUtils.setChildRaId(ConnectDeviceActivity.this, seperateid[1]);
                    
                    if(!barcodescanresultStr.equalsIgnoreCase(""))
                    {
                    	ConnectDeviceActivity.this.runOnUiThread(new Runnable(){
 					        public void run() {
 					            //If there are stories, add them to the table
 					            try {
 					            	//searchContact();
// 					            	click_scan = true;
 			                    	new PostBarcodeScanResultAsynctask(seperateid[0],seperateid[1],seperateid[2]).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
 					            	AppUtils.hideSoftKeyboard(ConnectDeviceActivity.this);
 					            } catch (Exception ex) {
 					            	ex.printStackTrace();
 					            }
 					        }
 					    });
                    }
                    
                } else if(resultCode == RESULT_CANCELED && data != null) {
                    String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
                    if(!TextUtils.isEmpty(error)) {
//                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    
    public class PostBarcodeScanResultAsynctask extends AsyncTask<String, String,Object[]> {
		//ProgressDialog progressDialog ;
		String response = "";
		String uploadDeviceID,raId,model;
		
		public PostBarcodeScanResultAsynctask(String uploadDeviceID,String raId,String model) {
			this.uploadDeviceID = uploadDeviceID;
			this.raId = raId;
			this.model = model;
		}

		@Override
		public void onPreExecute() {
			mProgressDialog = ProgressDialog.show(ConnectDeviceActivity.this,"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.postScanResult(ConnectDeviceActivity.this,uploadDeviceID,raId,model); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			try{
				System.out.println("result "+result);
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					connect_imgView.setEnabled(true);
					detailBean = new DeviceDetailBean();
					detailBean  = (DeviceDetailBean) result[2]; 
					
					child_key_editText.setText(detailBean.getChild_Ra_id());
					
					DeviceAdapter adapter = new DeviceAdapter(ConnectDeviceActivity.this, detailBean.getLogin_device_name());
			        device_list_view.setAdapter(adapter);
			        
				}
				else
				{
					response = (String) result[1];
					AppUtils.showDialog(ConnectDeviceActivity.this, response);
				}
				/*if (click) {
					Intent intent = new Intent(ConnectDeviceActivity.this,DashBoardActivity.class);
					startActivity(intent);
					finish();
				}*/
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			mProgressDialog.dismiss();
		}
	}
    
    public class PostBarcodeScanResultWithouScanAsynctask extends AsyncTask<String, String,Object[]> {
		//ProgressDialog progressDialog ;
		String response = "";
		String childRaId;
		int pos_click;
		String device_id1;
		
		public PostBarcodeScanResultWithouScanAsynctask(String childRaId, int arg2, String device_id) {
			this.childRaId = childRaId;
			pos_click = arg2;
			device_id1 = device_id;
		}

		@Override
		public void onPreExecute() {
			mProgressDialog = ProgressDialog.show(ConnectDeviceActivity.this,"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.postScanResultWthoutScan(ConnectDeviceActivity.this,childRaId, device_id1); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			try{
				System.out.println("result "+result);
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					//Toast.makeText(ConnectDeviceActivity.this, response, Toast.LENGTH_SHORT).show();
					connect_imgView.setEnabled(true);
					mContedChilDeviceDetailBean = new ContedChilDeviceDetailBean();
					mmContedChilDeviceDetailBean  = (ArrayList<ContedChilDeviceDetailBean>) result[2]; 
					System.out.println(mmContedChilDeviceDetailBean); 
					
					//HinjAppPreferenceUtils.setChildUploadDeviceId(ConnectDeviceActivity.this, mmContedChilDeviceDetailBean.get(pos_click).getChild_upload_devide_id());
					
					HinjAppPreferenceUtils.setPhotoCount(ConnectDeviceActivity.this,  mmContedChilDeviceDetailBean.get(pos_click).getPhotoCount());
					HinjAppPreferenceUtils.setMusicCount(ConnectDeviceActivity.this,  mmContedChilDeviceDetailBean.get(pos_click).getMusicCount());
					HinjAppPreferenceUtils.setvideosCount(ConnectDeviceActivity.this, mmContedChilDeviceDetailBean.get(pos_click).getVideoCount());
					HinjAppPreferenceUtils.setAppsCount(ConnectDeviceActivity.this,  mmContedChilDeviceDetailBean.get(pos_click).getAppCount());
					HinjAppPreferenceUtils.setMessagesCount(ConnectDeviceActivity.this,  mmContedChilDeviceDetailBean.get(pos_click).getMessageCount());
					HinjAppPreferenceUtils.setContactsCount(ConnectDeviceActivity.this,  mmContedChilDeviceDetailBean.get(pos_click).getContactCount());
					HinjAppPreferenceUtils.setModelName(ConnectDeviceActivity.this,  mmContedChilDeviceDetailBean.get(pos_click).getModel());
					
				}
				else
				{
					response = (String) result[1];
					AppUtils.showDialog(ConnectDeviceActivity.this, response);
				}
				if (click) {					
					ConnectDeviceActivity.this.runOnUiThread(new Runnable(){
				        public void run() {
				            //If there are stories, add them to the table
				            try {
				            	//searchContact();
				            	new GetStorageAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				            } catch (final Exception ex) {
				            	ex.printStackTrace();
				            }
				        }
				    });
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			//mProgressDialog.dismiss();
		}
	}
    
    public class GetStorageAsynctask extends AsyncTask<String, String,Object[]> {
//		ProgressDialog progressDialog ;
		String response = "";
		String in,ex;

		@Override
		public void onPreExecute() {
//			progressDialog = ProgressDialog.show(ConnectDeviceActivity.this,"", "Please Wait");
		}
		@Override
		public Object[] doInBackground(String... params) {

			return HingAppParsing.getUserStorage(ConnectDeviceActivity.this, HinjAppPreferenceUtils.getChildUploadDeviceId(ConnectDeviceActivity.this)); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			try{
				
				System.out.println("result "+result);
				boolean status = (Boolean) result[0];
				
				try {
					internal = Integer.parseInt((String) result[1]);
					external = Integer.parseInt((String) result[2]);
					
					new YourAsyncAutoLoginForPN().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					/*Intent intent = new Intent(ConnectDeviceActivity.this,DashBoardActivity.class);
					startActivity(intent);
					finish();*/
				} catch (Exception e) {
					e.printStackTrace();
				}
				mProgressDialog.dismiss();
			}
			catch (Exception e) {
				e.printStackTrace();
				mProgressDialog.dismiss();
			}
		}
	}
    
    public class YourAsyncAutoLoginForPN extends AsyncTask<String, String,Object[]> 
    {
    	//ProgressDialog progressDialog ;
    	String response = "";
    	String emailStr;
	
    	@Override
    	public void onPreExecute() {
    		//progressDialog = ProgressDialog.show(LoginActivity.this,"Loading...", "Please Wait");
    	}

    	@Override
    	public Object[] doInBackground(String... params) {

    		return HingAppParsing.autoLoginForPN(ConnectDeviceActivity.this,BarcodeScanActivity.modal_send, 
				HinjAppPreferenceUtils.getChildRegisterDeviceId(ConnectDeviceActivity.this),
				HinjAppPreferenceUtils.getChildRaId(ConnectDeviceActivity.this)); 
    	}  

    	@Override
    	public void onPostExecute(Object[] result) {
		
    		//progressDialog.cancel();
    		try
    		{
    			boolean status = (Boolean) result[0];
    			response = (String) result[1];
			
    			if(status){
    				
    				Intent intent3 = new Intent(ConnectDeviceActivity.this,DashBoardActivity.class);
    				intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    				startActivity(intent3);
    			}else{
    				response = (String) result[1];
    				AppUtils.showDialog(ConnectDeviceActivity.this, response);
    			}
    		}
    		catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    }

	public void getResponse(Context context) {
		Intent intent = new Intent(context,DashBoardActivity.class);
		startActivity(intent);
		finish();
	}
}

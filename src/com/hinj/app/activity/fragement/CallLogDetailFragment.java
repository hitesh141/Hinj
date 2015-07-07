package com.hinj.app.activity.fragement;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hinj.app.activity.DashBoardActivity;
import com.hinj.app.activity.R;
import com.hinj.app.model.CallHistoryDetailBean;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.RequestPost;
import com.hinj.app.utils.Utility;
import com.hinj.parsing.HingAppParsing;
import com.hinj.secure.smsgps.DeviceInterface;

public class CallLogDetailFragment extends Fragment implements OnClickListener{
	private ListView calllog_detail_layout_listView;
	private TextView contact_text_view,contact_number_text_view;
	private ImageView back_imageView;
	
	ArrayList<CallHistoryDetailBean> callDetailBeans =  new ArrayList<CallHistoryDetailBean>();
	
	private LinearLayout call_LL,send_txt_msg_LL,view_contact_LL,delete_from_call_log_LL;
	
	private RequestPost mRequestPostClass;
	private DeviceInterface onComplete;	
	String CallDetailList="";
	String displayName = "", emailAddress = "", phoneNumber = "",UserAddress = "";
	
	String contactID; 
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 
		View rootView = inflater.inflate(R.layout.fragment_call_detail, container, false);
		  
		//	requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		contact_text_view=(TextView)rootView.findViewById(R.id.contact_text_view);
		back_imageView = (ImageView)rootView.findViewById(R.id.back_imageView);
		contact_number_text_view = (TextView)rootView.findViewById(R.id.contact_number_text_view);
		//contact_text_view.setText(getIntent().getStringExtra("group_name"));
		
		//imnew = ImageLoader.getInstance();
		
		/*	user_id = this.getIntent().getStringExtra("user_id");
		group_id = this.getIntent().getStringExtra("group_id");*/
		
		/*	*//** Back and home button **//*
		
	*/
		
		//options = new DisplayImageOptions.Builder() .showImageForEmptyUri(R.drawable.ic_launcher).cacheOnDisc() .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		
		calllog_detail_layout_listView=(ListView)rootView.findViewById(R.id.calllog_detail_layout_listView);
		
		call_LL =(LinearLayout)rootView.findViewById(R.id.call_LL);
		send_txt_msg_LL=(LinearLayout)rootView.findViewById(R.id.send_txt_msg_LL);
		view_contact_LL=(LinearLayout)rootView.findViewById(R.id.view_contact_LL);
		delete_from_call_log_LL=(LinearLayout)rootView.findViewById(R.id.delete_calls_detail);
		
		back_imageView.setOnClickListener(this);
		delete_from_call_log_LL.setOnClickListener(this);
		
		call_LL.setOnClickListener(this);
		send_txt_msg_LL.setOnClickListener(this);
		view_contact_LL.setOnClickListener(this);
		
		/*FetchGroupChat fetchsmsList = new FetchGroupChat();
		fetchsmsList.execute(user_id,group_id );*/
		
		new GetCallDetailAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		
        return rootView;
	}


	public void onBackPressed(){
	  if (getFragmentManager().getBackStackEntryCount() == 1){
	    getActivity().finish();
	  }
	 
	}

	@Override
	public void onClick(View v) {
		
		if(v == call_LL)
		{
			
		}
		
		if(v == send_txt_msg_LL)
		{
			 /** Creating an intent to initiate view action */
            Intent intent = new Intent("android.intent.action.VIEW");

            /** creates an sms uri */
            Uri data = Uri.parse("sms:");

            /** Setting sms uri to the intent */
            intent.setData(data);

            /** Initiates the SMS compose screen, because the activity contain ACTION_VIEW and sms uri */
            startActivity(intent);
		}
		
		if(v == view_contact_LL)
		{
			try {
				if(getContactList(getActivity(), HinjAppPreferenceUtils.getCallRecipientNumber(getActivity())))
				{ 
					System.out.println("already exists");

					Intent intent = new Intent(Intent.ACTION_VIEW);
					Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactID);
					intent.setData(uri);
					getActivity().startActivity(intent);
				}
				else
				{
					addContact(HinjAppPreferenceUtils.getCallRecipientNumber(getActivity()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
		
		if (v == delete_from_call_log_LL) {
			new DeleteCallLogAsynctask(CallLogsFragment.id,CallLogsFragment.unique_call_id).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		
		if(v == back_imageView)
		{
			
		}
	}

	private void addContact(String phone) {

		String DisplayName = "";
		 String MobileNumber = "";
		 String HomeNumber = "";
		 String WorkNumber = "";
		 String emailID = "";
		 String company = "";
		 String jobTitle = "";

			/*if(HeroAppPreferenceUtils.getComposeName(getActivity())!= null
					&& !HeroAppPreferenceUtils.getComposeName(getActivity()).equalsIgnoreCase(""))
			{
				DisplayName =  HeroAppPreferenceUtils.getComposeName(getActivity());
			}
			
			if(HeroAppPreferenceUtils.getOpencnamName(getActivity())!= null
					&& !HeroAppPreferenceUtils.getOpencnamName(getActivity()).equalsIgnoreCase(""))
			{
				DisplayName =  HeroAppPreferenceUtils.getOpencnamName(getActivity());
			}
			
			if(HeroAppPreferenceUtils.getPhone(getActivity())!= null && !HeroAppPreferenceUtils.getPhone(getActivity()).equalsIgnoreCase(""))
			{
				MobileNumber =  HeroAppPreferenceUtils.getPhone(getActivity());
			}
			
			if(HeroAppPreferenceUtils.getNumber(getActivity())!= null && !HeroAppPreferenceUtils.getNumber(getActivity()).equalsIgnoreCase(""))
			{
				MobileNumber =  HeroAppPreferenceUtils.getNumber(getActivity());
			}
			
			if(HeroAppPreferenceUtils.getFirstName(getActivity())!= null &&!HeroAppPreferenceUtils.getFirstName(getActivity()).equalsIgnoreCase(""))
			{
				DisplayName =  HeroAppPreferenceUtils.getCompanyName(getActivity());
			}
			
			if(HeroAppPreferenceUtils.getLastName(getActivity())!= null &&!HeroAppPreferenceUtils.getLastName(getActivity()).equalsIgnoreCase(""))
			{
			}
			
			if(HeroAppPreferenceUtils.getEmail(getActivity())!= null &&!HeroAppPreferenceUtils.getEmail(getActivity()).equalsIgnoreCase(""))
			{
				emailID =  HeroAppPreferenceUtils.getEmail(getActivity());
			}
			
			if(HeroAppPreferenceUtils.getCompanyName(getActivity())!= null &&!HeroAppPreferenceUtils.getCompanyName(getActivity()).equalsIgnoreCase(""))
			{
				company = HeroAppPreferenceUtils.getCompanyName(getActivity());
			}
			
			if(HeroAppPreferenceUtils.getStreetAddress(getActivity())!= null &&!HeroAppPreferenceUtils.getStreetAddress(getActivity()).equalsIgnoreCase(""))
			{
			}	*/ 
		 
		 ArrayList < ContentProviderOperation > ops = new ArrayList < ContentProviderOperation > ();

		 ops.add(ContentProviderOperation.newInsert(
		 ContactsContract.RawContacts.CONTENT_URI)
		     .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
		     .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
		     .build());

		 //------------------------------------------------------ Names
		 if (DisplayName != null) {
		     ops.add(ContentProviderOperation.newInsert(
		     ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
		         .withValue(
		     ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
		     DisplayName).build());
		 }

		 //------------------------------------------------------ Mobile Number                     
		 if (phone != null) {
		     ops.add(ContentProviderOperation.
		     newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
		         .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		     ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
		         .build());
		 }

		 //------------------------------------------------------ Home Numbers
		 if (HomeNumber != null) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
		         .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		     ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
		         .build());
		 }

		 //------------------------------------------------------ Work Numbers
		 if (WorkNumber != null) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
		         .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		     ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
		         .build());
		 }

		 //------------------------------------------------------ Email
		 if (emailID != null) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
		         .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
		         .build());
		 }

		 //------------------------------------------------------ Organization
		 if (!company.equals("") && !jobTitle.equals("")) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
		         .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
		         .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
		         .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
		         .build());
		 }

		 // Asking the Contact provider to create a new contact                 
		 try {
		     getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		     
		     Toast.makeText(getActivity(), "Contact Added", Toast.LENGTH_SHORT).show();
		 } catch (Exception e) {
		     e.printStackTrace();
		     Toast.makeText(getActivity(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		 } 
	
	}

	public class GetCallDetailAsynctask extends AsyncTask<String, String,Object[]> {
		
		ProgressDialog progressDialog ;
		String response = "";
		
		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			return HingAppParsing.getCallDetails(getActivity(), HinjAppPreferenceUtils.getChildRaId(getActivity()),"0",HinjAppPreferenceUtils.getChildUploadDeviceId(getActivity())); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try
			{
				boolean status = (Boolean) result[0];
				response = (String) result[1];

				if (status) {
					callDetailBeans = (ArrayList<CallHistoryDetailBean>) result[2];

					CallDetailAdapter adapter = new CallDetailAdapter(getActivity(), 0, callDetailBeans);
					calllog_detail_layout_listView.setAdapter(adapter); 

					contact_text_view.setText("Call "+HinjAppPreferenceUtils.getCallRecipientNumber(getActivity())); 
					contact_number_text_view.setText(HinjAppPreferenceUtils.getCallRecipientNumber(getActivity())); 
					
//					Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
				} else {
					response = (String) result[1];
					AppUtils.showDialog(getActivity(), response);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class CallDetailAdapter extends ArrayAdapter<CallHistoryDetailBean> {

			ArrayList<CallHistoryDetailBean> deviceArray;
			Context ctx;

			public CallDetailAdapter(Context context, int textViewResourceId,ArrayList<CallHistoryDetailBean> objects) {
				super(context, textViewResourceId);

				this.ctx = context;
				this.deviceArray = objects;
				notifyDataSetChanged();
			}
			
			@Override
			public CallHistoryDetailBean getItem(int position) {
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
					convertView = LayoutInflater.from(ctx).inflate(R.layout.list_item_call_log_detail, parent, false);
					
					holder.call_date_text_view = (TextView) convertView.findViewById(R.id.call_date_text_view);
					holder.call_time_text_view = (TextView) convertView.findViewById(R.id.call_time_text_view);
					holder.duration_text_view  = (TextView) convertView.findViewById(R.id.duration_text_view);
					
					convertView.setTag(holder);
				}
				else 
				{
	                holder = (ViewHolder)convertView.getTag();
	            }
				
				//holder.call_date_text_view.setText(deviceArray.get(position).getCall_time());
				
				String formatedDate = Utility.getSimpleDateFormat(Long.parseLong(deviceArray.get(position).getCall_time()), "EEEE dd LLL yyyy").toString();
				holder.call_date_text_view.setText(formatedDate);
				 
				
				String formatedDate2 = Utility.getSimpleDateFormat(Long.parseLong(deviceArray.get(position).getCall_time()), "hh:mm a").toString();
				holder.call_time_text_view.setText(formatedDate2);
				
				
				holder.duration_text_view.setText(deviceArray.get(position).getDuration());
		        
				return convertView;
			}
		}
		
	public static class ViewHolder {
		       public TextView call_date_text_view,call_time_text_view,duration_text_view;
	}
	
	public class DeleteCallLogAsynctask extends AsyncTask<String, String,Object[]> {
		
		ProgressDialog progressDialog ;
		String response = "";
		String id="",unique_call_id="";
		
		public DeleteCallLogAsynctask(String id,String unique_call_id) {
			this.id=id;
			this.unique_call_id = unique_call_id;
		}
		
		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.deleteCallLog(getActivity(), id, unique_call_id); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try
			{
				boolean status = (Boolean) result[0];
				response = (String) result[1];

				if (status) {
					Bundle bunldeObj = new Bundle();
					bunldeObj.putString("position", "4");
					CallLogsFragment mCallLogsFragment = new CallLogsFragment();
					DashBoardActivity.replaceFragementsClickBack(mCallLogsFragment, bunldeObj, "Call Logs");
//					Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
				} else {
					response = (String) result[1];
					AppUtils.showDialog(getActivity(), response);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean getContactList(Context context, String number) {
		
		boolean status = false; 
		try {
			ContentResolver cr = context.getContentResolver();
			Cursor cursor = context.getContentResolver().query(
					ContactsContract.Contacts.CONTENT_URI,
					null,
					null,
					null,
					ContactsContract.Contacts._ID /*+ " LIMIT "	+ PreCallListCount + "," + remaningContactList*/);

				while (cursor.moveToNext()) {
					displayName = "";
					emailAddress = "";
					phoneNumber = "";
					UserAddress = "";
					displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
					
					  try {
					        Cursor cur = context.getContentResolver().query(
					                ContactsContract.Data.CONTENT_URI,
					                null,
					                ContactsContract.Data.CONTACT_ID + "=" + id + " AND "
					                        + ContactsContract.Data.MIMETYPE + "='"
					                        + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
					                null);
					        if (cur != null) {
					            if (!cur.moveToFirst()) {
					            }
					        } else {
					        }
					    } catch (Exception e) {
					        e.printStackTrace();
					    }
					    
					if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
						Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = ?", new String[] { id },null);
						while (pCur.moveToNext()) {
							phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							
							if(PhoneNumberUtils.compare(phoneNumber, number))
							{
								System.out.println("exists   :");
								contactID = id;
								status = true;
								
								pCur.close();
								cursor.close();
								return status;
								
							}
							else
							{
								status = false;
							}
							
						}
						pCur.close();
					}
				}
				cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	} 
}

package com.hinj.app.activity.fragement;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hinj.app.activity.DashBoardActivity;
import com.hinj.app.activity.R;
import com.hinj.app.model.CallDetailBean;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.parsing.HingAppParsing;

public class CallLogsFragment extends Fragment {
	
	private ListView message_list_view;
	private ImageView incoming_imageView,outgoing_imageView,missed_imageView;
	private ArrayList<CallDetailBean> smsDetailBeans = new ArrayList<CallDetailBean>();
	private CallLogsFragment listener; 
	public static String id = "";
	public static String unique_call_id = "";
	
	public CallLogsFragment(){}
	
	public interface CallLogFragment {
		public void onItemClickedListener(String valueClicked);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_call_logs_list, container, false);
        
        message_list_view = (ListView) rootView.findViewById(R.id.message_list_view);
        
        incoming_imageView =  (ImageView) rootView.findViewById(R.id.incoming_imageView);
        outgoing_imageView =  (ImageView) rootView.findViewById(R.id.outgoing_imageView);
        missed_imageView =  (ImageView) rootView.findViewById(R.id.missed_imageView);
        
        incoming_imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				 new GetCallDetailAsynctask("incoming").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				
			}
		});
        
        outgoing_imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 new GetCallDetailAsynctask("outgoing").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				
			}
		});
 
        missed_imageView.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			 new GetCallDetailAsynctask("missed").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			
		}
	});
        
        return rootView;
    }
	
	public void registerForListener(CallLogsFragment dashBoardActivity) {
		this.listener = dashBoardActivity;
	
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		 new GetCallDetailAsynctask("incoming").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	 private class CallAdapter extends ArrayAdapter<CallDetailBean> {

			ArrayList<CallDetailBean> deviceArray;
			Context ctx;

			public CallAdapter(Context context, int textViewResourceId,ArrayList<CallDetailBean> objects) {
				super(context, textViewResourceId, objects);

				this.ctx = context;
				this.deviceArray = objects;
				notifyDataSetChanged();
			}
			
			@Override
			public CallDetailBean getItem(int position) {
				return super.getItem(position);
			}
			
			@Override
	        public int getCount() {
	            return deviceArray.size();
	        }

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) 
			{
				ViewHolder holder = null;
				if (convertView == null) 
				{
					holder = new ViewHolder();
					convertView = LayoutInflater.from(ctx).inflate(R.layout.list_item_call_logs, parent, false);
					
					holder.call_logs_row_mainLL = (LinearLayout) convertView.findViewById(R.id.call_logs_row_mainLL);
					holder.sender_name_text_view = (TextView) convertView.findViewById(R.id.sender_name_text_view);
					holder.test_msg_text_view = (TextView) convertView.findViewById(R.id.test_msg_text_view);
					holder.call_time_text_view = (TextView) convertView.findViewById(R.id.call_time_text_view);
					holder.contact_imageView = (ImageView) convertView.findViewById(R.id.contact_imageView);
					
					convertView.setTag(holder);
				}
				else 
				{
	                holder = (ViewHolder)convertView.getTag();
	            }
				
				holder.sender_name_text_view.setText(deviceArray.get(position).getPhone()); 
				holder.test_msg_text_view.setText(deviceArray.get(position).getDuration());
				holder.call_time_text_view.setText(AppUtils.getCurrentTimeStamp(getActivity().getApplicationContext(),
								Long.parseLong(deviceArray.get(position).getCall_time())));
		        
				holder.call_logs_row_mainLL.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						id = deviceArray.get(position).getId();
						unique_call_id = deviceArray.get(position).getCallUniqueId();
						HinjAppPreferenceUtils.setCallRecipientNumber(getActivity(), deviceArray.get(position).getPhone());
						CallLogDetailFragment mCallLogDetailFragment  = new CallLogDetailFragment();
						Bundle bunldeObj = new Bundle();
						bunldeObj.putString("position", "4");
						
						DashBoardActivity.replaceFragementsClick(mCallLogDetailFragment, bunldeObj,"Call Logs");
						
					}
				});
				
				holder.call_logs_row_mainLL.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {

				        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
			            builderSingle.setTitle("Options");
			            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
			            arrayAdapter.add("Add to contacts");
			            arrayAdapter.add("Call");
			            arrayAdapter.add("Send Text Message");
			            arrayAdapter.add("Delete from call logs");
			           
			            builderSingle.setAdapter(arrayAdapter,
			                    new DialogInterface.OnClickListener() {

			                        @Override
			                        public void onClick(DialogInterface dialog, int which) {
			                            String strName = arrayAdapter.getItem(which);

			                            if(strName.equalsIgnoreCase("Add to contacts"))
			                            {
			                    			String DisplayName = "";
			                    			 String MobileNumber = "";
			                    			 String HomeNumber = "";
			                    			 String WorkNumber = "";
			                    			 String emailID = "";
			                    			 String company = "";
			                    			 String jobTitle = "";

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
			                    			     deviceArray.get(position).getPhone()).build());
			                    			 }

			                    			 //------------------------------------------------------ Mobile Number                     
			                    			 if (MobileNumber != null) {
			                    			     ops.add(ContentProviderOperation.
			                    			     newInsert(ContactsContract.Data.CONTENT_URI)
			                    			         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
			                    			         .withValue(ContactsContract.Data.MIMETYPE,
			                    			     ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
			                    			         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,  deviceArray.get(position).getPhone())
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
			                    			         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,  deviceArray.get(position).getPhone())
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
			                    			         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,  deviceArray.get(position).getPhone())
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
			                    			     
//			                    			     Toast.makeText(getActivity(), "Contact Added", Toast.LENGTH_SHORT).show();
			                    			 } catch (Exception e) {
			                    			     e.printStackTrace();
//			                    			     Toast.makeText(getActivity(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
			                    			 } 
			                            }
			                            else if(strName.equalsIgnoreCase("Call"))
			                            {
			                            	try
										    {
										        Intent callIntent=new Intent(Intent.ACTION_CALL);
										        callIntent.setData(Uri.parse("tel:"+deviceArray.get(position).getPhone()));
										        startActivity(callIntent);
										    }
										    catch(ActivityNotFoundException ex){
										            ex.toString();
										            AppUtils.showDialog(getActivity(), ex.toString());
										    }
			                            }
			                            else if(strName.equalsIgnoreCase("Send Text Message"))
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
			                            else if(strName.equalsIgnoreCase("Delete from call logs"))
			                            {
			                            	new DeleteCallLogAsynctask(deviceArray.get(position).getId(),deviceArray.get(position).getCallUniqueId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			                            	deviceArray.remove(position);
											notifyDataSetChanged();
			                            }
			                        }
			                    });
			            builderSingle.show();
			            
						return false;
					}
				});
				
				return convertView;
			}
		}
		
		public static class ViewHolder {
		        public TextView sender_name_text_view,test_msg_text_view,sms_time_text_view,call_time_text_view;
		        public ImageView contact_imageView;
		        public LinearLayout call_logs_row_mainLL;
		}
		
		public class GetCallDetailAsynctask extends AsyncTask<String, String,Object[]> {
			
			ProgressDialog progressDialog ;
			String response = "";
			String callType="";
			
			public GetCallDetailAsynctask(String str) {
				this.callType = str;
			}
			
			@Override
			public void onPreExecute() {
				progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
			}

			@Override
			public Object[] doInBackground(String... params) {
				
				return HingAppParsing.getCalls(getActivity(), HinjAppPreferenceUtils.getChildUploadDeviceId(getActivity()),
									HinjAppPreferenceUtils.getChildRaId(getActivity())); 
			}  
	 
			@Override
			public void onPostExecute(Object[] result) {
				progressDialog.cancel();
				try{
					boolean status = (Boolean) result[0];
					response = (String) result[1];
					
					if(status)
					{
						smsDetailBeans = (ArrayList<CallDetailBean>) result[2] ; 
						ArrayList<CallDetailBean> callDetailBeans = new ArrayList<CallDetailBean>();
						
						if(callType.equalsIgnoreCase("incoming"))
						{
							for(int i=0;i<smsDetailBeans.size();i++)
							{
								if(smsDetailBeans.get(i).getDirection().equalsIgnoreCase("Incoming")) 
								{
									callDetailBeans.add(smsDetailBeans.get(i));
								}
							}
						}
						else if(callType.equalsIgnoreCase("outgoing"))
						{
							for(int i=0;i<smsDetailBeans.size();i++)
							{
								if(smsDetailBeans.get(i).getDirection().equalsIgnoreCase("Outgoing")) 
								{
									callDetailBeans.add(smsDetailBeans.get(i));
								}
							}
						}
						else if(callType.equalsIgnoreCase("missed"))
						{
							for(int i=0;i<smsDetailBeans.size();i++)
							{
								if(smsDetailBeans.get(i).getDirection().equalsIgnoreCase("Missed")) 
								{
									callDetailBeans.add(smsDetailBeans.get(i));
								}
							}
						}
						
						CallAdapter adapter = new CallAdapter(getActivity(), 0, callDetailBeans); 
					    message_list_view.setAdapter(adapter);
					        
//						Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
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
		
		public class DeleteCallLogAsynctask extends AsyncTask<String, String,Object[]> {
			
			ProgressDialog progressDialog ;
			String response = "";
			String id="",call_unique_id="";
			
			public DeleteCallLogAsynctask(String id,String call_unique_id) {
				this.id=id;
				this.call_unique_id =call_unique_id;
			}
			
			@Override
			public void onPreExecute() {
				progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
			}

			@Override
			public Object[] doInBackground(String... params) {
				
				return HingAppParsing.deleteCallLog(getActivity(), id,call_unique_id); 
			}  
	 
			@Override
			public void onPostExecute(Object[] result) {
				progressDialog.cancel();
				try
				{
					boolean status = (Boolean) result[0];
					response = (String) result[1];

					if (status) {
//						Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
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
}

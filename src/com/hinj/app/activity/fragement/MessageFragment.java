package com.hinj.app.activity.fragement;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.hinj.app.activity.DashBoardActivity;
import com.hinj.app.activity.R;
import com.hinj.app.model.SmsDetailBean;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.parsing.HingAppParsing;

public class MessageFragment extends Fragment {
	
	private com.costum.android.widget.LoadMoreListView message_list_view;
	private ImageView compose_message_imageView, image_message_folder_search;
	private ArrayList<SmsDetailBean> smsDetailBeans = new ArrayList<SmsDetailBean>();
	private ArrayList<SmsDetailBean> smsDetailBeansSearch = new ArrayList<SmsDetailBean>();
	private ArrayList<SmsDetailBean> smsDetailBeans_total = new ArrayList<SmsDetailBean>();
	private static int offset1=0, limit1=14;
	public static int arrayListLength = 0;
	
	private LinearLayout lay_edit_messageFolder;
	private EditText editTextmessageFolderSearch;
	
	private boolean isSearchVisibleMessageFragment=false;
	
	public MessageFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_message_list, container, false);

        smsDetailBeans.clear();
        smsDetailBeansSearch.clear();
        smsDetailBeans_total.clear();
        
        message_list_view = (com.costum.android.widget.LoadMoreListView) rootView.findViewById(R.id.message_list_view);
        compose_message_imageView = (ImageView) rootView.findViewById(R.id.compose_message_imageView);
        image_message_folder_search = (ImageView) rootView.findViewById(R.id.image_message_folder_search);
        
        editTextmessageFolderSearch = (EditText) rootView.findViewById(R.id.editTextmessageFolderSearch);
        lay_edit_messageFolder = (LinearLayout) rootView.findViewById(R.id.lay_edit_messageFolder);
        
        compose_message_imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				 Intent intent = new Intent("android.intent.action.VIEW");
		            
		            /** creates an sms uri */
		            Uri data = Uri.parse("sms:");

		            /** Setting sms uri to the intent */
		            intent.setData(data);

		            /** Initiates the SMS compose screen, because the activity contain ACTION_VIEW and sms uri */
		            startActivity(intent);
			}
		});    
        
        ((LoadMoreListView) message_list_view).setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				
					if (arrayListLength == 14) {
						arrayListLength = 0;
						new GetSmsDetailAsynctask(offset1+15, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						((LoadMoreListView) message_list_view).onLoadMore();
					}else {
						((LoadMoreListView) message_list_view).onLoadMoreComplete();
					}
			}
		});
        
        image_message_folder_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(isSearchVisibleMessageFragment)
				{
					lay_edit_messageFolder.setVisibility(View.GONE);
				}
				else
				{
					lay_edit_messageFolder.setVisibility(View.VISIBLE);
				}
				isSearchVisibleMessageFragment = !isSearchVisibleMessageFragment;
			}
		});
        
        
        editTextmessageFolderSearch.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				v.setFocusable(true);
				v.setFocusableInTouchMode(true);
				return false;
			}
		});
        
        editTextmessageFolderSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence t, int start, int before,
					int count) {
				smsDetailBeansSearch.clear();
					for (int i = 0; i < smsDetailBeans_total.size(); i++) {
						if (smsDetailBeans_total.get(i).getSender_number().toLowerCase()
								.contains(t)) {
							smsDetailBeansSearch.add(smsDetailBeans_total.get(i));
						}
					}
					if (smsDetailBeansSearch.size() > 0) {
						message_list_view.setAdapter(new SMSAdapter(getActivity(),0, smsDetailBeansSearch));
					} else {
						message_list_view.setAdapter(new SMSAdapter(getActivity(),0, smsDetailBeansSearch));
					}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
        
        return rootView;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		offset1=0; limit1=14;
		 new GetSmsDetailAsynctask(offset1, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	private class SMSAdapter extends ArrayAdapter<SmsDetailBean> {

		ArrayList<SmsDetailBean> deviceArray;
		Context ctx;

		public SMSAdapter(Context context, int textViewResourceId,ArrayList<SmsDetailBean> objects) {
			super(context, textViewResourceId, objects);

			this.ctx = context;
			this.deviceArray = objects;
			notifyDataSetChanged();
		}
		
		@Override
		public SmsDetailBean getItem(int position) {
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
				convertView = LayoutInflater.from(ctx).inflate(R.layout.list_item_message, parent, false);
				
				holder.message_row_mainLL = (LinearLayout) convertView.findViewById(R.id.message_row_mainLL);
				holder.sender_name_text_view = (TextView) convertView.findViewById(R.id.sender_name_text_view);
				holder.test_msg_text_view = (TextView) convertView.findViewById(R.id.test_msg_text_view);
				holder.sms_time_text_view = (TextView) convertView.findViewById(R.id.sms_time_text_view);
				holder.contact_imageView = (ImageView) convertView.findViewById(R.id.contact_imageView);
				
				convertView.setTag(holder);
			}
			else 
			{
                holder = (ViewHolder)convertView.getTag();
            }
			
			holder.sender_name_text_view.setText(deviceArray.get(position).getSender_number());
			holder.test_msg_text_view.setText(deviceArray.get(position).getText_message());
			holder.sms_time_text_view.setText(AppUtils.getCurrentTimeStamp(getActivity(), Long.parseLong(deviceArray.get(position).getModified())));
			
			holder.message_row_mainLL.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(editTextmessageFolderSearch.getWindowToken(), 0);
					set(position);
					
			        MessageDetailFragment mMessageDetailFragment  = new MessageDetailFragment();
					Bundle bunldeObj = new Bundle();
					bunldeObj.putString("position", "4");
					DashBoardActivity.replaceFragementsClick(mMessageDetailFragment, bunldeObj,"Messages");
				}
			});
			
			holder.message_row_mainLL.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {

			        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
		            builderSingle.setTitle("Message Options");
		            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
		            arrayAdapter.add("Call Number");
		            arrayAdapter.add("Delete");
		           
		           /* builderSingle.setNegativeButton("Cancel",
		                    new DialogInterface.OnClickListener() {

		                        @Override
		                        public void onClick(DialogInterface dialog, int which) {
		                            dialog.dismiss();
		                        }
		                    });*/

		            builderSingle.setAdapter(arrayAdapter,
		                    new DialogInterface.OnClickListener() {

		                        @Override
		                        public void onClick(DialogInterface dialog, int which) {
		                            String strName = arrayAdapter.getItem(which);

		                            if(strName.equalsIgnoreCase("Call Number"))
		                            {
									    try
									    {
									        Intent callIntent=new Intent(Intent.ACTION_CALL);
									        callIntent.setData(Uri.parse("tel:"+deviceArray.get(position).getRecipient_number()));
									        startActivity(callIntent);
									    }
									    catch(ActivityNotFoundException ex){
									            ex.toString();
									            AppUtils.showDialog(getActivity(), ex.toString());
									    }
		                            }
		                            else if(strName.equalsIgnoreCase("Delete"))
		                            {
										new DeleteSmsAsynctask(deviceArray.get(position).getId(),deviceArray.get(position).getSms_unique_id()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

		protected void set(int position) {
			HinjAppPreferenceUtils.setMessageChatRecipientNumber(getActivity(), smsDetailBeans_total.get(position).getSender_number());
		}
	}
	
	public static class ViewHolder {
	        public TextView sender_name_text_view,test_msg_text_view,sms_time_text_view;
	        public ImageView contact_imageView;
	        public LinearLayout message_row_mainLL;
	}
	  
	public class GetSmsDetailAsynctask extends AsyncTask<String, String,Object[]> {
		
		ProgressDialog progressDialog ;
		String response = "";
		int offset, limit;
		
		public GetSmsDetailAsynctask(int offset1, int limit1) {
			offset = offset1;
			limit = limit1;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.getSmsDetails(getActivity(), HinjAppPreferenceUtils.getChildUploadDeviceId(getActivity()),
								HinjAppPreferenceUtils.getChildRaId(getActivity()), offset, limit); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try
			{
				boolean status = (Boolean) result[0];
				response = (String) result[1];

				if (status) {
					smsDetailBeans = (ArrayList<SmsDetailBean>) result[2];
					
					for (int i = 0; i < smsDetailBeans.size(); i++) {
						smsDetailBeans_total.add(smsDetailBeans.get(i));
					}
					
					message_list_view.setAdapter(new SMSAdapter(getActivity(),0, smsDetailBeans_total));

//					Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
				} else {
					response = (String) result[1];
					AppUtils.showDialog(getActivity(), response);
				}
				
				((LoadMoreListView) message_list_view).onLoadMoreComplete();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public class DeleteSmsAsynctask extends AsyncTask<String, String,Object[]> {
		
		ProgressDialog progressDialog ;
		String response = "";
		String id="",smsUniqueId="";
		
		public DeleteSmsAsynctask(String id, String smsUniqueId) {
			this.id=id;
			this.smsUniqueId = smsUniqueId;
		}
		
		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.deleteSms(getActivity(), id, smsUniqueId); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try
			{
				boolean status = (Boolean) result[0];
				response = (String) result[1];

				if (status) {
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
}

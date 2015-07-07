package com.hinj.app.activity.fragement;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.hinj.app.activity.DashBoardActivity;
import com.hinj.app.activity.R;
import com.hinj.app.model.ContactDetailBean;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.imageload.FileSystemPersistence;
import com.hinj.imageload.HttpImageManager;
import com.hinj.parsing.HingAppParsing;

public class ContactsFragment extends Fragment {
	
	private com.costum.android.widget.LoadMoreListView contact_list_view;
	private ArrayList<ContactDetailBean> smsDetailBeans = new ArrayList<ContactDetailBean>();
	public ArrayList<ContactDetailBean> smsDetailBeans_total = new ArrayList<ContactDetailBean>();
	private ArrayList<ContactDetailBean> smsDetailBeans_delete = new ArrayList<ContactDetailBean>();
	
	public ContactsFragment(){}
	public static boolean[] isCheck_contact;
	public int pos;
	public ViewHolder holder = null;
	public ImageView delete_contact, contact_select_all;
	public static String multipul_id = "", mobileNumber = "";
	public boolean all_cleck = false;
	private ViewHolder mHolder;
	private static int offset1=0, limit1=14;
	public static int arrayListLength = 0;
	
	public static final String BASEDIR = "/sdcard/httpimage";
	public HttpImageManager mHttpImageManager;
	ContactsAdapter adapter;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        
        contact_list_view = (com.costum.android.widget.LoadMoreListView) rootView.findViewById(R.id.contact_list_view);
        delete_contact = (ImageView) rootView.findViewById(R.id.delete_contact);
        contact_select_all = (ImageView) rootView.findViewById(R.id.contact_select_all);
        
        delete_contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (int i = 0; i < smsDetailBeans_total.size(); i++) {
					if (isCheck_contact[i]) {
						smsDetailBeans_delete.add(new ContactDetailBean(smsDetailBeans_total.get(i).getId(),smsDetailBeans_total.get(i).getMobile()));
					}
				}
				
				smsDetailBeans_delete.size();
				for (int j = 0; j < smsDetailBeans_delete.size(); j++) {
					if (smsDetailBeans_delete.size() == 1) {
						new DeleteAsynctask(smsDetailBeans_delete.get(j).getId(),smsDetailBeans_delete.get(j).getMobile()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						break;
					}else {
						new DeleteAsynctaskMultipul(smsDetailBeans_delete).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						break;
					}
				}
			}
		});
        
        contact_select_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (int i = 0; i < ContactsFragment.isCheck_contact.length; i++) {
					if (ContactsFragment.isCheck_contact[i]) {
		        		ContactsFragment.isCheck_contact[i] = false;
		        		contact_select_all.setImageResource(R.drawable.icon_check_box);
					}else {
						ContactsFragment.isCheck_contact[i] = true;
						contact_select_all.setImageResource(R.drawable.icon_checked_box);
					}
				}
				
				ContactsAdapter adapter = new ContactsAdapter(getActivity(), 0, smsDetailBeans_total); 
				contact_list_view.setAdapter(adapter);
			}
		});
        
        
        contact_list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, final int mPosition, long arg3) {
				final ImageView check_box_contact = (ImageView) v.findViewById(R.id.check_box_contact);
				LinearLayout lay_check_box_contact = (LinearLayout) v.findViewById(R.id.lay_check_box_contact);
				LinearLayout cotact_lay = (LinearLayout) v.findViewById(R.id.cotact_lay);
				
				lay_check_box_contact.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(ContactsFragment.isCheck_contact[mPosition]==false){
			        		ContactsFragment.isCheck_contact[mPosition]=true;
			        		check_box_contact.setImageResource(R.drawable.icon_check_box);
						}else{
							check_box_contact.setImageResource(R.drawable.icon_checked_box);
						}
					}
				});
				
				cotact_lay.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						CotactDetailFragment mCotactDetailFragment  = new CotactDetailFragment(
								smsDetailBeans_total.get(mPosition).getAddress(),
								smsDetailBeans_total.get(mPosition).getCompose_name(),
								smsDetailBeans_total.get(mPosition).getCreated(),
								smsDetailBeans_total.get(mPosition).getDateStr(),
								smsDetailBeans_total.get(mPosition).getEmailAdd(),
								smsDetailBeans_total.get(mPosition).getFirstname(),
								smsDetailBeans_total.get(mPosition).getId(),
								smsDetailBeans_total.get(mPosition).getImage(),
								smsDetailBeans_total.get(mPosition).getJobTitleStr(),
								smsDetailBeans_total.get(mPosition).getLast_name(),
								smsDetailBeans_total.get(mPosition).getLatitude(),
								smsDetailBeans_total.get(mPosition).getLongitude(),
								smsDetailBeans_total.get(mPosition).getMobile(),
								smsDetailBeans_total.get(mPosition).getOrgStr(),
								smsDetailBeans_total.get(mPosition).getPhone(),
								smsDetailBeans_total.get(mPosition).getZipCode());
						Bundle bunldeObj = new Bundle();
						bunldeObj.putString("position", "4");
						DashBoardActivity.replaceFragementsClick(mCotactDetailFragment, bunldeObj,"Contacts");
					}
				});
			}
		});
        
        ((LoadMoreListView) contact_list_view).setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				
					if (arrayListLength == 14) {
						arrayListLength = 0;
						new GetContactsAsynctask(offset1+15, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						((LoadMoreListView) contact_list_view).onLoadMore();
					}else {
						((LoadMoreListView) contact_list_view).onLoadMoreComplete();
					}
				}
			});
        	return rootView;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		offset1=0; limit1=14;
		 new GetContactsAsynctask(offset1, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	 private class ContactsAdapter extends ArrayAdapter<ContactDetailBean> {

			ArrayList<ContactDetailBean> deviceArray;
			Context ctx;

			public ContactsAdapter(Context context, int textViewResourceId,ArrayList<ContactDetailBean> objects) {
				super(context, textViewResourceId, objects);

				this.ctx = context;
				this.deviceArray = objects;
				notifyDataSetChanged();
			}
			
			@Override
			public ContactDetailBean getItem(int position) {
				return super.getItem(position);
			}
			
			@Override
	        public int getCount() {
	            return deviceArray.size();
	        }

			@SuppressLint("UseValueOf")
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) 
			{
				
				if (convertView == null) 
				{
					holder = new ViewHolder();
					convertView = LayoutInflater.from(ctx).inflate(R.layout.list_item_contacts, parent, false);
					
					holder.contact_name_text_view = (TextView) convertView.findViewById(R.id.contact_name_text_view);
					holder.check_box_contact = (ImageView) convertView.findViewById(R.id.check_box_contact);
					holder.lay_check_box_contact = (LinearLayout) convertView.findViewById(R.id.lay_check_box_contact);
					holder.cotact_lay = (LinearLayout) convertView.findViewById(R.id.cotact_lay);
					
					holder.contact_imageView = (ImageView) convertView.findViewById(R.id.contact_imageView);
					
					convertView.setTag(holder);
				}
				else 
				{
	                holder = (ViewHolder)convertView.getTag();
	            }
				
				mHttpImageManager = new HttpImageManager(HttpImageManager.createDefaultMemoryCache(), new FileSystemPersistence(BASEDIR));
				Uri uri = Uri.parse(deviceArray.get(position).getImage());
				Bitmap bitmap = mHttpImageManager.loadImage(new HttpImageManager.LoadRequest(uri, holder.contact_imageView));
				if (bitmap != null) {

					holder.contact_imageView.setImageBitmap(bitmap);
				}
				
				
				if (isCheck_contact[position]) {
					holder.check_box_contact.setImageResource(R.drawable.icon_checked_box);
				} else {
					holder.check_box_contact.setImageResource(R.drawable.icon_check_box);
				}
				
				holder.contact_name_text_view.setText(deviceArray.get(position).getFirstname());
		        
				return convertView;
			}
		}
		
		public static class ViewHolder {
		        public TextView contact_name_text_view,test_msg_text_view,sms_time_text_view;
		        public ImageView contact_imageView, check_box_contact;
		        public LinearLayout lay_check_box_contact, cotact_lay;
		}
		
		public class GetContactsAsynctask extends AsyncTask<String, String,Object[]> {
			
			ProgressDialog progressDialog ;
			String response = "";
			
			public GetContactsAsynctask(int i, int j) {
				offset1 = i;
				limit1 = j;
			}

			@Override
			public void onPreExecute() {
				progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
			}

			@Override
			public Object[] doInBackground(String... params) {
				
				return HingAppParsing.getContactsDetails(getActivity(), HinjAppPreferenceUtils.getChildUploadDeviceId(getActivity()),
									HinjAppPreferenceUtils.getChildRaId(getActivity()), offset1, limit1); 
			}  
	 
			@Override
			public void onPostExecute(Object[] result) {
				progressDialog.cancel();
				try{
					boolean status = (Boolean) result[0];
					response = (String) result[1];
					
					if(status)
					{
						smsDetailBeans = (ArrayList<ContactDetailBean>) result[2] ; 
						
						for (int i = 0; i < smsDetailBeans.size(); i++) {
							smsDetailBeans_total.add(smsDetailBeans.get(i));
						}
						
						isCheck_contact = new boolean[smsDetailBeans_total.size()];
						// Locate listview last item
						int position = contact_list_view.getLastVisiblePosition();
						adapter = new ContactsAdapter(getActivity(), 0, smsDetailBeans_total); 
						adapter.notifyDataSetChanged();  
						contact_list_view.setAdapter(adapter);
						// Show the latest retrived results on the top
						contact_list_view.setSelectionFromTop(position, 0);
						
//						Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
					}
					else
					{
						response = (String) result[1];
						AppUtils.showDialog(getActivity(), response);
					}
					((LoadMoreListView) contact_list_view).onLoadMoreComplete();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		public class DeleteAsynctask extends AsyncTask<String, String,Object[]> {
			
			ProgressDialog progressDialog ;
			String response = "";
			String value,number;
			
			public DeleteAsynctask(String value,String number) {
				this.value = value;
				this.number=number;
			}

			@Override
			public void onPreExecute() {
				progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
			}

			@Override
			public Object[] doInBackground(String... params) {
				
				return HingAppParsing.deleteSingleContact(getActivity(), HinjAppPreferenceUtils.getChildRaId(getActivity()), "4", value,number); 
			}  
	 
			@Override
			public void onPostExecute(Object[] result) {
				progressDialog.cancel();
				try{
					smsDetailBeans.clear();
					smsDetailBeans_total.clear();
					smsDetailBeans_delete.clear();
					offset1=0; limit1=14;
					new GetContactsAsynctask(offset1, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		public class DeleteAsynctaskMultipul extends AsyncTask<String, String,Object[]> {
			
			ProgressDialog progressDialog ;
			String response = "";
			String value;
			ArrayList<ContactDetailBean> smsDetailBeans_delete_;
			
			
			public DeleteAsynctaskMultipul(ArrayList<ContactDetailBean> smsDetailBeans_delete) {
				this.smsDetailBeans_delete_ = smsDetailBeans_delete;
			}

			@Override
			public void onPreExecute() {
				progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
			}

			@Override
			public Object[] doInBackground(String... params) {
				
				for (int i = 0; i < smsDetailBeans_delete_.size(); i++) {
					if(i == smsDetailBeans_delete_.size() -1){
						multipul_id = multipul_id+smsDetailBeans_delete_.get(i).getId();
						mobileNumber = mobileNumber+smsDetailBeans_delete_.get(i).getMobile();
					}else{
						multipul_id = multipul_id+smsDetailBeans_delete_.get(i).getId()+",";
						mobileNumber = mobileNumber+smsDetailBeans_delete_.get(i).getMobile()+",";
					}
				}
				
				return HingAppParsing.deleteContactMultipuleContacts(getActivity(), HinjAppPreferenceUtils.getChildRaId(getActivity()), "4", multipul_id, mobileNumber); 
			}  
	 
			@Override
			public void onPostExecute(Object[] result) {
				progressDialog.cancel();
				try{
					smsDetailBeans.clear();
					smsDetailBeans_total.clear();
					smsDetailBeans_delete.clear();
					offset1=0; limit1=14;
					new GetContactsAsynctask(offset1+15, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
}

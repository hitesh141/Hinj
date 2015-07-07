package com.hinj.app.activity.fragement;

import java.net.URLDecoder;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.hinj.app.activity.R;
import com.hinj.app.model.BrowserHistoryBean;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.parsing.HingAppParsing;

public class BrowserHistoryFragment extends Fragment {
	
	private com.costum.android.widget.LoadMoreListView browser_list_view;
	private ArrayList<BrowserHistoryBean> smsDetailBeans = new ArrayList<BrowserHistoryBean>();
	private ArrayList<BrowserHistoryBean> smsDetailBeans_total = new ArrayList<BrowserHistoryBean>();
	private static int offset1=0, limit1=14;
	public static int arrayListLength = 0;
	
	public BrowserHistoryFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_browser_history_list, container, false);
        
        browser_list_view = (com.costum.android.widget.LoadMoreListView) rootView.findViewById(R.id.message_list_view);
        
        browser_list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLDecoder.decode(smsDetailBeans_total.get(arg2).getPage_url())));
			    	startActivity(browserIntent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
        
        ((LoadMoreListView) browser_list_view).setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
					if (arrayListLength == 14) {
						arrayListLength = 0;
						 new GetBrowserDetailAsynctask(offset1+15, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						//new GetSmsDetailAsynctask(offset1+15, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						((LoadMoreListView) browser_list_view).onLoadMore();
					}else {
						((LoadMoreListView) browser_list_view).onLoadMoreComplete();
					}
			}
		});
        return rootView;
    }
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		offset1=0; limit1=14;
		new GetBrowserDetailAsynctask(offset1, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	 private class BrowserHistoryAdapter extends ArrayAdapter<BrowserHistoryBean> {

			ArrayList<BrowserHistoryBean> deviceArray;
			Context ctx;

			public BrowserHistoryAdapter(Context context, int textViewResourceId,ArrayList<BrowserHistoryBean> objects) {
				super(context, textViewResourceId, objects);

				this.ctx = context;
				this.deviceArray = objects;
				notifyDataSetChanged();
			}
			
			@Override
			public BrowserHistoryBean getItem(int position) {
				return super.getItem(position);
			}
			
			@Override
	        public int getCount() {
	            return deviceArray.size();
	        }

			@SuppressWarnings("deprecation")
			@Override
			public View getView(int position, View convertView, ViewGroup parent) 
			{
				ViewHolder holder = null;
				if (convertView == null) 
				{
					holder = new ViewHolder();
					convertView = LayoutInflater.from(ctx).inflate(R.layout.list_item_browser_history, parent, false);
					
					holder.title_text_view = (TextView) convertView.findViewById(R.id.title_text_view);
					holder.url_text_view = (TextView) convertView.findViewById(R.id.url_text_view);
					
					convertView.setTag(holder);
				}
				else 
				{
	                holder = (ViewHolder)convertView.getTag();
	            }
				
				holder.title_text_view.setText(URLDecoder.decode(deviceArray.get(position).getPage_title()));
				holder.url_text_view.setText(URLDecoder.decode(deviceArray.get(position).getPage_url()));
		        
				return convertView;
			}
		}
		
	 public static class ViewHolder {
		       public TextView title_text_view,url_text_view;
		       public ImageView contact_imageView;
	 }
		
		public class GetBrowserDetailAsynctask extends AsyncTask<String, String,Object[]> {
			
			ProgressDialog progressDialog ;
			String response = "";
			
			int offset, limit;
			
			public GetBrowserDetailAsynctask(int offset1, int limit1) {
				offset = offset1;
				limit = limit1;
			}
			
			@Override
			public void onPreExecute() {
				progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
			}

			@Override
			public Object[] doInBackground(String... params) {
				
				return HingAppParsing.getBrowserDetails(getActivity(), HinjAppPreferenceUtils.getChildUploadDeviceId(getActivity()),
									HinjAppPreferenceUtils.getChildRaId(getActivity()),offset, limit); 
			}  
	 
			@Override
			public void onPostExecute(Object[] result) {
				progressDialog.cancel();
				try{
					boolean status = (Boolean) result[0];
					response = (String) result[1];
					
					if(status)
					{
						smsDetailBeans = (ArrayList<BrowserHistoryBean>) result[2] ; 
						
						for (int i = 0; i < smsDetailBeans.size(); i++) {
							smsDetailBeans_total.add(smsDetailBeans.get(i));
						}
						int position = browser_list_view.getLastVisiblePosition();
						BrowserHistoryAdapter adapter = new BrowserHistoryAdapter(getActivity(), 0, smsDetailBeans_total); 
						adapter.notifyDataSetChanged();  
					    browser_list_view.setAdapter(adapter);
					    
					    browser_list_view.setSelectionFromTop(position, 0);
					        
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
}

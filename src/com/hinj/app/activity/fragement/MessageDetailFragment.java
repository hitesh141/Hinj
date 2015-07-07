package com.hinj.app.activity.fragement;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hinj.app.activity.DashBoardActivity;
import com.hinj.app.activity.R;
import com.hinj.app.model.SmsChatDetailBean;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.parsing.HingAppParsing;

public class MessageDetailFragment extends Fragment {
	private ListView linechatDetailList;
	private int pageNo=1;
	private View footerView;
	//ArrayList<LineChatDetailsBean> totalEventBeans=new ArrayList<LineChatDetailsBean>();
	
	private static final String TAG="LineChatDetailsActivity";
	private TextView contact_text_view;
	//private DisplayImageOptions options;
	//ImageLoader imnew;
	private ImageView linedesc_back_image,linedesc_home_image;
	private String user_id, group_id;
	
	private ImageView back_imageView;
	
	ArrayList<SmsChatDetailBean> smChatDetailBeans;
	private LinearLayout back_message_deail;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 
		View rootView = inflater.inflate(R.layout.fragment_message_detail_list, container, false);
		 
		contact_text_view=(TextView)rootView.findViewById(R.id.contact_text_view);
		back_imageView = (ImageView)rootView.findViewById(R.id.back_imageView);
		
		linechatDetailList=(ListView)rootView.findViewById(R.id.message_detail_layout_listView);
		back_message_deail = (LinearLayout) rootView.findViewById(R.id.back_message_deail);
		
		new GetSmsDetailAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		
		contact_text_view.setText(HinjAppPreferenceUtils.getMessageChatRecipientNumber(getActivity()));
		
		back_message_deail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bunldeObj = new Bundle();
				bunldeObj.putString("position", "4");
				MessageFragment mMessageFragment = new MessageFragment();
				DashBoardActivity.replaceFragementsClickBack(mMessageFragment, bunldeObj, "Message");
			}
		});

		linechatDetailList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int possion, long arg3) {
				AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
	            builderSingle.setTitle("Message Options");
	            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
	            arrayAdapter.add("Copy");
	            arrayAdapter.add("Delete");
	           
	            builderSingle.setAdapter(arrayAdapter,
	                    new DialogInterface.OnClickListener() {

	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                        	if (which == 0) {
	                        		ClipboardManager myClipboard = (ClipboardManager)getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
		        					myClipboard.setText(smChatDetailBeans.get(possion).getText_message());
								}else {
									new DeleteSmsDetailAsynctask(smChatDetailBeans.get(possion).getId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
								}
	                        	
	                        }
	                    });
	            builderSingle.show();
				return false;
			}
		});
        return rootView;
	}


	public void onBackPressed(){
	  if (getFragmentManager().getBackStackEntryCount() == 1){
	    getActivity().finish();
	  }
	 
	}
	
	public class GetSmsDetailAsynctask extends AsyncTask<String, String,Object[]> {
		
		ProgressDialog progressDialog ;
		String response = "";
		
		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait", true);
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.getSmsChatDetails(getActivity(), "", HinjAppPreferenceUtils.getChildUploadDeviceId(getActivity()),"0"); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try
			{
				boolean status = (Boolean) result[0];
				response = (String) result[1];

				if (status) {
					smChatDetailBeans = (ArrayList<SmsChatDetailBean>) result[2];

					ListViewAdapter adapter = new ListViewAdapter(smChatDetailBeans);
					linechatDetailList.setAdapter(adapter);

					//contact_text_view.setText(HinjAppPreferenceUtils.getMessageChatRecipientNumber(getActivity())); 
					
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


	static class ViewHolder {
		TextView wechatsendText;
		TextView wechatsendTextTime;
		TextView wechatReceiveText;
		TextView wechatReceiveTextTime;
		RelativeLayout sent;
		RelativeLayout receive;
		ImageView sentStricker;
		ImageView receiveSticker;
	}
	
	// chat list adapter class
	public class ListViewAdapter extends BaseAdapter 
	{
		ArrayList<SmsChatDetailBean> totalEventBeans;

		public ListViewAdapter(ArrayList<SmsChatDetailBean> totalEventBeans) {
			this.totalEventBeans=totalEventBeans;

		}

		@Override
		public int getCount() {
			return totalEventBeans.size();
		}

		@Override
		public Object getItem(int arg0) {
			return totalEventBeans.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_messgae_detail, null);
				holder = new ViewHolder();
				holder.sent=(RelativeLayout)convertView.findViewById(R.id.chatdetailrawlayout_RLTopSender);
				holder.receive=(RelativeLayout)convertView.findViewById(R.id.chatdetailrawlayout_RLBottomReceiver);
				holder.wechatsendText = (TextView) convertView.findViewById(R.id.chatdetailrawlayout_lastMessageSendertext);
				holder.wechatsendTextTime = (TextView) convertView.findViewById(R.id.chatdetailrawlayout_lastMessageSendertime);
				holder.wechatReceiveText = (TextView) convertView.findViewById(R.id.chatdetailrawlayout_lastMessageReceivertext);
				holder.wechatReceiveTextTime = (TextView) convertView.findViewById(R.id.chatdetailrawlayout_lastMessageReceivertime);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.receive .setVisibility(View.VISIBLE);
				holder.wechatReceiveText .setVisibility(View.VISIBLE);
				holder.sent.setVisibility(View.VISIBLE);
				holder.wechatsendText.setVisibility(View.VISIBLE);
			}
			
			
			if(totalEventBeans.get(position).getSms_type().equals("Received")){
				if (totalEventBeans.get(position).getSms_type().equals("Received")) {
					holder.receive .setVisibility(View.GONE);
				
					holder.wechatsendText.setText(totalEventBeans.get(position).getText_message());
					holder.wechatsendTextTime.setText(AppUtils.getCurrentTimeStamp(getActivity(), Long.parseLong(totalEventBeans.get(position).getCreated())));
					holder.wechatsendTextTime.setTextColor(Color.RED);
	
				} else {
					holder.sent.setVisibility(View.GONE);
					holder.wechatReceiveText.setText(totalEventBeans.get(position).getText_message());
					holder.wechatReceiveTextTime.setText(AppUtils.getCurrentTimeStamp(getActivity(), Long.parseLong(totalEventBeans.get(position).getCreated())));
					holder.wechatReceiveTextTime.setTextColor(Color.RED);
				}
			}else if(totalEventBeans.get(position).getSms_type().equals("Sent")){
				if (totalEventBeans.get(position).getSms_type().equals("Sent")) {
					holder.receive .setVisibility(View.GONE);
					holder.wechatsendText.setVisibility(View.GONE);
					holder.wechatsendTextTime.setText(AppUtils.getCurrentTimeStamp(getActivity(), Long.parseLong(totalEventBeans.get(position).getCreated())));
					holder.wechatsendTextTime.setTextColor(Color.RED);
	
				} else {
					holder.sent.setVisibility(View.GONE);
					holder.wechatReceiveText.setVisibility(View.GONE);
					holder.receiveSticker.setVisibility(View.VISIBLE);

					holder.wechatReceiveTextTime.setText(AppUtils.getCurrentTimeStamp(getActivity(), Long.parseLong(totalEventBeans.get(position).getCreated())));
					holder.wechatReceiveTextTime.setTextColor(Color.RED);
				}
			}
			
			else if(totalEventBeans.get(position).getSms_type().equals("Sent")){
				if (totalEventBeans.get(position).getSms_type().equals("Sent")) {
					holder.receive .setVisibility(View.GONE);
					holder.wechatsendText.setText("Location");
					holder.wechatsendTextTime.setText(AppUtils.getCurrentTimeStamp(getActivity(), Long.parseLong(totalEventBeans.get(position).getCreated())));
					holder.wechatsendTextTime.setTextColor(Color.RED);
	
				} else {
					holder.sent.setVisibility(View.GONE);
					holder.wechatReceiveText.setText("Location");
					holder.wechatReceiveTextTime.setText(AppUtils.getCurrentTimeStamp(getActivity(), Long.parseLong(totalEventBeans.get(position).getCreated())));
					holder.wechatReceiveTextTime.setTextColor(Color.RED);
				}
			}

			else if(totalEventBeans.get(position).getSms_type().equals("Sent")){
				if (totalEventBeans.get(position).getSms_type().equals("Sent")) {
					holder.receive .setVisibility(View.GONE);
					holder.wechatsendText.setText("Voice");
					holder.wechatsendTextTime.setText(AppUtils.getCurrentTimeStamp(getActivity(), Long.parseLong(totalEventBeans.get(position).getCreated())));
					holder.wechatsendTextTime.setTextColor(Color.RED);
	
				} else {
					holder.sent.setVisibility(View.GONE);
					holder.wechatReceiveText.setText("Voice");
					holder.wechatReceiveTextTime.setText(AppUtils.getCurrentTimeStamp(getActivity(), Long.parseLong(totalEventBeans.get(position).getCreated())));
					holder.wechatReceiveTextTime.setTextColor(Color.RED);
				}
				
				
			}
			/** for images **/
			else if(totalEventBeans.get(position).getSms_type().equals("Sent")){
				
				if (totalEventBeans.get(position).getSms_type().equals("Sent")) {
					holder.receive .setVisibility(View.GONE);
					holder.wechatsendText.setVisibility(View.GONE);
					
					holder.sentStricker.setVisibility(View.VISIBLE);
					holder.wechatsendTextTime.setText(totalEventBeans.get(position).getCreated());
					holder.wechatsendTextTime.setTextColor(Color.RED);
						
				}
				else
				{
					holder.sent.setVisibility(View.GONE);
					
					holder.wechatReceiveText.setVisibility(View.GONE);
					holder.receiveSticker.setVisibility(View.VISIBLE);
					holder.wechatReceiveTextTime.setText(totalEventBeans.get(position).getCreated());
					holder.wechatReceiveTextTime.setTextColor(Color.RED);
			
				}
				System.out.println("IMAGEE");
			}
			return convertView;
		}
	}

	public class DeleteSmsDetailAsynctask extends AsyncTask<String, String,Object[]> {
		
		ProgressDialog progressDialog ;
		String response = "";
		String id="";
		
		public DeleteSmsDetailAsynctask(String id) {
			this.id=id;
		}
		
		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.deleteDetailSms(getActivity(), id); 
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
					new GetSmsDetailAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

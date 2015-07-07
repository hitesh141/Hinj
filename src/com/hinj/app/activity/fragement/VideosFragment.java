package com.hinj.app.activity.fragement;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.hinj.app.activity.DashBoardActivity;
import com.hinj.app.activity.R;
import com.hinj.app.model.MediaDetailBean;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.parsing.HingAppParsing;

public class VideosFragment extends Fragment {
	
	private com.costum.android.widget.LoadMoreListView video_folder_gridView;
	private ArrayList<MediaDetailBean> videoDetailBeans = new ArrayList<MediaDetailBean>();
	private ArrayList<MediaDetailBean> videoDetailBeansSearch = new ArrayList<MediaDetailBean>();
	private ArrayList<MediaDetailBean> videoDetailBeans_total = new ArrayList<MediaDetailBean>();
	private ArrayList<MediaDetailBean> videoDetailBeans_delete = new ArrayList<MediaDetailBean>();
	private ArrayList<String> GRID_DATA =  new ArrayList<String>();
	
	public static Context mContext;
	public static boolean[] isCheck_video;
	private ImageView video_delete_folder, video_select_folder, image_video_folder_search;
	private int count;
	public static String album_name_video = "";
	private boolean checkStatusClick_video=false;
	public static String multipul_id_music = "";
	private boolean click = false;
	private static int offset1=0, limit1=14;
	public static int arrayListLength = 0;
	private LinearLayout lay_edit_VideoFolder;
	private EditText editTextVideoFolderSearch;
	
	public VideosFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_videos_list, container, false);
        
        videoDetailBeans.clear();
        videoDetailBeansSearch.clear();
        videoDetailBeans_total.clear();
        videoDetailBeans_delete.clear();
        
        video_folder_gridView = (com.costum.android.widget.LoadMoreListView) rootView.findViewById(R.id.video_folder_gridView);
        video_delete_folder = (ImageView) rootView.findViewById(R.id.video_delete_folder);
        video_select_folder = (ImageView) rootView.findViewById(R.id.video_select_folder);
        image_video_folder_search = (ImageView) rootView.findViewById(R.id.image_video_folder_search);
        
        editTextVideoFolderSearch = (EditText) rootView.findViewById(R.id.editTextVideoFolderSearch);
        lay_edit_VideoFolder = (LinearLayout) rootView.findViewById(R.id.lay_edit_VideoFolder);
        
        // This Data show in grid ( Used by adapter )
        // GRID_DATA = new String[] {};
        
        video_folder_gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
            	final ImageView photo_single_itemCheckBox1 = (ImageView) v
						.findViewById(R.id.video_single_seclect_folder);
				LinearLayout video_folder_click = (LinearLayout) v.findViewById(R.id.video_folder_click);
				
				photo_single_itemCheckBox1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if (isCheck_video[position] == false) {
							isCheck_video[position] = true;
							photo_single_itemCheckBox1.setImageResource(R.drawable.icon_checked_box);
						} else {
							isCheck_video[position] = false;
							photo_single_itemCheckBox1.setImageResource(R.drawable.icon_check_box);
						}
					}
				});
				
				video_folder_click.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(editTextVideoFolderSearch.getWindowToken(), 0);
						
						if (videoDetailBeansSearch.size()>0) {
							album_name_video = videoDetailBeansSearch.get(position).getAlbum();
						}else {
							album_name_video = videoDetailBeans_total.get(position).getAlbum();
						}
		            	
		            	VideoDetailFragment mVideoDetailFragment  = new VideoDetailFragment();
						Bundle bunldeObj = new Bundle();
						bunldeObj.putString("position", "4");
						DashBoardActivity.replaceFragementsClick(mVideoDetailFragment, bunldeObj,"Videos");
					}
				});
            }
        });
        
        video_delete_folder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				videoDetailBeans_delete.clear();
				for (int i = 0; i < videoDetailBeans_total.size(); i++) {
					if (isCheck_video[i]) {
						videoDetailBeans_delete.add(new MediaDetailBean(videoDetailBeans_total.get(i).getAlbum()));
					}
				}
				for (int j = 0; j < videoDetailBeans_delete.size(); j++) {
					if (videoDetailBeans_delete.size() == 1) {
						new DeleteSingleFolderAsynctask(videoDetailBeans_delete.get(j).getId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						break;
					} else {
						new DeleteMultipleFolderAsynctask(videoDetailBeans_delete).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						break;
					}
				}
			}
		});
        
        video_select_folder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					for (int i = 0; i < isCheck_video.length; i++) {
						if (isCheck_video[i]) {
							isCheck_video[i] = false;
							video_select_folder.setBackgroundResource(R.drawable.icon_check_box);
							
						} else {
							isCheck_video[i] = true;
							video_select_folder.setBackgroundResource(R.drawable.icon_checked_box);
							
						}
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				for(int i=0;i<videoDetailBeans_total.size();i++)
				{
					if(!GRID_DATA.contains(videoDetailBeans_total.get(i).getAlbum()))
					{
						GRID_DATA.add(videoDetailBeans_total.get(i).getAlbum());
					}
					
				}
				
				video_folder_gridView.setAdapter(new AlbumGridAdapter(getActivity(), videoDetailBeans_total));
			}
		});
        
        ((LoadMoreListView) video_folder_gridView).setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				
					if (arrayListLength == 14) {
						arrayListLength = 0;
						new GetVideoAsynctask(offset1+15, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						((LoadMoreListView) video_folder_gridView).onLoadMore();
					}else {
						((LoadMoreListView) video_folder_gridView).onLoadMoreComplete();
					}
			}
		});
        
        image_video_folder_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				lay_edit_VideoFolder.setVisibility(View.VISIBLE);
			}
		});

		editTextVideoFolderSearch.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				v.setFocusable(true);
				v.setFocusableInTouchMode(true);
				return false;
			}
		});
        
		editTextVideoFolderSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence t, int start, int before,
					int count) {
				videoDetailBeansSearch.clear();
					for (int i = 0; i < videoDetailBeans_total.size(); i++) {
						if (videoDetailBeans_total.get(i).getAlbum().toLowerCase()
								.contains(t)) {
							videoDetailBeansSearch.add(videoDetailBeans_total.get(i));
						}
					}
					if (videoDetailBeansSearch.size() > 0) {
						video_folder_gridView.setAdapter(new AlbumGridAdapter(getActivity(), videoDetailBeansSearch));
					} else {
						video_folder_gridView.setAdapter(new AlbumGridAdapter(getActivity(), videoDetailBeansSearch));
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
		new GetVideoAsynctask(offset1, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
		public class AlbumGridAdapter extends BaseAdapter {
			 
	        private Context context;
	        private ArrayList<MediaDetailBean> arrayList;
	     
	        //Constructor to initialize values
	        public AlbumGridAdapter(Context context, ArrayList<MediaDetailBean> photoDetailBeans) {
	 
	            this.context        = context;
	            this.arrayList		= photoDetailBeans;
	        }
	         
			@Override
	        public int getCount() {
	            // Number of times getView method call depends upon gridValues.length
	            return arrayList.size();
	        }
	     
	        @Override
	        public Object getItem(int position) {
	             
	            return null;
	        }
	     
	        @Override
	        public long getItemId(int position) {
	             
	            return 0;
	        }
	         
	        // Number of times getView method call depends upon gridValues.length
	         
	        public View getView(int position, View convertView, ViewGroup parent) {
	     
	            // LayoutInflator to call external grid_item.xml file
	             
	            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	     
	            ViewHolder mViewHolder = null;
	   	     
	            if (convertView == null) {
	     
	            	convertView = new View(context);
	     
	                // get layout from grid_item.xml ( Defined Below )
	 
	            	convertView = inflater.inflate( R.layout.video_row , null);
	            	mViewHolder = new ViewHolder();
	     
	                // set value into textview
	                 
	            	mViewHolder.sender_name_text_view = (TextView) convertView.findViewById(R.id.grid_item_label);
	            	mViewHolder.test_msg_text_view = (TextView) convertView.findViewById(R.id.grid_item_label_size);
	            	mViewHolder.video_single_seclect_folder = (ImageView) convertView.findViewById(R.id.video_single_seclect_folder);
	 
	            	mViewHolder.sender_name_text_view.setText(arrayList.get(position).getAlbum()); 
	            	mViewHolder.test_msg_text_view.setText(arrayList.get(position).getFile_size()); 
	     
	                // set image based on selected text
	                 
	            	mViewHolder.contact_imageView = (ImageView) convertView.findViewById(R.id.grid_item_image_video);
	     
	                mViewHolder.contact_imageView.setImageResource(R.drawable.icon_folder);
	                  
	            } else {
	            	convertView = (View) convertView;
	            }
	            
	            try {
	            	if (isCheck_video[position]) {
		            	mViewHolder.video_single_seclect_folder.setImageResource(R.drawable.icon_checked_box);
					} else {
						mViewHolder.video_single_seclect_folder.setImageResource(R.drawable.icon_check_box);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
	            return convertView;
	        }
	 	}
	 
		public static class ViewHolder {
		        public TextView sender_name_text_view,test_msg_text_view,sms_time_text_view;
		        public ImageView contact_imageView, video_single_seclect_folder;
		}
		
		public class GetVideoAsynctask extends AsyncTask<String, String,Object[]> {
			
			ProgressDialog progressDialog ;
			String response = "";
			int offset, limit;
			
			public GetVideoAsynctask(int offset1, int limit1) {
			  offset = offset1;
			  limit = limit1;
			}

			@Override
			public void onPreExecute() {
				progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
			}

			@Override
			public Object[] doInBackground(String... params) {
				
				return HingAppParsing.getMediaDetails(getActivity(), HinjAppPreferenceUtils.getChildUploadDeviceId(getActivity()),
									HinjAppPreferenceUtils.getChildRaId(getActivity()),"2", offset, limit,"3"); 
			//// type=1 for image,2 for video,3 for audio
			}  
	 
			@SuppressWarnings("unchecked")
			@Override
			public void onPostExecute(Object[] result) {
				progressDialog.cancel();
				try
				{
					boolean status = (Boolean) result[0];
					response = (String) result[1];
					
					if(status)
					{
						videoDetailBeans = (ArrayList<MediaDetailBean>) result[2] ; 
						
						for (int i = 0; i < videoDetailBeans.size(); i++) {
							videoDetailBeans_total.add(videoDetailBeans.get(i));
						}
						
						isCheck_video = new boolean[videoDetailBeans_total.size()];
						
						for(int i=0;i<videoDetailBeans_total.size();i++)
						{
							if(!GRID_DATA.contains(videoDetailBeans_total.get(i).getAlbum()))
							{
								GRID_DATA.add(videoDetailBeans_total.get(i).getAlbum());
							}
						}
						
						video_folder_gridView.setAdapter(new AlbumGridAdapter(getActivity(), videoDetailBeans_total));
					        
						//Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
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
		
		public class DeleteSingleFolderAsynctask extends AsyncTask<String, String, Object[]> {

			ProgressDialog progressDialog;
			String response = "";
			String value;

			public DeleteSingleFolderAsynctask(String value) {
				this.value = value;
			}

			@Override
			public void onPreExecute() {
				progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please Wait");
			}

			@Override
			public Object[] doInBackground(String... params) {

				return HingAppParsing.deleteFolder(getActivity(),HinjAppPreferenceUtils.getChildRaId(getActivity()), "2",value,
						HinjAppPreferenceUtils.getChildUploadDeviceId(getActivity()));
			}

			@Override
			public void onPostExecute(Object[] result) {
				progressDialog.cancel();
				try {
					videoDetailBeans_total.clear();
					videoDetailBeans_delete.clear();
					GRID_DATA.clear();
					video_select_folder.setBackgroundResource(R.drawable.icon_check_box);
					
					new GetVideoAsynctask(offset1, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public class DeleteMultipleFolderAsynctask extends AsyncTask<String, String, Object[]> {

			ProgressDialog progressDialog;
			String response = "";
			String value;
			ArrayList<MediaDetailBean> mMediaDetailBeanDelete_;

			public DeleteMultipleFolderAsynctask(ArrayList<MediaDetailBean> mMediaDetailBeanDelete) {
				this.mMediaDetailBeanDelete_ = mMediaDetailBeanDelete;
			}

			@Override
			public void onPreExecute() {
				progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please Wait");
			}

			@Override
			public Object[] doInBackground(String... params) {

				for (int i = 0; i < mMediaDetailBeanDelete_.size(); i++) {
					if (i == mMediaDetailBeanDelete_.size() - 1) {
						multipul_id_music = multipul_id_music + mMediaDetailBeanDelete_.get(i).getId();
					} else {
						multipul_id_music = multipul_id_music + mMediaDetailBeanDelete_.get(i).getId() + ",";
					}
				}

				return HingAppParsing.deleteMultipulFolder(getActivity(),
						HinjAppPreferenceUtils.getChildRaId(getActivity()), "2",
						multipul_id_music, HinjAppPreferenceUtils.getChildUploadDeviceId(getActivity()));
			}

			@Override
			public void onPostExecute(Object[] result) {
				progressDialog.cancel();
				try {
					multipul_id_music = "";
					videoDetailBeans_total.clear();
					videoDetailBeans_delete.clear();
					GRID_DATA.clear();

					video_select_folder.setBackgroundResource(R.drawable.icon_check_box);
					new GetVideoAsynctask(offset1, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
}

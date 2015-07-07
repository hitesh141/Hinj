package com.hinj.app.activity.fragement;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.internal.el;
import com.hinj.app.activity.DashBoardActivity;
import com.hinj.app.activity.R;
import com.hinj.app.model.MediaDetailBean;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.parsing.HingAppParsing;

public class MusicFragment extends Fragment {
	
	private com.costum.android.widget.LoadMoreListView music_folder_listView;
	private ArrayList<MediaDetailBean> audioDetailBeans = new ArrayList<MediaDetailBean>();
	private ArrayList<MediaDetailBean> audioDetailBeansSearch = new ArrayList<MediaDetailBean>();
	private ArrayList<MediaDetailBean> audioDetailBeans_total = new ArrayList<MediaDetailBean>();
	private ArrayList<MediaDetailBean> audioDetailBeans_delete = new ArrayList<MediaDetailBean>();
	private ArrayList<String> GRID_DATA =  new ArrayList<String>();
	
	public ImageView select_all_music_folder, delete_all_music_folder, image_music_folder_search,image_music_folder_sort;
	public static int oneTimeOnly = 0;
	Handler seekHandler = new Handler();
	private ViewHolder mHolder;
	public static boolean[] isCheck_contact_music;
	public static String multipul_id_music = "";
	public MusicFragment(){}
	public static String album_name = "";
	private static int offset1=0, limit1=14;
	public static int arrayListLength = 0;
	private LinearLayout lay_edit_musicFolder;
	private EditText editTextMusicFolderSearch;
	
	public boolean isSearchVisibleMusicFragment = false;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_audio_list, container, false);
        audioDetailBeans.clear();
        audioDetailBeansSearch.clear();
        audioDetailBeans_total.clear();
        audioDetailBeans_delete.clear();
        
        music_folder_listView = (com.costum.android.widget.LoadMoreListView) rootView.findViewById(R.id.music_folder_listView);
       
        delete_all_music_folder = (ImageView)rootView.findViewById(R.id.delete_all_music_folder);
        select_all_music_folder = (ImageView)rootView.findViewById(R.id.select_all_music_folder);
        image_music_folder_search = (ImageView)rootView.findViewById(R.id.image_music_folder_search);
        image_music_folder_sort = (ImageView)rootView.findViewById(R.id.image_music_folder_sort);
        
        editTextMusicFolderSearch = (EditText) rootView.findViewById(R.id.editTextMusicFolderSearch);
        lay_edit_musicFolder = (LinearLayout) rootView.findViewById(R.id.lay_edit_musicFolder);
        
        delete_all_music_folder.setVisibility(View.GONE);
        
        setOnClickListener();
        
        return rootView;
    }
	
	private void setOnClickListener() {
		image_music_folder_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//lay_edit_musicFolder.setVisibility(View.VISIBLE);
				if(isSearchVisibleMusicFragment)
				{
					lay_edit_musicFolder.setVisibility(View.GONE);
				}
				else
				{
					lay_edit_musicFolder.setVisibility(View.VISIBLE);
				}
				isSearchVisibleMusicFragment = !isSearchVisibleMusicFragment;
			}
		});
		
		editTextMusicFolderSearch.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				v.setFocusable(true);
				v.setFocusableInTouchMode(true);
				return false;
			}
		});
        
		editTextMusicFolderSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence t, int start, int before,
					int count) {
				audioDetailBeansSearch.clear();
					for (int i = 0; i < audioDetailBeans_total.size(); i++) {
						if (audioDetailBeans_total.get(i).getAlbum().toLowerCase()
								.contains(t)) {
							audioDetailBeansSearch.add(audioDetailBeans_total.get(i));
						}
					}
					if (audioDetailBeansSearch.size() > 0) {
						music_folder_listView.setAdapter(new AlbumGridAdapter(getActivity(), audioDetailBeansSearch));
					} else {
						music_folder_listView.setAdapter(new AlbumGridAdapter(getActivity(), audioDetailBeansSearch));
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
		
	  
//	  ---------------------------------
	  music_folder_listView.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
          	final ImageView music_single_select = (ImageView) v.findViewById(R.id.music_single_select);
				LinearLayout music_folder_click = (LinearLayout) v.findViewById(R.id.music_folder_click);
				
				music_single_select.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if (isCheck_contact_music[position] == false) {
							isCheck_contact_music[position] = true;
							music_single_select.setImageResource(R.drawable.icon_checked_box);
						} else {
							isCheck_contact_music[position] = false;
							music_single_select.setImageResource(R.drawable.icon_check_box);
						}
							if (isCheck_contact_music[position]) {
								delete_all_music_folder.setVisibility(View.VISIBLE);
							}else {
								delete_all_music_folder.setVisibility(View.GONE);
						}
						
//						if (isCheck_contact_music.length>0) {
//							delete_all_music_folder.setVisibility(View.VISIBLE);
//						}else {
//							delete_all_music_folder.setVisibility(View.GONE);
//						}
					}
				});
				
				
				music_folder_click.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(editTextMusicFolderSearch.getWindowToken(), 0);
						
						if (audioDetailBeansSearch.size()>0) {
							album_name = audioDetailBeansSearch.get(position).getAlbum();
						}else {
							album_name = audioDetailBeans_total.get(position).getAlbum();
						}
						
						
		            	
		            	MusicListFragment mMusicListFragment  = new MusicListFragment();
						Bundle bunldeObj = new Bundle();
						bunldeObj.putString("position", "4");
						
						DashBoardActivity.replaceFragementsClick(mMusicListFragment, bunldeObj,"Music");
					}
				});
          }
      });
      
      
	  delete_all_music_folder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				audioDetailBeans_delete.clear();
				for (int i = 0; i < audioDetailBeans_total.size(); i++) {
					if (isCheck_contact_music[i]) {
						audioDetailBeans_delete.add(new MediaDetailBean(audioDetailBeans_total.get(i).getAlbum()));
					}
				}
				for (int j = 0; j < audioDetailBeans_delete.size(); j++) {
					if (audioDetailBeans_delete.size() == 1) {
						new DeleteSingleFolderAsynctask(audioDetailBeans_delete.get(j).getId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						break;
					} else {
						new DeleteMultipleFolderAsynctask(audioDetailBeans_delete).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						break;
					}
				}
			}
		});

      
	  select_all_music_folder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					for (int i = 0; i < isCheck_contact_music.length; i++) {
						if (isCheck_contact_music[i]) {
							isCheck_contact_music[i] = false;
							select_all_music_folder.setBackgroundResource(R.drawable.icon_check_box);
							delete_all_music_folder.setVisibility(View.GONE);
						} else {
							isCheck_contact_music[i] = true;
							select_all_music_folder.setBackgroundResource(R.drawable.icon_checked_box);
							delete_all_music_folder.setVisibility(View.VISIBLE);
							
						}
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				for(int i=0;i<audioDetailBeans_total.size();i++)
				{
					if(!GRID_DATA.contains(audioDetailBeans_total.get(i).getAlbum()))
					{
						GRID_DATA.add(audioDetailBeans_total.get(i).getAlbum());
					}
					
				}
				
				
				music_folder_listView.setAdapter(new AlbumGridAdapter(getActivity(), audioDetailBeans_total));
			}
		});
	  
	  ((LoadMoreListView) music_folder_listView).setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				
					if (arrayListLength == 14) {
						arrayListLength = 0;
						new GetAudioAsynctask(offset1+15, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						((LoadMoreListView) music_folder_listView).onLoadMore();
					}else {
						((LoadMoreListView) music_folder_listView).onLoadMoreComplete();
					}
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		offset1=0; limit1=14;
		 new GetAudioAsynctask(offset1, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
		
		public class AlbumGridAdapter extends BaseAdapter {
			 
	        private Context context;
	        private ArrayList<MediaDetailBean> arrayList;
	     
	        //Constructor to initialize values
	        public AlbumGridAdapter(Context context,ArrayList<MediaDetailBean> photoDetailBeans) {
	 
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
	     
	            ViewHolder mViewHolder;
	            if (convertView == null) {
	     
	            	convertView = new View(context);
	     
	                // get layout from grid_item.xml ( Defined Below )
	 
	            	convertView = inflater.inflate( R.layout.audio_list_row , null);
	                mViewHolder = new ViewHolder();
	                // set value into textview
	                 
	                mViewHolder.list_audio_item_label = (TextView) convertView.findViewById(R.id.list_audio_item_label);
	                mViewHolder.music_single_select = (ImageView) convertView.findViewById(R.id.music_single_select);
	                
	                mViewHolder.list_audio_item_label.setText(arrayList.get(position).getAlbum()); 
	     
	                // set image based on selected text
	                 
	                mViewHolder.list_audio_item_image = (ImageView) convertView.findViewById(R.id.list_audio_item_image);
	     
	                mViewHolder.list_audio_item_image.setImageResource(R.drawable.icon_folder);
	                  
	                convertView.setTag(mViewHolder);
	            } else {
	            	mViewHolder = (ViewHolder) convertView.getTag();
	            }
	            
	            try {
	            	if (isCheck_contact_music[position]) {
		            	mViewHolder.music_single_select.setImageResource(R.drawable.icon_checked_box);
					} else {
						mViewHolder.music_single_select.setImageResource(R.drawable.icon_check_box);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
	            
	            return convertView;
	        }
	 	}
	 
		public static class ViewHolder {
		        public TextView list_audio_item_label, list_audio_item_label_size;
		        public ImageView list_audio_item_image, music_single_select;
		}
		
		public class GetAudioAsynctask extends AsyncTask<String, String,Object[]> {
			
			ProgressDialog progressDialog ;
			String response = "";
			int offset, limit;
			
			public GetAudioAsynctask(int i, int limit1) {
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
									HinjAppPreferenceUtils.getChildRaId(getActivity()),"3",offset,limit,"2"); 
			}  
	 
			@SuppressWarnings({ "unchecked", "unchecked" })
			@Override
			public void onPostExecute(Object[] result) {
				progressDialog.cancel();
				try
				{
					boolean status = (Boolean) result[0];
					response = (String) result[1];
					
					if(status)
					{
						audioDetailBeans = (ArrayList<MediaDetailBean>) result[2] ; 
						
						for (int i = 0; i < audioDetailBeans.size(); i++) {
							audioDetailBeans_total.add(audioDetailBeans.get(i));
						}
						
						isCheck_contact_music = new boolean[audioDetailBeans_total.size()];
						
						for(int i=0;i<audioDetailBeans_total.size();i++)
						{
							if(!GRID_DATA.contains(audioDetailBeans_total.get(i).getAlbum()))
							{
								GRID_DATA.add(audioDetailBeans_total.get(i).getAlbum());
							}
						}
						
						music_folder_listView.setAdapter(new AlbumGridAdapter(getActivity(), audioDetailBeans_total));
					        
//						Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
					}
					else
					{
						response = (String) result[1];
						AppUtils.showDialog(getActivity(), response);
					}
					
					((LoadMoreListView) music_folder_listView).onLoadMoreComplete();
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

				return HingAppParsing.deleteFolder(getActivity(),HinjAppPreferenceUtils.getChildRaId(getActivity()), "3",value,
						HinjAppPreferenceUtils.getUploadDeviceID(getActivity()));
			}

			@Override
			public void onPostExecute(Object[] result) {
				progressDialog.cancel();
				try {
					audioDetailBeans_total.clear();
					audioDetailBeans_delete.clear();
					GRID_DATA.clear();
					select_all_music_folder.setBackgroundResource(R.drawable.icon_check_box);
					
					new GetAudioAsynctask(offset1, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
						HinjAppPreferenceUtils.getChildRaId(getActivity()), "3",
						multipul_id_music, HinjAppPreferenceUtils.getChildUploadDeviceId(getActivity()));
			}

			@Override
			public void onPostExecute(Object[] result) {
				progressDialog.cancel();
				try {
					multipul_id_music = "";
					audioDetailBeans_total.clear();
					audioDetailBeans_delete.clear();

					select_all_music_folder.setBackgroundResource(R.drawable.icon_check_box);
					new GetAudioAsynctask(offset1, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
}

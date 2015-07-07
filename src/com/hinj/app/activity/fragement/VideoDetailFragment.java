package com.hinj.app.activity.fragement;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.hinj.app.activity.DashBoardActivity;
import com.hinj.app.activity.R;
import com.hinj.app.model.FilePropertiesBean;
import com.hinj.app.model.MediaDetailBean;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.Utility;
import com.hinj.imageload.FileSystemPersistence;
import com.hinj.imageload.HttpImageManager;
import com.hinj.parsing.HingAppParsing;

@SuppressLint("ValidFragment")
public class VideoDetailFragment extends Fragment implements OnScrollListener{
	
	public ArrayList<MediaDetailBean> mVideoDetailBean = new ArrayList<MediaDetailBean>();
	public ArrayList<MediaDetailBean> mVideoDetailBeanSearch = new ArrayList<MediaDetailBean>();
	public ArrayList<MediaDetailBean> mVideoDetailBean_total = new ArrayList<MediaDetailBean>();
	private ArrayList<MediaDetailBean> mVideoDetailBean_delete = new ArrayList<MediaDetailBean>();
	private ArrayList<String> GRID_DATA = new ArrayList<String>();
	private com.costum.android.widget.LoadMoreListView video_detail_folder_detail_listView;
	public static final String BASEDIR = "/sdcard/httpimage";
	
	public ImageView select_all_music, download_all_music_list, image_video_search, image_sort_video_list;
	public ImageView image_delete_video_list, image_video_download, image_share_video_list, delete_video_list;
	
	public LinearLayout video_list_beforeClick_LL, video_list_afterClick_LL;
	
	public boolean isSearchVisible = false;
	
	public static int oneTimeOnly = 0;
	Handler seekHandler = new Handler();
	public static boolean[] isCheck_contact_music;
	public static String multipul_id_music = "";
	private long[] downloadIdsNew;
	static int CountIDs = 0;
	private DownloadManager downloadManager;
	public String destinationFolder = "/Hinj/Video";
	public Context mContext;
	private boolean click = false;
	
	private static int offset1=0, limit1=14;
	public static int arrayListLength = 0;
	
	public HttpImageManager mHttpImageManager;
	
	private LinearLayout lay_edit_Video;
	private EditText editTextVideoSearch;
	
	public LinearLayout menu_LL;
	
	public VideoDetailFragment(){}
	
	public VideoDetailFragment(ArrayList<MediaDetailBean> mVideoDetailBean) {
		this.mVideoDetailBean_total = mVideoDetailBean;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.video_detail_fragment, container, false);
        
        mVideoDetailBean.clear();
        mVideoDetailBeanSearch.clear();
        mVideoDetailBean_total.clear();
        mVideoDetailBean_delete.clear();
        
        initView(rootView);
		setOnClickListener();
		
		((LoadMoreListView) video_detail_folder_detail_listView).setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				
					if (arrayListLength == 14) {
						arrayListLength = 0;
						new GetVideoAsynctask(offset1+15, limit1,"name").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						((LoadMoreListView) video_detail_folder_detail_listView).onLoadMore();
					}else {
						((LoadMoreListView) video_detail_folder_detail_listView).onLoadMoreComplete();
					}
			}
		});
		
		video_detail_folder_detail_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, final int position, long arg3) {
				
					final ImageView music_single_select_list = (ImageView) v.findViewById(R.id.video_single_select_list);
					LinearLayout lay_music_single_select_list = (LinearLayout) v.findViewById(R.id.lay_video_single_select_list);
					TextView list_audio_item_label_list = (TextView) v.findViewById(R.id.list_video_item_label_list);
					
					lay_music_single_select_list.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {

							if (isCheck_contact_music[position] == false) {
								
								video_list_beforeClick_LL.setVisibility(View.GONE);
								video_list_afterClick_LL.setVisibility(View.VISIBLE);
								
								isCheck_contact_music[position] = true;
								music_single_select_list.setImageResource(R.drawable.icon_checked_box);
							} else {

								video_list_beforeClick_LL.setVisibility(View.VISIBLE);
								video_list_afterClick_LL.setVisibility(View.GONE);
								
								isCheck_contact_music[position] = false;
								music_single_select_list.setImageResource(R.drawable.icon_check_box);
							}
						}
					});
					
					list_audio_item_label_list.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							hello(position);
						}
					});
			}
		});

		registerForContextMenu(video_detail_folder_detail_listView);
		
        return rootView;
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.video_menu, menu);		
	}

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		
		case R.id.dial_lay_download_video:
			newDownload(mVideoDetailBean_total.get(info.position).getOriginal(),destinationFolder, mVideoDetailBean_total.get(info.position).getFile());
			return true;
		case R.id.dial_lay_delete_video:
			new DeleteAsynctask(mVideoDetailBean_total.get(info.position).getId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			return true;
		case R.id.dial_lay_rename_video:
			renameFile(mVideoDetailBean_total.get(info.position).getId());
			return true;
		case R.id.dial_lay_properties_video:
			new GetPropertiesAsynctask(mVideoDetailBean_total.get(info.position).getId(),"2").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	private void initView(View rootView) {
		
		video_detail_folder_detail_listView = (com.costum.android.widget.LoadMoreListView) rootView.findViewById(R.id.video_detail_folder_detail_listView);
		
		mContext = this.getActivity();

		downloadIdsNew = new long[30];
		downloadManager = (DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);

		select_all_music = (ImageView) rootView.findViewById(R.id.select_all_video_list);
		download_all_music_list = (ImageView) rootView.findViewById(R.id.download_all_video_list);
		image_video_search = (ImageView) rootView.findViewById(R.id.image_video_search);
		
		image_sort_video_list = (ImageView) rootView.findViewById(R.id.image_sort_video_list);
		image_video_download = (ImageView) rootView.findViewById(R.id.image_video_download);
		image_share_video_list = (ImageView) rootView.findViewById(R.id.image_share_video_list);
		delete_video_list = (ImageView) rootView.findViewById(R.id.image_delete_video_list);
		
		image_delete_video_list = (ImageView) rootView.findViewById(R.id.image_delete_video_list); 
		
		video_list_beforeClick_LL = (LinearLayout) rootView.findViewById(R.id.video_list_beforeClick_LL);
		video_list_afterClick_LL = (LinearLayout) rootView.findViewById(R.id.video_list_afterClick_LL);
		
		video_list_afterClick_LL.setVisibility(View.GONE);
		
		editTextVideoSearch = (EditText) rootView.findViewById(R.id.editTextVideoSearch);
        lay_edit_Video = (LinearLayout) rootView.findViewById(R.id.lay_edit_Video);
        
        menu_LL = (LinearLayout) rootView.findViewById(R.id.menu_LL);

	}
	
	private void setOnClickListener() {
		
		image_video_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(isSearchVisible)
				{
					lay_edit_Video.setVisibility(View.GONE);
				}
				else
				{
					lay_edit_Video.setVisibility(View.VISIBLE);
				}
				
				isSearchVisible = !isSearchVisible;
			}
		});

		editTextVideoSearch.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				v.setFocusable(true);
				v.setFocusableInTouchMode(true);
				return false;
			}
		});
        
		editTextVideoSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence t, int start, int before,
					int count) {
				mVideoDetailBeanSearch.clear();
					for (int i = 0; i < mVideoDetailBean_total.size(); i++) {
						if (mVideoDetailBean_total.get(i).getFile2().toLowerCase()
								.contains(t)) {
							mVideoDetailBeanSearch.add(mVideoDetailBean_total.get(i));
						}
					}
					if (mVideoDetailBeanSearch.size() > 0) {
						video_detail_folder_detail_listView.setAdapter(new VideoDetailListAdapter(getActivity(), mVideoDetailBeanSearch));
					} else {
						video_detail_folder_detail_listView.setAdapter(new VideoDetailListAdapter(getActivity(), mVideoDetailBeanSearch));
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
		
		image_delete_video_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mVideoDetailBean_delete.clear();
				for (int i = 0; i < mVideoDetailBean_total.size(); i++) {
					if (isCheck_contact_music[i]) {
						mVideoDetailBean_delete.add(new MediaDetailBean(
								mVideoDetailBean_total.get(i).getId()));
					}

				}

				mVideoDetailBean_delete.size();
				for (int j = 0; j < mVideoDetailBean_delete.size(); j++) {
					if (mVideoDetailBean_delete.size() == 1) {
						new DeleteAsynctask(mVideoDetailBean_delete.get(j).getId()).execute();
						break;
					} else {
						new DeleteAsynctaskMultipulMusic(mVideoDetailBean_delete).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						break;
					}

				}
			}
		});

		select_all_music.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < isCheck_contact_music.length; i++) {
					if (isCheck_contact_music[i]) {
						isCheck_contact_music[i] = false;
						select_all_music
								.setBackgroundResource(R.drawable.icon_check_box);

					} else {
						isCheck_contact_music[i] = true;
						select_all_music
								.setBackgroundResource(R.drawable.icon_checked_box);

					}
				}
				video_detail_folder_detail_listView.setAdapter(new VideoDetailListAdapter(
						getActivity(), mVideoDetailBean_total));
			}
		});

		image_video_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				DeviceVideoListFragment mFilesFragment = new DeviceVideoListFragment();
				Bundle bunldeObj = new Bundle();
				bunldeObj.putString("position", "4");
				DashBoardActivity.replaceFragementsClick(mFilesFragment, bunldeObj,"Video");
				
				
				
				/*
				mVideoDetailBean_delete.clear();
				for (int i = 0; i < mVideoDetailBean_total.size(); i++) {
					if (isCheck_contact_music[i]) {
						mVideoDetailBean_delete.add(new MediaDetailBean(
								mVideoDetailBean_total.get(i).getOriginal(),
								mVideoDetailBean_total.get(i).getFile(), mVideoDetailBean_total.get(i).getFile2()));
					}

				}
				for (int i = 0; i < mVideoDetailBean_delete.size(); i++) {
					newDownload(mVideoDetailBean_delete.get(i).getOriginal(),
							destinationFolder, mVideoDetailBean_delete.get(i).getFile2());

					//Toast.makeText(mContext, "Start Music Downloading", Toast.LENGTH_LONG).show();
				}
				
				new GetVideoAsynctask(offset1, limit1,"name").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			*/}
		});
		
		
		download_all_music_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DeviceVideoListFragment mFilesFragment = new DeviceVideoListFragment();
				Bundle bunldeObj = new Bundle();
				bunldeObj.putString("position", "4");
				DashBoardActivity.replaceFragementsClick(mFilesFragment, bunldeObj,"Video");
				
			}
		});
		
		
		image_share_video_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, "www.google.com");  
				startActivity(Intent.createChooser(intent, "Share with"));
				
				/*audioDetailBeans_delete.clear();
				for (int i = 0; i < audioDetailBeans_total.size(); i++) {
					if (isCheck_contact_music[i]) {
						audioDetailBeans_delete.add(new MediaDetailBean(
								audioDetailBeans_total.get(i).getOriginal(),
								audioDetailBeans_total.get(i).getFile()));
					}
				}*/
			}
		});
		
		image_sort_video_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				setPopupMenu();
			}
		});
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		offset1=0; limit1=14;
		new GetVideoAsynctask(offset1, limit1,"name").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	
	public class VideoDetailListAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<MediaDetailBean> arrayList;

		// Constructor to initialize values
		public VideoDetailListAdapter(Context context, ArrayList<MediaDetailBean> photoDetailBeans) {

			this.context = context;
			this.arrayList = photoDetailBeans;
		}

		@Override
		public int getCount() {
			// Number of times getView method call depends upon
			// gridValues.length
			return arrayList.size();
		}

		@Override
		public MediaDetailBean getItem(int position) {

			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		// Number of times getView method call depends upon gridValues.length

		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder mViewHolder;
			// LayoutInflator to call external grid_item.xml file

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (convertView == null) {

				convertView = new View(context);

				// get layout from grid_item.xml ( Defined Below )

				convertView = inflater.inflate(R.layout.video_detail_row, null);
				mViewHolder = new ViewHolder();
				// set value into textview

				mViewHolder.list_audio_item_label = (TextView) convertView.findViewById(R.id.list_video_item_label_list);
				mViewHolder.videoTime = (TextView) convertView.findViewById(R.id.videoTime);
				mViewHolder.list_video_item_label_size_list = (TextView) convertView.findViewById(R.id.list_video_item_label_size_list);
				
				mViewHolder.music_single_select = (ImageView) convertView.findViewById(R.id.video_single_select_list);
				mViewHolder.lay_music_long_press = (LinearLayout) convertView.findViewById(R.id.lay_video_long_press);
				mViewHolder.lay_music_single_select_list = (LinearLayout) convertView.findViewById(R.id.lay_video_single_select_list);
				mViewHolder.list_audio_item_image = (ImageView) convertView.findViewById(R.id.list_video_item_image_list);
				mViewHolder.list_audio_item_label_size = (TextView) convertView.findViewById(R.id.list_audio_item_label_size);

				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			mViewHolder.videoTime.setText(arrayList.get(position).getDuration());
			mViewHolder.list_audio_item_label.setText(arrayList.get(position).getFile2());
			mViewHolder.list_video_item_label_size_list.setText(arrayList.get(position).getFile_size()+" MB");
			
			mHttpImageManager = new HttpImageManager(HttpImageManager.createDefaultMemoryCache(), new FileSystemPersistence(BASEDIR));
			Uri uri = Uri.parse(arrayList.get(position).getFile());
			Bitmap bitmap = mHttpImageManager.loadImage(new HttpImageManager.LoadRequest(uri, mViewHolder.list_audio_item_image));
			if (bitmap != null) {

				mViewHolder.list_audio_item_image.setImageBitmap(bitmap);
			}

			if (isCheck_contact_music[position]) {
				mViewHolder.music_single_select.setImageResource(R.drawable.icon_checked_box);
			} else {
				mViewHolder.music_single_select.setImageResource(R.drawable.icon_check_box);
			}
			
			return convertView;
		}
	}

	public static class ViewHolder {
		public TextView list_audio_item_label, list_audio_item_label_size, videoTime,list_video_item_label_size_list;
		public ImageView list_audio_item_image, music_single_select;
		public LinearLayout lay_music_long_press, lay_music_single_select_list;
		
	}

	public class GetVideoAsynctask extends AsyncTask<String, String, Object[]> {

		ProgressDialog progressDialog;
		String response = "";
		int offset , limit;
		String sortType;

		public GetVideoAsynctask(int i, int j,String sortType) {
			offset = i;
			limit = j;
			this.sortType = sortType;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {

			//return HingAppParsing.getMediaByAlbumNames(getActivity(), "2",VideosFragment.album_name_video, offset, limit, "3");
			return HingAppParsing.getMediaByAlbumNamesSorted(getActivity(),"2",VideosFragment.album_name_video,offset, limit, "2",sortType); // type=1 for image,2 for video,3 for audio
			
			// // type=1 for image,2 for video,3 for audio
		}

		@SuppressWarnings({ "unchecked", "unchecked" })
		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try {
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				if (status) {
					mVideoDetailBean.clear();
					mVideoDetailBean_total.clear();
					
					mVideoDetailBean = (ArrayList<MediaDetailBean>) result[2];
					
					for (int i = 0; i < mVideoDetailBean.size(); i++) {
						mVideoDetailBean_total.add(mVideoDetailBean.get(i));
					}

					isCheck_contact_music = new boolean[mVideoDetailBean_total.size()];

					// PhotoAdapter adapter = new PhotoAdapter(getActivity(), 0,
					// photoDetailBeans);
					// ImageAdapter adapter2 = new ImageAdapter(getActivity());
					// image_folder_gridView.setAdapter(adapter2);

					for (int i = 0; i < mVideoDetailBean_total.size(); i++) {
						if (!GRID_DATA.contains(mVideoDetailBean_total.get(i).getFile())) {
							GRID_DATA.add(mVideoDetailBean_total.get(i).getFile());
						}
					}

					video_detail_folder_detail_listView.setAdapter(new VideoDetailListAdapter(getActivity(), mVideoDetailBean_total));

					//Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT)
							//.show();
				} else {
					response = (String) result[1];
					AppUtils.showDialog(getActivity(), response);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class DeleteAsynctask extends AsyncTask<String, String, Object[]> {

		ProgressDialog progressDialog;
		String response = "";
		String value;

		public DeleteAsynctask(String value) {
			this.value = value;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(), "Loading...",
					"Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {

			return HingAppParsing.deleteSingle(getActivity(),HinjAppPreferenceUtils.getChildRaId(getActivity()), "7", value);
		}

		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try {
				mVideoDetailBean_total.clear();
				mVideoDetailBean_delete.clear();
				select_all_music.setBackgroundResource(R.drawable.icon_check_box);
				
				video_list_afterClick_LL.setVisibility(View.GONE);
				video_list_beforeClick_LL.setVisibility(View.VISIBLE);
				
				new GetVideoAsynctask(offset1, limit1,"name").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class DeleteAsynctaskMultipulMusic extends AsyncTask<String, String, Object[]> {

		ProgressDialog progressDialog;
		String response = "";
		String value;
		ArrayList<MediaDetailBean> mMediaDetailBeanDelete_;

		public DeleteAsynctaskMultipulMusic(ArrayList<MediaDetailBean> mMediaDetailBeanDelete) {
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
					multipul_id_music = multipul_id_music
							+ mMediaDetailBeanDelete_.get(i).getId();
				} else {
					multipul_id_music = multipul_id_music
							+ mMediaDetailBeanDelete_.get(i).getId() + ",";
				}
			}

			return HingAppParsing.deleteContactMultipul(getActivity(),
					HinjAppPreferenceUtils.getChildRaId(getActivity()), "7",
					multipul_id_music);
		}

		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try {
				multipul_id_music = "";
				mVideoDetailBean_total.clear();
				mVideoDetailBean_delete.clear();

				select_all_music.setBackgroundResource(R.drawable.icon_check_box);
				new GetVideoAsynctask(offset1, limit1,"name").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// ******************************************************
	public void newDownload(String downloadPath, String folderName,
			String FileName) {
		try {

			/*
			 * Download_Uri_Image =
			 * KytabuConstant.DOWNLOAD_URI+"/App_Shared/Images/"
			 * +strGetImageID+".png"; String destinationFolder="/kytabu/Images";
			 * String mFileName=strGetImageID+".png";
			 */

			Uri Download_Uri = Uri.parse(downloadPath);

			DownloadManager.Request request = new DownloadManager.Request(
					Download_Uri);
			// Restrict the types of networks over which this download may
			// proceed.
			request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
					| DownloadManager.Request.NETWORK_MOBILE);
			// Set whether this download may proceed over a roaming connection.
			request.setAllowedOverRoaming(false);
			request.setVisibleInDownloadsUi(true);
			request.setShowRunningNotification(false);

			// Set the local destination for the downloaded file to a path
			// within
			// the application's external files directory
			// request.setDestinationInExternalFilesDir(ChatWindowActivity.this,FileUtils.getDirectory(folderName),fileName);
			
			String delimiter = "\\.";
			
			String[] parts = FileName.split(delimiter);//video_142504341323.png
			String part1 = parts[0]; // 004
			String path = Utility.getDirectory(part1) + File.separator
					+ part1;
			File file = null;
			try {
				file = new File(path);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Uri external = Uri.fromFile(file);
			request.setDestinationUri(external);
			try {
				downloadIdsNew[CountIDs] = downloadManager.enqueue(request);
				/*
				 * if(!FormatID.equals("0")){ boolean
				 * dbStatus=dataSource.updateDownloadFile(getContentidtext,
				 * ""+downloadIdsNew[CountIDs]); }
				 * Log.d("Here is the Calling newDownload",
				 * "=====================" +downloadIdsNew[CountIDs]);
				 */

			} catch (Exception ex) {

				Log.d("Here is the Exception", "====================="
						+ downloadIdsNew[CountIDs]);
				ex.printStackTrace();
			}

			// Log.d("Here is the Calling newDownload", "====================="
			// +CountIDs);
			CountIDs++;

			IntentFilter filter = new IntentFilter(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE);
			getActivity().registerReceiver(downloadReceiver, filter);

		} catch (Exception ex) {
			 ex.printStackTrace();
		}

	}

	public BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			try {

				// Log.d("downloadReceiver", "====================="
				// +downloadIds[]);
				DownloadManager.Query query = new DownloadManager.Query();
				query.setFilterById(downloadIdsNew);
				Cursor cursor = downloadManager.query(query);

				if (cursor.moveToFirst()) {
					int columnIndex = cursor
							.getColumnIndex(DownloadManager.COLUMN_STATUS);
					int status = cursor.getInt(columnIndex);
					int columnReason = cursor
							.getColumnIndex(DownloadManager.COLUMN_REASON);
					int reason = cursor.getInt(columnReason);
					long id = intent.getLongExtra(
							DownloadManager.EXTRA_DOWNLOAD_ID, 0);

					if (status == DownloadManager.STATUS_SUCCESSFUL) {

						try {
							Log.e("Ref ID***********", "" + id);

							mContext.unregisterReceiver(downloadReceiver);

						} catch (Exception e) {
							 e.printStackTrace();
						} finally {
							// dataSource.closeDB();
						}

					} else if (status == DownloadManager.STATUS_FAILED) {
						//Toast.makeText(getActivity(), "FAILED!\n" + "reason of " + reason, Toast.LENGTH_LONG).show();
					} else if (status == DownloadManager.STATUS_PAUSED) {
						//Toast.makeText(getActivity(), "PAUSED!\n" + "reason of " + reason, Toast.LENGTH_LONG).show();
					} else if (status == DownloadManager.STATUS_PENDING) {
						//Toast.makeText(getActivity(), "PENDING!", Toast.LENGTH_LONG).show();
					} else if (status == DownloadManager.STATUS_RUNNING) {
						//Toast.makeText(getActivity(), "RUNNING!", Toast.LENGTH_LONG).show();
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};

	protected void showCustomDialog(final int arg2) {

		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.customdailog);
		
		LinearLayout dial_lay_download = (LinearLayout) dialog
				.findViewById(R.id.dial_lay_download);
		LinearLayout dial_lay_delete = (LinearLayout) dialog
				.findViewById(R.id.dial_lay_delete);
		LinearLayout dial_lay_rename = (LinearLayout) dialog
				.findViewById(R.id.dial_lay_rename);
		LinearLayout dial_lay_properties = (LinearLayout) dialog
				.findViewById(R.id.dial_lay_properties);
		
		dial_lay_download.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				newDownload(mVideoDetailBean_delete.get(arg2).getOriginal(),
						destinationFolder, mVideoDetailBean_delete.get(arg2)
								.getFile2());
				dialog.dismiss();
			}
		});
		
		dial_lay_delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			    new DeleteAsynctask(mVideoDetailBean_delete.get(arg2).getId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				dialog.dismiss();
			}
		});
		
		dial_lay_rename.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				dialog.dismiss();
			}
		});
		
		dial_lay_properties.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				dialog.dismiss();
				new GetPropertiesAsynctask(mVideoDetailBean_delete.get(arg2).getId(),"2").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				
			}
		});
		
		
		dialog.show();
	}

	protected void hello(int position) {
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editTextVideoSearch.getWindowToken(), 0);
		VideoViewFragment mVideoViewFragment;
		if (mVideoDetailBeanSearch.size()>0) {
			mVideoViewFragment  = new VideoViewFragment(mVideoDetailBeanSearch.get(position).getOriginal());
		}else {
			mVideoViewFragment  = new VideoViewFragment(mVideoDetailBean.get(position).getOriginal());
		}
		
		Bundle bunldeObj = new Bundle();
		bunldeObj.putString("position", "4");
		DashBoardActivity.replaceFragementsClick(mVideoViewFragment, bunldeObj,"Videos");
	}
	
	private void renameFile(final String pos) {
		
		final String filename  = "";
		//final File dir = getActivity().getDir(UtilityFuctions.USER_LISTS, Context.MODE_PRIVATE);
        final File myFile = new File(filename);

        AlertDialog.Builder fileDialog = new AlertDialog.Builder(getActivity());
        fileDialog.setTitle("Rename file");

        // Set an EditText view to get user input
        final EditText input = new EditText(getActivity());
        input.setText(filename);
        fileDialog.setView(input);
        fileDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String fileName = input.getText().toString();

                            myFile.renameTo(new File(filename)); 
                            
                            new RenameFileAsynctask(pos,fileName).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            
                           // Toast.makeText(getActivity(), "Raname Success", Toast.LENGTH_SHORT).show();
                           // UtilityFuctions.createToast(getActivity(), "file rename successfully", 0);
                        }
                    }
               );
        fileDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        fileDialog.create();
        fileDialog.show();
	}

	public class RenameFileAsynctask extends AsyncTask<String, String,Object[]> {
		
		ProgressDialog progressDialog ;
		String response = "";
		String id;
		String newName;
		
		public RenameFileAsynctask(String id,String newName) {
			this.id = id;
			this.newName = newName;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.renameFile(getActivity(), String.valueOf(id), "2", newName);  
			 // type=1 for image,2 for video,3 for audio
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			
			progressDialog.cancel();
			try{
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					new GetVideoAsynctask(offset1, limit1,"name").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

	public class GetPropertiesAsynctask extends AsyncTask<String, String,Object[]> {
		
		ProgressDialog progressDialog ;
		FilePropertiesBean filePropertiesBean =  new FilePropertiesBean();
		String response = "";
		String id;
		String type;
		
		public GetPropertiesAsynctask(String id,String type) {
			this.id = id;
			this.type = type;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.getProperties(getActivity(), String.valueOf(id), type);  
			 // type=1 for image,2 for video,3 for audio
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			
			progressDialog.cancel();
			try{
				boolean status = (Boolean) result[0];
				filePropertiesBean = (FilePropertiesBean) result[1];
				
				if(status)
				{
					AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
		            builderSingle.setTitle("Properties");
		            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
		            arrayAdapter.add("Type  "+ filePropertiesBean.getExtension());
		            arrayAdapter.add("Size  "+filePropertiesBean.getFile_size()+" MB");
		            builderSingle.setAdapter(arrayAdapter, null); 
		            builderSingle.show();
				
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void setPopupMenu() {

		PopupMenu popup = new PopupMenu(getActivity(), menu_LL);  
		
		//Inflating the Popup using xml file  
		popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());  

		//registering popup with OnMenuItemClickListener  
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
			public boolean onMenuItemClick(MenuItem item) { 
				System.out.println(item); 
				
				if(item.getTitle().toString().equalsIgnoreCase("Name"))
				{
					offset1=0; limit1=14;
					new GetVideoAsynctask(offset1, limit1,"name").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				else if(item.getTitle().toString().equalsIgnoreCase("Size"))
				{
					offset1=0; limit1=14;
					new GetVideoAsynctask(offset1, limit1,"size").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			    //	new GetAudioAsynctask(offset1, limit1,"size").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				else if(item.getTitle().toString().equalsIgnoreCase("Length"))
				{
					offset1=0; limit1=14;
					new GetVideoAsynctask(offset1, limit1,"length").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				//	new GetAudioAsynctask(offset1, limit1,"length").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				else if(item.getTitle().toString().equalsIgnoreCase("Date Added"))
				{
					offset1=0; limit1=14;
					new GetVideoAsynctask(offset1, limit1,"date_added").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				//	new GetAudioAsynctask(offset1, limit1,"date_added").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				
				return true;  
			}  
		});  

		popup.show();//showing popup menu  
	}  
}

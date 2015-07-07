package com.hinj.app.activity.fragement;

import java.io.File;
import java.util.ArrayList;

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
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class MusicListFragment extends Fragment  {

	private com.costum.android.widget.LoadMoreListView music_folder_listView;
	private ArrayList<MediaDetailBean> audioDetailBeans = new ArrayList<MediaDetailBean>();
	private ArrayList<MediaDetailBean> audioDetailBeansSearch = new ArrayList<MediaDetailBean>();
	private ArrayList<MediaDetailBean> audioDetailBeans_total = new ArrayList<MediaDetailBean>();
	private ArrayList<MediaDetailBean> audioDetailBeans_delete = new ArrayList<MediaDetailBean>();
	private ArrayList<String> GRID_DATA = new ArrayList<String>();

	// Music Player

	public TextView songName;
	private MediaPlayer mediaPlayer;
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000; // 5000 milliseconds
	private SeekBar seekbar;
	private ImageButton play, pause, reword, forward;
	
	public ImageView select_all_music, download_all_music_list, image_music_search,image_music_folder_sort;
	public ImageView image_music_share,image_music_download,delete_all_music;
	
	public static int oneTimeOnly = 0;
	Handler seekHandler = new Handler();
	public static boolean[] isCheck_contact_music;
	public static String multipul_id_music = "";
	private long[] downloadIdsNew;
	static int CountIDs = 0;
	private DownloadManager downloadManager;
	public String destinationFolder = "/Hinj/Music";
	Context mContext;
	private boolean click = false;
	
	private static int offset1=0, limit1=14;
	public static int arrayListLength = 0;
	private LinearLayout lay_edit_music;
	private EditText editTextMusicSearch;

	public LinearLayout menu_LL;
	
	public static final String BASEDIR = "/sdcard/httpimage";
	public HttpImageManager mHttpImageManager;
	
	public LinearLayout music_list_beforeClick_LL,music_list_afterClick_LL;
	
	public boolean isSearchVisible = false;
	
	public MusicListFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 	Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.music_list_fragment, 	container, false);

		ArrayList<String> arrayList = new ArrayList<String>();
		mContext = this.getActivity();
		music_folder_listView = (com.costum.android.widget.LoadMoreListView) rootView.findViewById(R.id.music_folder_listView_list);

		downloadIdsNew = new long[30];
		downloadManager = (DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);

		seekbar = (SeekBar) rootView.findViewById(R.id.seekBar1);
		play = (ImageButton) rootView.findViewById(R.id.play);
		pause = (ImageButton) rootView.findViewById(R.id.pause);

		music_list_beforeClick_LL = (LinearLayout) rootView.findViewById(R.id.music_list_beforeClick_LL);
		music_list_afterClick_LL = (LinearLayout) rootView.findViewById(R.id.music_list_afterClick_LL);
		
		music_list_afterClick_LL.setVisibility(View.GONE);
		
		delete_all_music = (ImageView) rootView.findViewById(R.id.delete_all_music_list);
		image_music_search = (ImageView) rootView.findViewById(R.id.image_music_search);
		select_all_music = (ImageView) rootView.findViewById(R.id.select_all_music_list);
		image_music_share = (ImageView) rootView.findViewById(R.id.image_music_share);
		download_all_music_list = (ImageView) rootView.findViewById(R.id.download_all_music_list);
		image_music_download = (ImageView) rootView.findViewById(R.id.image_music_download);
		editTextMusicSearch = (EditText) rootView.findViewById(R.id.editTextMusicSearch);
        lay_edit_music = (LinearLayout) rootView.findViewById(R.id.lay_edit_music);

		reword = (ImageButton) rootView.findViewById(R.id.reword);
		
		forward = (ImageButton) rootView.findViewById(R.id.forward);
		
		image_music_folder_sort = (ImageView) rootView .findViewById(R.id.image_music_folder_sort);

		menu_LL= (LinearLayout) rootView.findViewById(R.id.menu_LL);
		 
		seekbar.setClickable(false);

		setOnClickListener();

		music_folder_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, final int position, long arg3) {
				
					final ImageView music_single_select_list = (ImageView) v.findViewById(R.id.music_single_select_list);
				//	final LinearLayout lay_music_long_press = (LinearLayout) v.findViewById(R.id.lay_music_long_press);
					LinearLayout lay_music_single_select_list = (LinearLayout) v.findViewById(R.id.lay_music_single_select_list);
					TextView list_audio_item_label_list = (TextView) v.findViewById(R.id.list_audio_item_label_list);
					
					lay_music_single_select_list.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {

							if (isCheck_contact_music[position] == false) {
								
								music_list_beforeClick_LL.setVisibility(View.GONE);
								music_list_afterClick_LL.setVisibility(View.VISIBLE);
								
								isCheck_contact_music[position] = true;
								music_single_select_list.setImageResource(R.drawable.icon_checked_box);
							} else {
								
								music_list_beforeClick_LL.setVisibility(View.VISIBLE);
								music_list_afterClick_LL.setVisibility(View.GONE);
								
								isCheck_contact_music[position] = false;
								music_single_select_list.setImageResource(R.drawable.icon_check_box);
							}
							
							/*if(isCheck_contact_music.length == audioDetailBeans_total.size())
							{
								select_all_music.setBackgroundResource(R.drawable.icon_checked_box);
							}
							else
							{
								select_all_music.setBackgroundResource(R.drawable.icon_check_box);
							}*/
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
		
			((LoadMoreListView) music_folder_listView).setOnLoadMoreListener(new OnLoadMoreListener() {
				public void onLoadMore() {
					
						if (arrayListLength == 14) {
							arrayListLength = 0;
							new GetAudioAsynctask(offset1+15, limit1,"name").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
							((LoadMoreListView) music_folder_listView).onLoadMore();
						}else {
							((LoadMoreListView) music_folder_listView).onLoadMoreComplete();
						}
				}
			});
		
		image_music_folder_sort.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				setPopupMenu();
			}
		});

		
	registerForContextMenu(music_folder_listView);
		
		return rootView;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.music_menu, menu);		
	}

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		
		case R.id.dial_lay_download:
			newDownload(audioDetailBeans_total.get(info.position).getOriginal(),destinationFolder, audioDetailBeans_total.get(info.position).getFile());
			return true;
		case R.id.dial_lay_delete:
			new DeleteAsynctask(audioDetailBeans_total.get(info.position).getId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			return true;
		case R.id.dial_lay_rename:
			renameFile(audioDetailBeans_total.get(info.position).getId());
			return true;
		case R.id.dial_lay_properties:
			new GetPropertiesAsynctask(audioDetailBeans_total.get(info.position).getId(),"3").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}
	
	private void setOnClickListener() {
		
		image_music_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(isSearchVisible)
				{
					lay_edit_music.setVisibility(View.GONE);
				}
				else
				{
					lay_edit_music.setVisibility(View.VISIBLE);
				}
				
				isSearchVisible = !isSearchVisible;
				
			}
		});

		editTextMusicSearch.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				v.setFocusable(true);
				v.setFocusableInTouchMode(true);
				return false;
			}
		});
        
		editTextMusicSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence t, int start, int before, int count) {
				audioDetailBeansSearch.clear();
					for (int i = 0; i < audioDetailBeans_total.size(); i++) {
						if (audioDetailBeans_total.get(i).getFile().toLowerCase().contains(t)) {
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
		
		
		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					if (!mediaPlayer.isPlaying()) {
						mediaPlayer.start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});

		pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					mediaPlayer.pause();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});

		forward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					if (mediaPlayer.isPlaying()) {
						// get current song position
						int currentPosition = mediaPlayer.getCurrentPosition();
						// check if seekForward time is lesser than song duration
						if (currentPosition + seekForwardTime <= mediaPlayer
								.getDuration()) {
							// forward song
							mediaPlayer.seekTo(currentPosition + seekForwardTime);
						} else {
							// forward to end position
							mediaPlayer.seekTo(mediaPlayer.getDuration());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		reword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					if (mediaPlayer.isPlaying()) {
						// get current song position
						int currentPosition = mediaPlayer.getCurrentPosition();
						// check if seekBackward time is greater than 0 sec
						if (currentPosition - seekBackwardTime >= 0) {
							// forward song
							mediaPlayer.seekTo(currentPosition - seekBackwardTime);
						} else {
							// backward to starting position
							mediaPlayer.seekTo(0);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		delete_all_music.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				audioDetailBeans_delete.clear();
				for (int i = 0; i < audioDetailBeans_total.size(); i++) {
					if (isCheck_contact_music[i]) {
						audioDetailBeans_delete.add(new MediaDetailBean(
								audioDetailBeans_total.get(i).getId()));
					}

				}

				audioDetailBeans_delete.size();
				for (int j = 0; j < audioDetailBeans_delete.size(); j++) {
					if (audioDetailBeans_delete.size() == 1) {
						new DeleteAsynctask(audioDetailBeans_delete.get(j)
								.getId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						break;
					} else {
						new DeleteAsynctaskMultipulMusic(
								audioDetailBeans_delete).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
						select_all_music.setBackgroundResource(R.drawable.icon_check_box);
						
						/*music_list_afterClick_LL.setVisibility(View.GONE);
						music_list_beforeClick_LL.setVisibility(View.VISIBLE);*/

					} else {
						isCheck_contact_music[i] = true;
						select_all_music.setBackgroundResource(R.drawable.icon_checked_box);
						
						/*music_list_afterClick_LL.setVisibility(View.VISIBLE);
						music_list_beforeClick_LL.setVisibility(View.GONE); */
					}
				}
				music_folder_listView.setAdapter(new AlbumGridAdapter(getActivity(), audioDetailBeans_total));
			}
		});

		download_all_music_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				DeviceMusicListFragment mFilesFragment = new DeviceMusicListFragment();
				Bundle bunldeObj = new Bundle();
				bunldeObj.putString("position", "4");
				DashBoardActivity.replaceFragementsClick(mFilesFragment, bunldeObj,"Music");
				
				/*
				audioDetailBeans_delete.clear();
				for (int i = 0; i < audioDetailBeans_total.size(); i++) {
					if (isCheck_contact_music[i]) {
						audioDetailBeans_delete.add(new MediaDetailBean(audioDetailBeans_total.get(i).getOriginal(), audioDetailBeans_total.get(i).getFile()));
					}
				}
				for (int i = 0; i < audioDetailBeans_delete.size(); i++) {
					newDownload(audioDetailBeans_delete.get(i).getOriginal(), destinationFolder, audioDetailBeans_delete.get(i).getFile());

					Toast.makeText(mContext, "Start Music Downloading", Toast.LENGTH_LONG).show();
				}
				
				new GetAudioAsynctask(offset1, limit1,"name").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				 */
				}
		});
		
		image_music_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				audioDetailBeans_delete.clear();
				for (int i = 0; i < audioDetailBeans_total.size(); i++) {
					if (isCheck_contact_music[i]) {
						audioDetailBeans_delete.add(new MediaDetailBean(audioDetailBeans_total.get(i).getOriginal(), audioDetailBeans_total.get(i).getFile()));
					}

				}
				for (int i = 0; i < audioDetailBeans_delete.size(); i++) {
					newDownload(audioDetailBeans_delete.get(i).getOriginal(), destinationFolder, audioDetailBeans_delete.get(i).getFile2());

					Toast.makeText(mContext, "Start Music Downloading", Toast.LENGTH_LONG).show();
				}
				
				new GetAudioAsynctask(offset1, limit1,"name").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		});
		
		
		image_music_share.setOnClickListener(new OnClickListener() {

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
	}

	Runnable run = new Runnable() {
		@Override
		public void run() {
			seekUpdation();
		}
	};

	public void seekUpdation() {
		seekbar.setProgress(mediaPlayer.getCurrentPosition());
		seekHandler.postDelayed(run, 1000);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		offset1=0; limit1=14;
		new GetAudioAsynctask(offset1, limit1,"name").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public class AlbumGridAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<MediaDetailBean> arrayList;
		LinearLayout LAY_MUSIC_LONG_LL_CLICK=null;
		LinearLayout LAY_MUSIC_SINGLE_CB_CLICK=null;
		ImageView MUSIC_SINGLE_SELECT=null;
		
	
		public AlbumGridAdapter(Context context, ArrayList<MediaDetailBean> photoDetailBeans) {

			this.context = context;
			this.arrayList = photoDetailBeans;
		}

		@Override
		public int getCount() {
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

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder mViewHolder;
			// LayoutInflator to call external grid_item.xml file

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (convertView == null) {

				convertView = new View(context);

				// get layout from grid_item.xml ( Defined Below )

				convertView = inflater.inflate(
						R.layout.audio_list_click_fragment, null);
				mViewHolder = new ViewHolder();
				// set value into textview

				mViewHolder.list_audio_item_label = (TextView) convertView.findViewById(R.id.list_audio_item_label_list);
				mViewHolder.list_audio_item_label_size_list = (TextView) convertView.findViewById(R.id.list_audio_item_label_size_list);
				mViewHolder.music_single_select = (ImageView) convertView.findViewById(R.id.music_single_select_list);
				mViewHolder.lay_music_long_press = (LinearLayout) convertView.findViewById(R.id.lay_music_long_press);
				mViewHolder.lay_music_single_select_list = (LinearLayout) convertView.findViewById(R.id.lay_music_single_select_list);
				mViewHolder.list_audio_item_image = (ImageView) convertView.findViewById(R.id.list_audio_item_image_list);

				
				try {
					mHttpImageManager = new HttpImageManager(HttpImageManager.createDefaultMemoryCache(), new FileSystemPersistence(BASEDIR));
					Uri uri = Uri.parse(arrayList.get(position).getAlbum_image());
					Bitmap bitmap = mHttpImageManager.loadImage(new HttpImageManager.LoadRequest(uri, mViewHolder.list_audio_item_image));
					if (bitmap != null) {

						mViewHolder.list_audio_item_image.setImageBitmap(bitmap);
					}
					else
					{
						mViewHolder.list_audio_item_image.setImageResource(R.drawable.icon_music);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
					
				mViewHolder.lay_music_long_press.setTag("LAY_MUSIC_LONG_LL" + position);
				mViewHolder.lay_music_single_select_list.setTag("LAY_MUSIC_SINGLE_CB" + position);
				mViewHolder.music_single_select.setTag("MUSIC_SINGLE_SELECT" + position);
				

				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
				
				mViewHolder.lay_music_long_press.setTag("LAY_MUSIC_LONG_LL" + position);
				mViewHolder.lay_music_single_select_list.setTag("LAY_MUSIC_SINGLE_CB" + position);
				mViewHolder.music_single_select.setTag("MUSIC_SINGLE_SELECT" + position);
				
			}
			
			mViewHolder.list_audio_item_label.setText(arrayList.get(position).getFile());
			mViewHolder.list_audio_item_label_size_list.setText(arrayList.get(position).getDuration()+" Seconds");
			
			if (isCheck_contact_music[position]) {
			  mViewHolder.music_single_select.setImageResource(R.drawable.icon_checked_box); 
			}else {
			  mViewHolder.music_single_select.setImageResource(R.drawable.icon_check_box); 
			}
			return convertView;
		}

	}

	public static class ViewHolder {
		public TextView list_audio_item_label, list_audio_item_label_size,list_audio_item_label_size_list;
		public ImageView list_audio_item_image, music_single_select;
		public LinearLayout lay_music_long_press, lay_music_single_select_list;
	}

	public class GetAudioAsynctask extends AsyncTask<String, String, Object[]> {

		ProgressDialog progressDialog;
		String response = "";
		int offset , limit;
		String sortType;

		public GetAudioAsynctask(int i, int j,String sortType) {
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

			//return HingAppParsing.getMediaByAlbumNames(getActivity(),"3",MusicFragment.album_name,offset, limit, "2"); // type=1 for image,2 for video,3 for audio
			return HingAppParsing.getMediaByAlbumNamesSorted(getActivity(),"3",MusicFragment.album_name,offset, limit, "2",sortType); // type=1 for image,2 for video,3 for audio
			
		}

		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try {
				boolean status = (Boolean) result[0];
				response = (String) result[1];

				if (status) {
					audioDetailBeans.clear();
					audioDetailBeans_total.clear();
					audioDetailBeans = (ArrayList<MediaDetailBean>) result[2];
					for (int i = 0; i < audioDetailBeans.size(); i++) {
						audioDetailBeans_total.add(audioDetailBeans.get(i));
					}

					isCheck_contact_music = new boolean[audioDetailBeans_total.size()];

					for (int i = 0; i < audioDetailBeans_total.size(); i++) {
						if (!GRID_DATA.contains(audioDetailBeans_total.get(i) .getFile())) {
							GRID_DATA.add(audioDetailBeans_total.get(i).getFile());
						}
					}

					music_folder_listView.setAdapter(new AlbumGridAdapter(getActivity(), audioDetailBeans_total));

					music_list_afterClick_LL.setVisibility(View.GONE);
					music_list_beforeClick_LL.setVisibility(View.VISIBLE);
					
					Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT) .show();
				} else {
					response = (String) result[1];
					AppUtils.showDialog(getActivity(), response);
				}
				
				((LoadMoreListView) music_folder_listView).onLoadMoreComplete();
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

			return HingAppParsing.deleteSingle(getActivity(),
					HinjAppPreferenceUtils.getChildRaId(getActivity()), "8",
					value);
		}

		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try {
				audioDetailBeans_total.clear();
				audioDetailBeans_delete.clear();
				select_all_music.setBackgroundResource(R.drawable.icon_check_box);
				new GetAudioAsynctask(offset1, limit1,"name").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class DeleteAsynctaskMultipulMusic extends
			AsyncTask<String, String, Object[]> {

		ProgressDialog progressDialog;
		String response = "";
		String value;
		ArrayList<MediaDetailBean> mMediaDetailBeanDelete_;

		public DeleteAsynctaskMultipulMusic(
				ArrayList<MediaDetailBean> mMediaDetailBeanDelete) {
			this.mMediaDetailBeanDelete_ = mMediaDetailBeanDelete;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(), "Loading...",
					"Please Wait");
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
					HinjAppPreferenceUtils.getChildRaId(getActivity()), "8",
					multipul_id_music);
		}

		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try {
				multipul_id_music = "";
				audioDetailBeans_total.clear();
				audioDetailBeans_delete.clear();

				select_all_music
						.setBackgroundResource(R.drawable.icon_check_box);
				new GetAudioAsynctask(offset1, limit1,"name").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// ******************************************************
	public void newDownload(String downloadPath, String folderName,
			String FileName) {
		try {
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

			String path = Utility.getDirectory(folderName) + File.separator+ FileName;
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
			try {
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
						Toast.makeText(getActivity(),
								"FAILED!\n" + "reason of " + reason,
								Toast.LENGTH_LONG).show();
					} else if (status == DownloadManager.STATUS_PAUSED) {
						Toast.makeText(getActivity(),
								"PAUSED!\n" + "reason of " + reason,
								Toast.LENGTH_LONG).show();
					} else if (status == DownloadManager.STATUS_PENDING) {
						Toast.makeText(getActivity(), "PENDING!",
								Toast.LENGTH_LONG).show();
					} else if (status == DownloadManager.STATUS_RUNNING) {
						Toast.makeText(getActivity(), "RUNNING!",
								Toast.LENGTH_LONG).show();
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
		
		LinearLayout dial_lay_download = (LinearLayout) dialog.findViewById(R.id.dial_lay_download);
		LinearLayout dial_lay_delete = (LinearLayout) dialog.findViewById(R.id.dial_lay_delete);
		LinearLayout dial_lay_rename = (LinearLayout) dialog.findViewById(R.id.dial_lay_rename);
		LinearLayout dial_lay_properties = (LinearLayout) dialog.findViewById(R.id.dial_lay_properties);
		
		dial_lay_download.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				newDownload(audioDetailBeans_delete.get(arg2).getOriginal(),destinationFolder, audioDetailBeans_delete.get(arg2).getFile());
				dialog.dismiss();
			}
		});
		
		dial_lay_delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			    new DeleteAsynctask(audioDetailBeans_delete.get(arg2).getId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
				
				new GetPropertiesAsynctask(audioDetailBeans_delete.get(arg2).getId(),"3").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		});
		
		dialog.show();
	}
	
	protected void hello(int position) {
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			
			if (audioDetailBeansSearch.size()>0) {
				mediaPlayer.setDataSource(audioDetailBeansSearch.get(position)
						.getOriginal());
			}else {
				mediaPlayer.setDataSource(audioDetailBeans_total.get(position)
						.getOriginal());
			}
			
			
			mediaPlayer.prepare(); // might take long! (for buffering, etc)
			mediaPlayer.start();

			seekbar.setMax(mediaPlayer.getDuration());

			seekUpdation();
		} catch (Exception e) {
			e.printStackTrace();
		}

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
			
			return HingAppParsing.renameFile(getActivity(), String.valueOf(id), "3", newName);  
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
					new GetAudioAsynctask(offset1, limit1,"name").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
		FilePropertiesBean filePropertiesBean;
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
					new GetAudioAsynctask(offset1, limit1,"name").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				else if(item.getTitle().toString().equalsIgnoreCase("Size"))
				{
					offset1=0; limit1=14;
					new GetAudioAsynctask(offset1, limit1,"size").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				else if(item.getTitle().toString().equalsIgnoreCase("Length"))
				{
					offset1=0; limit1=14;
					new GetAudioAsynctask(offset1, limit1,"length").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				else if(item.getTitle().toString().equalsIgnoreCase("Date Added"))
				{
					offset1=0; limit1=14;
					new GetAudioAsynctask(offset1, limit1,"date_added").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				
				return true;  
			}  
		});  

		popup.show();//showing popup menu  
	}  
}

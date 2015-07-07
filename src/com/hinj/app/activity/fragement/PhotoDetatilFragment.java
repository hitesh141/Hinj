package com.hinj.app.activity.fragement;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.hinj.app.activity.DashBoardActivity;
import com.hinj.app.activity.R;
import com.hinj.app.model.MediaDetailBean;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.Utility;
import com.hinj.imageload.FileSystemPersistence;
import com.hinj.imageload.HttpImageManager;
import com.hinj.parsing.HingAppParsing;

@SuppressLint("ValidFragment")
public class PhotoDetatilFragment extends Fragment {
	
	private GridView photo_folder_gridView_list;
	
	private ImageView back_imageView;
	
	private ArrayList<MediaDetailBean> mPhotoDetailBean = new ArrayList<MediaDetailBean>();
	private ArrayList<MediaDetailBean> mPhotoDetailBean_delete = new ArrayList<MediaDetailBean>();

	public ImageView select_all_photo_list, delete_all_photo_list,
		download_all_photo_list;
	public static int oneTimeOnly = 0;
	public static boolean[] isCheck_contact_music;
	public static String multipul_id_music = "";
	private long[] downloadIdsNew;
	static int CountIDs = 0;
	private DownloadManager downloadManager;
	public String destinationFolder = "/Hinj/Photo";
	Context mContext;
	private boolean click = false;
	public static final String BASEDIR = "/sdcard/httpimage";
	public HttpImageManager mHttpImageManager;
	public boolean click_select = false;

	public PhotoDetatilFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.photo_detail_fragment, container, false);
        
        initialization(rootView);
        
        setOnClickListener();
        
        return rootView;
    }

	private void initialization(View rootView) {
		photo_folder_gridView_list = (GridView) rootView.findViewById(R.id.photo_folder_gridView_list);
		
		ArrayList<String> arrayList = new ArrayList<String>();
		mContext = this.getActivity();

		 downloadIdsNew = new long[30];
	        downloadManager = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
		
		delete_all_photo_list = (ImageView) rootView.findViewById(R.id.delete_all_photo_detail_list);
		select_all_photo_list = (ImageView) rootView.findViewById(R.id.select_all_photo_detail_list);
		download_all_photo_list = (ImageView) rootView.findViewById(R.id.download_all_photo_detail_list);
		
		back_imageView = (ImageView) rootView.findViewById(R.id.back_imageView);
		
		delete_all_photo_list.setVisibility(View.GONE);
	}
	
	private void setOnClickListener() {
		
		back_imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bunldeObj = new Bundle();
				bunldeObj.putString("position", "4");
				PhotosFragment mPhotosFragment = new PhotosFragment();
				DashBoardActivity.replaceFragementsClickBack(mPhotosFragment, bunldeObj, "Photos");
			}
		});
		
		photo_folder_gridView_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				
				if (click) {
					
				}else {
					click = false;
					PhotoFullViewFragment mPhotoFullViewFragment  = new PhotoFullViewFragment(mPhotoDetailBean.get(position).getOriginal());
					Bundle bunldeObj = new Bundle();
					bunldeObj.putString("position", "4");
					DashBoardActivity.replaceFragementsClick(mPhotoFullViewFragment, bunldeObj,"Photos");
				}
			}
		});
		
		photo_folder_gridView_list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v, int position, long arg3) {
				click = true;
				ImageView photo_grid_single_select_list = (ImageView) v.findViewById(R.id.photo_grid_single_select_list);

				if (isCheck_contact_music[position] == false) {
					photo_grid_single_select_list.setVisibility(View.VISIBLE);
					isCheck_contact_music[position] = true;
					photo_grid_single_select_list.setImageResource(R.drawable.icon_checked_box);
					delete_all_photo_list.setVisibility(View.VISIBLE);
				} else {
					isCheck_contact_music[position] = false;
					photo_grid_single_select_list.setImageResource(R.drawable.icon_check_box);
					photo_grid_single_select_list.setVisibility(View.GONE);
					delete_all_photo_list.setVisibility(View.GONE);
				}
				return false;
			}
		});

		delete_all_photo_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPhotoDetailBean_delete.clear();
				for (int i = 0; i < mPhotoDetailBean.size(); i++) {
					if (isCheck_contact_music[i]) {
						mPhotoDetailBean_delete.add(new MediaDetailBean(
								mPhotoDetailBean.get(i).getId()));
					}

				}

				mPhotoDetailBean_delete.size();
				for (int j = 0; j < mPhotoDetailBean_delete.size(); j++) {
					if (mPhotoDetailBean_delete.size() == 1) {
						new DeleteAsynctask(mPhotoDetailBean_delete.get(j)
								.getId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						break;
					} else {
						new DeleteAsynctaskMultipulMusic(
								mPhotoDetailBean_delete).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						break;
					}
				}
				photo_folder_gridView_list.setAdapter(new PhotoDetailAdapter(getActivity(), mPhotoDetailBean));
			}
		});

		select_all_photo_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				for (int i = 0; i < isCheck_contact_music.length; i++) {
					if (isCheck_contact_music[i]) {
						isCheck_contact_music[i] = false;
						delete_all_photo_list.setVisibility(View.GONE);
						select_all_photo_list.setBackgroundResource(R.drawable.icon_check_box);

					} else {
						isCheck_contact_music[i] = true;
						delete_all_photo_list.setVisibility(View.VISIBLE);
						select_all_photo_list.setBackgroundResource(R.drawable.icon_checked_box);

					}
				}
				photo_folder_gridView_list.setAdapter(new PhotoDetailAdapter(getActivity(), mPhotoDetailBean));
			}
		});

		download_all_photo_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				DeviceImagesListFragment mFilesFragment = new DeviceImagesListFragment();
				Bundle bunldeObj = new Bundle();
				bunldeObj.putString("position", "4");
				DashBoardActivity.replaceFragementsClick(mFilesFragment, bunldeObj,"Photos");
				
				/*mPhotoDetailBean_delete.clear();
				for (int i = 0; i < mPhotoDetailBean.size(); i++) {
					if (isCheck_contact_music[i]) {
						mPhotoDetailBean_delete.add(new MediaDetailBean(
								mPhotoDetailBean.get(i).getOriginal(),
								mPhotoDetailBean.get(i).getFile(), mPhotoDetailBean.get(i).getFile()));
					}

				}
				for (int i = 0; i < mPhotoDetailBean_delete.size(); i++) {
					String result = "";
					try {
						String fileName=mPhotoDetailBean_delete.get(i).getFile2();
						result = fileName.substring(fileName.lastIndexOf("/") + 1); 
						System.out.println("Image name " + result);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					newDownload(mPhotoDetailBean_delete.get(i).getOriginal(), destinationFolder, result);
					photo_folder_gridView_list.setAdapter(new PhotoDetailAdapter(getActivity(), mPhotoDetailBean));
					//Toast.makeText(mContext, "Start Photo Downloading", Toast.LENGTH_LONG).show();
				}*/
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		new GetAudioAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public class PhotoDetailAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<MediaDetailBean> arrayList;

		// Constructor to initialize values
		public PhotoDetailAdapter(Context context, ArrayList<MediaDetailBean> photoDetailBeans) {

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

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder mViewHolder;
			// LayoutInflator to call external grid_item.xml file

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (convertView == null) {

				convertView = new View(context);

				// get layout from grid_item.xml ( Defined Below )

				convertView = inflater.inflate(R.layout.photo_detail_row, null);
				mViewHolder = new ViewHolder();
				// set value into textview

				mViewHolder.imag_grid_item_photo_detail = (ImageView) convertView.findViewById(R.id.imag_grid_item_photo_detail);
				mViewHolder.photo_grid_single_select_list = (ImageView) convertView.findViewById(R.id.photo_grid_single_select_list);
				
				
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			
			mHttpImageManager = new HttpImageManager(HttpImageManager.createDefaultMemoryCache(), new FileSystemPersistence(BASEDIR));
			Uri uri = Uri.parse(arrayList.get(position).getOriginal());
			Bitmap bitmap = mHttpImageManager.loadImage(new HttpImageManager.LoadRequest(uri, mViewHolder.imag_grid_item_photo_detail));
			if (bitmap != null) {

				mViewHolder.imag_grid_item_photo_detail.setImageBitmap(bitmap);
			}
			
				if (isCheck_contact_music[position]) {
					mViewHolder.photo_grid_single_select_list.setVisibility(View.VISIBLE);
					mViewHolder.photo_grid_single_select_list.setImageResource(R.drawable.icon_checked_box);
				} else {
					mViewHolder.photo_grid_single_select_list.setImageResource(R.drawable.icon_check_box);
					mViewHolder.photo_grid_single_select_list.setVisibility(View.GONE);
				}
			
			return convertView;
		}
	}

	public static class ViewHolder {
		public ImageView photo_grid_single_select_list, imag_grid_item_photo_detail;
	}

	public class GetAudioAsynctask extends AsyncTask<String, String, Object[]> {

		ProgressDialog progressDialog;
		String response = "";

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {

			return HingAppParsing.getMediaByAlbumNames(getActivity(), "1",PhotosFragment.photo_album_name,0,15,"");
			// // type=1 for image,2 for video,3 for audio
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try {
				boolean status = (Boolean) result[0];
				response = (String) result[1];

				if (status) {
					mPhotoDetailBean = (ArrayList<MediaDetailBean>) result[2];

					isCheck_contact_music = new boolean[mPhotoDetailBean.size()];

					photo_folder_gridView_list.setAdapter(new PhotoDetailAdapter(getActivity(), mPhotoDetailBean));

					//Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
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

			return HingAppParsing.deleteSingle(getActivity(),
					HinjAppPreferenceUtils.getChildRaId(getActivity()), "6",
					value);
		}

		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try {
				mPhotoDetailBean.clear();
				mPhotoDetailBean_delete.clear();
				delete_all_photo_list.setVisibility(View.GONE);
				select_all_photo_list
						.setBackgroundResource(R.drawable.icon_check_box);
				new GetAudioAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				
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
					HinjAppPreferenceUtils.getChildRaId(getActivity()), "6",
					multipul_id_music);
		}

		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try {
				multipul_id_music = "";
				mPhotoDetailBean.clear();
				mPhotoDetailBean_delete.clear();
				delete_all_photo_list.setVisibility(View.GONE);
				select_all_photo_list
						.setBackgroundResource(R.drawable.icon_check_box);
				new GetAudioAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// ******************************************************
			public void newDownload(String downloadPath, String folderName, String FileName) {
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
					String path = Utility.getDirectory(folderName) + File.separator + FileName;
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

					IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
					getActivity().registerReceiver(downloadReceiver, filter);

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}

			public BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

				@Override
				public void onReceive(Context arg0, Intent intent) {
					try {
						// Log.d("downloadReceiver", "====================="
						// +downloadIds[]);
						DownloadManager.Query query = new DownloadManager.Query();
						query.setFilterById(downloadIdsNew);
						Cursor cursor = downloadManager.query(query);

						if (cursor.moveToFirst()) {
							int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
							int status = cursor.getInt(columnIndex);
							int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
							int reason = cursor.getInt(columnReason);
							long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

							if (status == DownloadManager.STATUS_SUCCESSFUL) {
								try {
									Log.e("Ref ID***********", "" + id);

									getActivity().unregisterReceiver(downloadReceiver);

								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									// dataSource.closeDB();
								}

							} else if (status == DownloadManager.STATUS_FAILED) {
								Toast.makeText(getActivity(),"FAILED!\n" + "reason of " + reason,
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
}
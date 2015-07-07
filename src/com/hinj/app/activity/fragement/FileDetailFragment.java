package com.hinj.app.activity.fragement;

import java.io.File;
import java.util.ArrayList;

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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hinj.app.activity.R;
import com.hinj.app.model.FileDataBean;
import com.hinj.app.model.MediaDetailBean;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.Utility;
import com.hinj.imageload.FileSystemPersistence;
import com.hinj.imageload.HttpImageManager;
import com.hinj.parsing.HingAppParsing;

public class FileDetailFragment extends Fragment {
	
	private GridView photo_folder_gridView_list;
	private ArrayList<FileDataBean> mPhotoDetailBean = new ArrayList<FileDataBean>();
	private ArrayList<FileDataBean> mPhotoDetailBean_delete = new ArrayList<FileDataBean>();

	public ImageView select_all_photo_list, delete_all_photo_list, download_all_photo_list;
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
	
	public TextView file_type_textView;

	String fileType = "";
	
	public FileDetailFragment(String type) {
		this.fileType = type;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_file_detail, container, false);
        
        initialization(rootView);
        
        setOnClickListener();
        
        return rootView;
    }

	private void initialization(View rootView) {
		photo_folder_gridView_list = (GridView) rootView.findViewById(R.id.photo_folder_gridView_list);
		
		ArrayList<String> arrayList = new ArrayList<String>();
		mContext = this.getActivity();

		downloadIdsNew = new long[30];
		downloadManager = (DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
		
		delete_all_photo_list = (ImageView) rootView.findViewById(R.id.delete_all_photo_list);
		select_all_photo_list = (ImageView) rootView.findViewById(R.id.select_all_photo_list);
		download_all_photo_list = (ImageView) rootView.findViewById(R.id.download_all_photo_list);
		
		file_type_textView = (TextView) rootView.findViewById(R.id.file_type_textView);
		
		String finalType="";
		if(fileType.equalsIgnoreCase("1"))
		{
			finalType = "Image";
		}
		else if(fileType.equalsIgnoreCase("2"))
		{
			finalType = "Video";
		}
		else if(fileType.equalsIgnoreCase("3"))
		{
			finalType = "Audio";
		}
		else if(fileType.equalsIgnoreCase("4"))
		{
			finalType = "Apk";
		}
		else if(fileType.equalsIgnoreCase("5"))
		{
			finalType = "Others";
		}
		
		file_type_textView.setText(finalType);
	}
	
	private void setOnClickListener() {
	/*	photo_folder_gridView_list.setOnItemClickListener(new OnItemClickListener() {
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
		});*/
		
		/*photo_folder_gridView_list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v, int position, long arg3) {
				click = true;
				ImageView photo_grid_single_select_list = (ImageView) v.findViewById(R.id.photo_grid_single_select_list);

				if (isCheck_contact_music[position] == false) {
					photo_grid_single_select_list.setVisibility(View.VISIBLE);
					isCheck_contact_music[position] = true;
					photo_grid_single_select_list.setImageResource(R.drawable.icon_checked_box);
				} else {
					isCheck_contact_music[position] = false;
					photo_grid_single_select_list.setImageResource(R.drawable.icon_check_box);
					photo_grid_single_select_list.setVisibility(View.GONE);
				}
				return false;
			}
		});*/

	/*	delete_all_photo_list.setOnClickListener(new OnClickListener() {
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
						//new DeleteAsynctaskMultipulMusic( mPhotoDetailBean_delete).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						break;
					}
				}
			}
		});*/

		/*select_all_photo_list.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				for (int i = 0; i < isCheck_contact_music.length; i++) {
					if (isCheck_contact_music[i]) {
						isCheck_contact_music[i] = false;
						select_all_photo_list.setBackgroundResource(R.drawable.icon_check_box);

					} else {
						isCheck_contact_music[i] = true;
						select_all_photo_list.setBackgroundResource(R.drawable.icon_checked_box);

					}
				}
				photo_folder_gridView_list.setAdapter(new PhotoDetailAdapter(getActivity(), mPhotoDetailBean));
			}
		});

		download_all_photo_list.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPhotoDetailBean_delete.clear();
				for (int i = 0; i < mPhotoDetailBean.size(); i++) {
					if (isCheck_contact_music[i]) {
						mPhotoDetailBean_delete.add(new MediaDetailBean(
								mPhotoDetailBean.get(i).getOriginal(),
								mPhotoDetailBean.get(i).getFile(), mPhotoDetailBean.get(i).getFile2()));
					}
				}
				for (int i = 0; i < mPhotoDetailBean_delete.size(); i++) {
					newDownload(mPhotoDetailBean_delete.get(i).getOriginal(),
							destinationFolder, mPhotoDetailBean_delete.get(i).getFile2());

					Toast.makeText(mContext, "Start Photo Downloading",
							Toast.LENGTH_LONG).show();
				}
			}
		});*/
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		new GetFileDetailAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public class PhotoDetailAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<FileDataBean> arrayList;

		// Constructor to initialize values
		public PhotoDetailAdapter(Context context, ArrayList<FileDataBean> photoDetailBeans) {

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
		public FileDataBean getItem(int position) {

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

				convertView = inflater.inflate(R.layout.row_fragment_file_detail, null);
				mViewHolder = new ViewHolder();
				// set value into textview

				mViewHolder.imag_grid_item_photo_detail = (ImageView) convertView.findViewById(R.id.imag_grid_item_photo_detail);
				mViewHolder.photo_grid_single_select_list = (ImageView) convertView.findViewById(R.id.photo_grid_single_select_list);
				
				mViewHolder.file_name_textView = (TextView) convertView.findViewById(R.id.file_name_textView);
				
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			
			mViewHolder.file_name_textView.setText(arrayList.get(position).getFile_name());
			
			mHttpImageManager = new HttpImageManager(HttpImageManager.createDefaultMemoryCache(), new FileSystemPersistence(BASEDIR));
			
			if(arrayList.get(position).getType().equalsIgnoreCase("1"))
			{
				Uri uri = Uri.parse(arrayList.get(position).getOriginal());
				Bitmap bitmap = mHttpImageManager.loadImage(new HttpImageManager.LoadRequest(uri, mViewHolder.imag_grid_item_photo_detail));
			
				if (bitmap != null) {
					mViewHolder.imag_grid_item_photo_detail.setImageBitmap(bitmap);
				}
			}
			else if(!arrayList.get(position).getThumb().equalsIgnoreCase(""))
			{
				Uri uri = Uri.parse(arrayList.get(position).getThumb());
				Bitmap bitmap = mHttpImageManager.loadImage(new HttpImageManager.LoadRequest(uri, mViewHolder.imag_grid_item_photo_detail));
				if (bitmap != null) {
					mViewHolder.imag_grid_item_photo_detail.setImageBitmap(bitmap);
				}
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
		public TextView file_name_textView;
	}

	public class GetFileDetailAsynctask extends AsyncTask<String, String, Object[]> {

		ProgressDialog progressDialog;
		String response = "";

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {

			return HingAppParsing.getDeviceData(getActivity(), fileType);
			
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
					mPhotoDetailBean = (ArrayList<FileDataBean>) result[2];

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
				select_all_photo_list
						.setBackgroundResource(R.drawable.icon_check_box);
				new GetFileDetailAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

				select_all_photo_list
						.setBackgroundResource(R.drawable.icon_check_box);
				new GetFileDetailAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
			String path = Utility.getDirectory(folderName) + File.separator
					+ FileName;
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
			// ex.printStackTrace();
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
							// TODO Auto-generated catch block
							// e.printStackTrace();
						} finally {
							// dataSource.closeDB();
						}

					} else if (status == DownloadManager.STATUS_FAILED) {
						/*Toast.makeText(getActivity(),
								"FAILED!\n" + "reason of " + reason,
								Toast.LENGTH_LONG).show();*/
					} else if (status == DownloadManager.STATUS_PAUSED) {
					/*	Toast.makeText(getActivity(),
								"PAUSED!\n" + "reason of " + reason,
								Toast.LENGTH_LONG).show();*/
					} else if (status == DownloadManager.STATUS_PENDING) {
						/*Toast.makeText(getActivity(), "PENDING!",
								Toast.LENGTH_LONG).show();*/
					} else if (status == DownloadManager.STATUS_RUNNING) {
					/*	Toast.makeText(getActivity(), "RUNNING!",
								Toast.LENGTH_LONG).show();*/
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};
}
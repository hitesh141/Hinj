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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.facebook.internal.Utility;
import com.hinj.app.activity.R;
import com.hinj.app.model.InstallAppDetailBean;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.imageload.FileSystemPersistence;
import com.hinj.imageload.HttpImageManager;
import com.hinj.parsing.HingAppParsing;

public class InstallAppsFragment extends Fragment {
	
	private LoadMoreListView install_apps_list_view;
	private ArrayList<InstallAppDetailBean> instaCllAppDetailBeans = new ArrayList<InstallAppDetailBean>();
	private ArrayList<InstallAppDetailBean> installAppDetailBeans_total = new ArrayList<InstallAppDetailBean>();
	private static int offset1=0, limit1=14;
	public static int arrayListLengthApp = 0;
	
	public InstallAppsFragment(){}
	
	public ImageView download_all_apk;
	
	private DownloadManager downloadManager;
	public String destinationFolder = "/Hinj/Apk";
	public static boolean[] isCheck_contact_apk;
	public static String multipul_id_apk = "";
	private long[] downloadIdsNew;
	static int CountIDsApk = 0;
	
	public static final String BASEDIR = "/sdcard/httpimage";
	public HttpImageManager mHttpImageManager;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_apps_list, container, false);
        
        install_apps_list_view = (LoadMoreListView) rootView.findViewById(R.id.install_apps_list_view);
        
        download_all_apk = (ImageView) rootView.findViewById(R.id.download_all_apk);
        downloadIdsNew = new long[30];
        downloadManager = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
        
        ((LoadMoreListView) install_apps_list_view).setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				
					if (arrayListLengthApp == 14) {
						arrayListLengthApp = 0;
						new GetInstallAppAsynctask(offset1+15, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						((LoadMoreListView) install_apps_list_view).onLoadMore();
					}else {
						((LoadMoreListView) install_apps_list_view).onLoadMoreComplete();
					}
				}
			});
        
        /*  download_all_apk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				for (int i = 0; i < installAppDetailBeans_total.size(); i++) {
					newDownload(installAppDetailBeans_total.get(i).getDownload_link(),
							destinationFolder, installAppDetailBeans_total.get(i).getApk_file());

					Toast.makeText(getActivity(), "Start Photo Downloading",
							Toast.LENGTH_LONG).show();
				}
			}
		});*/

        return rootView;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		offset1=0; limit1=14;
		new GetInstallAppAsynctask(offset1, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	 private class InstallAppAdapter extends ArrayAdapter<InstallAppDetailBean> {

			ArrayList<InstallAppDetailBean> deviceArray;
			Context ctx;

			public InstallAppAdapter(Context context, int textViewResourceId,ArrayList<InstallAppDetailBean> smsDetailBeans) {
				super(context, textViewResourceId, smsDetailBeans);

				this.ctx = context;
				this.deviceArray = smsDetailBeans;
				notifyDataSetChanged();
			}
			
			@Override
			public InstallAppDetailBean getItem(int position) {
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
					convertView = LayoutInflater.from(ctx).inflate(R.layout.list_item_install_app, parent, false);
					
					holder.app_icon_imageView = (ImageView) convertView.findViewById(R.id.app_icon_imageView);
					holder.install_app_name_textView = (TextView) convertView.findViewById(R.id.install_app_name_textView);
					holder.download_icon = (ImageView) convertView.findViewById(R.id.download_icon);
					holder.cancel_imgView = (ImageView) convertView.findViewById(R.id.cancel_imgView);
					
					try {
						mHttpImageManager = new HttpImageManager(HttpImageManager.createDefaultMemoryCache(), new FileSystemPersistence(BASEDIR));
						Uri uri = Uri.parse(installAppDetailBeans_total.get(position).getApk_icon_file());
						Bitmap bitmap = mHttpImageManager.loadImage(new HttpImageManager.LoadRequest(uri, holder.app_icon_imageView));
						if (bitmap != null) {

							holder.app_icon_imageView.setImageBitmap(bitmap);
						}
						else
						{
							//holder.app_icon_imageView.setBackgroundResource(R.drawable.icon_photo);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					holder.download_icon .setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							newDownload(installAppDetailBeans_total.get(position).getDownload_link(),
									destinationFolder, installAppDetailBeans_total.get(position).getApk_file());							
						}
					});
							
					holder.cancel_imgView .setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							new DeleteAsynctask(installAppDetailBeans_total.get(position).getId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
							installAppDetailBeans_total.remove(position);
							notifyDataSetChanged();
						}
					});
					
					convertView.setTag(holder);
				}
				else 
				{
	                holder = (ViewHolder)convertView.getTag();
	            }
				
				holder.install_app_name_textView.setText(deviceArray.get(position).getApp_name());
				/*holder.test_msg_text_view.setText(deviceArray.get(position).getText_message());
				holder.sms_time_text_view.setText(deviceArray.get(position).getModified());*/
				
				//holder.contact_imageView.setBackgroundResource(R.drawable.addcontacticon);
		        
				return convertView;
			}
		}
		
		public static class ViewHolder {
		        public TextView install_app_name_textView;
		        public ImageView download_icon,cancel_imgView,app_icon_imageView;
		}
		
		public class GetInstallAppAsynctask extends AsyncTask<String, String,Object[]> {
			
			ProgressDialog progressDialog ;
			String response = "";
			int offset , limit;
			
			public GetInstallAppAsynctask(int offset1, int limit1) {
				offset = offset1;
				limit = limit1;
			}

			@Override
			public void onPreExecute() {
				progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
			}

			@Override
			public Object[] doInBackground(String... params) {
				
				return HingAppParsing.getInstallAppsDetails(getActivity(), HinjAppPreferenceUtils.getChildUploadDeviceId(getActivity()),
									HinjAppPreferenceUtils.getChildRaId(getActivity()), offset, limit,"in"); 
			}  
	 
			@Override
			public void onPostExecute(Object[] result) {
				progressDialog.cancel();
				try
				{
					boolean status = (Boolean) result[0];
					response = (String) result[1];
					
					if(status)
					{
						instaCllAppDetailBeans.clear();
						instaCllAppDetailBeans = (ArrayList<InstallAppDetailBean>) result[2] ; 
						
						for (int i = 0; i < instaCllAppDetailBeans.size(); i++) {
							installAppDetailBeans_total.add(instaCllAppDetailBeans.get(i));
						}
						
						InstallAppAdapter adapter = new InstallAppAdapter(getActivity(), 0, installAppDetailBeans_total); 
						install_apps_list_view.setAdapter(adapter);
					        
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
					downloadIdsNew[CountIDsApk] = downloadManager.enqueue(request);
					/*
					 * if(!FormatID.equals("0")){ boolean
					 * dbStatus=dataSource.updateDownloadFile(getContentidtext,
					 * ""+downloadIdsNew[CountIDs]); }
					 * Log.d("Here is the Calling newDownload",
					 * "=====================" +downloadIdsNew[CountIDs]);
					 */

				} catch (Exception ex) {

					Log.d("Here is the Exception", "====================="
							+ downloadIdsNew[CountIDsApk]);
					ex.printStackTrace();
				}

				// Log.d("Here is the Calling newDownload", "====================="
				// +CountIDs);
				CountIDsApk++;

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

		public class DeleteAsynctask extends AsyncTask<String, String, Object[]> {

			ProgressDialog progressDialog;
			String response = "";
			String value;

			public DeleteAsynctask(String value) {
				this.value = value;
			}

			@Override
			public void onPreExecute() {
				progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please Wait");
			}

			@Override
			public Object[] doInBackground(String... params) {

				return HingAppParsing.deleteSingle(getActivity(), HinjAppPreferenceUtils.getChildRaId(getActivity()), "3", value);
			}

			@Override
			public void onPostExecute(Object[] result) {
				progressDialog.cancel();
				try {
					instaCllAppDetailBeans.clear();
					offset1=0; limit1=14;
					new GetInstallAppAsynctask(offset1, limit1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
}

package com.hinj.app.activity.fragement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.costum.android.widget.LoadMoreListView;
import com.hinj.app.activity.R;
import com.hinj.app.model.InstallAppDetailBean;
import com.hinj.app.utils.HingAppConstants;
import com.hinj.app.utils.InstalledApp;
import com.hinj.app.utils.JsonPostRequest;
import com.hinj.app.utils.SpyCallSharedPrefrence;
import com.hinj.app.utils.Utility;
import com.hinj.imageload.HttpImageManager;

public class AppsFragment extends Fragment {
	
	private LoadMoreListView install_apps_list_view;
	public static int arrayListLengthApp = 0;
	
	public AppsFragment(){}
	
	public ImageView download_all_apk;
	
	public String destinationFolder = "/Hinj/Apk";
	public static boolean[] isCheck_contact_apk;
	public static String multipul_id_apk = "";
	static int CountIDsApk = 0;
	
	public HttpImageManager mHttpImageManager;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_apps_list, container, false);
        
        install_apps_list_view = (LoadMoreListView) rootView.findViewById(R.id.install_apps_list_view);
        
        download_all_apk = (ImageView) rootView.findViewById(R.id.download_all_apk);
        
        return rootView;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		try {
			InstalledApp[] app = getAllInstalledApp(getActivity());
			
			InstallAppAdapter adapter = new InstallAppAdapter(getActivity(), 0, app); 
			install_apps_list_view.setAdapter(adapter);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class InstallAppAdapter extends ArrayAdapter<InstallAppDetailBean> {

		 	InstalledApp[] deviceArray;
			Context ctx;

			public InstallAppAdapter(Context context, int textViewResourceId,InstalledApp[] installAppBeans) {
				super(context, textViewResourceId);

				this.ctx = context;
				this.deviceArray = installAppBeans;
				notifyDataSetChanged();
			}
			
			@Override
			public InstallAppDetailBean getItem(int position) {
				return super.getItem(position);
			}
			
			@Override
	        public int getCount() {
	            return deviceArray.length;
	        }

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) 
			{
				ViewHolder holder = null;
				if (convertView == null) 
				{
					holder = new ViewHolder();
					convertView = LayoutInflater.from(ctx).inflate(R.layout.list_item_fragment_apps, parent, false);
					
					holder.app_icon_imageView = (ImageView) convertView.findViewById(R.id.app_icon_imageView);
					holder.install_app_name_textView = (TextView) convertView.findViewById(R.id.install_app_name_textView);
					holder.upload_icon = (ImageView) convertView.findViewById(R.id.upload_icon);
					holder.upload_apk_LL = (LinearLayout) convertView.findViewById(R.id.upload_apk_LL);
					
					/*try {
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
					}*/
					
					holder.upload_apk_LL.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
						
							try {
								if(SpyCallSharedPrefrence.getAppUploadName(getActivity()).equalsIgnoreCase(""))
								{
									if (Utility.isNetworkAvailable(ctx)) {
										
										String apkPath = "";
										String appName = "";
										double sizeMb=0;
										
										for (ApplicationInfo app : getActivity().getPackageManager().getInstalledApplications(0)) {
											
											Log.d("PackageList", "package: " + app.packageName + ", sourceDir: " + app.sourceDir);
											  
											if(deviceArray[position].getApplicationPackage().equalsIgnoreCase(app.packageName))
											{
												apkPath = app.publicSourceDir;
												appName = app.packageName;
												
												double size = new File(app.sourceDir).length();
												System.out.println(size);
												sizeMb = (size/1024)/1024;
												System.out.println(sizeMb);
												
											}
										}
										
										if(sizeMb > 5)
										{
											Toast.makeText(getActivity(), "This APK is more than 5 Mb.", Toast.LENGTH_SHORT).show();
											SpyCallSharedPrefrence.saveAppUploadName(getActivity(), "");
										}
										else
										{
											Toast.makeText(getActivity(), "Uploading Start...", Toast.LENGTH_SHORT).show();
											
											SpyCallSharedPrefrence.saveAppUploadName(getActivity(), appName);
											
											new AppUploadAsyncTask().execute(apkPath, appName,"","");
										}
									}
								}
								else
								{
									Toast.makeText(getActivity(), "One apk is already in uploading process.", Toast.LENGTH_SHORT).show();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
							
					convertView.setTag(holder);
				}
				else 
				{
	                holder = (ViewHolder)convertView.getTag();
	            }
				
				holder.install_app_name_textView.setText(deviceArray[position].getApplicationName());
				/*holder.test_msg_text_view.setText(deviceArray.get(position).getText_message());
				holder.sms_time_text_view.setText(deviceArray.get(position).getModified());*/
				//holder.contact_imageView.setBackgroundResource(R.drawable.addcontacticon);
		        
				return convertView;
			}
		}
		
		public static class ViewHolder {
		        public TextView install_app_name_textView;
		        public ImageView upload_icon,app_icon_imageView;
		        public LinearLayout upload_apk_LL;
		}
		
		private InstalledApp[] latestInstalledApp;
		public final  InstalledApp[]  getAllInstalledApp(Context ctx) { 

			final List<PackageInfo> packs = ctx.getPackageManager().getInstalledPackages(0);
			latestInstalledApp = new InstalledApp[packs.size()];
			
					for (int i = 0; i < packs.size(); i++) {

					final String packageName = packs.get(i).packageName;
					final String applicationname = packs.get(i).applicationInfo.loadLabel( ctx.getPackageManager()).toString();
					final String version = packs.get(i).versionName;

					final long installedDate = getallapptime(packageName);
					latestInstalledApp[i]=new InstalledApp(Uri.encode(applicationname),Uri.encode(packageName),version,installedDate);

				}
				System.out.println(latestInstalledApp); 
				return latestInstalledApp;
		}
		
		public final long getallapptime(String packageName){
			final PackageManager pm = getActivity().getPackageManager();
			ApplicationInfo appInfo = null;
			try {
				appInfo = pm.getApplicationInfo(packageName, 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			final String appFile = appInfo.sourceDir;

			final long installed = new File(appFile).lastModified();
			return installed;
		}

		class AppUploadAsyncTask extends AsyncTask<String, Void, Void> {

			int getcountImage=0;
			int getRemainingCount;
			String getResponceImages;

			@Override
			protected void onPreExecute() {
			}

			@Override
			protected Void doInBackground(String... params) {
				// make your request here - it will run in a different thread
				try {
					getResponceImages = uploadImage2(params[0], params[1]);

					JSONObject responseObject = new JSONObject(getResponceImages);
					//JSONArray responseArray = responseObject.getJSONArray("upload");
					JSONObject JsonResponse =responseObject.getJSONObject("response");
					if(JsonResponse.getString("apk").toString().contains("Apk file uploaded successfully"))
					{
						SpyCallSharedPrefrence.saveAppUploadName(getActivity(), "");
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {			
			}
		}
		
		public String uploadImage2(String newFilePath, String appname) {
			
			String android_id = Secure.getString(getActivity().getContentResolver(),Secure.ANDROID_ID);
			Log.i("resonse", "anotherone");
			File file = new File(newFilePath);
			ArrayList<String> arraylist2 = new ArrayList<String>();
			arraylist2.add(file.getPath());
			ArrayList<String[]> arraylist1 = new ArrayList<String[]>();		
			String a[] = { "apk_name",appname};
			arraylist1.add(a);
			String c[] = { "upload_device_id", android_id };
			arraylist1.add(c); 
			
			JsonPostRequest postrequest = new JsonPostRequest();
			//http://evt17.com/iphone/android_services/whatsapp_upload
			String imageResponse = postrequest.doPostWithApkFile(HingAppConstants.URL_67+HingAppConstants.SET_APP_UPLOAD, arraylist1,arraylist2,appname);
			arraylist2.clear();
			arraylist1.clear();         
			return imageResponse;
		}
		
}

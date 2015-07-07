package com.hinj.app.activity.fragement;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hinj.app.activity.R;
import com.hinj.app.gpsTraker.GPSTracker;
import com.hinj.app.model.GpsDetailBean;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.parsing.HingAppParsing;

public class LocateDeviceFragment extends Fragment implements OnInfoWindowClickListener, OnMarkerClickListener {
	
	private ArrayList<GpsDetailBean> gpsDetailBeans = new ArrayList<GpsDetailBean>();
	
	private MapView mapView;
	private GoogleMap map;

	private static Double latitude=0.0, longitude=0.0;
 
	public LocateDeviceFragment(){}
	
	public double latitude1, longitude1, web_lat, web_long;
	public GetGPSDetailAsynctask mGetGPSDetailAsynctask;
	
	@SuppressLint("NewApi")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_locate_device_list, container, false);
        
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        
        mapView.onCreate(savedInstanceState);
        
        turnGPSOn();
        locationDetecter();
        
 
		// Needs to call MapsInitializer before doing any CameraUpdateFactory calls
		try {
			// Gets to GoogleMap from the MapView and does initialization stuff
			map = mapView.getMap();
			map.getUiSettings().setMyLocationButtonEnabled(false);
			map.setMyLocationEnabled(true);
			
			MapsInitializer.initialize(getActivity());
			
			final LatLng JAIPUR = new LatLng(latitude1, longitude1);
			LatLngBounds bounds = new LatLngBounds.Builder()
					.include(JAIPUR).build();
			map.addMarker(new MarkerOptions()
					.position(JAIPUR)
					.title("Create Route")
					.infoWindowAnchor(0.5f, 0.3f)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.rad_pin)));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(
					JAIPUR, 15.0f));
			
			map.getUiSettings().setZoomControlsEnabled(false);
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			map.setOnMarkerClickListener(this);
			map.setOnInfoWindowClickListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

        
        return rootView;
    }
	
	private void turnGPSOn() {
		String provider = android.provider.Settings.Secure.getString(getActivity().getContentResolver(),
				android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (!provider.contains("gps")) { // if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			getActivity().sendBroadcast(poke);
		}
	}
	
	private void locationDetecter() {

		GPSTracker gps = new GPSTracker(getActivity());

		if (gps.canGetLocation()) {
			latitude1 = gps.getLatitude();
			longitude1 = gps.getLongitude();
		} else {

		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		 new GetGPSDetailAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	 /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
       
    }
    
    @Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}
 
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
 
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}
    
	public static class ViewHolder {
		 public TextView sender_name_text_view,test_msg_text_view,sms_time_text_view;
		 public ImageView contact_imageView;
	}
		
	public class GetGPSDetailAsynctask extends AsyncTask<String, String,Object[]> {
			
		ProgressDialog progressDialog ;
		String response = "";
			
		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
				
			return HingAppParsing.getGPSDetails(getActivity(), HinjAppPreferenceUtils.getChildUploadDeviceId(getActivity()),
								HinjAppPreferenceUtils.getChildRaId(getActivity())); 
		}  
	 
		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try{
				boolean status = (Boolean) result[0];
				response = (String) result[1];
					
				if(status)
				{
					gpsDetailBeans = (ArrayList<GpsDetailBean>) result[2] ; 
					for (int i = 0; i < gpsDetailBeans.size(); i++) {
						web_lat = Double.parseDouble(gpsDetailBeans.get(i).getLatitude());
						web_long = Double.parseDouble(gpsDetailBeans.get(i).getLongitude());
					}
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
	
	@Override
	public boolean onMarkerClick(final Marker marker) {
		
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		StringBuilder url = new StringBuilder(); 
	    url.append("http://maps.google.com/maps?f=d&hl=en"); 
	    url.append("&saddr=");
	    url.append(latitude1); 
	    url.append(","); 
	    url.append(longitude1); 
	    url.append("&daddr=");
	    url.append(web_lat); 
	    url.append(","); 
	    url.append(web_long);   
	    url.append("&dirflg=d&nav=1"); 
	    Intent intent = new Intent(Intent.ACTION_VIEW);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setData(Uri.parse(url.toString()));
		intent.addFlags(0x10800000);
		intent.setClassName("com.google.android.apps.m4ps", "com.google.android.maps.driveabout.app.NavigationActivity");
		
		try {
			getActivity().startActivity(intent); 
		} catch (ActivityNotFoundException e) {
	
			intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.driveabout.app.NavigationActivity");
			try {
				getActivity().startActivity(intent);
			} catch (ActivityNotFoundException e2) { }
		}
	}
}

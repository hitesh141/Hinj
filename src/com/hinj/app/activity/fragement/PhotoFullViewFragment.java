package com.hinj.app.activity.fragement;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hinj.app.activity.R;
import com.hinj.imageload.FileSystemPersistence;
import com.hinj.imageload.HttpImageManager;

@SuppressLint("ValidFragment")
public class PhotoFullViewFragment  extends Fragment {
	
	private ImageView full_view_image;
	public static final String BASEDIR = "/sdcard/httpimage";
	private String full_image;
	public HttpImageManager mHttpImageManager;
	
	public PhotoFullViewFragment(){}
	
	public PhotoFullViewFragment(String full_image) {
		this.full_image = full_image;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.photo_full_view_fragment, container, false);
        
        insalization(rootView);
        
        return rootView;
    }

	private void insalization(View rootView) {
		full_view_image = (ImageView) rootView.findViewById(R.id.full_view_image);
		
		mHttpImageManager = new HttpImageManager(HttpImageManager.createDefaultMemoryCache(), new FileSystemPersistence(BASEDIR));
		Uri uri = Uri.parse(full_image);
		Bitmap bitmap = mHttpImageManager.loadImage(new HttpImageManager.LoadRequest(uri, full_view_image));
		if (bitmap != null) {

			full_view_image.setImageBitmap(bitmap);
		}
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}
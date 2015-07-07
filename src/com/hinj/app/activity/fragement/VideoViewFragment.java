package com.hinj.app.activity.fragement;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.hinj.app.activity.R;

@SuppressLint("ValidFragment")
public class VideoViewFragment extends Fragment {
	
	private String full_video;
	private ProgressDialog pDialog;
	private VideoView videoview;
 
	public VideoViewFragment(){}
	
	public VideoViewFragment(String full_video) {
		this.full_video = full_video;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.video_view, container, false);
        
        insalization(rootView);
        return rootView;
    }

	private void insalization(View rootView) {
		// Find your VideoView in your video_main.xml layout
				videoview = (VideoView) rootView.findViewById(R.id.VideoView);
				// Execute StreamVideo AsyncTask
		 
				// Create a progressbar
				pDialog = new ProgressDialog(getActivity());
				// Set progressbar title
				pDialog.setTitle("Video Streaming");
				// Set progressbar message
				pDialog.setMessage("Buffering...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				// Show progressbar
				pDialog.show();
		 
				try {
					// Start the MediaController
					MediaController mediacontroller = new MediaController(
							getActivity());
					mediacontroller.setAnchorView(videoview);
					// Get the URL from String VideoURL
					Uri video = Uri.parse(full_video);
					videoview.setMediaController(mediacontroller);
					videoview.setVideoURI(video);
		 
				} catch (Exception e) {
					Log.e("Error", e.getMessage());
					pDialog.dismiss();
					e.printStackTrace();
				}
		 
				videoview.requestFocus();
				videoview.setOnPreparedListener(new OnPreparedListener() {
					// Close the progress bar and play the video
					public void onPrepared(MediaPlayer mp) {
						pDialog.dismiss();
						videoview.start();
					}
				});
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}
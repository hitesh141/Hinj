package com.hinj.app.activity.fragement;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hinj.app.activity.R;
import com.hinj.app.utils.AppUtils;
import com.hinj.app.utils.HinjAppPreferenceUtils;
import com.hinj.parsing.HingAppParsing;

@SuppressLint("ValidFragment")
public class ContactUpdateFragment extends Fragment {
	
	private EditText username_update, mobile_update, email_update, address_update, job_title_update, last_name_update, organ_update, zipe_code_update, phone_update;
	
	private LinearLayout userName_update_lay, mobile_update_lay, email_update_lay, address_update_lay, job_title_update_lay, last_name_update_lay, organi_update_lay,
	zip_code_update_lay, phone_update_lay, update_contaact_imag; 
	
	private String address, compose_name, created, dateStr, emailAdd, firstname, id, image, jobTitleStr,
			last_name, latitude, longitude, mobile, orgStr, phone, zipCode;
	
	
	public ContactUpdateFragment(String address, String compose_name,
			String created, String dateStr, String emailAdd,
			String firstname, String id, String image, String jobTitleStr,
			String last_name, String latitude, String longitude,
			String mobile, String orgStr, String phone, String zipCode) {
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


	public ContactUpdateFragment() {
		// TODO Auto-generated constructor stub
	}

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.contact_update_fragment, container, false);
        
        insalization(rootView);
        
        setOnClickListener();
        return rootView;
    }
	
	private void insalization(View rootView) {
		username_update = (EditText) rootView.findViewById(R.id.username_update);
		mobile_update = (EditText) rootView.findViewById(R.id.mobile_update);
		email_update = (EditText) rootView.findViewById(R.id.email_update);
		address_update = (EditText) rootView.findViewById(R.id.address_update);
		job_title_update = (EditText) rootView.findViewById(R.id.job_title_update);
		last_name_update = (EditText) rootView.findViewById(R.id.last_name_update);
		organ_update = (EditText) rootView.findViewById(R.id.organ_update);
		zipe_code_update = (EditText) rootView.findViewById(R.id.zipe_code_update);
		phone_update = (EditText) rootView.findViewById(R.id.phone_update);
		
		
		userName_update_lay = (LinearLayout) rootView.findViewById(R.id.userName_update_lay);
		mobile_update_lay = (LinearLayout) rootView.findViewById(R.id.mobile_update_lay);
		email_update_lay = (LinearLayout) rootView.findViewById(R.id.email_update_lay);
		address_update_lay = (LinearLayout) rootView.findViewById(R.id.address_update_lay);
		job_title_update_lay = (LinearLayout) rootView.findViewById(R.id.job_title_update_lay);
		last_name_update_lay = (LinearLayout) rootView.findViewById(R.id.last_name_update_lay);
		organi_update_lay = (LinearLayout) rootView.findViewById(R.id.organi_update_lay);
		zip_code_update_lay = (LinearLayout) rootView.findViewById(R.id.zip_code_update_lay);
		phone_update_lay = (LinearLayout) rootView.findViewById(R.id.phone_update_lay);
		
		update_contaact_imag = (LinearLayout) rootView.findViewById(R.id.update_contaact_imag);
		
		
		if (!firstname.equalsIgnoreCase("")) {
			userName_update_lay.setVisibility(View.VISIBLE);
		}if (!address.equalsIgnoreCase("")) {
			address_update_lay.setVisibility(View.VISIBLE);
		}if (!zipCode.equalsIgnoreCase("")) {
			zip_code_update_lay.setVisibility(View.VISIBLE);
		}if (!orgStr.equalsIgnoreCase("")) {
			organi_update_lay.setVisibility(View.VISIBLE);
		}if (!last_name.equalsIgnoreCase("")) {
			last_name_update_lay.setVisibility(View.VISIBLE);
		}if (!jobTitleStr.equalsIgnoreCase("")) {
			job_title_update_lay.setVisibility(View.VISIBLE);
		}if (!emailAdd.equalsIgnoreCase("")) {
			email_update_lay.setVisibility(View.VISIBLE);
		}if (!mobile.equalsIgnoreCase("")) {
			mobile_update_lay.setVisibility(View.VISIBLE);
		}if (!phone.equalsIgnoreCase("")) {
			phone_update_lay.setVisibility(View.VISIBLE);
		}
		
		username_update.setText(firstname);
		mobile_update.setText(mobile);
		email_update.setText(emailAdd);
		address_update.setText(address);
		job_title_update.setText(jobTitleStr);
		last_name_update.setText(last_name);
		organ_update.setText(orgStr);
		zipe_code_update.setText(zipCode);
		phone_update.setText(phone);
	}
	
	private void setOnClickListener() {
		update_contaact_imag.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new UpdateContactsAsynctask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	
	public class UpdateContactsAsynctask extends AsyncTask<String, String,Object[]> {
		
		ProgressDialog progressDialog ;
		String response = "";
		int limit;
		

		@Override
		public void onPreExecute() {
			progressDialog = ProgressDialog.show(getActivity(),"Loading...", "Please Wait");
		}

		@Override
		public Object[] doInBackground(String... params) {
			
			return HingAppParsing.updateContactsDetails(getActivity(),
					username_update.getText().toString(),
					mobile_update.getText().toString(),
					email_update.getText().toString(),
					address_update.getText().toString(),
					job_title_update.getText().toString(),
					last_name_update.getText().toString(),
					organ_update.getText().toString(),
					zipe_code_update.getText().toString(),
					phone_update.getText().toString(),
					HinjAppPreferenceUtils.getChildUploadDeviceId(getActivity()),
					HinjAppPreferenceUtils.getChildRaId(getActivity()), id); 
		}  
 
		@Override
		public void onPostExecute(Object[] result) {
			progressDialog.cancel();
			try{
				boolean status = (Boolean) result[0];
				response = (String) result[1];
				
				if(status)
				{
//					Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
				}
				else
				{
					response = (String) result[1];
					AppUtils.showDialog(getActivity(), response);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

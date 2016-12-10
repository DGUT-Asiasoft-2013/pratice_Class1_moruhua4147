package com.bia.fragment.pages;

import java.io.IOException;

import com.bia.R;
import com.bia.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import api.Server;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MeFragment extends Fragment {

	View view;
	TextView infoView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_page_my_profile, null);
			infoView=(TextView) view.findViewById(R.id.me_info);
		}
		return view;
	}
	
	
	@Override
	public void onResume() {
		
		super.onResume();
		
		OkHttpClient client =Server.getsharedClient();
		Request request = Server.requestBuilderWithApi("me")
				.method("get", null)
				.build();
		
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0,final Response arg1) throws IOException {
				ObjectMapper objectMapper=new ObjectMapper();
				final User user =objectMapper.readValue(arg1.body().string(),User.class);
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						MeFragment.this.onResponse(arg0,user.getAccount());
						
					}
				});
			}
			
			@Override
			public void onFailure(final Call arg0,final IOException arg1) {
				new Runnable() {
					
					@Override
					public void run() {
						MeFragment.this.onFailure(arg0,arg1);
						
					}
				};
				
			}
		});
		
	}


	protected void onFailure(Call arg0, IOException arg1) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "´íÎó£¡", Toast.LENGTH_SHORT);
	}


	protected void onResponse(Call arg0, String account) {
		infoView.setText(account);
		
	}
}

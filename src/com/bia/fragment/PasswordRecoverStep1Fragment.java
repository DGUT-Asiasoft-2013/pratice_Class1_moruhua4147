package com.bia.fragment;

import java.io.IOException;

import com.bia.LoginActivity;
import com.bia.R;
import com.bia.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import api.Server;
import inputcells.SimpleTextInputCellFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PasswordRecoverStep1Fragment extends Fragment {

	SimpleTextInputCellFragment fragEmail;
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_password_recover_step1, null);
			fragEmail = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);

			view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					
					goNext();
				}
			});
		}
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		fragEmail.setLabelText("” œ‰");
		fragEmail.setHintText("«Î ‰»Î” œ‰");
	}

	public static interface OnGoNextListener {
		void onGoNext();
	}

	OnGoNextListener onGoNextListener;

	public void setOnGoNextListener(OnGoNextListener onGoNextListener) {
		this.onGoNextListener = onGoNextListener;
	}

	void goNext() {
		if (onGoNextListener != null) {
			onGoNextListener.onGoNext();
		}
	}
	
	public String getText(){
		return fragEmail.getText();
	}
	
}

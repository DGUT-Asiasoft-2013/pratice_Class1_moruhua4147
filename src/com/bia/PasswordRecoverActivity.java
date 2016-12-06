package com.bia;

import com.bia.fragment.PasswordRecoverStep1Fragment;
import com.bia.fragment.PasswordRecoverStep1Fragment.OnGoNextListener;
import com.bia.fragment.PasswordRecoverStep2Fragment;

import android.app.Activity;
import android.os.Bundle;

public class PasswordRecoverActivity extends Activity {

	PasswordRecoverStep1Fragment step1Fragment=new PasswordRecoverStep1Fragment();
	PasswordRecoverStep2Fragment step2Fragment=new PasswordRecoverStep2Fragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_recover);
		
		step1Fragment.setOnGoNextListener(new OnGoNextListener() {			
			@Override
			public void onGoNext() {
				goStep2();				
			}
		});
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.container, step1Fragment)
		.commit();
	}
	
	void goStep2(){
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.container, step2Fragment)
		.addToBackStack(null)
		.commit();
	}
}

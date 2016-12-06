package com.bia;

import com.bia.fragment.SimpleTextInputCellFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends Activity {
	SimpleTextInputCellFragment fraginputid;
	SimpleTextInputCellFragment fraginputpassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		fraginputid = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_id);
		fraginputpassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);

		findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goRegister();
			}
		});

		findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goLogin();

			}
		});

		findViewById(R.id.for_password).setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				goRecoverPassword();
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		fraginputid.setLabelText("’À∫≈");
		fraginputid.setHintText("«Î ‰»Î’À∫≈");
		fraginputpassword.setLabelText("√‹¬Î");
		fraginputpassword.setHintText("«Î ‰»Î√‹¬Î");
		fraginputpassword.setIsPassword(true);

	}

	void goRegister() {
		Intent itnt = new Intent(this, RegisterActivity.class);
		startActivity(itnt);
	}

	void goLogin() {
		Intent intent = new Intent(this, HelloWorldActivity.class);
		startActivity(intent);
	}
	void goRecoverPassword(){
		Intent itnt = new Intent(this, PasswordRecoverActivity.class);
		startActivity(itnt);
	}
}

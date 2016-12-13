package com.bia;

import java.io.IOException;

import org.omg.CORBA.SetOverrideType;

import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import api.Server;
import entity.User;
import inputcells.SimpleTextInputCellFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
		//		Intent intent = new Intent(this, HelloWorldActivity.class);
		//		startActivity(intent);
		String id=fraginputid.getText();
		String password=fraginputpassword.getText();

		MultipartBody.Builder requestBodyBulider=new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("account", id)
				.addFormDataPart("passwordHash", password);


		//OkHttpClient client = new OkHttpClient();
		OkHttpClient client =Server.getsharedClient();
				
//		Request request=new Request.Builder()
//				.url("http://172.27.0.21:8080/membercenter/api/login")
//				.method("post", null)
//				.post(requestBodyBulider.build())
//				.build();
		
		Request request = Server.requestBuilderWithApi("login")
				.method("post", null)
				.post(requestBodyBulider.build())
				.build();

		final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
		progressDialog.setMessage("Loding");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();



		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0,final Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.dismiss();		
					}
				});
				
				try{
					ObjectMapper oMapper = new ObjectMapper();
					final User user = oMapper.readValue(arg1.body().string(), User.class);
					runOnUiThread(new Runnable() {					
						@Override
						public void run() {
							LoginActivity.this.onResponse(arg0, user.getAccount());
						}
					});
				}catch (final Exception e) {
					runOnUiThread(new Runnable() {					
						@Override
						public void run() {
							LoginActivity.this.onFailure(arg0, e);
						}
					});	
				}
			}

			@Override
			public void onFailure(final Call arg0,final IOException arg1) {
				progressDialog.dismiss();
				runOnUiThread(new Runnable() {					
					@Override
					public void run() {

						LoginActivity.this.onFailure(arg0, arg1);
					}
				});				
			}
		});

	}
	void goRecoverPassword(){
		Intent itnt = new Intent(this, PasswordRecoverActivity.class);
		startActivity(itnt);
	}

	void onResponse(Call arg0,String string){
		new AlertDialog.Builder(LoginActivity.this)
		.setMessage("’Àªß£∫"+string+"ª∂”≠ π”√")
		.setPositiveButton("Rua!",new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, HelloWorldActivity.class);
				startActivity(intent);
			}
		})
		.show();
	}


	void onFailure(Call arg0,Exception arg1){
		new AlertDialog.Builder(LoginActivity.this)
		.setTitle(" ß∞‹RU∞°")
		.setMessage(arg1.getLocalizedMessage())
		.setPositiveButton("Rua!",null)
		.show();
	}
}

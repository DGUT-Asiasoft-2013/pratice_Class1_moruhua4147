package com.bia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import inputcells.PictureInputCellFragment;
import inputcells.SimpleTextInputCellFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends Activity {

	SimpleTextInputCellFragment fragInputCellAccount;
	SimpleTextInputCellFragment fragInputEmailAddress;
	SimpleTextInputCellFragment fragInputName;
	SimpleTextInputCellFragment fragInputCellPassword;
	SimpleTextInputCellFragment fragInputCellPasswordRepeat;
	PictureInputCellFragment pictureInputCellpicselect;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		fragInputCellAccount=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);
		fragInputName=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_name);
		fragInputEmailAddress=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);
		fragInputCellPassword=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
		fragInputCellPasswordRepeat=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);
		pictureInputCellpicselect=(PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.pic_select);
		
		findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				submit();
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		fragInputCellAccount.setLabelText("账户名");
		fragInputCellAccount.setHintText("输入账户名");
		fragInputCellPassword.setLabelText("密码");
		fragInputCellPassword.setHintText("请输入密码");
		fragInputCellPassword.setIsPassword(true);
		fragInputCellPasswordRepeat.setLabelText("重复密码");
		fragInputCellPasswordRepeat.setHintText("再次输入密码");
		fragInputCellPasswordRepeat.setIsPassword(true);
		fragInputEmailAddress.setLabelText("邮箱");
		fragInputEmailAddress.setHintText("请输入邮箱");
		fragInputName.setLabelText("名字");
		fragInputName.setHintText("RU啊！");
	}
	
	
	void submit(){
		//判断两次密码是否一致
		String password = fragInputCellPassword.getText();
		String passwordRepeat=fragInputCellPasswordRepeat.getText();
		if(!password.equals(passwordRepeat)){
			new AlertDialog.Builder(RegisterActivity.this).setMessage("两次密码输入不一致").setPositiveButton("确认", null).show();
			return;
		}
		String account=fragInputCellAccount.getText();
		String name=fragInputName.getText();
		String email=fragInputEmailAddress.getText();
		String passWord=fragInputCellPassword.getText();
		passWord=MD5.getMD5(passWord);
		MultipartBody.Builder requestBodyBulider=new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("account", account)
				.addFormDataPart("name", name)
				.addFormDataPart("email", email)
				.addFormDataPart("passwordHash", passWord);

		
		if (pictureInputCellpicselect.getPngData()!=null){
			RequestBody pngDataBody=RequestBody.create(MediaType.parse("image/png"), pictureInputCellpicselect.getPngData());
			requestBodyBulider.addFormDataPart("avatar","avatar.png" , pngDataBody);
			
		}
		
		
		OkHttpClient client=new OkHttpClient();
		okhttp3.Request request=new okhttp3.Request.Builder()
				.url("http://172.27.0.21:8080/membercenter/api/register")
				.method("post", null)
				.post(requestBodyBulider.build())
				.build();
		
		final ProgressDialog progressDialog=new ProgressDialog(RegisterActivity.this);
		progressDialog.setMessage("Loding");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0,final Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						progressDialog.dismiss();
						try {
							RegisterActivity.this.onResponse(arg0,arg1.body().string());
						} catch (Exception e) {
							e.printStackTrace();
							
						}
					}
				});
				
			}
			
			@Override
			public void onFailure(final Call arg0,final IOException arg1) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						progressDialog.dismiss();
						RegisterActivity.this.onFailure(arg0,arg1);
						
					}
				});
				
			}
		});
	}

	void onFailure(Call arg0, IOException arg1) {
		new AlertDialog.Builder(this)
		.setTitle("请求失败")
		.setMessage(arg1.getLocalizedMessage())
		.setPositiveButton("确认", null)
		.show();
	}

	void onResponse(Call arg0, String string) {
		new AlertDialog.Builder(this)
		.setMessage("请求成功")
		.setPositiveButton("确认", null)
		.show();
	}
	
	
	
	
}

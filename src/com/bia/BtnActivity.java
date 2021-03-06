package com.bia;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import api.Server;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class BtnActivity extends Activity {

	Button btn;
	EditText edit;
	EditText editTitle;
	
	//中间按钮发表文章
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_btn);
		btn=(Button) findViewById(R.id.button1);
		edit=(EditText) findViewById(R.id.edit1);
		editTitle=(EditText) findViewById(R.id.edit_title);
		
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendContent();
				finish();
				overridePendingTransition(R.anim.slide_out_top, 0);
			}
		});
	}
	
	

	
	
	
	 void sendContent() {
		String text=edit.getText().toString();
		String title=editTitle.getText().toString();
//		if(text==null||title==null){
//			Toast.makeText(BtnActivity.this, "输入不能为空哦",
//				     Toast.LENGTH_SHORT).show();
//					 return;
//		}
		
		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("title", title)
				.addFormDataPart("text", text)
				.build();
		
		Request request = Server.requestBuilderWithApi("article")
				.post(body)
				.build();
		
		Server.getsharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				final String responseBody = arg1.body().string();
				
				runOnUiThread(new Runnable() {					
					@Override
					public void run() {
						BtnActivity.this.onSucceed(responseBody);						
					}
				});
				
			}
			
			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						BtnActivity.this.onFailed(arg0,arg1);
					}
				});
				
			}
		});
		
	}
	 void onSucceed(String responseBody) {
		 new AlertDialog.Builder(BtnActivity.this)
			.setTitle("上传成功RUA!")
			.setMessage(responseBody)
			.setPositiveButton("Rua!",null)
			.show();
		
	}
	 void onFailed(Call arg0,Exception arg1){
		 new AlertDialog.Builder(BtnActivity.this)
			.setTitle("上传失败")
			.setMessage(arg1.getLocalizedMessage())
			.setPositiveButton("Rua!",null)
			.show();
	 }
}

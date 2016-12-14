package com.bia;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager.Request;
import android.content.DialogInterface;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import api.Server;
import entity.Article;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Response;

//
//
//             点击评论后弹出的输入页面
//
//
public class BtnCommentActivity extends Activity{

	Article article;
	Button sButton ;//button1
	EditText editText; // edit1
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_btn_comment);
		sButton=(Button) findViewById(R.id.button1);
		editText=(EditText) findViewById(R.id.edit1);
		article = (Article) getIntent().getSerializableExtra("article");

		
		sButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendComment();				
			}
		});
	}

	void sendComment() {
		String commentText = editText.getText().toString();
		
		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("text", commentText)
				.build();
		
		okhttp3.Request request = Server
				.requestBuilderWithApi("article/"+article.getId()+"/comments")
				.post(body)
				.build();
		
		Server.getsharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				final String responseBody=arg1.body().string();
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						BtnCommentActivity.this.onSucceed(responseBody);
						
					}
				});
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				BtnCommentActivity.this.onFailure(arg1);
				
			}
		});
	}

	  void onFailure(Exception e) {
		new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
		
	}

	void onSucceed(String text) {
		new AlertDialog.Builder(this).setMessage(text)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
				overridePendingTransition(R.anim.none, R.anim.slide_out_top);
			}			
		}).show();
		
	}
}

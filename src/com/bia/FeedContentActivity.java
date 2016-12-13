package com.bia;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import api.Server;

public class FeedContentActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_feed_content);
		
		String list_contenttext = getIntent().getStringExtra("list_contenttext");
		String list_contenttitle = getIntent().getStringExtra("list_contenttitle");
		String list_authorname  = getIntent().getStringExtra("list_authorname");
		String list_creatDate = getIntent().getStringExtra("list_creatDate");
		String list_authoravatar = getIntent().getStringExtra("list_authoravatar");
		
		TextView contentText = (TextView) findViewById(R.id.text_authorwrite);
		TextView contentTitle=(TextView) findViewById(R.id.text1);
		TextView authorname = (TextView) findViewById(R.id.text_authorname);
		TextView creatDate = (TextView) findViewById(R.id.text_creatDate);
		AvatarView avatarView = (AvatarView) findViewById(R.id.feed_avatar);
		
		
		contentText.setText(list_contenttext);
		contentTitle.setText(list_contenttitle);
		authorname.setText(list_authorname);
		creatDate.setText(list_creatDate);
		avatarView.load(Server.serverAddress+list_authoravatar);
		
		
	}
}

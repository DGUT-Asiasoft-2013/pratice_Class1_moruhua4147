package com.bia;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class FeedContentActivity extends Activity {

	TextView text1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_feed_content);
		
		text1=(TextView) findViewById(R.id.text1);
		String text=getIntent().getStringExtra("Text");
		text1.setText("ABCDEFG"+text);
		text1.setTextColor(Color.BLACK);
	}
}

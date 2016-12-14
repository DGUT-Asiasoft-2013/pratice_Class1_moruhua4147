package com.bia;


import java.io.IOException;
import java.util.List;
import java.util.zip.Inflater;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import api.Server;
import entity.Comment;
import entity.Page;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import entity.Article;

public class FeedContentActivity extends Activity {

	Article article;
	ListView commentListView;
	View btnLoadMore;
	List<Comment> data;
	int page = 0;
	//
	//   文章详细内容页面
	//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_feed_content);
		
		
		LayoutInflater.from(this).inflate(R.layout.widget_load_more_button,null);
		commentListView.addFooterView(btnLoadMore);
		
		commentListView=(ListView) findViewById(R.id.list_comment);
		commentListView.setAdapter(commentListAdapter);
		
		
		String list_contenttext = getIntent().getStringExtra("list_contenttext");
		String list_contenttitle = getIntent().getStringExtra("list_contenttitle");
		String list_authorname  = getIntent().getStringExtra("list_authorname");
		String list_creatDate = getIntent().getStringExtra("list_creatDate");
		String list_authoravatar = getIntent().getStringExtra("list_authoravatar");
		article = (Article) getIntent().getSerializableExtra("article");
		
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
		
		
		findViewById(R.id.btn_comment).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(FeedContentActivity.this,BtnCommentActivity.class);
				intent.putExtra("article",article);
				startActivity(intent);	
				FeedContentActivity.this.overridePendingTransition(R.anim.slide_in_bottom, R.anim.none);
			}
		});
		
		
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadComment();
	}


	//连接服务器获取评论
	 void loadComment() {
		// TODO Auto-generated method stub
		Request request = Server.
				 requestBuilderWithApi("article/"+article.getId()+"/comments")
				 .get().build();
		
		Server.getsharedClient().newCall(request).enqueue(new Callback() {			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final Page<Comment> data= new ObjectMapper().readValue(arg1.body().string(), 
							new TypeReference<Page<Comment>>() {});
					FeedContentActivity.this.runOnUiThread(new Runnable() {
					@Override
						public void run() {
							FeedContentActivity.this.page=data.getNumber();
							FeedContentActivity.this.data=data.getContent();
							commentListAdapter.notifyDataSetInvalidated();
						}
					});
				} catch (final Exception e) {
					FeedContentActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							new AlertDialog.Builder(FeedContentActivity.this).setMessage(e.getMessage()).show();
							
						}
					});
				}		
			}
			
			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				FeedContentActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						new AlertDialog.Builder(FeedContentActivity.this).setMessage(arg1.getMessage()).show();
						
					}
				});
				
			}
		});
	}
	 
	 
	 
	 
	 //定义设配器
	 BaseAdapter commentListAdapter = new BaseAdapter() {
		
		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if(convertView==null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.fragment_showlistview, null);				
			}else{
				view=convertView;
			}
			TextView textView = (TextView) view.findViewById(R.id.text_rua);
			AvatarView imageView=(AvatarView) view.findViewById(R.id.image_bear);
			Comment comment = data.get(position);
			//配置内容
			textView.setText(comment.getText());
			imageView.load(Server.serverAddress+article.getAuthorAvatar());
			
			
			
			return view;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data == null ? 0 : data.size();
		}
	};
	 
}

package com.bia.fragment.pages;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.bia.AvatarView;
import com.bia.FeedContentActivity;
import com.bia.LoginActivity;
import com.bia.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import api.Server;
import entity.Article;
import entity.Page;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FeedListFragment extends Fragment {

	View view;
	ListView listView;
	
	View btnLoadMore;
	TextView textLoadMore;
	List<Article> data;
	int page = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_page_feed_list, null);
			btnLoadMore=inflater.inflate(R.layout.widget_load_more_button, null);
			textLoadMore=(TextView) btnLoadMore.findViewById(R.id.text);
			
			listView = (ListView) view.findViewById(R.id.list);
			listView.addFooterView(btnLoadMore);
			listView.setAdapter(listAdapter);

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					onItemClicked(position);
				}

			});
			
			btnLoadMore.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					loadmore();
				}
			});
		}
		return view;
	}
	

	void onItemClicked(int position) {
		String list_contenttext = data.get(position).getText();
		String list_contenttitle =data.get(position).getTitle();
		String list_authorname  = data.get(position).getAuthorName();
		String list_creatDate   = DateFormat.format("yyyy-MM-dd hh:mm",data.get(position).getCreateDate()).toString();
		String list_authoravatar= data.get(position).getAuthorAvatar();
		
		Intent intent=new Intent(getActivity(), FeedContentActivity.class);		
		//intent.putExtra("Text", text);     // 标题  内容
		intent.putExtra("list_contenttext", list_contenttext);
		intent.putExtra("list_contenttitle",list_contenttitle );
		intent.putExtra("list_authorname",list_authorname );
		intent.putExtra("list_creatDate",list_creatDate );
		intent.putExtra("list_authoravatar",list_authoravatar);
		startActivity(intent);
	}
	
	//在onresume实现数据连接
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Request request = Server.requestBuilderWithApi("feeds")
				//.method("get", null)
				.get()
				.build();
		
		Server.getsharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse( Call arg0, Response arg1) throws IOException {
				try {
					final Page<Article> data=new ObjectMapper().readValue(arg1.body().string(), 
																	new TypeReference<Page<Article>>() {});
					
					getActivity().runOnUiThread(new Runnable() {						
						public void run() {
							FeedListFragment.this.page=data.getNumber();
							FeedListFragment.this.data=data.getContent();
							listAdapter.notifyDataSetInvalidated();
						}
					});
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							new AlertDialog.Builder(getActivity()).setMessage(e.getMessage()).show();
						}
					});
				}
				
			}
			
			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						new AlertDialog.Builder(getActivity()).setMessage(arg1.getMessage()).show();
						
					}
				});
				
			}
		});
	}
	 



	 	//定义设配器

	BaseAdapter listAdapter = new BaseAdapter() {

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.fragment_showlistview, null);

			} else {
				view = convertView;
			}
			TextView textView = (TextView) view.findViewById(R.id.text_rua);
			AvatarView imageView=(AvatarView) view.findViewById(R.id.image_bear);
			Article article=data.get(position);
			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm",article.getCreateDate()).toString();
			textView.setText("作者："+article.getAuthorName()+"--"+article.getText()+" "+dateStr);
			textView.setTextColor(Color.BLACK);			
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
	
	
	
	void loadmore(){
		btnLoadMore.setEnabled(false);
		textLoadMore.setText("载入中");
		
		Request request=Server.requestBuilderWithApi("feeds/"+(page+1)).get().build();
		Server.getsharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
						
					}
				});
				
				try {
					Page<Article> feeds = new ObjectMapper().readValue(arg1.body().string(), 
															new TypeReference<Page<Article>>() {});
					if (feeds.getNumber()>page) {
						if (data==null) {
							data=feeds.getContent();
						} else {
							data.addAll(feeds.getContent());
						}
						page = feeds.getNumber();
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								listAdapter.notifyDataSetChanged();
							}
						});
					}
				} catch (final Exception e) {	
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							new AlertDialog.Builder(getActivity())
							.setTitle("失败啊")
							.setMessage(e.getLocalizedMessage())
							.setPositiveButton("ok",null)
							.show();
						}
					});
					
				}
				
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
					}
				});
				
			}
		});
	}
}

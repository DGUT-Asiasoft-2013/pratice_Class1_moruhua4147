package com.bia.fragment.pages;



import java.io.IOException;
import java.util.List;

import com.bia.AvatarView;
import com.bia.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class NotesListFragment extends Fragment {

	View view;
	TextView noteCommentme;
	TextView noteMyComment;
	ListView listViewforNote;
	List<Comment> dataforNote;
	String actionUrl=("article/author_id/receivedcomment");
	int Page = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_page_note_list, null);			
			listViewforNote=(ListView) view.findViewById(R.id.note_list);
			listViewforNote.setAdapter(noteListAdapter);
			
			noteCommentme=(TextView) view.findViewById(R.id.note_commentme);
			noteMyComment=(TextView) view.findViewById(R.id.note_mycomment);
			
			noteCommentme.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					commentMe();				
				}
			});
			noteMyComment.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					myComment();
				}
			});
			
		}
		return view;
	}
	
	protected void myComment() {
		noteCommentme.setTextColor(Color.BLACK);
		noteMyComment.setTextColor(Color.BLUE);
		actionUrl=("article/author_id/mycomments");
		loadnoot();
		
	}

	protected void commentMe() {
		noteCommentme.setTextColor(Color.BLUE);
		noteMyComment.setTextColor(Color.BLACK);
		actionUrl=("article/author_id/receivedcomment");
		loadnoot();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		noteCommentme.setBackgroundColor(color.holo_blue_light);
//		noteMyComment.setBackgroundColor(color.holo_green_dark);
//		loadnoot();
	}
	
	
	BaseAdapter noteListAdapter = new BaseAdapter() {
		
		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView==null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.example_received_comment, null);
			} else {
				view=convertView;
			}
			TextView text = (TextView) view.findViewById(R.id.example_text);
			TextView laber=(TextView) view.findViewById(R.id.example_laber);
			TextView name = (TextView) view.findViewById(R.id.example_name);
			AvatarView img = (AvatarView) view.findViewById(R.id.example_img);
			Comment comment = dataforNote.get(position);
			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm",comment.getCreateDate()).toString();
			text.setText(comment.getArticle().getTitle()+"---"+comment.getText());
			text.setTextColor(Color.BLACK);
			laber.setText(dateStr);
			laber.setTextColor(Color.BLACK);
			name.setText(comment.getAuthor().getName());
			name.setTextColor(Color.BLACK);
			img.load(Server.serverAddress+comment.getAuthor().getAvatar());
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
			return dataforNote.get(position);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataforNote == null ? 0 : dataforNote.size();
		}
	};
	
	
	
	

	private void loadnoot() {
		Request request = Server.requestBuilderWithApi(actionUrl)
					.get().build();
		Server.getsharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final Page<Comment> data = new ObjectMapper().readValue(arg1.body().string()
												,new TypeReference<Page<Comment>>() {
												});
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							NotesListFragment.this.Page=data.getNumber();
							NotesListFragment.this.dataforNote=data.getContent();
							noteListAdapter.notifyDataSetInvalidated();
						}
					});
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
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
	
	
	
}

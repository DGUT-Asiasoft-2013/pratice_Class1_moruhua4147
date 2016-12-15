package com.bia.fragment.pages;

import java.io.IOException;
import java.util.List;

import javax.imageio.plugins.bmp.BMPImageWriteParam;

import com.bia.AvatarView;
import com.bia.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import api.Server;
import entity.Article;
import entity.Page;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment {

	View view;
	ListView listViewSearch;
	EditText textSearch;
	Button btnSearch;
	List<Article> data;
	int page = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_page_search_list, null);

			textSearch=(EditText) view.findViewById(R.id.text_search);
			btnSearch=(Button) view.findViewById(R.id.btn_search);
			listViewSearch=(ListView) view.findViewById(R.id.list_search);
			listViewSearch.setAdapter(searchAdapter);
			//String inputSearch = textSearch.getText();

			btnSearch.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {


					goSearch();
				}
			});
		}

		return view;
	}

	void goSearch() {


		InputMethodManager inputMethodManager = 
				(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(textSearch.getWindowToken(), 0);

		String searchText = textSearch.getText().toString();

		Log.d("search for text", searchText);

		Request request = Server.requestBuilderWithApi("article/s/"+searchText)
				.get().build();
		Server.getsharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final Page<Article> data = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Article>>() {
					});
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							SearchFragment.this.page=data.getNumber();
							SearchFragment.this.data=data.getContent();
							searchAdapter.notifyDataSetInvalidated();
						}
					});
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
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
						// TODO Auto-generated method stub
						new AlertDialog.Builder(getActivity()).setMessage(arg1.getMessage()).show();
					}
				});

			}
		});

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		//showSearchList();
	}



	BaseAdapter searchAdapter = new BaseAdapter() {
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
			textView.setText(article.getTitle());
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


}

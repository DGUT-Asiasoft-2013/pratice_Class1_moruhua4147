package com.bia.fragment.pages;

import java.util.Random;

import com.bia.FeedContentActivity;
import com.bia.R;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FeedListFragment extends Fragment {

	View view;
	ListView listView;
	String[] data;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_page_feed_list, null);
			listView = (ListView) view.findViewById(R.id.list);
			listView.setAdapter(listAdapter);

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					onItemClicked(position);
				}

			});

			Random random = new Random();
			data = new String[10 + random.nextInt(30) % 20];
			for (int i = 0; i < data.length; i++) {
				data[i] = "YOUYOUYOU" + random.nextInt();
			}

		}
		return view;
	}

	void onItemClicked(int position) {
		// TODO Auto-generated method stub
		String text=data[position];
		Intent intent=new Intent(getActivity(), FeedContentActivity.class);		
		intent.putExtra("Text", text);
		startActivity(intent);
	}

	BaseAdapter listAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(android.R.layout.simple_list_item_1, null);

			} else {
				view = convertView;
			}
			TextView textView = (TextView) view.findViewById(android.R.id.text1);
			textView.setText("A" + data[position]);
			textView.setTextColor(Color.BLACK);
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
			return data[position];
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data == null ? 0 : data.length;
		}
	};
}

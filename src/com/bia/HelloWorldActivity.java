package com.bia;

import com.bia.fragment.MainTabbarFragment;
import com.bia.fragment.MainTabbarFragment.OnTabSelectedListener;
import com.bia.fragment.pages.FeedListFragment;
import com.bia.fragment.pages.MeFragment;
import com.bia.fragment.pages.NotesListFragment;
import com.bia.fragment.pages.SearchFragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class HelloWorldActivity extends Activity {

	FeedListFragment contentFeedList = new FeedListFragment();
	NotesListFragment contentNotelist = new NotesListFragment();
	MeFragment contentMyProfile = new MeFragment();
	SearchFragment contentSearchpage = new SearchFragment();
	MainTabbarFragment tabbarFragment;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_helloworld);

		tabbarFragment = (MainTabbarFragment) getFragmentManager().findFragmentById(R.id.frag_tabbar);
		tabbarFragment.setOnTabSelectedListener(new OnTabSelectedListener() {

			@Override
			public void onTabSelected(int index) {
				// TODO Auto-generated method stub
				changeContentFragment(index);
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		tabbarFragment.setSelectedItem(0);
	}

	void changeContentFragment(int index) {
		Fragment newFrag = null;

		switch (index) {
		case 0:
			newFrag = contentFeedList;
			break;
		case 1:
			newFrag = contentNotelist;
			break;
		case 2:
			newFrag = contentSearchpage;
			break;
		case 3:
			newFrag = contentMyProfile;
			break;
		default:
			break;
		}

		if (newFrag == null)
			return;
		getFragmentManager().beginTransaction().replace(R.id.content, newFrag).commit();
	}
}

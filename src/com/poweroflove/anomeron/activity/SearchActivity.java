package com.poweroflove.anomeron.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.poweroflove.anomeron.R;
import com.poweroflove.anomeron.adapter.SearchArrayAdapter;
import com.poweroflove.anomeron.model.Venue;
import com.poweroflove.anomeron.service.AnoMeronService;
import com.poweroflove.anomeron.util.HttpUtil;

public class SearchActivity extends SherlockListActivity {
	private EditText mSearch;
	
	private double nLong = 121.018379331;
	private double nLat = 14.5590258956;
	
	private ActionBar mActionBar;

	private boolean mIsBound;
	private AnoMeronService mBoundService;

	private List<Venue> mVenue;
	private SearchArrayAdapter mAdapter;
	
	private ListView mLv;

	private BroadcastReceiver mBr = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			String action = arg1.getAction();

			if (action.equals("search venues")) {
				System.out.println("refresh");
				
				mVenue.clear();
				mVenue.addAll(Venue.getEntries());
				System.out.println(mVenue);
				mAdapter.notifyDataSetChanged();
				findViewById(R.id.empty).setVisibility(View.GONE);
				mLv.setEmptyView(null);
			}
			else if (action.equals("reset venues")) {
				System.out.println("reset");
				findViewById(R.id.empty).setVisibility(View.VISIBLE);
				mLv.setEmptyView(findViewById(R.id.empty));
				mVenue.clear();
				mVenue.addAll(Venue.getEntries());
				System.out.println(mVenue);
				mAdapter.notifyDataSetChanged();
			}
		}

	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		mSearch = (EditText) findViewById(R.id.search);
		
		mVenue = new ArrayList<Venue>();
		mLv = getListView();
		
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Venue venue = mVenue.get(arg2);
				HttpUtil.getFeeds(getApplicationContext(), venue.longitude, venue.lattitude);
				finish();
			}
			
		});
		
		mAdapter = new SearchArrayAdapter(getApplicationContext(), 0, mVenue);
		mLv.setAdapter(mAdapter);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("search venues");
		filter.addAction("reset venues");
		registerReceiver(mBr, filter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		unregisterReceiver(mBr);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add("Search").setIcon(R.drawable.ic_action_search)
				.setTitle("Search")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String title = (String) item.getTitle();
		if(title.equals("Search")) {
			String term = mSearch.getText().toString();
			
			HttpUtil.getNearby(getApplicationContext(), term, nLong + "", nLat + "");
		}
		
		return true;
	}
}

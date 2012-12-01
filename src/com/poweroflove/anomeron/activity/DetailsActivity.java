package com.poweroflove.anomeron.activity;

import httpimage.HttpImageManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.poweroflove.anomeron.R;
import com.poweroflove.anomeron.model.Entry;
import com.poweroflove.anomeron.service.AnoMeronService;

public class DetailsActivity extends SherlockActivity {
	private ActionBar mActionBar;

	private boolean mIsBound;
	private AnoMeronService mBoundService;
	
	Entry mEntry;
	
	private TextView mUser, mLocation, mBody, mTimestamp;
	private ImageView mUserThumb, mUploadedImage;
	
	private HttpImageManager mHttpImageManager;


	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mBoundService = ((AnoMeronService.LocalBinder) service)
					.getService();
		}

		public void onServiceDisconnected(ComponentName className) {
			mBoundService = null;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		
		Intent i = getIntent();
		mEntry = Entry.getEntryFromId(i.getLongExtra("id", -1));
		
		mUser = (TextView) findViewById(R.id.username);
		mLocation = (TextView) findViewById(R.id.location);
		mBody = (TextView) findViewById(R.id.message);
		//mTimestamp = (TextView) findViewById(R.id.timestamp);
		mUserThumb = (ImageView) findViewById(R.id.user_thumb);
		mUploadedImage = (ImageView) findViewById(R.id.uploaded_image);
		
		mHttpImageManager = HttpImageManager.getInstance();
		
		if(mEntry != null) {
			mUser.setText(mEntry.user);
			mLocation.setText(mEntry.location);
			mBody.setText(mEntry.body);
			
			String thumbUri = mEntry.userThumbUri;
		    
		    if(thumbUri != null) {
		    	Bitmap bm = mHttpImageManager.loadImage(new HttpImageManager.LoadRequest(Uri.parse(thumbUri), mUserThumb));
		    	if(bm != null) {
		    		mUserThumb.setImageBitmap(bm);
		    	}
		    }
		    
		    String uploadedImageUri = mEntry.imageURI;
		    
		    if(uploadedImageUri != null) {
		    	Bitmap bm = mHttpImageManager.loadImage(new HttpImageManager.LoadRequest(Uri.parse(uploadedImageUri), mUploadedImage));
		    	if(bm != null) {
		    		mUploadedImage.setImageBitmap(bm);
		    	}
		    }
		}
		
		doBindService();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		doUnbindService();
	}
	
	void doBindService() {
		// Establish a connection with the service. We use an explicit
		// class name because we want a specific service implementation that
		// we know will be running in our own process (and thus won't be
		// supporting component replacement by other applications).
		startService(new Intent(DetailsActivity.this, AnoMeronService.class));
		bindService(new Intent(DetailsActivity.this, AnoMeronService.class),
				mConnection, Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	void doUnbindService() {
		if (mIsBound) {
			// Detach our existing connection.
			unbindService(mConnection);
			mIsBound = false;
		}
	}
}

package com.poweroflove.anomeron.service;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;

import com.poweroflove.anomeron.util.HttpUtil;

public class AnoMeronService extends Service {
	
	private double nLong = 121.018379331;
	private double nLat = 14.5590258956;

	private LocationManager mLocMgr = null;

	public class LocalBinder extends Binder {
		public AnoMeronService getService() {
			return AnoMeronService.this;
		}
	}

	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("Service started");
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		System.out.println("Service created");
		mLocMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		//test
		/*Entry entry = new Entry();
		entry.imageURI = "http://sphotos-c.ak.fbcdn.net/hphotos-ak-ash3/556959_289677717819644_1332078842_n.jpg";
		entry.body = "hello world";
		entry.user = "M A Ignacio";
		entry.location = "Happy Town";
		entry.userThumbUri = "https://twimg0-a.akamaihd.net/profile_images/2739187536/b8eff7c27419d4bd8a09286b561285d1_normal.jpeg";
		entry.save();*/
		
		Location loc = getLocation();
		HttpUtil.getFeeds(getApplicationContext(), nLong + "", nLat + "");
	}

	public Location getLocation() {
		List<String> providers = mLocMgr.getAllProviders();
		
		Location l = null;
		
		for (int i = providers.size() - 1; i >= 0; i--) {
			l = mLocMgr.getLastKnownLocation(providers.get(i));
			if(l != null) {
				break;
			}
		}
		
		System.out.println(l.getLongitude());
		System.out.println(l.getLatitude());
		
		return l;
	}
}

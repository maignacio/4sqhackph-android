package com.poweroflove.anomeron.util;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.activeandroid.ActiveAndroid;
import com.poweroflove.anomeron.model.Entry;
import com.poweroflove.anomeron.model.Venue;

public class HttpUtil {
	private final static String FSQ_APP_ID = "DJYW5M54EASFQKEJ0NJXZUWIXOQXOXCEYZNRZ2Y4C352HBEU";
	private final static String FSQ_APP_KEY = "WMTH2TZ4IMDDTLOYJI22KLUCBPIPWHJFGN02JXMH54B1GAOW";
	
	private final static String API_URL = "http://192.168.1.107/sandbox_hackathon/";
	private static final String API_GET_FEEDS = API_URL + "posts.php";
	private static final String API_GET_NEARBY = API_URL + "nearby.php";
	
	private final static String FQS_VENUE_SEARCH_URI = "https://api.foursquare.com/v2/venues/search?intent=browse&radius=1000&client_id=" + FSQ_APP_ID + "&client_secret=" + FSQ_APP_KEY + "&ll=";
	
	private static ExecutorService pool = Executors.newFixedThreadPool(1);
	
	public static void getNearby(final Context context, final String term, final String longitude, final String lattitude) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String jsonString = (String) msg.obj;
				System.out.println(jsonString);
				
				try {
					JSONArray data = new JSONArray(jsonString);
					int count = data.length();
					Venue.deleteAllEntries();
					ActiveAndroid.beginTransaction();
					JSONObject entry = null;
					Venue item;
					
					for(int i = 0; i < count; i++) {
						entry = data.getJSONObject(i);
						item = new Venue();
						item.fsq_id = entry.getString("id");
						item.name = entry.getString("name");
						item.lattitude = entry.getString("lat");
						item.longitude = entry.getString("long");
						item.save();
					}
					ActiveAndroid.setTransactionSuccessful();
					ActiveAndroid.endTransaction();
					
					Intent i = new Intent();
					i.setAction("search venues");
					context.sendBroadcast(i);
System.out.println("end no errors");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("errors");
				}
			}
		};
		
		pool.submit(new Runnable() {

			@Override
			public void run() {
				Intent i = new Intent();
				i.setAction("reset venues");
				context.sendBroadcast(i);
				HttpResponse resp = null;
				try {
					System.out.println(API_GET_NEARBY + "?long=" + longitude + "&lat=" + lattitude + "&name=" + term);
					resp = doGet(API_GET_NEARBY + "?long=" + longitude + "&lat=" + lattitude + "&name=" + term);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String body = null;
				
				try {
					body = EntityUtils.toString(resp.getEntity());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Message msg = Message.obtain();
				msg.obj = body;
				handler.sendMessage(msg);
			}
			
		});
	}
	
	public static void getFeeds(final Context context, final String longitude, final String lattitude) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String jsonString = (String) msg.obj;
				System.out.println(jsonString);
				try {
					JSONArray data = new JSONArray(jsonString);
					int count = data.length();
					Entry.deleteAllEntries();
					ActiveAndroid.beginTransaction();
					JSONObject entry = null;
					Entry item;
					
					for(int i = 0; i < count; i++) {
						entry = data.getJSONObject(i);
						item = new Entry();
						item.body = entry.getString("body");
						item.startTime = new Date(Long.parseLong(entry.getString("start_time")));
						item.endTime = new Date(Long.parseLong(entry.getString("end_time")));
						item.location = entry.getString("name");
						item.lattitude = Double.parseDouble(entry.getString("lat"));
						item.longitude = Double.parseDouble(entry.getString("long"));
						item.user = entry.getString("user");
						item.userThumbUri = entry.getString("user_pic");
						item.imageURI = entry.getString("pic_url");
						item.save();
					}
					ActiveAndroid.setTransactionSuccessful();
					ActiveAndroid.endTransaction();
					
					Intent i = new Intent();
					i.setAction("refresh feed");
					context.sendBroadcast(i);
System.out.println("end no errors");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("errors");
				}
			}
		};
		
		pool.submit(new Runnable() {

			@Override
			public void run() {
				Intent i = new Intent();
				i.setAction("reset feed");
				context.sendBroadcast(i);
				HttpResponse resp = null;
				try {
					System.out.println(API_GET_FEEDS + "?long=" + longitude + "&lat=" + lattitude);
					resp = doGet(API_GET_FEEDS + "?long=" + longitude + "&lat=" + lattitude);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String body = null;
				
				try {
					body = EntityUtils.toString(resp.getEntity());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Message msg = Message.obtain();
				msg.obj = body;
				handler.sendMessage(msg);
			}
			
		});
	}
	
	private static HttpResponse doGet(String url) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = null;
		httpResponse = httpClient.execute(httpGet);
		
		return httpResponse;
	}
}

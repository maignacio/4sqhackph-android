package com.poweroflove.anomeron.adapter;

import httpimage.HttpImageManager;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.poweroflove.anomeron.R;
import com.poweroflove.anomeron.model.Entry;

public class EntryArrayAdapter extends ArrayAdapter<Entry> {
	private Context mContext;
	private List<Entry> mItems;
	private LayoutInflater inflater;
	private HttpImageManager mHttpImageManager;

	public EntryArrayAdapter(Context context, int textViewResourceId,
			List<Entry> items) {
		super(context, textViewResourceId, items);

		mContext = context;
		mItems = items;
		
		inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		
		mHttpImageManager = HttpImageManager.getInstance();
	}
	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    Entry item = mItems.get(position);
		
		View rowView = convertView;
	    ViewHolder viewHolder;
	    
	    if (rowView == null) {
	      rowView = inflater.inflate(R.layout.list_item_entry, null);
	      viewHolder = new ViewHolder(rowView);
	      rowView.setTag(viewHolder);
	    }
	    else {
	    	viewHolder = (ViewHolder) rowView.getTag();
	    }
	    
	    TextView user = viewHolder.getUsernameTextView();
	    TextView location = viewHolder.getLocationTextView();
	    TextView body = viewHolder.getMessageBodyTextView();
	    TextView timestamp = viewHolder.getTimestampTextView();
	    ImageView userThumb = viewHolder.getUserThumbnailImageView();
	    ImageView uploadedImage = viewHolder.getUploadedImageView();
	    
	    user.setText(item.user);
	    location.setText("at " + item.location);
	    body.setText(item.body);
	    //timestamp.setText("on " + item.timestamp);
	    
	    String thumbUri = item.userThumbUri;
	    
	    if(thumbUri != null) {
	    	Bitmap bm = mHttpImageManager.loadImage(new HttpImageManager.LoadRequest(Uri.parse(thumbUri), userThumb));
	    	if(bm != null) {
	    		userThumb.setImageBitmap(bm);
	    	}
	    }
	    
	    String uploadedImageUri = item.imageURI;
	    
	    if(uploadedImageUri != null) {
	    	Bitmap bm = mHttpImageManager.loadImage(new HttpImageManager.LoadRequest(Uri.parse(uploadedImageUri), uploadedImage));
	    	if(bm != null) {
	    		uploadedImage.setImageBitmap(bm);
	    	}
	    	uploadedImage.setVisibility(View.VISIBLE);
	    }
	    else {
	    	uploadedImage.setVisibility(View.GONE);
	    }

	    return rowView;
	  }

	static class ViewHolder {
		private TextView mUser, mLocation, mBody, mTimestamp;
		private ImageView mUserThumb, mUploadedImage;
		
		public ViewHolder(View v) {
			mUser = (TextView) v.findViewById(R.id.username);
			mLocation = (TextView) v.findViewById(R.id.location);
			mBody = (TextView) v.findViewById(R.id.message);
			//mTimestamp = (TextView) v.findViewById(R.id.timestamp);
			mUserThumb = (ImageView) v.findViewById(R.id.user_thumb);
			mUploadedImage = (ImageView) v.findViewById(R.id.uploaded_image);
		}
		
		public TextView getUsernameTextView() {
			return mUser;
		}
		
		public TextView getLocationTextView() {
			return mLocation;
		}
		
		public TextView getMessageBodyTextView() {
			return mBody;
		}
		
		public TextView getTimestampTextView() {
			return mTimestamp;
		}
		
		public ImageView getUserThumbnailImageView() {
			return mUserThumb;
		}
		
		public ImageView getUploadedImageView() {
			return mUploadedImage;
		}
	}
}

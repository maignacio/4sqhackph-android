package com.poweroflove.anomeron.adapter;

import httpimage.HttpImageManager;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.poweroflove.anomeron.R;
import com.poweroflove.anomeron.model.Venue;

public class SearchArrayAdapter extends ArrayAdapter<Venue> {
	private Context mContext;
	private List<Venue> mItems;
	private LayoutInflater inflater;
	private HttpImageManager mHttpImageManager;
	
	public SearchArrayAdapter(Context context, int textViewResourceId,
			List<Venue> items) {
		super(context, textViewResourceId, items);

		mContext = context;
		mItems = items;
		
		inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );		
	}
	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    final int pos = position;
		final Venue item = mItems.get(position);
		
		View rowView = convertView;
	    ViewHolder viewHolder;
	    
	    if (rowView == null) {
	      rowView = inflater.inflate(R.layout.list_item_search, null);
	      viewHolder = new ViewHolder(rowView);
	      rowView.setTag(viewHolder);
	    }
	    else {
	    	viewHolder = (ViewHolder) rowView.getTag();
	    }
	    
	    TextView tv = viewHolder.getTv();
	    
	    tv.setText(item.name);
	    
	    /*rowView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
			}
	    	
	    });*/
	    return rowView;
	  }
	
	/*public interface EntryArrayAdapterActionListener {
		public void onItemSelected(long id);
	}*/

	static class ViewHolder {
		private TextView tv;
		
		public ViewHolder(View v) {
			tv = (TextView) v.findViewById(R.id.textView1);
		}
		
		public TextView getTv() {
			return tv;
		}
	}
}

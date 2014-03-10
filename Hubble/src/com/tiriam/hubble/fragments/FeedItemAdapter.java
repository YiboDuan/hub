package com.tiriam.hubble.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tiriam.hubble.R;

public class FeedItemAdapter extends ArrayAdapter<JSONObject> {
	private final Context context;
	private final JSONObject[] fences;

	public FeedItemAdapter (Context context, JSONObject[] fences) {
		super(context, R.layout.feedrow_layout, fences);
		this.context = context;
		this.fences = fences;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.feedrow_layout, parent, false);
		TextView rowTitle = (TextView) rowView.findViewById(R.id.fence_title);
		TextView rowDesc = (TextView) rowView.findViewById(R.id.fence_desc);
		try {
			if(fences != null) {
				rowTitle.setText(fences[position].getString("name"));
				rowDesc.setText(fences[position].getString("name"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rowView;
	}

}

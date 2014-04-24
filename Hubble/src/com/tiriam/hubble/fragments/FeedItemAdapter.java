package com.tiriam.hubble.fragments;

import java.util.Calendar;
import java.util.Date;

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
				Date createdDate = parseDate(fences[position].getString("time"));
				rowDesc.setText(makeTimeDiffStr(createdDate));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rowView;
	}
	
	private Date parseDate(String time) {
		int year = Integer.parseInt(time.substring(0,4));
		int month = Integer.parseInt(time.substring(5,7));
		int day = Integer.parseInt(time.substring(8,10));
		int hour = Integer.parseInt(time.substring(10,12));
		int minute = Integer.parseInt(time.substring(13,15));
		int second = Integer.parseInt(time.substring(16));
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day, hour, minute, second);
		Date parsedDate = c.getTime();
		return parsedDate;
	}
	
	private String makeTimeDiffStr(Date createdTime) {
		String str = "";
		Calendar c = Calendar.getInstance(); 
		Date now = c.getTime();
		
		long diff = now.getTime() - createdTime.getTime();
		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);
		
		long[] date = {diffDays, diffHours, diffMinutes, diffSeconds};
		String[] dateTags = {"day", "hour", "minute", "second"};
		int dateIndex = 0;
		
		for (int i = 0; i < date.length; i++) {
			if (date[i] > 0) {
				dateIndex = i;
				break;
			}
		}
		
		if(dateTags[dateIndex].equalsIgnoreCase("second")) {
			str = "1 minute ago";
		} else {
			if(date[dateIndex] > 1) {
				str = date[dateIndex] + " " + dateTags[dateIndex] + "s ago";
			} else {
				str = "1 " + dateTags[dateIndex] + " ago";
			}
		}
		
		return str;
	}

}

package com.tiriam.hubble.fragments;

import java.util.Date;

import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiriam.hubble.R;

public class MenuItemAdapter extends ArrayAdapter<String> {

	Context context;
	String[] mOptions;
	
	public MenuItemAdapter(Context context, String[] options) {
		super(context, R.layout.menurow_layout, options);
		mOptions = options;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.menurow_layout, parent, false);
		TextView rowText = (TextView) rowView.findViewById(R.id.menu_row_text);
		rowText.setText(mOptions[position]);
		
		ImageView rowImage = (ImageView) rowView.findViewById(R.id.menu_row_icon);
		int imageViewSource = 0;
		switch(position) {
			case 0:
				imageViewSource = R.drawable.question_image;
				break;
			case 1:
				imageViewSource = R.drawable.settings_image;
				break;
			case 2:
				imageViewSource = R.drawable.disk_image;
				break;
			case 3:
				imageViewSource = R.drawable.exit_image;
				break;
		}
		rowImage.setImageResource(imageViewSource);
		
		return rowView;
	}

}

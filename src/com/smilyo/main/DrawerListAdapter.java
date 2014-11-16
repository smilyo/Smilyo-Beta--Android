package com.smilyo.main;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DrawerListAdapter extends BaseAdapter {

	Context context;
	ArrayList<DrawerItem> drawerItems;

	public DrawerListAdapter(Context context, ArrayList<DrawerItem> drawerItems) {
		this.context = context;
		this.drawerItems = drawerItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return drawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return drawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int postion, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.list_item, null);
		}

		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

		txtTitle.setText(drawerItems.get(postion).getTitle());

		return convertView;

	}

}

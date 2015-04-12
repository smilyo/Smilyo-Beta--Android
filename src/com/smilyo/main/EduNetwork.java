/** This is the eduNetwork fragment class which is displayed when eduNetwork is selected from the slider menu.
 * It is mostly same as the Articles fragment class. **/
package com.smilyo.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AbsListView.OnScrollListener;

public class EduNetwork extends Fragment {

	public EduNetwork() {

	}

	ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
	ListView lists;
	SimpleAdapter adapter;

	View footer;

	String url;
	String id = "";

	private int currentPage = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView;

		this.id = getArguments().getString("got");

		/*
		 * This class mostly shows the portals which are selected from the
		 * college portal list. Here if none of the portals are selected then
		 * the alert is shown to add college portal and if pressed ok then it it
		 * takes to the college portal list.
		 */
		if (id == "") {
			rootView = inflater.inflate(R.layout.button, container, false);
			AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
			d.setCancelable(true);
			d.setMessage("Please Add College Portals").setTitle("Add Portals");
			d.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Intent i = new Intent(getActivity(), CollegePortal.class);
					getActivity().startActivityForResult(i, 1);
				}
			});

			AlertDialog dialog = d.create();
			dialog.show();
		} else {

			rootView = inflater.inflate(R.layout.articles, container, false);

			url = "" // Sample URL
					+ Integer.toString(currentPage) + "&ids=" + id;

			lists = (ListView) rootView.findViewById(R.id.list);

			footer = ((LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer,
					null, false);

			lists.addFooterView(footer);

			new JSONParse(url).execute();

			adapter = new SimpleAdapter(getActivity(), myList,
					R.layout.edu_network_list_item, new String[] {
							"group_name", "type", "data", "author", "date" },
					new int[] { R.id.tvGroupName, R.id.tvType, R.id.tvData,
							R.id.tvAuthor, R.id.tvDate });
			lists.setAdapter(adapter);

			lists.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub

					if (totalItemCount >= 5 * (currentPage + 1)) {

						boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

						if (loadMore) {

							currentPage++;

							new JSONParse(url).execute();

						}
					}
				}

				@Override
				public void onScrollStateChanged(AbsListView arg0, int arg1) {
					// TODO Auto-generated method stub

				}

			});
		}

		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.edu_button, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.button:
			Intent i = new Intent(getActivity(), CollegePortal.class);
			getActivity().startActivityForResult(i, 1);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	class JSONParse extends
			AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

		String url1;

		public JSONParse(String args) {
			this.url1 = args;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			lists.addFooterView(footer);

		}

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... arg0) {
			// TODO Auto-generated method stub

			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

			JSONParser json = new JSONParser();
			String jsonData = json.getJSONFromUrl(url1);
			try {
				JSONArray jArray = new JSONArray(jsonData);
				for (int i = 0; i < jArray.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();

					JSONObject jObj = jArray.getJSONObject(i);
					map.put("group_name", jObj.getString("group_name"));
					map.put("type", jObj.getString("type"));
					map.put("data", jObj.getString("data"));
					map.put("author", jObj.getString("author"));
					map.put("date", jObj.getString("date"));
					list.add(map);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return list;
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> list) {
			// TODO Auto-generated method stub

			lists.removeFooterView(footer);
			myList.addAll(list);
			adapter.notifyDataSetChanged();
		}
	}
}

/** This is the fragment class which is put in the frame layout of the Navigation Drawer  **/
package com.smilyo.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Articles extends Fragment {

	public Articles() {
	}

	// This is used to store the items of the articles.
	ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
	ListView lists;
	SimpleAdapter adapter;

	// This variable is used to store integer argument received from the main
	// activity for the fragment.
	int c;

	// Footer view is used to display a custom loading progress.
	View footer;

	// This variable is used to store the url which must be given to the async
	// task to download the json data and parse it.
	String url;

	// This is mostly used in the scroll listener.
	private int currentPage = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.articles, container, false);

		// Getting the arguments in the variable c
		c = getArguments().getInt("article");

		// Setting the url of the json file in the smilyo database to this
		// variable.
		url = "http://smilyo.com/get_articles.php?limit=" // Sample URL
				+ Integer.toString(currentPage) + "&category="
				+ Integer.toString(c);

		lists = (ListView) rootView.findViewById(R.id.list);

		// inflating the footer view at the start of the fragment.
		footer = ((LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer,
				null, false);

		lists.addFooterView(footer);

		// Executing async task in the background to load the json data and
		// parse it.
		new JSONParse(url).execute();

		// Default Adapter to set the items to the list view of the fragment.
		adapter = new SimpleAdapter(getActivity(), myList, R.layout.item,
				new String[] { "title", "summary", "author", "date_created" },
				new int[] { R.id.tvTitle, R.id.tvSummary, R.id.tvAuthor,
						R.id.tvDate });
		lists.setAdapter(adapter);

		lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// Body class is the activity opened when the item in the list
				// is clicked.
				Intent i = new Intent(getActivity(), Body.class);
				i.putExtra("title", myList.get(arg2).get("title"));
				i.putExtra("body", myList.get(arg2).get("body"));
				i.putExtra("author", myList.get(arg2).get("author"));
				startActivity(i);
			}
		});

		lists.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

				if (totalItemCount >= 5 * (currentPage + 1)) {

					boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

					if (loadMore) {

						currentPage++;

						// While scrolling if we reach to the end of the list
						// then the loader is executed and an async task is run
						// to get the further data.
						new JSONParse(
								"http://smilyo.com/get_articles.php?limit="
										+ Integer.toString(currentPage)
										+ "&category=" + Integer.toString(c))
								.execute();

					}
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub

			}

		});

		return rootView;
	}

	/*
	 * Async task is used to load the data from the json file and parse it. Then
	 * it maps the data in a HashMap which is then set to the adapter.
	 */
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

			// This HashMap is used locally to store downloaded parsed data and
			// then they are added to the global
			// variable myList which is set to the adapter.
			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

			JSONParser json = new JSONParser();
			String jsonData = json.getJSONFromUrl(url1);
			try {
				JSONArray jArray = new JSONArray(jsonData);
				for (int i = 0; i < jArray.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();

					JSONObject jObj = jArray.getJSONObject(i);
					map.put("title", jObj.getString("title"));
					map.put("summary", jObj.getString("summary"));
					map.put("author", jObj.getString("author"));
					map.put("date_created", jObj.getString("date_created"));
					map.put("body", jObj.getString("body"));
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

			// After the data is parsed the loader is removed and the items are
			// displayed.
			lists.removeFooterView(footer);
			myList.addAll(list);
			adapter.notifyDataSetChanged();
		}
	}
}

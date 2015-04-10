package com.smilyo.main;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class CollegePortal extends Activity {

	ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
	ListView lists;

	SparseBooleanArray sp;
	String selected;

	ArrayAdapter<String> adapter;

	Button show;

	ArrayList<String> portals = new ArrayList<String>();

	ArrayList<Integer> h = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle s) {
		// TODO Auto-generated method stub
		super.onCreate(s);
		setContentView(R.layout.college_portal);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		lists = (ListView) findViewById(R.id.portallist);

		new JSONParse("http://smilyo.com/app/get_edunetwork_portals.php") // Sample URL
				.execute();

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, portals);

		lists.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		lists.setAdapter(adapter);

		SharedPreferences prefs = getSharedPreferences("saved", 0);
		if (prefs != null) {
			for (int i = 0; i < prefs.getInt("size", 0); i++) {
				lists.setItemChecked(prefs.getInt("a" + i, 0), true);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			save();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		save();
	}

	private void save() {
		ArrayList<String> st = new ArrayList<String>();
		selected = "";

		sp = lists.getCheckedItemPositions();

		for (int i = 0; i < lists.getCount(); i++) {
			if (sp.get(i)) {
				h.add(i);
				st.add(myList.get(i).get("id"));
			}
		}
		for (int j = 0; j < st.size(); j++) {
			selected += st.get(j);
			if (j < st.size() - 1)
				selected = selected + ",";
		}

		SharedPreferences prefs = getSharedPreferences("saved", 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("size", h.size());
		for (int i = 0; i < h.size(); i++) {
			editor.putInt("a" + i, h.get(i));
		}

		editor.commit();

		send();

	}

	protected void send() {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.putExtra("s", selected);
		setResult(Activity.RESULT_OK, i);
		finish();
	}

	class JSONParse extends
			AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

		String url1;

		public JSONParse(String args) {
			this.url1 = args;
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
					map.put("id", jObj.getString("id"));
					map.put("name", jObj.getString("name"));
					map.put("description", jObj.getString("description"));
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

			myList.addAll(list);
			for (HashMap<String, String> l : list)
				portals.add(l.get("name"));
			adapter.notifyDataSetChanged();
		}
	}
}

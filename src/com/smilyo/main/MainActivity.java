/** This is the Main Activity class in which the Navigation Drawer is instantiated. The navigation drawer contains a frame
 * container in which we put the fragments and a side menu bar which come out from the left edge. **/

package com.smilyo.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {

	// This is the drawer layout variable in which we put the fragments
	private DrawerLayout mDrawerLayout;

	// This is for the side menu list
	private ListView mDrawerList;

	// This is used for the toggle in the top left corner in the action bar
	private ActionBarDrawerToggle mDrawerToggle;

	// This is used for the Drawer Title
	private CharSequence mDrawerTitle;

	// This is used to store app title
	private CharSequence mTitle;

	// Array to store the titles of slide menu items
	private String[] navMenuTitles = { "Home", "Articles", "Poems",
			"Articles/Poems", "EduNetwork" };

	// ArrayList for the side menu items. The custom items(DrawerItem) are saved
	// in a list form.
	private ArrayList<DrawerItem> navDrawerItems;

	// This adapter sets the list items of the navDrawerItems to the list view
	// of the side bar menu
	private DrawerListAdapter adapter;

	// This is a map in which the json data of menu_item.php is stored.
	HashMap<String, String> map = new HashMap<String, String>();

	/*
	 * This variable is used to store the id numbers of portals which are
	 * displayed in the eduNetwork. The id numbers come in the form of string
	 * with each id separated by a comma. This is then set as argument of the
	 * eduNetwork fragment.
	 */
	String result = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// This is used to execute the async task in the background. This gets
		// the data from the json menu_item.php.
		//new JSONParse().execute();

		// This is used to save the data of the result variable so that it is
		// not lost when the app is closed.
		// Thus it will ensure that the portals selected are saved.
		SharedPreferences selected = getSharedPreferences("First", 0);
		if (selected != null)
			result = selected.getString("result", result);

		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.slider_menu);

		/*
		 * The DrawerItem is just a class for the list items to be displayed in
		 * the side bar menu
		 */
		navDrawerItems = new ArrayList<DrawerItem>();

		for (int i = 0; i < navMenuTitles.length; i++) {
			navDrawerItems.add(new DrawerItem(navMenuTitles[i]));
		}

		// Listener is set to the items of the Slider Menu
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// Adapter to connect the Slider Menu List to its items in the ArrayList
		adapter = new DrawerListAdapter(this, navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}

		// Simple method to display the slider menu for few seconds at the start
		// of the app
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mDrawerLayout.openDrawer(Gravity.LEFT);
			}
		}, 500);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			}
		}, 1200);
	}

	/**
	 * slider menu items click listener
	 **/

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		adapter.notifyDataSetChanged();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Called when invalidateOptionsMenu() is triggered
	 **/

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/*
	 * The result from the activity CollegePortal is received and set equal to
	 * the variable result
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			result = data.getStringExtra("s");
		} else {
			result = "";
		}
		displayView(4);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		// steps used to save data using shared preferences.
		SharedPreferences selected = getSharedPreferences("First", 0);
		SharedPreferences.Editor editor = selected.edit();
		editor.putString("result", result);

		editor.commit();
	}

	// invoked when the items in the slider menu are pressed
	private void displayView(int position) {
		// updating the main contents by replacing with the fragments
		Bundle bundle = new Bundle();
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new Home();
			break;
		case 1:
			bundle.putInt("article", 2);
			fragment = new Articles();
			fragment.setArguments(bundle);
			break;
		case 2:
			bundle.putInt("article", 1);
			fragment = new Articles();
			fragment.setArguments(bundle);
			break;
		case 3:
			bundle.putInt("article", 0);
			fragment = new Articles();
			fragment.setArguments(bundle);
			break;
		case 4:
			bundle.putString("got", result);
			fragment = new EduNetwork();
			fragment.setArguments(bundle);
			break;
		case 5:
			Uri uri = Uri.parse(map.get("URL"));
			Intent i = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(i);
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating Fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, we must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggle
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	// A simple execution of async task which load json data for a given URL.
	// This basically gives the recruitments july item for the slider menu if
	// present.
	class JSONParse extends AsyncTask<String, String, HashMap<String, String>> {

		@Override
		protected HashMap<String, String> doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			JSONParser json = new JSONParser();

			try {

				String jsonData = json
						.getJSONFromUrl("http://www.smilyo.com/app/menu_item.php"); //Sample URL

				JSONObject jObj = new JSONObject(jsonData);

				map.put("title", jObj.getString("title"));
				map.put("URL", jObj.getString("URL"));

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return map;
		}

		@Override
		protected void onPostExecute(HashMap<String, String> result) {
			// TODO Auto-generated method stub

			if (result.get("URL") != null)
				navDrawerItems.add(new DrawerItem(result.get("title")));

		}
	}
}

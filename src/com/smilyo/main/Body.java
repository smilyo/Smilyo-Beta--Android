/** This class is used as the activity in which the pressed item of articles are shown. **/
package com.smilyo.main;


import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.TextView;

public class Body extends Activity {

	TextView title, author;
	WebView body;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.body);
		title = (TextView) findViewById(R.id.bodyTitle);
		author = (TextView) findViewById(R.id.bodyAuthor);
		body = (WebView) findViewById(R.id.body);
		body.getSettings().setJavaScriptEnabled(true);
		body.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		WebSettings settings = body.getSettings();
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setBuiltInZoomControls(true);
		title.setText(getIntent().getStringExtra("title"));
		author.setText(getIntent().getStringExtra("author"));
		body.loadData(getIntent().getStringExtra("body"), "text/html", "utf-8");
	}
}

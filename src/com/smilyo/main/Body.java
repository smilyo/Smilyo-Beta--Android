/** This class is used as the activity in which the pressed item of articles are shown. **/
package com.smilyo.main;

import org.apache.commons.lang3.StringEscapeUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
		title.setText(getIntent().getStringExtra("title"));
		author.setText(getIntent().getStringExtra("author"));
		String s =getIntent().getStringExtra("body");
		//s=s.replaceAll("[\u0000-\u001f]", "");


		Log.e("s is ", ""+s);
		body.loadData(s, "text/html", "utf-8");
	}

}

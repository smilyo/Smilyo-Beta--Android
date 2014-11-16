package com.smilyo.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class RecruitmentsJuly extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recruitments_july);

		String url = getIntent().getStringExtra("rec");

		TextView rec = (TextView) findViewById(R.id.url);

		rec.setText(url);
	}
}
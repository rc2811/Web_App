package com.example.web_app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FamilyMemberActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LinearLayout mLinearLayout = new LinearLayout(this);
		Intent intent = getIntent();
		int pic_id = intent.getIntExtra("id", 10);
		
		Button b = new Button(this);
		b.setText("hello");
		mLinearLayout.addView(b);
		
		ImageView i = new ImageView(this);
		i.setImageResource(pic_id);
		
		mLinearLayout.addView(i);
		setContentView(mLinearLayout);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.family_member, menu);
		return true;
	}

}

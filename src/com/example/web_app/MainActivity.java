package com.example.web_app;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	public final static String USERNAME = "com.example.web_app.USERNAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//title.setBackgroundColor(android.graphics.Color.TRANSPARENT);
	
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void getAccountInfo(View view) {
		EditText username = (EditText) findViewById(R.id.editUsername);
		EditText password = (EditText) findViewById(R.id.editPassword);
		
		String usernameString = username.getText().toString();
		String passwordString = username.getText().toString();
		
		if (!accountCheck(usernameString, passwordString)) {
			Intent intent  = getIntent();
			finish();
			startActivity(intent);
			
		} else {
		
			Intent intent = new Intent(this, HomeScreenActivity.class);
		
			intent.putExtra(USERNAME, usernameString);
			startActivity(intent);
		}
		
		
	}
	
	public boolean accountCheck(String username, String password) {
		
		//TODO implement a call to the database to check details
		
		/*if (!username.equals("Rob")) {
			Toast.makeText(getApplicationContext(),
					"No account exists under that username", Toast.LENGTH_SHORT);
			return false;
		}
		if (!username.equals("hello")) {
			Toast.makeText(getApplicationContext(),
					"Incorrect Password", Toast.LENGTH_SHORT);
			return false;
		} */
		return true;
	
	}
	
	public void register(View view) {
		
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	
	}

}

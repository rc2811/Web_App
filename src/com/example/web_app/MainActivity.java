package com.example.web_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Session;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements contextSwitcher {
	
	public Context context;
	public static final String PREFS_NAME = "UserPrefs";
	private static final String PREF_USERNAME = "username";
	private static final String PREF_PASSWORD = "password";
	
	public final static String USERNAME = "com.example.web_app.USERNAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		context = this;
		
		setContentView(R.layout.activity_main);
		
		SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);   
		String username = pref.getString(PREF_USERNAME, null);
		String password = pref.getString(PREF_PASSWORD, null);
		
		EditText eUser = (EditText) findViewById(R.id.editUsername);
		EditText ePass = (EditText) findViewById(R.id.editPassword);
		CheckBox cBox = (CheckBox) findViewById(R.id.rememberUser);

		if (username != null && password != null) {
			eUser.setText(username);
			ePass.setText(password);
			cBox.setChecked(true);
		}
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void getAccountInfo(View view) throws InterruptedException {
		EditText username = (EditText) findViewById(R.id.editUsername);
		EditText password = (EditText) findViewById(R.id.editPassword);
		
		String usernameString = username.getText().toString();
		String passwordString = password.getText().toString();
		
		
		Request request = new Request(Command.LOGIN, new String[] {usernameString,passwordString});
		ServerRequest servReq = new ServerRequest(this);
		
		servReq.execute(request);
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	public void register(View view) {
		
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	
	}
	
	@Override
	public void cSwitch(String result) {
		Log.v("Message from server", result);
		if(result.equals("OK")) {
			Log.v("message comparison", "Login Good");
			 Intent intent = new Intent(context, HomeScreenActivity.class);
			startActivity(intent);
		} else {
			Context context = getApplicationContext();
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, result, duration);
			toast.show();
		}
	}
	
	public void rememberUser(View view) {
		
		EditText username = (EditText) findViewById(R.id.editUsername);
		EditText password = (EditText) findViewById(R.id.editPassword);
		String usernameString = username.getText().toString();
		String passwordString = password.getText().toString();
		
		getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit().putString(PREF_USERNAME, usernameString ).commit();
		getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit().putString(PREF_PASSWORD, passwordString ).commit();
	}

}

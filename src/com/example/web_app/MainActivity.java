package com.example.web_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Session;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	public Request request;
	public String REQUEST = "";
	
	public final static String USERNAME = "com.example.web_app.USERNAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
	
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
		ServerRequest servReq = new ServerRequest();
		servReq.execute(request);
		
		/*String reply = servReq.getReply();
		
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_LONG;

		Toast toast = Toast.makeText(context, reply, duration);
		toast.show();*/
		
		//Intent intent = new Intent(this, HomeScreenActivity.class);
		//startActivity(intent);
		
		
		/*if (!accountCheck(usernameString, passwordString)) {
			Intent intent  = getIntent();
			finish();
			startActivity(intent);
			
		} else {
		
			Intent intent = new Intent(this, HomeScreenActivity.class);
		
			intent.putExtra(USERNAME, usernameString);
			startActivity(intent);
		}*/
		
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
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

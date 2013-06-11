package com.example.web_app;

import java.util.Arrays;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MessagingActivity extends Activity implements RequestHandler{

	public static final String PREFS_NAME = "UserPrefs";
	private static final String PREFF_CURR_USER = "currUser";
	SharedPreferences pref;
	String currUser;
	RelativeLayout mainLayout;
	RelativeLayout r;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
		currUser = pref.getString(PREFF_CURR_USER, null);
		LayoutInflater inflater = getLayoutInflater();
		mainLayout = (RelativeLayout) inflater.inflate(R.layout.activity_messaging,null);
		setContentView(R.layout.activity_messaging);
		getMessages();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.messaging, menu);
		return true;
	}

	@Override
	public void doOnRequestComplete(String s) {
		Log.v("server string", s);
		if(s.equals("YOU HAVE NO NOTES")) {
			Context context = getApplicationContext();
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, s, duration);
			toast.show();
			
		} else {
			String[] messages = s.split("~");
			 @SuppressWarnings("deprecation")
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
		                RelativeLayout.LayoutParams.FILL_PARENT,
		                RelativeLayout.LayoutParams.FILL_PARENT);
			 rlp.addRule(RelativeLayout.ABOVE, findViewById(R.id.goToSendMessageButton).getId());
			r = new RelativeLayout(getApplicationContext());
			ScrollView sV = new ScrollView(getApplicationContext());
			GridLayout g = new GridLayout(getApplicationContext());
			g.setColumnCount(1);
			for(String x  : messages) {
				String y = new String(Arrays.copyOfRange(x.toCharArray(), 1, x.length()-1));
				String[] z = y.split(",", 2);
				String message = z[0] + ": " + z[1];
				displayMessage(message, g);
			}
			sV.addView(g);
			r.addView(sV);
			mainLayout.addView(r, rlp);
			setContentView(mainLayout);
			
		}

		
		
	}
	
	public void sendMessage(View view) {
		Intent intent = new Intent(this, SendMessageActivity.class);
		startActivity(intent);
	}
	
	public void getMessages() {
		ServerRequest servReq = new ServerRequest(this);
		servReq.getMessages(currUser);
	}
	
	public void displayMessage(String s, GridLayout l) {
		TextView t = new TextView(getApplicationContext());
		t.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		//t.setBackgroundColor(Color.RED);
		t.setTextSize(50);
		t.setTextColor(Color.RED);
		t.setText(s);
		l.addView(t);
	}

}

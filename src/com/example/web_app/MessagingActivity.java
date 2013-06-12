package com.example.web_app;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

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
	int[] colors = new int[] {Color.RED, Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.YELLOW};
	Map<String, Integer> name_colors_map = new LinkedHashMap<String, Integer>();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Your Notes");
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
			for(int i = messages.length - 1; i >=0; i--) {
				String y = new String(Arrays.copyOfRange(messages[i].toCharArray(), 1, messages[i].length()-1));
				String[] z = y.split(",", 2);
				String sender = z[0];
				String message = z[1];
				displayMessage(sender, message, g);
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
	
	public void displayMessage(String s, String m, GridLayout l) {
		TextView t = new TextView(getApplicationContext());
		t.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		//t.setBackgroundColor(Color.RED);
		t.setTextSize(50);
		if(!name_colors_map.containsKey(s)) {
			Random r = new Random();
			int c = r.nextInt(6);
			while(name_colors_map.containsValue(colors[c]) && name_colors_map.size() < 6){
				c = r.nextInt(6);
			}
			name_colors_map.put(s, colors[c]);

		}
		t.setTextColor(name_colors_map.get(s));
		t.setText(s + ": " + m);
		l.addView(t);
	}

}

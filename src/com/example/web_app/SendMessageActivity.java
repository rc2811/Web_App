package com.example.web_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SendMessageActivity extends Activity implements RequestHandler {
	
	public static final String PREFS_NAME = "UserPrefs";
	private static final String PREFF_CURR_USER = "currUser";
	SharedPreferences pref;
	String currUser;
	Spinner sendSpinner;
	EditText messageField;
	int reqID = -1;
	String[] friends;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_message);
		setTitle("Send a message");
		
		pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
		currUser = pref.getString(PREFF_CURR_USER, null);
		
		sendSpinner = (Spinner) findViewById(R.id.sendSpinner);
		messageField = (EditText) findViewById(R.id.messageText);
		
		ServerRequest servReq = new ServerRequest(this);
		reqID = 0;
		servReq.getFriends(currUser);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return false;
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    super.onKeyDown(keyCode, event);
        switch(keyCode)
        {
        case KeyEvent.KEYCODE_BACK:
         
            finishActivity(0);
            return true;
        }
        return false;
	}
	
	public void sendMessage(View view) {
		String message = messageField.getText().toString();
		ServerRequest servReq = new ServerRequest(this);
		reqID = 1;
		servReq.sendMessage(currUser, sendSpinner.getSelectedItem().toString(), message);
	}

	@Override
	public void doOnRequestComplete(String s) {
		if(reqID == 0 ) {
			friends = s.split("~");
			
			//populate send to dropdown options
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinnerlayout, friends);
			adapter.setDropDownViewResource(R.layout.spinnerlayout);
			sendSpinner.setAdapter(adapter);
			
			reqID = -1;
			
		} else if(reqID == 1) {
			int duration = Toast.LENGTH_SHORT;
			String result;
			Class<?> next;
			if(s.equals("OK")) {
				result = "Message Sent";
				next = (MessagingActivity.class);
				Toast toast = Toast.makeText(this, result, duration);
				toast.show();
				finishActivity(0);
			} else {
				result = s;
				Toast toast = Toast.makeText(this, result, duration);
				toast.show();
			}
			reqID = -1;
		}
		
		
		
		
	}

}

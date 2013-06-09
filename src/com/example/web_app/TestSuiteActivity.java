package com.example.web_app;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("WrongCall")
public class TestSuiteActivity extends Activity implements RequestHandler{
	
	public static final String PREFS_NAME = "UserPrefs";
	private static final String PREFF_CURR_USER = "currUser";
	SharedPreferences pref;
	String currUser;
	ImageView imageView;
	EditText msgBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		currUser = pref.getString(PREFF_CURR_USER, null);
		setContentView(R.layout.activity_test_suite);
		
		msgBox = (EditText)findViewById(R.id.editTextTest);
		imageView = (ImageView)findViewById(R.id.imageView1);
		imageView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int eid = event.getAction();
                switch (eid) {
                case MotionEvent.ACTION_MOVE:

                    RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                    int x = (int) event.getRawX();
                    int y = (int) event.getRawY();
                    mParams.leftMargin = x - 50;
                    mParams.topMargin = y - 50;
                    imageView.setLayoutParams(mParams);


                    break;

                default:
                    break;
                }
                return true;
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.test_suite, menu);
		
		return true;
	}
	
	public void testAction(View view) {
    	ServerRequest servReq = new ServerRequest(this);
    	servReq.clearIDs(currUser);
	}
	
	public void testAction2(View view) {
    	ServerRequest servReq = new ServerRequest(this);
    	String msg = msgBox.getText().toString();
    	servReq.sendMessage(currUser, "88888888", msg);
	}
	
	@Override
	public void doOnRequestComplete(String s) {
		Log.v("server reply", s);
		TextView textView = new TextView(this);
		
		String[] reply = s.split("~");
		
		String result = "";
		
		/*if(reply.length != 1) {
		for(String x  : reply) {
			String z = new String(Arrays.copyOfRange(x.toCharArray(), 1, x.length()-1));
			String[] y = z.split(",", 2);
			result += y[0] + ": " + y[1] + "\n";
		}
		} else {
			result = reply[0];
		}*/

		result = s;
		textView.setText(result);
		textView.setTextSize(30);
		setContentView(textView);
		
	}

}

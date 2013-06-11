package com.example.web_app;

import java.util.Arrays;
import java.util.List;

import com.facebook.Session;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class QuizStartActivity extends Activity implements RequestHandler {
	

	private static final List<String> PERMISSIONS = Arrays.asList("friends_birthday", "user_photos", "friends_photos", "read_friendlists", "user_relationships");
	
	
	SharedPreferences pref;
	String currUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz_start);
		setTitle("Start Quiz");
		
		pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
		currUser = pref.getString("currUser", null);
		
		EditText textEdit = (EditText) findViewById(R.id.num_questions);
		textEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		Session session1 = new Session(this);
		session1.openForRead(new Session.OpenRequest(this));

		session1.requestNewReadPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS));
		
		Session.setActiveSession(session1);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.quiz_start, menu);
		return false ;
	}
	
	
	public void quiz(View view) {
		
		ServerRequest s = new ServerRequest(this);
		s.fetchIDs(currUser);
	}
		

	@Override
	public void doOnRequestComplete(String s) {
		
		String[] ids = s.split(":");
		
		if (ids.length< 4) {
					
			Toast.makeText(getApplicationContext(), "You do not have enough family members added to do a quiz", Toast.LENGTH_SHORT).show();
			finishActivity(0);
					
		} else {
		
			EditText editText = (EditText) findViewById(R.id.num_questions);
		
			Intent intent = new Intent(this, QuizActivity.class);
		
			String num_questions_string = null;
		
			int num_questions = 0;
		
			Editable t = editText.getText();
			if (t != null) {
				num_questions_string = t.toString();
			}
		
			if (!num_questions_string.equals("")) {
				num_questions = Integer.parseInt(num_questions_string);
		
				if (num_questions < 0 || num_questions > 20) {
					Toast.makeText(this, "Please enter a number between 1 and 20 inclusive", Toast.LENGTH_SHORT).show();

				} else {
			
					intent.putExtra("num_questions", num_questions);
					startActivity(intent);
				
				}
			} else {
				
				Toast.makeText(this, "Please enter a number between 1 and 20 inclusive", Toast.LENGTH_SHORT).show();
			}
	
		
		}
	}

		
	}
	
	



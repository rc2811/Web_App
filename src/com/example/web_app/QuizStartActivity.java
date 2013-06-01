package com.example.web_app;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;

public class QuizStartActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz_start);
		setTitle("Start Quiz");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz_start, menu);
		return true;
	}
	
	
	public void quiz(View view) {
		//RadioButton one_radio = (RadioButton) view.findViewById(R.id.one_question_radio);
		//RadioButton five_radio = (RadioButton) view.findViewById(R.id.five_questions_radio);
		
		Intent intent = new Intent(this, QuizActivity.class);
		
		//if (five_radio.isActivated()) {
	//	} else {
		//	intent.putExtra("num_questions", 1);
	//	}
		
		startActivity(intent);
		
	}
	
	

}

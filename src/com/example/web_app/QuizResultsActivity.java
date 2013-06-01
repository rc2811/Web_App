package com.example.web_app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class QuizResultsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz_results);
		setTitle("Results");
		
		Intent intent = getIntent();
		int numQuestions = intent.getExtras().getInt("NumQuestions");
		int correct = intent.getExtras().getInt("Correct");
		
		TextView t = (TextView) findViewById(R.id.quiz_results);
		t.setText("You scored " + correct + " out of " + numQuestions);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz_results, menu);
		return true;
	}
	
	public void backToMenu(View view) {
		Intent intent = new Intent(this, HomeScreenActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		
	}

}

package com.example.web_app;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class QuizActivity extends Activity implements OnClickListener {
	
	private int correctAnswer;
	private int maxQuestions;
	private int currentQuestion;
	private int correct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		
		maxQuestions = 1;
		
		correct = 0;
		quiz(1);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}
	
	public void quiz(int question_num) {
		
		if (question_num != maxQuestions +1) {
			setupQuestion(question_num);
			currentQuestion = question_num;
		} else {
			Intent intent  = new Intent(this, QuizResultsActivity.class);
			intent.putExtra("NumQuestions", maxQuestions);
			intent.putExtra("Correct", correct);
			startActivity(intent);
		}
		
		
	}
	
	public void setupQuestion(int question_number) {
		setTitle("Question " + question_number);
		
		ImageView quiz_picture = (ImageView) this.findViewById(R.id.quiz_picture);
		quiz_picture.setBackgroundResource(R.drawable.andrew);
		
		Button option_1 = (Button) this.findViewById(R.id.option_1);
		option_1.setText("Robert");
		Button option_2 = (Button) this.findViewById(R.id.option_2);
		option_2.setText("Andrew");
		Button option_3 = (Button) this.findViewById(R.id.option_3);
		option_3.setText("Maggie");
		Button option_4 = (Button) this.findViewById(R.id.option_4);
		option_4.setText("Brian");
		

		option_1.setOnClickListener(this);

		option_2.setOnClickListener(this);

		option_3.setOnClickListener(this);
		
		option_4.setOnClickListener(this);
		
		correctAnswer = 2;
		
		
	}
	
	public void onClick(View view) {
		
		int response = 1;
		
		if (view.getId() == R.id.option_1) {
			response = 1;
		} else if (view.getId() == R.id.option_2) {
			response = 2;
		} else if (view.getId() == R.id.option_3) {
			response = 3;
		} else if (view.getId() == R.id.option_4) {
			response = 4;
		}
		
		if (response != correctAnswer) {
			Toast.makeText(getApplicationContext(), "Incorrect Answer", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(), "Correct Answer", Toast.LENGTH_SHORT).show();
			correct++;
		} 
		
		currentQuestion++;
		quiz(currentQuestion);
		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {

	    }
	    return super.onKeyDown(keyCode, event);
	}

}

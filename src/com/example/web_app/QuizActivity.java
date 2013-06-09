package com.example.web_app;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

public class QuizActivity extends Activity implements OnClickListener {
	
	private int correctAnswer;
	private int maxQuestions;
	private int currentQuestion;
	private int correct;
	private int questionNumber;
	
	private static final List<String> PERMISSIONS = Arrays.asList("friends_birthday", "user_photos", "friends_photos",
										"read_friendlists", "user_relationships");
	
	private String TAG = "QuizActivity";
	
	private List<String> uids = Arrays.asList("746975053", "1767412253", "1384204844", "672863965", "100002592216325");
	private List<String> names;
	private List<String> birthdays;
	private List<String> schools;
	private List<String> work;
	private List<String> ages;
	private List<String> profile_picture_urls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		
		Session session1 = new Session(this);
		session1.openForRead(new Session.OpenRequest(this));

		session1.requestNewReadPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS));
		
		Session.setActiveSession(session1);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		getFamilyData();
		
		maxQuestions = 1;
		
		correct = 0;
		quiz(1);
		
	}
	
	private void getFamilyData() {

		for (int i = 0; i < 5; i++) {
		
		String fqlQuery = "select name, birthday, education, work, pic_big from user where uid = " + uids.get(i);

		Bundle params = new Bundle();
		params.putString("q", fqlQuery);

		Session session = Session.getActiveSession();
		Request request = new Request(session, 
		    "/fql", 
		    params, 
		    HttpMethod.GET, 
		    new Request.Callback(){ 

				@Override
				public void onCompleted(Response response) {
			        Log.i(TAG, "Got results: " + response.toString());
			        GraphObject g = response.getGraphObject();
			        
			       	JSONObject j = g.getInnerJSONObject();
			       	try {
						JSONArray data = j.getJSONArray("data");
						Log.i(TAG, data.toString());
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

			       
				}
			       

		        });
		Request.executeBatchAsync(request);
		        }
		        

		}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}
	
	public void quiz(int question_num) {
		
		if (question_num != maxQuestions +1) {
		//	setupQuestion(question_num);
			currentQuestion = question_num;
		} else {
			Intent intent  = new Intent(this, QuizResultsActivity.class);
			intent.putExtra("NumQuestions", maxQuestions);
			intent.putExtra("Correct", correct);
			startActivity(intent);
		}
		
		
	}
	
	public void setupQuestion(String answer_a, String answer_b, String answer_c, String answer_d, int correct, String person_img_URL) {
		setTitle("Question " + questionNumber);
		
		ImageView quiz_picture = (ImageView) this.findViewById(R.id.quiz_picture);
		quiz_picture.setBackgroundResource(R.drawable.andrew);
		
		Button option_1 = (Button) this.findViewById(R.id.option_1);
		option_1.setText(answer_a);
		Button option_2 = (Button) this.findViewById(R.id.option_2);
		option_2.setText(answer_b);
		Button option_3 = (Button) this.findViewById(R.id.option_3);
		option_3.setText(answer_b);
		Button option_4 = (Button) this.findViewById(R.id.option_4);
		option_4.setText(answer_c);
		

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

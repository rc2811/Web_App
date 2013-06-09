package com.example.web_app;

import java.util.ArrayList;
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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

public class QuizActivity extends Activity implements OnClickListener {
	
	private String correctAnswerUID;
	private int maxQuestions;
	private int currentQuestion;
	private int correct;
	private int questionNumber;
	private String choice_a;
	private String choice_b;
	private String choice_c;
	private String choice_d;
	
	private static final List<String> PERMISSIONS = Arrays.asList("friends_birthday", "user_photos", "friends_photos",
										"read_friendlists", "user_relationships");
	
	private String TAG = "QuizActivity";
	
	private List<String> uids;
	private List<FamilyMember> family;

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
		
		family = new ArrayList<FamilyMember>();
		uids = new ArrayList<String>();
		
		uids.add("746975053");
		uids.add("1767412253");
		uids.add("1384204844");
		uids.add("672863965");
		uids.add("100002592216325");
		
		getFamilyData();
		
		Log.i(TAG, family.toString());
	
		
		maxQuestions = 1;
		
		correct = 0;
		quiz(1); 
		
	}
	
	private void getFamilyData() {

		for (int i = 1; i < 2 ; i++) {
		
		String fqlQuery = "select uid, name, birthday, education, work, pic_big from user where uid = " + uids.get(i);

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
						JSONObject info = data.getJSONObject(0);
						Log.i(TAG, data.toString());
						
						FamilyMember f = new FamilyMember(info.getString("uid"), info.getString("name"), info.getString("birthday"),
								info.getString("pic_big"));
						
						Log.i(TAG, "SUCCESS");
						
						Log.i(TAG, f.toString());
						
						family.add(f);
						
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
	
	private List<FamilyMember> getChoices() {
		
		List<String> uids = new ArrayList<String>();
		List<FamilyMember> family_choices = new ArrayList<FamilyMember>();

		int family_size = family.size();
		
		Log.d(TAG, "size of family "+ family_size);
		
		int i = 0;
		while (i < 4) {
			int rand = (int) Math.random() * family_size;
			Log.d(TAG, family.get(rand).toString());
			if (family.get(rand).getBirthday() != null && !family_choices.contains(family.get(rand))) {
				family_choices.add(family.get(rand));
				i++;	
			}	
		}
		
		return family_choices;
		
	}
	

	
	public void quiz(int question_num) {
		
		if (question_num != maxQuestions +1) {
			
			
			List<FamilyMember> choices = getChoices();
			int answer = (int) Math.random() * 4;
			
			setupQuestion(choices.get(0), choices.get(1), choices.get(2), choices.get(3), choices.get(answer));

			currentQuestion = question_num;
		} else {
			Intent intent  = new Intent(this, QuizResultsActivity.class);
			intent.putExtra("NumQuestions", maxQuestions);
			intent.putExtra("Correct", correct);
			startActivity(intent);
		}
		
		
	}
	
	public void setupQuestion(FamilyMember answer_a, FamilyMember answer_b, FamilyMember answer_c,
			FamilyMember answer_d, FamilyMember correct) {
		setTitle("Question " + questionNumber);
		
		TextView question = (TextView) findViewById(R.id.question);

		question.setText("When is " + correct.getName() + "'s birthday");

		
		
		ImageView quiz_picture = (ImageView) this.findViewById(R.id.quiz_picture);
		quiz_picture.setBackgroundResource(R.drawable.andrew);
		
		Button option_1 = (Button) this.findViewById(R.id.option_1);
		option_1.setText(answer_a.getBirthday());
		this.choice_a = answer_a.getUid();
		Button option_2 = (Button) this.findViewById(R.id.option_2);
		option_2.setText(answer_b.getBirthday());
		this.choice_b = answer_b.getUid();
		Button option_3 = (Button) this.findViewById(R.id.option_3);
		option_3.setText(answer_c.getBirthday());
		this.choice_c = answer_c.getUid();
		Button option_4 = (Button) this.findViewById(R.id.option_4);
		option_4.setText(answer_d.getBirthday());
		this.choice_d = answer_d.getUid();
		

		option_1.setOnClickListener(this);

		option_2.setOnClickListener(this);

		option_3.setOnClickListener(this);
		
		option_4.setOnClickListener(this); 
		
		correctAnswerUID = correct.getUid();
		
		
	}
	
	public void onClick(View view) {
		
		String response = null;
		
		if (view.getId() == R.id.option_1) {
			response = choice_a;
		} else if (view.getId() == R.id.option_2) {
			response = choice_b;
		} else if (view.getId() == R.id.option_3) {
			response = choice_c;
		} else if (view.getId() == R.id.option_4) {
			response = choice_d;
		}
		
		if (response != correctAnswerUID) {
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

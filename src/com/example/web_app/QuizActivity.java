package com.example.web_app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
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

public class QuizActivity extends Activity implements OnClickListener, RequestHandler {
	
	private String correctAnswer;
	private int maxQuestions;
	private int currentQuestion;
	private int correct;
	private int questionNumber;
	private String choice_a;
	private String choice_b;
	private String choice_c;
	private String choice_d;
	private int currentId;
	private List<ArrayList<String>> user_photos;
	
	SharedPreferences pref;
	String currUser;
	
	private static final List<String> PERMISSIONS = Arrays.asList("friends_birthday", "user_photos", "friends_photos",
										"read_friendlists", "user_relationships", "hometown_location", "friends_work_history");
	
	private String TAG = "QuizActivity";
	
	private String[] uids;
	private List<FamilyMember> family;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		
		hideButtons();
		
		setTitle("Quiz");
		pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
		currUser = pref.getString("currUser", null);
		
		Session session1 = new Session(this);
		session1.openForRead(new Session.OpenRequest(this));

		session1.requestNewReadPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS));
		
		Session.setActiveSession(session1);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		family = new ArrayList<FamilyMember>();

		
		Intent intent = getIntent();
		maxQuestions = intent.getIntExtra("num_questions", 1);
		
		
		correct = 0;
		currentQuestion = 1;
		
		ServerRequest s = new ServerRequest(this);
		s.fetchIDs(currUser);
		
	}
	
	public void quiz(int question_num) throws MalformedURLException, IOException {
		
		if (question_num != maxQuestions +1) {
			
			//int questionType = 4;
			Random r = new Random();
			int questionType=r.nextInt(7-0) + 0;
			
		
			
			if (questionType == 0) {
				birthdayQuestion();
				
			} else if (questionType > 0 && questionType < 4) {
				nameImageQuestion();
				
			} else if (questionType == 4) {
				
				hometownQuestion();
				
			}  else if (questionType == 5 && enoughSchoolData()) {
				
				schoolQuestion();
	
			} else if (questionType == 6 && enoughWorkData()) {
				
				workQuestion();
			} else {
				quiz(currentQuestion);

			}

		} else {
			Intent intent  = new Intent(this, QuizResultsActivity.class);
			intent.putExtra("NumQuestions", maxQuestions);
			intent.putExtra("Correct", correct);
			startActivity(intent);
		}
		
		
	}
	
	private boolean enoughWorkData() {
		int i = 0;
		
		for (FamilyMember f: family) {
			if (f.getWork_list() != null) {
				i++;
			}
		}
		
		if (i < 4) {
			return false;
		}
		return true;
	}
	
	private boolean enoughSchoolData() {
		int i = 0;
		
		for (FamilyMember f: family) {
			if (f.getSchool_list() != null) {
				i++;
			}
		}
		
		if (i < 4) {
			return false;
		}
		return true;
	}
	
	
	private void birthdayQuestion() {
	
		FamilyMember[] family_members = new FamilyMember[4];
		List<String> seen_uids = new ArrayList<String>();
		int i = 0;
		
		
		ImageView quiz_picture = (ImageView) this.findViewById(R.id.quiz_picture);
		quiz_picture.setImageResource(R.drawable.bow_tie);
		
		Log.i(TAG, "family size is " + family.size());
		
		while (i < 4) {
			
			Random r = new Random();
			int rand=r.nextInt(family.size()-0) + 0;
			
			FamilyMember f = family.get(rand);
			
			if (f.getBirthday() != null && !seen_uids.contains(f.getUid())) {
				Log.i(TAG, "choice " + (i+1) + " is" + family.get(rand).getName());
				family_members[i] = f;
				seen_uids.add(f.getUid());
				i++;
			}	
		}
		
		Random r = new Random();
		int correct=r.nextInt(3-0) + 0;
		
		TextView question = (TextView) findViewById(R.id.question);
		question.setText("Whose birthday is on " + family_members[correct].getBirthday() + "?");

		setupQuestion(family_members[0].getName(), family_members[1].getName(),
				family_members[2].getName(), family_members[3].getName(), family_members[correct].getName());

	}
	
	private void schoolQuestion() {
		
		FamilyMember[] family_members = new FamilyMember[4];
		List<String> seen_uids = new ArrayList<String>();
		int i = 0;

		ImageView quiz_picture = (ImageView) this.findViewById(R.id.quiz_picture);
		quiz_picture.setImageResource(R.drawable.bow_tie);
		
		Log.i(TAG, "family size is " + family.size());
		
		while (i < 4) {
			
			Random r = new Random();
			int rand=r.nextInt(family.size()-0) + 0;
			
			FamilyMember f = family.get(rand);
			
			if (f.getSchool_list() != null && !f.getSchool_list().isEmpty() && !seen_uids.contains(f.getUid())) {
				Log.i(TAG, "choice " + (i+1) + " is" + family.get(rand).getName());
				family_members[i] = f;
				seen_uids.add(f.getUid());
				i++;
			}	
		}
		
		Random r = new Random();
		int correct=r.nextInt(3-0) + 0;
		
		TextView question = (TextView) findViewById(R.id.question);
		
		int school_index = r.nextInt(family_members[correct].getSchool_list().size()-0) +0;
		
		question.setText("Who attended " + family_members[correct].getSchool_list().get(school_index) + "?");

		setupQuestion(family_members[0].getName(), family_members[1].getName(),
				family_members[2].getName(), family_members[3].getName(), family_members[correct].getName());

	}
	
	private void workQuestion() {
		
		FamilyMember[] family_members = new FamilyMember[4];
		List<String> seen_uids = new ArrayList<String>();
		int i = 0;

		ImageView quiz_picture = (ImageView) this.findViewById(R.id.quiz_picture);
		quiz_picture.setImageResource(R.drawable.bow_tie);
		
		Log.i(TAG, "family size is " + family.size());
		
		while (i < 4) {
			
			Random r = new Random();
			int rand=r.nextInt(family.size()-0) + 0;
			
			FamilyMember f = family.get(rand);
			
			if (f.getWork_list() != null && !f.getWork_list().isEmpty() && !seen_uids.contains(f.getUid())) {
				Log.i(TAG, "choice " + (i+1) + " is" + family.get(rand).getName());
				family_members[i] = f;
				seen_uids.add(f.getUid());
				i++;
			}	
		}
		
		Random r = new Random();
		int correct=r.nextInt(3-0) + 0;
		
		TextView question = (TextView) findViewById(R.id.question);
		
		int work_index = r.nextInt(family_members[correct].getSchool_list().size()-0) +0;
		
		question.setText("Who worked at " + family_members[correct].getWork_list().get(work_index) + "?");

		setupQuestion(family_members[0].getName(), family_members[1].getName(),
				family_members[2].getName(), family_members[3].getName(), family_members[correct].getName());

	}
	

	
	private void nameImageQuestion() throws MalformedURLException, IOException {
		
		FamilyMember[] family_members = new FamilyMember[4];
		List<String> seen_uids = new ArrayList<String>();
		int i = 0;
		
		Log.i(TAG, "family size is " + family.size());
		
		while (i < 4) {
			
			Random r = new Random();
			int rand=r.nextInt(family.size()-0) + 0;
			
			FamilyMember f = family.get(rand);
			
			if (!seen_uids.contains(f.getUid())) {
				Log.i(TAG, "choice " + (i+1) + " is" + family.get(rand).getName());
				family_members[i] = f;
				seen_uids.add(f.getUid());
				i++;
			}	
		}
		
		Random r = new Random();
		int correct=r.nextInt(3-0) + 0;
		
		TextView question = (TextView) findViewById(R.id.question);
		question.setText("Who is in this photo?");
		Log.i(TAG, "correct is " + family_members[correct].getName());


		setupQuestion(family_members[0].getName(), family_members[1].getName(),
				family_members[2].getName(), family_members[3].getName(), family_members[correct].getName());
		
		Drawable d = drawable_from_url(family_members[correct].getRandomURL(), "src");
		
		ImageView quiz_picture = (ImageView) this.findViewById(R.id.quiz_picture);
		quiz_picture.setImageDrawable(d);
		
	}
	
	
	private void hometownQuestion() {
		
		FamilyMember[] family_members = new FamilyMember[4];
		List<String> seen_uids = new ArrayList<String>();
		int i = 0;
		
		
		ImageView quiz_picture = (ImageView) this.findViewById(R.id.quiz_picture);
		quiz_picture.setImageResource(R.drawable.bow_tie);
		
		Log.i(TAG, "family size is " + family.size());
		
		while (i < 4) {
			
			Random r = new Random();
			int rand=r.nextInt(family.size()-0) + 0;
			
			FamilyMember f = family.get(rand);
			
			if (f.getHometown() != null && !seen_uids.contains(f.getUid())) {
				Log.i(TAG, "choice " + (i+1) + " is" + family.get(rand).getName());
				family_members[i] = f;
				seen_uids.add(f.getUid());
				i++;
			}	
		}
		
		Random r = new Random();
		int correct=r.nextInt(3-0) + 0;
		
		TextView question = (TextView) findViewById(R.id.question);
		question.setText("Whose hometown is " + family_members[correct].getHometown() + "?");
		
		Log.i(TAG, "correct is " + family_members[correct].getName());

		setupQuestion(family_members[0].getName(), family_members[1].getName(),
				family_members[2].getName(), family_members[3].getName(), family_members[correct].getName());
		
		
	}
	

	public void hideButtons() {
		Button option_1 = (Button) this.findViewById(R.id.option_1);
		option_1.setVisibility(View.INVISIBLE);

		Button option_2 = (Button) this.findViewById(R.id.option_2);
		option_2.setVisibility(View.INVISIBLE);

		
		Button option_3 = (Button) this.findViewById(R.id.option_3);
		option_3.setVisibility(View.INVISIBLE);

		
		Button option_4 = (Button) this.findViewById(R.id.option_4);
		option_4.setVisibility(View.INVISIBLE);

		
	}
	
	public void showButtons() {
		Button option_1 = (Button) this.findViewById(R.id.option_1);
		option_1.setVisibility(View.VISIBLE);

		Button option_2 = (Button) this.findViewById(R.id.option_2);
		option_2.setVisibility(View.VISIBLE);

		
		Button option_3 = (Button) this.findViewById(R.id.option_3);
		option_3.setVisibility(View.VISIBLE);

		
		Button option_4 = (Button) this.findViewById(R.id.option_4);
		option_4.setVisibility(View.VISIBLE);
		
	}
	
	
	public void setupQuestion(String answer_a, String answer_b, String answer_c,
			String answer_d, String correct) {
		setTitle("Question " + currentQuestion);	
		
		
		Log.i(TAG, "starting setting options");
		
		Button option_1 = (Button) this.findViewById(R.id.option_1);
		option_1.setText(answer_a);
		this.choice_a = answer_a;
		Log.i(TAG, "set answer a: " + answer_a);
		
		Button option_2 = (Button) this.findViewById(R.id.option_2);
		option_2.setText(answer_b);
		this.choice_b = answer_b;
		Log.i(TAG, "set answer b: " + answer_b);
		
		Button option_3 = (Button) this.findViewById(R.id.option_3);
		option_3.setText(answer_c);
		this.choice_c = answer_c;
		Log.i(TAG, "set answer c: " + answer_c);
		
		Button option_4 = (Button) this.findViewById(R.id.option_4);
		option_4.setText(answer_d);
		this.choice_d = answer_d;
		Log.i(TAG, "set answer d: " + answer_d);
		

		option_1.setOnClickListener(this);

		option_2.setOnClickListener(this);

		option_3.setOnClickListener(this);
		
		option_4.setOnClickListener(this); 
		
		correctAnswer = correct;
		
		
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
		
		if (response != correctAnswer) {
			Toast.makeText(getApplicationContext(), "Incorrect Answer, the answer was " + correctAnswer, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(), "Correct Answer", Toast.LENGTH_SHORT).show();
			correct++;
		} 
		
		currentQuestion++;
		try {
			quiz(currentQuestion);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	finishActivity(0);
	    }
	    return super.onKeyDown(keyCode, event);
	}


	
	@Override
	public void doOnRequestComplete(String s) {
		Log.i("QuizActivity", "Retrived family from server ok");
		uids = s.split(":");
		
		getFamilyData();

	final Handler handler = new Handler();
	handler.postDelayed(new Runnable() {
		@Override
		public void run() {

				getFamilyPhotos();

	  }
	}, 3000);
	
	final Handler handler1 = new Handler();
	handler1.postDelayed(new Runnable() {
		@Override
		public void run() {
  	  		try {
  	  			showButtons();
  	  			
  	  			
				quiz(1);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
	}, 6000);
	
	
	}

			       

	
	
	private void getFamilyData() {

		for (int i = 0; i < uids.length; i++) {
		
		String fqlQuery = "select uid, name, birthday, education, work, pic_big, hometown_location from user where uid = " + uids[i];

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
			   //     Log.i(TAG, "Got results: " + response.toString());
			        GraphObject g = response.getGraphObject();
			        
			        
			       	JSONObject j = g.getInnerJSONObject();
			       	try {
						JSONArray data = j.getJSONArray("data");
						JSONObject info = data.getJSONObject(0);
						

						JSONArray education;
						List<String> schools_list = new ArrayList<String>();
											
						
						if (!info.getString("education").equals("null")) {
							education = info.getJSONArray("education");
						
							for (int i = 0; i < education.length(); i++) {
								JSONObject result = education.getJSONObject(i);
	
								JSONObject school = result.getJSONObject("school");
								String school_name = school.getString("name");
								schools_list.add(school_name);
							}
						}

						JSONArray work;
						List<String> work_list = new ArrayList<String>();
						
						
						if (!info.getString("work").equals("null")) {
							
							work = info.getJSONArray("work");
						//	Log.i(TAG, work.toString());
							
							for (int i = 0; i < work.length(); i++) {
								JSONObject result = work.getJSONObject(i);
								
								JSONObject workplace = result.getJSONObject("employer");
								String workplace_name = workplace.getString("name");
								work_list.add(workplace_name);
								Log.i(TAG, "adding " + workplace_name + " for " + info.getString("name"));
							}
						} 
						
						String hometown = null;
						if (!info.getString("hometown_location").equals("null")) {
							JSONObject hometown_object = info.getJSONObject("hometown_location");
							hometown = hometown_object.getString("name");

	
						}
						
						FamilyMember f = new FamilyMember(info.getString("uid"), info.getString("name"), info.getString("birthday"),
								info.getString("pic_big"), hometown, schools_list, work_list);
						

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
	
	private void getFamilyPhotos() {

		
		user_photos = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < uids.length; i++) {
			
		
		String fqlQuery = "SELECT owner, src_big FROM photo WHERE aid IN (SELECT aid FROM album WHERE owner = " +
				uids[i] + " AND type = 'profile')";

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
			 //       Log.i(TAG, "Got results: " + response.toString());

			       	try {
			       		
			       		List<String> urls = new ArrayList<String>();
			        	JSONArray photos = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
			        	String uid = photos.getJSONObject(0).getString("owner");
			        	
			        	for (int i = 0; i < photos.length(); i++) {


							String url = photos.getJSONObject(i).getString("src_big");
						//	Log.i(TAG, photos.getJSONObject(i).getString("src_big"));
							urls.add(url);
						
			        	}

						addPhotosToUser(urls, uid);


						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

			       
				}
			       

		        });
		Request.executeBatchAsync(request);
		}
		        
		}
	
	private void addPhotosToUser(List<String> urls, String uid) {
		
		
		for (FamilyMember f : family) {
			if (f.getUid().equals(uid)) {
				f.setPicture_urls(urls);
			}
			
		}
		
	}
	
	private Drawable drawable_from_url(String url, String src_name) throws 
	   java.net.MalformedURLException, java.io.IOException {
		
	   return Drawable.createFromStream(((java.io.InputStream)
	      new java.net.URL(url).getContent()), src_name);
	}

}

package com.example.web_app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class MyFamilyActivity extends Activity implements RequestHandler {
	
	private static final List<String> PERMISSIONS = Arrays.asList("friends_birthday", "user_photos", "friends_photos", "read_friendlists", "user_relationships");
	private String[] ids;
	
	private String TAG = "MyFamilyActivity";
	List<Drawable> profile_pics;
	
	SharedPreferences pref;
	String currUser;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_layout);
		setTitle("Your Family");
		profile_pics = new ArrayList<Drawable>();
		
		
		pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
		currUser = pref.getString("currUser", null);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	
		
		
		Session session1 = new Session(this);
		session1.openForRead(new Session.OpenRequest(this));

		session1.requestNewReadPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS));
			
			
		Session.setActiveSession(session1);
		
		ServerRequest s = new ServerRequest(this);
		s.fetchIDs(currUser);

	    
	}

	
	private void getPhotos() {

		for (int i = 0; i < 5; i++) {
		
		String fqlQuery = "select pic_big from user where uid = " + ids[i];

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

			       JSONArray  photo = null;
			        
					try {
						 photo = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						//Log.i(TAG, photos.getJSONObject(i).getString("src"));
						String url = null;
						try {
							Log.i(TAG, photo.getString(0).toString());
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							url = photo.getJSONObject(0).getString("pic_big");

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Drawable d = null;
						try {
							d = drawable_from_url(url, "img");
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if (d == null) {
							Log.i(TAG, "no drawable");
						} else {
						
							profile_pics.add(d);
					} 
					
				}

		        });
		Request.executeBatchAsync(request);
		        }
		        

		}
	
	@SuppressWarnings("unused")
	private Drawable drawable_from_url(String url, String src_name) throws 
	   java.net.MalformedURLException, java.io.IOException {
		
	   return Drawable.createFromStream(((java.io.InputStream)
	      new java.net.URL(url).getContent()), src_name);
	}
	
	
	public void selectFamilyMember(int position, GridView view) {
		Intent intent = new Intent(this, FamilyMemberActivity.class);
		intent.putExtra("fb_id", ids[(position + 1) % ids.length]);
		startActivity(intent);
	}


	@Override
	public void doOnRequestComplete(String s) {
		ids = s.split(":");
		
		Session session1 = Session.getActiveSession();
		if (session1 != null && session1.isOpened()) {
			Log.i(TAG, "session ok");
			getPhotos();

		} else {
			Log.d(TAG, "no session");
		}
		
		
		final GridView gridView = (GridView) findViewById(R.id.grid_view);

		gridView.setAdapter(new ImageAdapter(this, profile_pics));


	    gridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	           selectFamilyMember(position, gridView);
	        }
	    });
		
	}

}
	


	


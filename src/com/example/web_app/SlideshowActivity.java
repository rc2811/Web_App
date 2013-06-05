package com.example.web_app;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SlideshowActivity extends Activity {
	
	Session session;
	String TAG = "Slideshow";
	private String defaultValue = "1";
	private static final List<String> PERMISSIONS = Arrays.asList("user_birthday");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slideshow);
		session = Session.getActiveSession();
		
		Log.i(TAG, session.getPermissions().toString());
		
	
	//	private static final int REAUTH_ACTIVITY_CODE = 100;
		// Check for publish permissions    
		List<String> permissions = session.getPermissions();
		

		  
		   session.requestNewReadPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS));
		   session.openForRead(new Session.OpenRequest(this));

		
		
		if (session != null && session.isOpened()) {
			Log.i(TAG, "session ok");
		//	getUserData(session);
		} else {
			Log.d(TAG, "no session");
		}
		
		Intent intent = getIntent();
		String id = intent.getStringExtra("family");
		Log.i(TAG, id + "");
		
		getUserData(id);
		
	}
	
	private void getUserData(String id) {
		
		String fqlQuery = "select uid, name, birthday from user where uid = " + id;
		//		"access_token = " + session.getAccessToken();
		Bundle params = new Bundle();
		params.putString("q", fqlQuery);

		Session session = Session.getActiveSession();
		Request request = new Request(session, 
		    "/fql", 
		    params, 
		    HttpMethod.GET, 
		    new Request.Callback(){ 
		        public void onCompleted(Response response) {
		        Log.i(TAG, "Got results: " + response.toString());
		    }
		});
		Request.executeBatchAsync(request);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.slideshow, menu);
		return true;
	}
	
	
	

	
	 /*private void getUserData(final Session session){
	    Request request = Request.newMeRequest(session, 
	        new Request.GraphUserCallback() {

			@Override
			public void onCompleted(GraphUser user, Response response) {
	            if(user != null && session == Session.getActiveSession()){
	                //pictureView.setProfileId(user.getId());
	               // userName.setText(user.getName());
	                getFriends();

	            }
	            if(response.getError() !=null){

	            }
				
			}
	    });
	    request.executeAsync();
	}
	
	private void getFriends(){
	    Session activeSession = Session.getActiveSession();
	    if(activeSession.getState().isOpened()){
	        Request friendRequest = Request.newMyFriendsRequest(activeSession, 
	            new GraphUserListCallback(){

					@Override
					public void onCompleted(List<GraphUser> users,
							Response response) {
	                    Log.d("INFO", response.toString());
                    	TableLayout tableLayout = (TableLayout) findViewById(R.layout.activity_slideshow);
                    	
	                    for (int i = 0; i < users.size(); i++) {
	                   // 	if (users.get(i).getFirstName() != null && users.get(i).getLastName() != null) {
	                    //		textView.setText(users.get(i).getFirstName() + " " + users.get(i).getLastName());
	                   // 		tableLayout.addView(textView);
	                   // 		Toast.makeText(getApplicationContext, text, duration)
	                    	Log.i(TAG, "" + users.get(i).getFirstName());
	                    	
	                    } 
						
					}
	        });
	        Bundle params = new Bundle();
	        params.putString("fields", "id, name, picture");
	        friendRequest.setParameters(params);
	        friendRequest.executeAsync();


	    }
	} */
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
}

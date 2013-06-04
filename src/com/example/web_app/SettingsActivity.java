package com.example.web_app;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.facebook.FacebookException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class SettingsActivity extends FragmentActivity {
	
	
	private Fragment mainFragment;
	private static final String TAG = "SettingsFragment";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("settings", "activity starting");
		setContentView(R.layout.activity_settings);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		
	    LoginButton authButton = (LoginButton) findViewById(R.id.facebookAuthButton);
	    authButton.setReadPermissions(Arrays.asList("user_location", "user_birthday", "user_likes", "user_name",
	    		"basic_info","email", "user_relationships", "friends_relationships", "read_friendlists"));
	    authButton.setOnErrorListener(new OnErrorListener() {
	     
	     @Override
	     public void onError(FacebookException error) {
	      Log.i(TAG, "Error " + error.getMessage());
	     }
	    });
	    
	    // session state call back event
	    authButton.setSessionStatusCallback(new Session.StatusCallback() {
	     
	     @Override
	     public void call(Session session, SessionState state, Exception exception) {
	      
	    	 if (session.isOpened()) {

	    		 Bundle params = new Bundle();
	    		 params.putString("fields", "members");
	    		 Request req = new Request(session, "/me/friendlists/family", params, HttpMethod.GET, new Request.Callback() {
	    		     @Override
	    		     public void onCompleted(Response response) {
	    		         Log.i(TAG, response.toString());
	    		         GraphObject g = response.getGraphObject();
	    		         if (g == null) {
	    		        	 Log.i(TAG, g.toString());
	    		         } else {
	    		        	 Log.i(TAG, "JSON" + g.getInnerJSONObject().toString());
	    		         }
	    		     }
	    		 
	    		 });

	    		 
	    		 req.executeAsync();
	    		 
	    		 
	    	 }
	    	 
	
	   }
	    });
	}
	    	 
	  	   /* 		 Log.i(TAG,"Access Token"+ session.getAccessToken());
    		 Request.executeMeRequestAsync(session,
    				 new Request.GraphUserCallback() {
       	                     @Override
       	                     public void onCompleted(GraphUser user,Response response) {
       	                    	 if (user != null) { 
       	                    		 Log.i(TAG,"User ID "+ user.getId());
       	                    		 Log.i(TAG,"Email "+ user.asMap().get("email"));
       	                    		 
                                }
                            }
    		 		 }); */

	    	 
	  	   /* 		 Request.executeMyFriendsRequestAsync(session,
			 new Request.GraphUserListCallback() {
		 			@Override
		 			public void onCompleted(List<GraphUser> users, Response response) {
		 				for (int i = 0; i < users.size(); i++) {
		 					users.get(i).
		 					Log.i(TAG, users.get(i).getName() + " ");
		 				}
			
		}
	}); 
	
	
	*/
	    

	   @Override
	   public void onActivityResult(int requestCode, int resultCode, Intent data) {
	       super.onActivityResult(requestCode, resultCode, data);
	       Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	   }

}

    

    






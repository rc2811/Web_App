package com.example.web_app;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import com.facebook.*;
import com.facebook.model.*;
import com.facebook.Request;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener;
import com.google.android.gms.plus.PlusClient.OnPersonLoadedListener;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.Person.Name;
import com.google.android.gms.plus.model.people.PersonBuffer;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ShareCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends FragmentActivity {
	
	
	private Fragment mainFragment;
	private static final String TAG = "SettingsFragment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("settings", "activity starting");
		setContentView(R.layout.activity_settings);
		
	    LoginButton authButton = (LoginButton) findViewById(R.id.facebookAuthButton);
	    authButton.setOnErrorListener(new OnErrorListener() {
	     
	     @Override
	     public void onError(FacebookException error) {
	      Log.i(TAG, "Error " + error.getMessage());
	     }
	    });
	    
	 // set permission list, Don't foeget to add email
	    authButton.setReadPermissions(Arrays.asList("basic_info","email"));
	    // session state call back event
	    authButton.setSessionStatusCallback(new Session.StatusCallback() {
	     
	     @Override
	     public void call(Session session, SessionState state, Exception exception) {
	      
	    	 if (session.isOpened()) {
	    		 Log.i(TAG,"Access Token"+ session.getAccessToken());
	    		 Request.executeMeRequestAsync(session,
	    				 new Request.GraphUserCallback() {
	       	                     @Override
	       	                     public void onCompleted(GraphUser user,Response response) {
	       	                    	 if (user != null) { 
	       	                    		 Log.i(TAG,"User ID "+ user.getId());
	       	                    		 Log.i(TAG,"Email "+ user.asMap().get("email"));
	       	                    		 
	                                }
	                            }
	    		 		 });
	    	 }
	      
	     }
	    });
	   }

	   @Override
	   public void onActivityResult(int requestCode, int resultCode, Intent data) {
	       super.onActivityResult(requestCode, resultCode, data);
	       Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	   }

}

    

    






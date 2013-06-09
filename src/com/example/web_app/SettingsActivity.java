package com.example.web_app;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class SettingsActivity extends FragmentActivity {
	
	private static final int SPLASH = 0;
	private static final int SELECTION = 1;
	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
	private boolean isResumed = false;
	private static final int SETTINGS = 2;
	private static final int FRAGMENT_COUNT = SETTINGS + 1;
	private MenuItem settings;
	
	private static final String TAG = "SettingsFragment";
	
	private static final List<String> PERMISSIONS = Arrays.asList("friends_birthday", "user_photos", "friends_photos"
			,"friends_education_history, about_me", "read_friendlists");

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		setContentView(R.layout.activity_settings);
		
	    FragmentManager fm = getSupportFragmentManager();
	    fragments[SPLASH] = fm.findFragmentById(R.id.splashFragment);
	    fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
	    fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);
	    
	    Intent intent = getIntent();
	    int n = intent.getIntExtra("num_selections", 0);
	    
	    if (n != 0) {
	    	Toast.makeText(getApplicationContext(), "Selected " + n + " family members", Toast.LENGTH_SHORT).show();
	    }

	    FragmentTransaction transaction = fm.beginTransaction();
	    for(int i = 0; i < fragments.length; i++) {
	        transaction.hide(fragments[i]);
	    }
	    transaction.commit();
	}
	
	private void showFragment(int fragmentIndex, boolean addToBackStack) {
	    FragmentManager fm = getSupportFragmentManager();
	    FragmentTransaction transaction = fm.beginTransaction();
	    for (int i = 0; i < fragments.length; i++) {
	        if (i == fragmentIndex) {
	            transaction.show(fragments[i]);
	        } else {
	            transaction.hide(fragments[i]);
	        }
	    }
	    if (addToBackStack) {
	        transaction.addToBackStack(null);
	    }
	    transaction.commit();
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	    isResumed = true;
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
  
	    isResumed = false;
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    // Only make changes if the activity is visible
	    if (isResumed) {

	        FragmentManager manager = getSupportFragmentManager();
	        // Get the number of entries in the back stack
	        int backStackSize = manager.getBackStackEntryCount();
	        // Clear the back stack
	        for (int i = 0; i < backStackSize; i++) {
	            manager.popBackStack();
	        }
	        if (state.isOpened()) {
	            // If the session state is open:
	            // Show the authenticated fragment
	            showFragment(SELECTION, false);
	        } else if (state.isClosed()) {
	            // If the session state is closed:
	            // Show the login fragment
	            showFragment(SPLASH, false);
	        }
	    }
	}
	
	@Override
	protected void onResumeFragments() {
	    super.onResumeFragments();
	    Session session = Session.getActiveSession();

	    if (session != null && session.isOpened()) {
	        // if the session is already open,
	        // try to show the selection fragment
	        showFragment(SELECTION, false);
	    } else {
	        // otherwise present the splash screen
	        // and ask the person to login.
	        showFragment(SPLASH, false);
	    }
	}
	
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = 
	    new Session.StatusCallback() {
	    @Override
	    public void call(Session session, 
	            SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    // only add the menu when the selection fragment is showing
	    if (fragments[SELECTION].isVisible()) {
	        if (menu.size() == 0) {
	            settings = menu.add(R.string.settings);
	        }
	        return true;
	    } else {
	        menu.clear();
	        settings = null;
	    }
	    return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.equals(settings)) {
	        showFragment(SETTINGS, true);
	        return true;
	    }
	    return false;
	}
	
	public void getFamily(View view) {
			
		Session session = Session.getActiveSession();
		
		
		String fqlQuery = "select name, uid from family where profile_id = me()";
		//		"access_token = " + session.getAccessToken();
		Bundle params = new Bundle();
		params.putString("q", fqlQuery);

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
	
	
    public void family_manual(View view) {
    	
	     Intent intent = new Intent();
	     intent.setData(PickerActivity.FRIEND_PICKER);
	     intent.setClass(this, PickerActivity.class);
	     startActivityForResult(intent, 0);

   }
    
    public void family_auto(View view) {
    	
    	Session session = Session.getActiveSession();
    		
    	String fqlQuery = "select name, flid from friendlist where owner = me()";
    	//		"access_token = " + session.getAccessToken();
    	Bundle params = new Bundle();
    	params.putString("q", fqlQuery);

    	Request request = new Request(session, 
    		   "/fql", 
    		   params, 
    		   HttpMethod.GET, 
    		   new Request.Callback(){ 
    		    public void onCompleted(Response response) {
    			   	Log.i(TAG, "Got results: " + response.toString());
    			   	GraphObject g = response.getGraphObject();
    			   	JSONObject j = g.getInnerJSONObject();
    			   	try {
						JSONArray data = (JSONArray) j.get("data");
						JSONObject o = data.getJSONObject(0);
						Log.i(TAG, data.length() +"");
						
	    			   	Log.i(TAG, o.toString());
						String flid = null;
						for (int i = 0; i < data.length(); i++) {

		    			   	JSONObject list = data.getJSONObject(i);
		    			   	

		    			   	Log.i("ok", 		    			   	list.getString("name"));
		    			   	
							if (list.getString("name").equals("Family")) {

								flid = (String) list.get("flid");
								break;
							}
						}
						if (flid != null) {
							family_auto_helper(flid);
							
						}
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    		    }
    		});
    		
    	Request.executeBatchAsync(request);
    			
    	}
    
    
    public void family_auto_helper(String flid) {
    		
    	Session session = Session.getActiveSession();
		
    	String fqlQuery = "select uid from friendlist_member where flid = " + flid;
    	//		"access_token = " + session.getAccessToken();
    	Bundle params = new Bundle();
    	params.putString("q", fqlQuery);

    	Request request = new Request(session, 
    		   "/fql", 
    		   params, 
    		   HttpMethod.GET, 
    		   new Request.Callback(){ 
    		    public void onCompleted(Response response) {
    			   	Log.i(TAG, "Got results: " + response.toString());
    			   	GraphObject g = response.getGraphObject();
    			   	JSONObject j = g.getInnerJSONObject();
    			   	try {
						JSONArray data = (JSONArray) j.get("data");
						
						
						
						
						Toast.makeText(getApplicationContext(), "Found " + data.length() + " family members", Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    		    }
    		});
    		
    	Request.executeBatchAsync(request);
    			
    	}
    
   

    	
    }



	
	
	
	     
	    
	   



    

    






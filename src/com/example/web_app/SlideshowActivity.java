package com.example.web_app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.FacebookError;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SlideshowActivity extends Activity implements RequestHandler {
	
	Session session;
	String TAG = "Slideshow";
	private String defaultValue = "1";
	private static final List<String> PERMISSIONS = Arrays.asList("friends_birthday", "user_photos", "friends_photos");
	private String[] ids;
	private Handler handler;
	SharedPreferences pref;
	String currUser;
	
	private List<String> urls;
	
	

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
		currUser = pref.getString("currUser", null);
		
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_slideshow);
		session = Session.getActiveSession();
		
		urls = new ArrayList<String>();
		
		Session session1 = new Session(this);
		session1.openForRead(new Session.OpenRequest(this));

		session1.requestNewReadPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS));
			
			
		Session.setActiveSession(session1);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		
		ServerRequest s = new ServerRequest(this);
		s.fetchIDs(currUser);
		

		
	}
	
	@Override
	protected void onDestroy() {
	      super.onDestroy();
	      handler.removeCallbacks(sendData);
	}

	private void slideshow() throws InterruptedException {
		
	      handler.post(sendData);
	}
	
	private final Runnable sendData = new Runnable(){
	    public void run(){
	        try {

				changePhotos();
	            handler.postDelayed(this, 5000);    
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }   
	    }
	};

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    super.onKeyDown(keyCode, event);
        switch(keyCode)
        {
        case KeyEvent.KEYCODE_BACK:
  	      	handler.removeCallbacks(sendData);
        	Intent intent = new Intent(this, HomeScreenActivity.class);
        	startActivity(intent);
         
            return true;
        }
        return false;
	}
	
	private void changePhotos() throws MalformedURLException, IOException {
		
		Log.i(TAG, "there are " + urls.size() + " urls");
		
		Random r = new Random();
		int rand=r.nextInt(urls.size()-0) + 0;
		
		Log.i(TAG, "using url " + rand);
				
		Drawable d = drawable_from_url(urls.get(rand), "img");
		//Log.i(TAG, "using url " + urls.get(rand));
		
		if (d == null) {
			Log.i(TAG, "no drawable");
		}
		
		ImageView imageView = (ImageView) findViewById(R.id.slideshow_image);
		imageView.setImageDrawable(d);
	}
	

	
	@SuppressLint("NewApi")
	private void getPhotos() {

		for (int j = 0; j < ids.length; j++) {
			
		
		String id = ids[j];
		String fqlQuery = "select src_big from photo where owner = " + id;


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
		        try {
		        	JSONArray photos = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
		        	for (int i = 0; i < photos.length(); i++) {


						String url = photos.getJSONObject(i).getString("src_big");
					//	Log.i(TAG, photos.getJSONObject(i).getString("src_big"));
						urls.add(url);
					
		        	}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
	
		        }
		        }
		});
		Request.executeBatchAsync(request);
		}
		
	}

	private Drawable drawable_from_url(String url, String src_name) throws 
	   java.net.MalformedURLException, java.io.IOException {
		
	   return Drawable.createFromStream(((java.io.InputStream)
	      new java.net.URL(url).getContent()), src_name);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.slideshow, menu);
		return true;
	}

	@Override
	public void doOnRequestComplete(String s) {
		ids = s.split(":");
		Log.i(TAG, ids.toString());
		
		this.handler = new Handler();
		
		
		getPhotos();
		
		 Thread thread=  new Thread(){
		        @Override
		        public void run(){
		            try {
		                synchronized(this){
		                    wait(1000);
		        			Log.i(TAG, "starting slideshow");
		        			slideshow();
		                }
		            }
		            catch(InterruptedException ex){                    
		            }

		            // TODO              
		        }
		    };

		    thread.start();      
		
	
		
	}
	
}

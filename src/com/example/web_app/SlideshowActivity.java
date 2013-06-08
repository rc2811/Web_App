package com.example.web_app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SlideshowActivity extends Activity {
	
	Session session;
	String TAG = "Slideshow";
	private String defaultValue = "1";
	private static final List<String> PERMISSIONS = Arrays.asList("friends_birthday", "user_photos", "friends_photos");
	private String[] ids = {"746975053", "1767412253", "1384204844", "672863965"};
//"100002592216325"
	private Handler handler;
	

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slideshow);
		session = Session.getActiveSession();
		
		Session session1 = new Session(this);
		session1.openForRead(new Session.OpenRequest(this));

		session1.requestNewReadPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS));
			
			
		Session.setActiveSession(session1);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		
		Log.i(TAG, session.getPermissions().toString());
		
		this.handler = new Handler();
		
		try {
			slideshow();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
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

				getPhoto();
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
	
	@SuppressLint("NewApi")
	private void getPhoto() {
		
		int idSelection = (int) (Math.random() * ids.length);
		Log.i(TAG, idSelection + "");
		String id = ids[idSelection];
		
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
		        	int i = (int) (Math.random() * photos.length());

						Log.i(TAG, photos.get(i).toString());
						String url = photos.getJSONObject(i).getString("src_big");
						Drawable d = drawable_from_url(url, "img");
						
						if (d == null) {
							Log.i(TAG, "no drawable");
						}
						
						ImageView imageView = (ImageView) findViewById(R.id.slideshow_image);

						imageView.setImageDrawable(d);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        }
		});
		Request.executeBatchAsync(request);
		
	}


	
	
	
	private Drawable drawable_from_url(String url, String src_name) throws 
	   java.net.MalformedURLException, java.io.IOException {
		
	   return Drawable.createFromStream(((java.io.InputStream)
	      new java.net.URL(url).getContent()), src_name);
	}
	
//	private String removeExtras(S)
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.slideshow, menu);
		return true;
	}
	
}

package com.example.web_app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class FamilyMemberActivity extends Activity {
	
	private static final List<String> PERMISSIONS = Arrays.asList("friends_birthday", "user_photos",
																 "friends_education_history", "friends_photos");
	
	private String TAG = "FamilyMemberActivity";
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_family_member);
		
		setTitle("Family Member");
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		Session session1 = new Session(this);
		session1.openForRead(new Session.OpenRequest(this));

		session1.requestNewReadPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS));
			
			
		Session.setActiveSession(session1);


		if (session1 != null && session1.isOpened()) {
			Log.i(TAG, "session ok");
			getUserData("746975053");
			
			
			
		} else {
			Log.d(TAG, "no session");
		}

	}
		
	
	private void getUserData(String id) {
		
		String fqlQuery = "select name, birthday, current_address, education, pic_big from user where uid = " + id;
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
		        
		    	GraphObject g;
		    	JSONObject j;
		    	JSONArray data;
		        
		        g = response.getGraphObject();
		        if (g != null) {
		        	j = g.getInnerJSONObject();
		        	try {
						data = j.getJSONArray("data");
				        if (data != null) {
				        	Log.i(TAG, data.toString());
				        	//data.getJSONArray(0);
				        	Log.i(TAG, data.getJSONObject(data.length() -1).toString());
				        	Log.i(TAG, data.getJSONObject(data.length() -1).get("pic_big").toString());
				        	
				        	String url = data.getJSONObject(0).get("pic_big").toString();
				        	String name = data.getJSONObject(0).getString("name").toString();
				        	String birthday = data.getJSONObject(0).getString("birthday").toString();
				        	
							Drawable d = drawable_from_url(url, "img");
							
							if (d == null) {
								Log.i(TAG, "no drawable");
							} else {
						
								//Bitmap bitmap = roundCorner(drawableToBitmap(d), 20);
							
								
								ImageView iv = (ImageView) findViewById(R.id.profile_image);
								iv.setBackground(d);
								
							} 
							if (name == null) {
								Log.i(TAG, "no name");
							
							} else {
								
								TextView textView = (TextView) findViewById(R.id.profile_name);
								textView.setText(name);
	
							} 
							if (birthday == null) {
								Log.i(TAG, "no birthday");
							
							} else {
								
								TextView textView = (TextView) findViewById(R.id.profile_birthday);
								textView.setText(birthday);
	
							}
							
				        	
				        	
				        }
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
		    }
		});
		Request.executeBatchAsync(request);
		
	}
	
	public static Bitmap drawableToBitmap (Drawable drawable) {

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}
	
	 public static Bitmap roundCorner(Bitmap src, float round) 
	 {
	     // image size
	     int width = src.getWidth();
	     int height = src.getHeight();

	     // create bitmap output
	     Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);

	     // set canvas for painting
	     Canvas canvas = new Canvas(result);
	     canvas.drawARGB(0, 0, 0, 0);

	     // config paint
	     final Paint paint = new Paint();
	     paint.setAntiAlias(true);
	     paint.setColor(Color.BLACK);

	     // config rectangle for embedding
	     final Rect rect = new Rect(0, 0, width, height);
	     final RectF rectF = new RectF(rect);

	     // draw rect to canvas
	     canvas.drawRoundRect(rectF, round, round, paint);

	     // create Xfer mode
	     paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	     // draw source image to canvas
	     canvas.drawBitmap(src, rect, rect, paint);

	     // return final image
	     return result;
	 }
	
	
	private Drawable drawable_from_url(String url, String src_name) throws 
	   java.net.MalformedURLException, java.io.IOException {
		
	   return Drawable.createFromStream(((java.io.InputStream)
	      new java.net.URL(url).getContent()), src_name);
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.family_member, menu);
		return true;
	}

}

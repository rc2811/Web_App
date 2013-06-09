package com.example.web_app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class FamilyMemberActivity extends Activity {
	
	private static final List<String> PERMISSIONS = Arrays.asList("friends_birthday", "user_photos",
																 "friends_education_history", "friends_photos", "friends_hometown");
	
	private String TAG = "FamilyMemberActivity";
	private List<String> photoURLS;
	private String currentProfilePicURL;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_family_member);
		
		Intent intent = getIntent();
		String id = intent.getStringExtra("fb_id");
		
		setTitle("Family Member");
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		Session session1 = new Session(this);
		session1.openForRead(new Session.OpenRequest(this));

		session1.requestNewReadPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS));

		Session.setActiveSession(session1);


		if (session1 != null && session1.isOpened()) {
			Log.i(TAG, "session ok");
			getUserData(id);
			getUserPhotos(id);

		} else {
			Log.d(TAG, "no session");
		}

	}
		
	
	private void getUserData(String id) {
		
		String fqlQuery = "select name, birthday, hometown_location, work, education, pic_big from user where uid = " + id;

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
				        	parseData(data);

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
					} finally {
		        		
		        	}
		        }
		        }
			});
		Request.executeBatchAsync(request);
		
		}
	
				private void parseData(JSONArray data) throws JSONException, MalformedURLException, IOException {
					Log.i(TAG, data.toString());
					Log.i(TAG, data.get(0).toString());
					JSONObject j = (JSONObject) data.getJSONObject(0);
					
					
					if (j != null) {
						
						Log.i(TAG, "Ok object");
					
						String pic_url = j.getString("pic_big");
						String birthday = j.getString("birthday");
						JSONArray education = j.getJSONArray("education");
						Log.i(TAG, education.toString());
						
						String name = j.getString("name");
						JSONArray work = j.getJSONArray("work");
						
						Log.i(TAG, "Name: " + name);
						
						if (!pic_url.isEmpty() && !pic_url.equals(null)) {
							Drawable d = drawable_from_url(pic_url, "src");
							currentProfilePicURL = pic_url;
							
							if (d != null) {
								ImageView iv = (ImageView) findViewById(R.id.profile_img);
								iv.setImageDrawable(d);
							}
						} 
						
						if (!birthday.equals("null")) {

							TextView textView = new TextView(this);
							textView.setText("Birthday: " + birthday);
							Log.i(TAG, "Got birthday " + name);
							
							TableLayout layout = (TableLayout) findViewById(R.id.family_member_info);
							layout.addView(textView);
						}
						
						if (!name.equals("null")) {
							TextView textView = new TextView(this);
							textView.setText(name);
							Log.i(TAG, "Got name " + name);
							
							setTitle(name);
							
						}
						
							
						if (j.get("hometown_location") != null) {
							JSONObject hometown_object = j.getJSONObject("hometown_location");
							String hometown = hometown_object.getString("name");


							TextView textView = new TextView(this);
							textView.setText("Hometown: " + hometown);	
							Log.i(TAG, "Got hometown " + name);
							
							TableLayout layout = (TableLayout) findViewById(R.id.family_member_info);
							layout.addView(textView);
						}
						
						
						if (!education.equals("null")) {
							for (int i = 0; i < education.length(); i++) {
								
								JSONObject result = education.getJSONObject(i);
								if (result.getString("type").equals("High School")) {
									JSONObject school = result.getJSONObject("school");
									String school_name = school.getString("name");
									TextView textView = new TextView(this);
									textView.setText("High School: " + school_name);
									
									TableLayout layout = (TableLayout) findViewById(R.id.family_member_info);
									layout.addView(textView);
									
								} else if (result.getString("type").equals("College")) {
									JSONObject school = result.getJSONObject("school");
									String school_name = school.getString("name");
									TextView textView = new TextView(this);
									textView.setText("University: " + school_name);
									
									TableLayout layout = (TableLayout) findViewById(R.id.family_member_info);
									layout.addView(textView);
									
									
								}
								
							}
						}
					
					}
							
					
				}
				
				
	private void getUserPhotos(String id) {
					
		String fqlQuery = "select src, src_big from photo where owner = " + id;

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

					try {
						JSONArray data = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
						parsePhotos(data);
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

				
	private void parsePhotos(JSONArray data) throws JSONException, MalformedURLException, IOException {
		
		Log.i(TAG, data.getJSONObject(0).toString());
		List<Drawable> profile_pics = new ArrayList<Drawable>();
		
		photoURLS = new ArrayList<String>();
		
		int num_photos = 20;
		if (data.length() < 20) {
			num_photos = data.length();
		}
		
		for (int i = 0; i < num_photos; i++) {
			
			String url = data.getJSONObject(i).getString("src");
			
			String big_img_url  = data.getJSONObject(i).getString("src_big");
			
			Drawable d = drawable_from_url(url, "src");
			
			profile_pics.add(d);
			photoURLS.add(big_img_url);
			
			
		}
								
		final GridView gridView = (GridView) findViewById(R.id.grid_view_photos);

		gridView.setAdapter(new ImageAdapter(this, profile_pics));

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				String url = photoURLS.get(position);
				
				Drawable d;
				try {
					d = drawable_from_url(url, "src");
					ImageView profileImg = (ImageView) findViewById(R.id.profile_img);
					profileImg.setImageDrawable(d);
					currentProfilePicURL = url;
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}); 
							
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
	
 
	public void displayBig(View view) {
		
		Intent intent = new Intent(this, FullScreenImageActivity.class);
		intent.putExtra("url", currentProfilePicURL);
		
		startActivity(intent);
		
	}

}

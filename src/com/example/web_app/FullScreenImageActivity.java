package com.example.web_app;

import java.io.IOException;
import java.net.MalformedURLException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class FullScreenImageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_full_screen_image);
		
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		
		try {
			Drawable d = drawable_from_url(url, "src");
			ImageView image = (ImageView) findViewById(R.id.fullscreen_image);
			image.setImageDrawable(d);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.full_screen_image, menu);
		return true;
	}
	
	
	private Drawable drawable_from_url(String url, String src_name) throws 
	   java.net.MalformedURLException, java.io.IOException {
		
	   return Drawable.createFromStream(((java.io.InputStream)
	      new java.net.URL(url).getContent()), src_name);
	}

}

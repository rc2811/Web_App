package com.example.web_app;

import com.facebook.Session;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class MyFamilyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_layout);
		setTitle("Your Family");
		
		final GridView gridView = (GridView) findViewById(R.id.grid_view);
		gridView.setAdapter(new ImageAdapter(this));


	    gridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	           selectFamilyMember(position, gridView);
	        }
	    });
	    
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_family, menu);
		return true;
	}
	
	
	public void selectFamilyMember(int position, GridView view) {
        int pic_id = view.getId();
		Intent intent = new Intent(this, FamilyMemberActivity.class);
		intent.putExtra("id", pic_id);
		startActivity(intent);
	}

}

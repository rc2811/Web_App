package com.example.web_app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setTitle("Register");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	public void register(View view) {
		
		EditText firstName = (EditText) this.findViewById(R.id.register_first_name);
		EditText surname = (EditText) this.findViewById(R.id.register_password);
		EditText username = (EditText) this.findViewById(R.id.register_username);
		EditText password = (EditText) this.findViewById(R.id.register_surname);
		
		String firstNameString = firstName.getText().toString();
		String surnameString = surname.getText().toString();
		String usernameString = username.getText().toString();
		String passwordString = password.getText().toString(); 
		
		//TODO put user details in database
		
		finish();
		
		//TODO shall we go back to login screen or just login straight away after register
	}

}

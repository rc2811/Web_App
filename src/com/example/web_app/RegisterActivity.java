package com.example.web_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnItemSelectedListener, RequestHandler{

	public Spinner spinner;
	public Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		
		setTitle("Register");
		setContentView(R.layout.activity_register);

		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		return false;
	}
	
	public void register(View view) {
		
		//EditText firstName = (EditText) this.findViewById(R.id.register_first_name);
		//EditText surname = (EditText) this.findViewById(R.id.register_password);
		EditText username = (EditText) this.findViewById(R.id.register_username);
		EditText password = (EditText) this.findViewById(R.id.register_password);
		
		//String firstNameString = firstName.getText().toString();
		//String surnameString = surname.getText().toString();
		String usernameString = username.getText().toString();
		String passwordString = password.getText().toString(); 
		
		Request request = new Request(Command.REGISTER, new String[] {usernameString, passwordString});
		ServerRequest servReq = new ServerRequest(this);
		servReq.execute(request);
		
		finish();
		
		//TODO shall we go back to login screen or just login straight away after register
	}

	@Override
	public void doOnRequestComplete(String result) {
		Log.v("Message from server. Register", result);
		if(result.equals("OK")) {
			Log.v("registration", "Registration Good");
			 Intent intent = new Intent(context, HomeScreenActivity.class);
			startActivity(intent);
		} else {
			Context context = getApplicationContext();
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, result, duration);
			toast.show();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}



}

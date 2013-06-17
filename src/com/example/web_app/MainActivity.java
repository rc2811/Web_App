package com.example.web_app;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Session;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements RequestHandler {
	
	public Context context;
	public EditText editUsername;
	public EditText editPassword;
	public CheckBox cBox;
	
	//User preferences for remembering username and password
	public static final String PREFS_NAME = "UserPrefs";
	private static final String PREF_USERNAME = "username";
	private static final String PREF_PASSWORD = "password";
	private static final String PREFF_CURR_USER = "currUser";
	
	public final static String USERNAME = "com.example.web_app.USERNAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		context = this;
		
		setContentView(R.layout.activity_main);
		
		printHashKey();
		
		
		SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);   
		String username = pref.getString(PREF_USERNAME, null);
		String password = pref.getString(PREF_PASSWORD, null);
		
		editUsername = (EditText) findViewById(R.id.editUsername);
		editPassword = (EditText) findViewById(R.id.editPassword);
		
		cBox = (CheckBox) findViewById(R.id.rememberUser);

		if (username != null && password != null) {
			editUsername.setText(username);
			editPassword.setText(password);
			cBox.setChecked(true);
		}
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return false;
	}
	
	//called by login button
	public void getAccountInfo(View view) throws InterruptedException {
		String usernameString = editUsername.getText().toString();
		String passwordString = editPassword.getText().toString();
		ServerRequest servReq = new ServerRequest(this);
		servReq.login(usernameString, passwordString);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	//called by register button
	public void register(View view) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}

	//called by ServerRequest on post execution
	@Override
	public void doOnRequestComplete(String result) {
		Log.v("Message from server", result);
		if(result.equals("OK")) {
			Log.v("message comparison", "Login Good");
			getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit().putString(PREFF_CURR_USER, editUsername.getText().toString()).commit();
			//rememberUser();
			Intent intent = new Intent(context, HomeScreenActivity.class);
			startActivity(intent);
		} else {
			Context context = getApplicationContext();
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, result, duration);
			toast.show();
		}
	}
	
	public void rememberUser(View view) {
		//Store current username and password in shared preferences
		if(cBox.isChecked()){
			String usernameString = editUsername.getText().toString();
			String passwordString = editPassword.getText().toString();
			getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit().putString(PREF_USERNAME, usernameString ).putString(PREF_PASSWORD, passwordString ).commit();
		} else {
			getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit().putString(PREF_USERNAME, null).putString(PREF_PASSWORD, null ).commit();
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    super.onKeyDown(keyCode, event);
        switch(keyCode)
        {
        case KeyEvent.KEYCODE_BACK:
   
            return false;
        }
        return false;
	}
	
	public void printHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.web_app",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("TEMPTAGHASH KEY:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }
	
}

package com.example.web_app;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener;
import com.google.android.gms.plus.PlusClient.OnPersonLoadedListener;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.Person.Name;
import com.google.android.gms.plus.model.people.PersonBuffer;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ShareCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;

public class SettingsActivity extends Activity implements
ConnectionCallbacks, OnConnectionFailedListener, View.OnClickListener, PlusClient.OnPeopleLoadedListener, PlusClient.OnPersonLoadedListener {

private static final String TAG = "LoginActivity";
private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

private ProgressDialog mConnectionProgressDialog;
private PlusClient mPlusClient;
private ConnectionResult mConnectionResult;



@SuppressLint("NewApi")
@Override
protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_settings);
			mPlusClient = new PlusClient.Builder(this, this, this).setScopes("https://www.googleapis.com/auth/plus.login"
				//	"https://www.googleapis.com/auth/userinfo.profile",
				//	"https://www.googleapis.com/auth/plus.me",
				//	"https://www.googleapis.com/auth/userinfo.email"
					)
				.setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
				.build();
			findViewById(R.id.sign_in_button).setOnClickListener(this);
			findViewById(R.id.share_button).setOnClickListener(this);
}




@Override
protected void onStart() {
	super.onStart();

}

@Override
protected void onStop() {
	super.onStop();
	//mPlusClient.disconnect();
}


@Override
public void onConnectionFailed(ConnectionResult result) {
	if (result.hasResolution()) {
		try {
			result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
		} catch (SendIntentException e) {
			mPlusClient.connect();
		}
	}
	// Save the result and resolve the connection failure upon a user click.
	mConnectionResult = result;
}


@Override
public void onDisconnected() {
	Log.d(TAG, "disconnected");	
}

@Override
public void onClick(View view) {

    if (view.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) {
    	
		mPlusClient.connect();
		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("Signing in...");
    	
        if (mConnectionResult == null) {
            mConnectionProgressDialog.show();
        } else {
            try {
                mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            } catch (SendIntentException e) {
                // Try connecting again.
                mConnectionResult = null;
                mPlusClient.connect();
            }
        }
    } else {
    	Log.d(TAG, "already connected");	
    }
    
    
    if (view.getId() == R.id.share_button) {

        if (mPlusClient.isConnected()) {
            Intent shareIntent = ShareCompat.IntentBuilder.from(SettingsActivity.this)
                    .setType("text/plain")
                    .setText("Calling all elderly people, try out Family Ties on the Play Store")
                    .getIntent()
                    .setPackage("com.google.android.apps.plus");

                startActivity(shareIntent);

            startActivity(shareIntent);
        } else {
            Toast.makeText(this, "Please Sign-in with google Account", Toast.LENGTH_LONG)
            .show();
        }
}
    
    
    
    
    
}

@Override
public void onPeopleLoaded(ConnectionResult status, PersonBuffer personBuffer,
        String nextPageToken) {

    if (status.getErrorCode() == ConnectionResult.SUCCESS) {
    	
        try {
            int count = personBuffer.getCount();

            for (int i = 0; i < count; i++) {
            
            	Person p = personBuffer.get(i);
            	String firstName = null;
            	String surname = null;
            	String id = p.getId();
            	String imageURL = null;
            	String workplace = null;
            	String location = null;
            	String birthday = null;
            	
            	if (p.hasDisplayName()) {
                  	Log.d(TAG, p.getDisplayName());
            	} else {
                  	Log.d(TAG, "No NAME");
            	}
            	if (p.hasImage()) {
            		imageURL = p.getImage().getUrl();
            	} else {
                  	Log.d(TAG, "No Image");
            	}
            	if (p.hasOrganizations()) {
            		workplace = p.getOrganizations().get(0).toString();
                  	Log.d(TAG, "Works at " + workplace);
            	} else {
                  	Log.d(TAG, "No Workplace");
            	}
            	if (p.hasPlacesLived()) {
            		location = p.getPlacesLived().get(0).toString();
                  	Log.d(TAG, "Lives at " + location);
            	} else {
                  	Log.d(TAG, "No Location");
            	}
            	if (p.hasBirthday()) {
            		birthday = p.getBirthday();
                  	Log.d(TAG, "Birthday: " + birthday);
            	} else {
                  	Log.d(TAG, "No Birthday");
            	}
            	
           
            	
            	//TODO add to Database details above
            	
         /*   	
            	
                Log.d(TAG, "Display Name: " + personBuffer.get(i).getDisplayName());
//              Log.d(TAG, "Worked: " + person.getOrganizations().toString());
              if (!personBuffer.get(i).hasImage()) {
                  Log.d(TAG, "No profile image");
              } else {
              	Log.d(TAG, "Profile image exists");
              	Log.d(TAG, personBuffer.get(i).getImage().getUrl());
              	
              	
              	ImageView iv = new ImageView(this);

              	URL url = null;
      			try {
      				url = new URL(personBuffer.get(i).getImage().getUrl());
      	        	Log.d(TAG, "URL OK");
      			} catch (MalformedURLException e1) {
      				// TODO Auto-generated catch block
      				e1.printStackTrace();
      			}
              	InputStream content = null;
      		try {
      				content = (InputStream) url.getContent();
      				if (content != null) {
      					Log.d(TAG, "Content ok");
      				}
      				
      			} catch (IOException e) {
      		//		// TODO Auto-generated catch block
      				e.printStackTrace();
      		}
            	Drawable d = Drawable.createFromStream(content , "src"); 
            	
              	iv.setImageDrawable(d);
              	//iv.getLayoutParams().height = 100;
              	
              	TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(500, 500);
              	iv.setLayoutParams(layoutParams);
              	
        //        ImageView image = (ImageView) this.findViewById(R.id.profile_pic);
        //        image.setBackground(d);
              	

                TableLayout layout = (TableLayout) this.findViewById(R.id.layout_settings);
                layout.addView(iv);
              }} */
            }
        } finally {
            personBuffer.close();
        }
    } else {
        Log.e(TAG, "Error listing people: " + status.getErrorCode());
    }
}



@Override
public void onConnected(Bundle arg0) {
    mConnectionProgressDialog.dismiss();
    Toast.makeText(this, "You are connected to Google+!", Toast.LENGTH_LONG).show();
   
    /*
    try {
		getData();
	} catch (UserRecoverableAuthException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (GoogleAuthException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} */
    
	mPlusClient.loadPerson(this, "104733386272203643983");
	mPlusClient.loadPerson(this, "115191497923996502724");
	
}



@SuppressLint("NewApi")
@Override
public void onPersonLoaded(ConnectionResult status, Person person) {
    if (status.getErrorCode() == ConnectionResult.SUCCESS) {
       // Log.d(TAG, "Display Name: " + person.getDisplayName());
        
    	if (person.hasDisplayName()) {
          	Log.d(TAG, person.getDisplayName());
    	} else {
          	Log.d(TAG, "No NAME");
    	}
    	
    	if (person.hasImage()) {
    		String imageURL = person.getImage().getUrl();
    	} else {
          	Log.d(TAG, "No Image");
    	}
    	
    	if (person.hasOrganizations()) {
    		String workplace = person.getOrganizations().get(0).toString();
          	Log.d(TAG, "Works at " + workplace);
    	} else {
          	Log.d(TAG, "No Workplace");
    	}
    	
    	if (person.hasPlacesLived()) {
    		String location = person.getPlacesLived().get(0).toString();
          	Log.d(TAG, "Lives at " + location);
    	} else {
          	Log.d(TAG, "No Location");
    	}
    	
    	if (person.hasBirthday()) {
    		String birthday = person.getBirthday();
          	Log.d(TAG, "Birthday: " + birthday);
    	} else {
          	Log.d(TAG, "No Birthday");
    	}
        
        
        
        
//        Log.d(TAG, "Worked: " + person.getOrganizations().toString());
       /* if (!person.hasImage()) {
            Log.d(TAG, "No profile image");
        } else {
        	Log.d(TAG, "Profile image exists");
        	Log.d(TAG, person.getImage().getUrl());
        	
        	
        	ImageView iv = new ImageView(this);

        	URL url = null;
			try {
				url = new URL(person.getImage().getUrl());
	        	Log.d(TAG, "URL OK");
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	InputStream content = null;
		try {
				content = (InputStream) url.getContent();
				if (content != null) {
					Log.d(TAG, "Content ok");
				}
				
			} catch (IOException e) {
		//		// TODO Auto-generated catch block
				e.printStackTrace();
		}
      	Drawable d = Drawable.createFromStream(content , "src"); 
      	
        	iv.setImageDrawable(d);
        	//iv.getLayoutParams().height = 100;
        	
        	TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(500, 500);
        	iv.setLayoutParams(layoutParams);
        	
  //        ImageView image = (ImageView) this.findViewById(R.id.profile_pic);
  //        image.setBackground(d);
        	iv.setX(0);
        	iv.setY(0);
        	

          TableLayout layout = (TableLayout) this.findViewById(R.id.layout_settings);
          layout.addView(iv);

//  		setContentView(R.id.layout_settings);
        	
        } */

    }
	
}



public void getData() throws UserRecoverableAuthException, IOException, GoogleAuthException {
	
//	final String token = GoogleAuthUtil.getToken(this, mPlusClient.getAccountName() , "oauth2:" + Scopes.PLUS_LOGIN +
//			" " + Scopes.PLUS_PROFILE);
	//mPlusClient.loadPerson(this, "115191497923996502'724");
	mPlusClient.loadPerson(this, "104733386272203643983");
}












}

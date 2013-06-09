package com.example.web_app;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.example.web_app.R;
import com.facebook.FacebookException;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;


public class PickerActivity extends FragmentActivity {
	
	public String MESSAGE = "com.example.web_app.MESSAGE";
	
	
	public static final Uri FRIEND_PICKER = Uri.parse("picker://friend");
	private FriendPickerFragment friendPickerFragment;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.pickers);

	    Bundle args = getIntent().getExtras();
	    FragmentManager manager = getSupportFragmentManager();
	    Fragment fragmentToShow = null;
	    Uri intentUri = getIntent().getData();

	    if (FRIEND_PICKER.equals(intentUri)) {
	        if (savedInstanceState == null) {
	            friendPickerFragment = new FriendPickerFragment(args);
	        } else {
	            friendPickerFragment = 
	                (FriendPickerFragment) manager.findFragmentById(R.id.picker_fragment);
	        }
	        // Set the listener to handle errors
	        friendPickerFragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
	            @Override
	            public void onError(PickerFragment<?> fragment,
	                                FacebookException error) {
	                PickerActivity.this.onError(error);
	            }
	        });
	        // Set the listener to handle button clicks
	        friendPickerFragment.setOnDoneButtonClickedListener(
	                new PickerFragment.OnDoneButtonClickedListener() {
	            @Override
	            public void onDoneButtonClicked(PickerFragment<?> fragment) {
	                finishActivity();
	            }
	        });
	        fragmentToShow = friendPickerFragment;

	    } else {
	        // Nothing to do, finish
	        setResult(RESULT_CANCELED);
	        finish();
	        return;
	    }

	    manager.beginTransaction()
	           .replace(R.id.picker_fragment, fragmentToShow)
	           .commit();
	}

	private void onError(Exception error) {
	    onError(error.getLocalizedMessage(), false);
	}

	private void onError(String error, final boolean finishActivity) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(R.string.error_dialog_title).
	            setMessage(error).
	            setPositiveButton(R.string.error_dialog_button_text, 
	               new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialogInterface, int i) {
	                    if (finishActivity) {
	                        finishActivity();
	                    }
	                }
	            });
	    builder.show();
	}

	private void finishActivity() {
	    setResult(RESULT_OK, null);
	    
	    
	    if (FRIEND_PICKER.equals(getIntent().getData())) {
	        if (friendPickerFragment != null) {
	        	Intent intent = new Intent(this, SettingsActivity.class);
	        	
	        	List<String> family_ids = getFamilyIds(friendPickerFragment.getSelection());
	        	
	        	intent.putExtra("num_selections", family_ids.size());

	    	    startActivity(intent);

	        }   
	    }  
	    setResult(RESULT_OK, null);

	    finish();
	}
	
	
	private List<String> getFamilyIds(List<GraphUser> selection) {
		
		List<String> ids = new ArrayList<String>();
		
		for (GraphUser g : selection) {
			ids.add(g.getId());
			
		}
		
		return ids;
		
	}
	
	@Override
	protected void onStart() {
	    super.onStart();
	    if (FRIEND_PICKER.equals(getIntent().getData())) {
	        try {
	            friendPickerFragment.loadData(false);
	        } catch (Exception ex) {
	            onError(ex);
	        }
	    }
	}
	
	

}

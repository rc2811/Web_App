package com.example.web_app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class HomeScreenActivity extends ListActivity {
	
    private static final String FROM_TITLE = "title";
    private static final String TITLE_KEY = "title";
    private static final String INTENT_KEY = "intent";
	
	private static final Map<String, String> HOME_SCREEN_MAP;
	static {
		HOME_SCREEN_MAP = new LinkedHashMap<String, String>();
		HOME_SCREEN_MAP.put("Slideshow", SlideshowActivity.class.getName());
		HOME_SCREEN_MAP.put("Quiz", QuizStartActivity.class.getName());
		HOME_SCREEN_MAP.put("My Family", MyFamilyActivity.class.getName());
		HOME_SCREEN_MAP.put("Settings", SettingsActivity.class.getName());
		HOME_SCREEN_MAP.put("Logout", MainActivity.class.getName());
		HOME_SCREEN_MAP.put("Testing Button!", TestSuiteActivity.class.getName());

	}
	
	//private UiLifecycleHelper uiHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new SimpleAdapter(this, getOptions(),
				R.layout.main_list_item, new String[] { FROM_TITLE },
				new int[] { android.R.id.text1 }));
			
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		
	}

    protected ArrayList<HashMap<String, Object>> getOptions() {
        ArrayList<HashMap<String, Object>> samples = new ArrayList<HashMap<String, Object>>();
        for (Map.Entry<String, String> sample : HOME_SCREEN_MAP.entrySet()) {
            Intent optionIntent = new Intent(Intent.ACTION_MAIN);
            optionIntent.setClassName(getApplicationContext(), sample.getValue());
            addItem(samples, sample.getKey(), optionIntent);
        }
        return samples;
    }
    
    private void addItem(List<HashMap<String, Object>> data, String title, Intent intent) {
        HashMap<String, Object> temp = new HashMap<String, Object>();
        temp.put(TITLE_KEY, title);
        temp.put(INTENT_KEY, intent);
        data.add(temp);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected void onListItemClick(ListView listView, View view, int position, long id) {
    	
        Map<String, Object> map = (Map<String, Object>) listView.getItemAtPosition(position);
        if (map.get(TITLE_KEY).equals("Logout") || map.get(TITLE_KEY).equals("Settings") || map.get(TITLE_KEY).equals("Testing Button!")) {
        	
			Intent intent = (Intent) map.get(INTENT_KEY);
			startActivity(intent);
        	
        } else {
    	
        	Session session = Session.getActiveSession();
    	
        	if (session != null) {
    		
        		List<String> permissions = session.getPermissions();

        		Intent intent = (Intent) map.get(INTENT_KEY);
        		startActivity(intent);

        	} else {
				Toast.makeText(this, "Select the 'Settings' option to log in with Facebook first.", Toast.LENGTH_SHORT).show();
        	}

        }
    }
    
    
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    super.onKeyDown(keyCode, event);
        switch(keyCode)
        {
        case KeyEvent.KEYCODE_BACK:
        	Toast.makeText(this, "Select the Logout button to log out", Toast.LENGTH_SHORT).show();
         
            return false;
        }
        return false;
	}

	
}




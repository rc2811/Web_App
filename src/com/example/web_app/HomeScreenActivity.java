package com.example.web_app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class HomeScreenActivity extends ListActivity implements contextSwitcher {
	
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
	}
	
	private UiLifecycleHelper uiHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new SimpleAdapter(this, getOptions(),
				R.layout.main_list_item, new String[] { FROM_TITLE },
				new int[] { android.R.id.text1 }));
		
		Session session = Session.getActiveSession();
		
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(getApplicationContext(), null, callback, savedInstanceState);
			} 
			if (session == null) {
				session = new Session(getApplicationContext());
			}
			Session.setActiveSession(session);
		}
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
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
        Intent intent = (Intent) map.get(INTENT_KEY);
        startActivity(intent);
    }
    
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}
	
	protected void onSessionStateChange(Session session2, SessionState state,
			Exception exception) {
		// TODO Auto-generated method stub
		
	}
	
	

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}   

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}   

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}   

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	public void requestIDs() {
		Request request = new Request(Command.FETCHIDS, new String[] {"matt"});
		ServerRequest servReq = new ServerRequest(this);
		servReq.execute(request);
	}

	@Override
	public void cSwitch(String s) {
		TextView textView = new TextView(this);
		
		String[] reply = s.split(";");
		String result = "";
		
		for(String x : reply) {
			result += x;
			result += "\n";
		}
		
		textView.setText(result);
		setContentView(textView);
	}
}



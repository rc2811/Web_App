package com.example.web_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class ServerRequest extends AsyncTask<Request, Void, String>{
	
	private String uri = "http://146.169.53.101:55555/s";
	
	private Intent intent;
	
	public ServerRequest() {
	}

	@Override
	protected String doInBackground(Request... request) {
		String retval = "";
		for(Request r : request) {
			Uri.Builder b = Uri.parse(uri).buildUpon();
	        b.appendQueryParameter("command", r.command.toString());
	        for(String s : r.args) {
	        	b.appendQueryParameter("args", s);
	        }
	        
	        URL url = null;
			try {
				url = new URL(b.build().toString());
			} catch (MalformedURLException e) {
				retval += e + "\n";
				e.printStackTrace();
			}
	        URLConnection connection = null;
			try {
				connection = url.openConnection();
			} catch (IOException e) {
				retval += e + "\n";
				e.printStackTrace();
			}
	        
	        BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} catch (IOException e) {
				retval += e + "\n";
				e.printStackTrace();
			}
	        
	        try {
				String returnString = in.readLine();
				retval += returnString + "\n";
			} catch (IOException e) {
				retval += e + "\n";
				e.printStackTrace();
			}
		}
		
		return retval;
	}
	
	@Override
	protected void onPostExecute(String result) {
		Log.v("Message from server", result);
		
	}
	
}
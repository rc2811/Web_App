package com.example.web_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.net.Uri;
import android.os.AsyncTask;

public class ServerRequest extends AsyncTask<Request, Void, String>{
	
	private String uri = "http://146.169.53.98:55555/s";
	//private String uri = "http://shell1.doc.ic.ac.uk:55555/s";
	public RequestHandler c;
	
	public ServerRequest(RequestHandler c) {
		this.c = c;
	}
	
	public void login(String username, String password) {
		Request request = new Request(Command.LOGIN, new String[] {username,password});
		execute(request);
	}
	
	public void register(String username, String password, String acc_type) {
		Request request = new Request(Command.REGISTER, new String[] {username, password, acc_type});
		execute(request);

	}
	
	public void fetchIDs(String username) {
		Request request = new Request(Command.FETCHIDS, new String[] {username});
		execute(request);
	}
	
	public void insertIDs(String username, String[] ids) {
		String[] result = new String[ids.length + 1];
		result[0] = username;
		int i = 1;
		for(String s : ids) {
			result[i] = s;
			i++;
		}
		Request request = new Request(Command.INSERTIDS, result);
		execute(request);
	}
	
	public void addFBID(String username, String fbID) {
		Request request = new Request(Command.ADDFBID, new String[] {username, fbID});
		execute(request);
	}
	
	public void sendMessage(String sender, String recipientFBID, String message) {
		Request request = new Request(Command.SENDMESSAGETO, new String[] {sender, recipientFBID, message});
		execute(request);
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
				retval += returnString;
			} catch (IOException e) {
				retval += "\n" + e;
				e.printStackTrace();
			}
		}
		
		return retval;
	}
	
	@Override
	protected void onPostExecute(String result) {
		c.doOnRequestComplete(result); 
	}
	
}

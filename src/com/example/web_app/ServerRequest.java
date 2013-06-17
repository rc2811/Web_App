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
	
	//private String uri = "http://146.169.53.97:55555/s";
	private String uri = "http://shell1.doc.ic.ac.uk:55555/s";
	public RequestHandler c;
	
	//all responses returned in classes doOnRequestComplete method via post execute
	public ServerRequest(RequestHandler c) {
		this.c = c;
	}
	
	//check login with server
	public void login(String username, String password) {
		Request request = new Request(Command.LOGIN, new String[] {username,password});
		execute(request);
	}
	
	//insert login password acc_type into database if available
	public void register(String username, String password, String acc_type) {
		Request request = new Request(Command.REGISTER, new String[] {username, password, acc_type});
		execute(request);

	}
	
	//fetch friend ids of username. Use String.split(":") in doOnRequestComplete to get array of ids
	public void fetchIDs(String username) {
		Request request = new Request(Command.FETCHIDS, new String[] {username});
		execute(request);
	}
	
	//insert friend ids for username into database
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
	
	//clear all friendIDS of username. For development purposes
	public void clearIDs(String username) {
		Request request = new Request(Command.CLEARIDS, new String[] {username});
		execute(request);
	}
	
	//insert facebook id of username into database
	public void addFBID(String username, String fbID) {
		Request request = new Request(Command.ADDFBID, new String[] {username, fbID});
		execute(request);
	}
	
	//insert message from sender to recipientFBID
	public void sendMessage(String sender, String friendName, String message) {
		if(message != "") {
			Request request = new Request(Command.SENDMESSAGETO, new String[] {sender, friendName, message});
			execute(request);
		}
	}
	
	//get messages sent to username. Format is string of ("sender", "message")~("sender2", "message2") etc
	public void getMessages(String username) {
		Request request = new Request(Command.GETMESSAGES, new String[] {username});
		execute(request);
	}
	
	//return array of names of friends of username. No friends returns NO FRIENDS;
	public void getFriends(String username) {
		Request request = new Request(Command.GETFRIENDS, new String[] {username});
		execute(request);
	}

	@Override
	protected String doInBackground(Request... request) throws RuntimeException {
		String retval = "";
		for(Request r : request) {
			Uri.Builder b = Uri.parse(uri).buildUpon();
	        b.appendQueryParameter("command", r.command.toString());
	        for(String s : r.args) {
	        	b.appendQueryParameter("args", s);
	        }
	        
	        URL url = null;
	        URLConnection connection = null;
	        BufferedReader in = null;
			try {
				url = new URL(b.build().toString());
				connection = url.openConnection();
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String returnString = in.readLine();
				retval += returnString;
			} catch (MalformedURLException e) {
				handleException(retval, e);
			} catch (IOException e) {
				handleException(retval, e);
			} catch (RuntimeException e) {
				handleException(retval, e);
			}
	}
		return retval;
	}
	
	@Override
	protected void onPostExecute(String result) {
		c.doOnRequestComplete(result); 
	}
	
	public String handleException(String s, Exception e) {
		s += e + "\n";
		e.printStackTrace();
		return s;
	}
	
}

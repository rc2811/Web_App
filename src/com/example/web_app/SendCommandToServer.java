package com.example.web_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.net.Uri;

public class SendCommandToServer extends Thread {
	
	private Command command;
	private String[] args;
	
	public SendCommandToServer(Command command, String[] arguments) {
		this.command = command;
		this.args = arguments;
	}
	
	public void run()
	{
		Uri.Builder b = Uri.parse("http://146.169.53.101:55555/s").buildUpon();
		
        b.appendQueryParameter("command", command.toString());
        //etc more arguments
        
        
        URL url = null;
		try {
			url = new URL(b.build().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        URLConnection connection = null;
		try {
			connection = url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        try {
			String returnString= in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

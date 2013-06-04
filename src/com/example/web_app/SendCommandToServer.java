/*package com.example.web_app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
		
        b.appendQueryParameter("name", inputString);
        //etc more arguments
        
        
        URL url = new URL(b.build().toString());
        URLConnection connection = url.openConnection();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        
        String returnString= in.readLine();
	}

}*/

package com.example.logintest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

public class Connection extends Thread {
	
	public void run() {
		try {
			
			URL ob = new URL("http://146.169.53.101:55555/s?login=pt1411");
			
			URLConnection c = ob.openConnection();
			
			c.setDoInput(true);
			c.setDoOutput(true);
			
			DataOutputStream out = new DataOutputStream(c.getOutputStream());
			DataInputStream in = new DataInputStream(c.getInputStream());
			out.write(3);

			byte[] bytes = new byte[1];
			bytes[0] = 4;
			in.read(bytes);
			
			Log.d("Connection", "" + bytes[0] );
			out.close();
			in.close();
			
			
			
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

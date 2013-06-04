package com.example.web_app;

import android.content.Intent;


//wrapper class for request passed to ServerRequest
public class Request {
	
	public Command command;
	public String[] args;
	
	public Request(Command command, String[] args) {
		this.command = command;
		this.args = args;
	}

}

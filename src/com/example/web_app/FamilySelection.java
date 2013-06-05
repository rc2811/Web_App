package com.example.web_app;

import java.util.List;

import com.facebook.model.GraphUser;

import android.app.Application;

public class FamilySelection extends Application {
	
	private List<GraphUser> selectedUsers;
	
	public List<GraphUser> getSelectedUsers() {
	    return selectedUsers;
	}

	public void setSelectedUsers(List<GraphUser> users) {
	    selectedUsers = users;
	}

}

package com.example.web_app;

import java.net.URL;

import android.os.Parcel;
import android.os.Parcelable;

public class Member implements Parcelable {

	private String firstName;
	private String surname;
	private String person_id;
	private String profilePicURL;
	private String workplace;
	private String location;
	
	public Member(String firstName, String surname, String person_id, String profilePicURL, String workplace,
					String location) {
		this.firstName = firstName;
		this.surname = surname;
		this.person_id = person_id;
		this.profilePicURL = profilePicURL;
		this.workplace = workplace;
		this.location = location;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public String getPersonID() {
		return person_id;
	}
	
	public String getProfilePicURL() {
		return profilePicURL;
	}
	
	public String getWorkPlace() {
		return workplace;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setProfilePicURL() {
		
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {

		
	}
	
}

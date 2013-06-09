package com.example.web_app;

public class FamilyMember {
	
	private String uid;
	private String name;
	private String birthday;
	private String profile_pic_url;
	
	public FamilyMember(String uid, String name, String birthday, String profile_pic_url) {
		this.uid = uid;
		this.name = name;
		this.birthday = birthday;
		this.profile_pic_url = profile_pic_url;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getProfile_pic_url() {
		return profile_pic_url;
	}

	public void setProfile_pic_url(String profile_pic_url) {
		this.profile_pic_url = profile_pic_url;
	}
	
	@Override
	public String toString() {
		
		return uid + " " + name + " " + birthday + " " + profile_pic_url;
		
	}

}

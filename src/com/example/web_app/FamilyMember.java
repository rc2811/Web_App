package com.example.web_app;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;

public class FamilyMember {
	
	private String uid;
	private String name;
	private String birthday;
	private String profile_pic_url;
	private String hometown;
	private List<String> picture_urls;
	private List<String> school_list;
	private List<String> work_list;
	
	public FamilyMember(String uid, String name, String birthday, String profile_pic_url, String hometown,
						List<String> school_list, List<String> work_list) {
		this.uid = uid;
		this.name = name;
		this.birthday = birthday;
		this.profile_pic_url = profile_pic_url;
		this.setHometown(hometown);
		setPicture_urls(new ArrayList<String>());
		this.school_list = school_list;
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

	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	public List<String> getPicture_urls() {
		return picture_urls;
	}

	public void setPicture_urls(List<String> picture_urls) {
		this.picture_urls = picture_urls;
	}
	
	public String getRandomURL() {
		
		Log.i("se", "number of urls " + picture_urls.size());
		
		Random r = new Random();
		int rand=r.nextInt(picture_urls.size()-0) + 0;
		
		return picture_urls.get(rand);
		
	}

	public List<String> getSchool_list() {
		return school_list;
	}

	public void setSchool_list(List<String> school_list) {
		this.school_list = school_list;
	}

	public List<String> getWork_list() {
		return work_list;
	}

	public void setWork_list(List<String> work_list) {
		this.work_list = work_list;
	}

}

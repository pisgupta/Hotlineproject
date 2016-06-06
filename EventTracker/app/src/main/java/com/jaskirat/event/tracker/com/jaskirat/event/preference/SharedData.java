package com.jaskirat.event.tracker.com.jaskirat.event.preference;

import android.app.Activity;

public class SharedData {
	
	private SharedData() {}
	
	private static SharedData instance;
	
	public static SharedData getInstance(){
		if(instance == null){
			instance = new SharedData();
		}
		
		return instance;
	}

	double latitude, longitude;
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	Activity activity;
	
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	public Activity getActivity() {
		return activity;
	}
	
	int screenWidth, screenHeight;
	
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
}

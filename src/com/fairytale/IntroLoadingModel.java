package com.fairytale;

import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class IntroLoadingModel {
	private DatabaseAccessModel dbm;
	private Context context;
	
	private String current_app_version;
	private HashMap<Integer,HashMap<Integer,Integer[]>> required;
	
	public IntroLoadingModel(Context context, DatabaseAccessModel db){
		this.dbm = db;
		this.context = context;
		try{
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			current_app_version = info.versionName;
			mLog.d("Current App Version: "+current_app_version);
		}catch(NameNotFoundException e){
			e.printStackTrace();
			current_app_version = null;
		}
	}
	
	public String getAppVersion(){
		return current_app_version;
	}

	public void setUpdate(HashMap<Integer,HashMap<Integer,Integer[]>> info){
		this.required = info;
	}
	public Integer[] storyUpdateList(){
		Integer[] list = new Integer[required.keySet().size()];
		return required.keySet().toArray(list);
	}
	public void itemUpdateList(){
		
	}
	public void imageUpdateList(){
		
	}
	public void soundUpdateList(){
		
	}
	public void bgmUpdateList(){
		
	}
	public void textUpdateList(){
		
	}
}

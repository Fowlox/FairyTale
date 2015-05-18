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
	public int itemUpdateLength(int story_id){
		return required.get(story_id).size();
	}
	public int imageUpdateLength(int story_id, int item_id){
		return this.required.get(story_id).get(item_id)[0];
	}
	public int soundUpdateLength(int story_id, int item_id){
		return this.required.get(story_id).get(item_id)[1];
	}
	public int bgmUpdateLength(int story_id, int item_id){
		return this.required.get(story_id).get(item_id)[2];
	}
	public int textUpdateLength(int story_id, int item_id){
		return this.required.get(story_id).get(item_id)[3];
	}
	
	public int getImageResource(int item_no, int img_no){
		if(item_no == 1){
			return R.drawable.scene01_1;
		}else{
			return R.drawable.scene02_1+(item_no-2)*2+img_no-1;
		}
	}
	public int getSoundResource(int item_no){
		if(item_no == 1){
			return R.raw.scene01;
		}else
			return R.raw.scene02+(item_no-2)*3;
	}
}

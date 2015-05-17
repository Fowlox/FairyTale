package com.fairytale;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class StoryListModel {
	private DatabaseAccessModel dba;
	
	private int number_of_story;
	private ArrayList<Integer> storys;
	private HashMap<Integer,String> titles;
	private HashMap<Integer,Bitmap> thumbs;
	

	public StoryListModel(Context context, DatabaseAccessModel database){
		this.dba = database;
		
		number_of_story = 0;
		storys = new ArrayList<Integer>();
		titles = new HashMap<Integer,String>();
		thumbs = new HashMap<Integer,Bitmap>();
		
		getStoryList();
	}
	
	private void getStoryList(){
		ArrayList<HashMap<String, Integer>> result = dba.selectQuery("STORY", new String[]{"STORY_ID"}, null, null, null, null, "STORY_ID asc");
		number_of_story = result.size();
		
		for(int story_loop = 0; story_loop < number_of_story; story_loop++){
			//Set ID
			Integer id = result.get(story_loop).get("STORY_ID"); 
			storys.add(id);
			//Set Title
			titles.put(id, dba.getString("STORY_TITLE", "STORY", new String[]{"STORY_ID"}, new String[]{""+id}));
			//Set Thumb
			byte[] blob = dba.getBlob("STORY_THUMB", "STORY", new String[]{"STORY_ID"}, new String[]{""+id});
			Bitmap img;
			if(blob == null)
				img = null;
			else
				img = BitmapFactory.decodeByteArray(blob, 0, blob.length);
			thumbs.put(id, img);
		}
		storys.add(1);
		titles.put(1, "북풍과 태양");
		
	}
	
	public Integer[] getIDs(){
		Integer[] data = new Integer[storys.size()];
		for(int loop = 0; loop<data.length; loop++) data[loop] = storys.get(loop);
		return data;
	}
	public Bitmap getThumb(int num){
		return thumbs.get(num);
	}
	public int getThumbID(int num){
		return R.raw.thumb;
	}
	public String getTitle(int num){
		return titles.get(num);
	}
}

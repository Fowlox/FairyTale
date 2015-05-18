package com.fairytale;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Font extends Activity {
	
	//private static final String TYPEFACE_NAME = "cherryblossomb.otf";
	private static final String TYPEFACE_NAME = "GodoB.otf";
	
	private Typeface typeface;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadTypeface();
		setContentView(R.layout.activity_font); //폰트 변경할레이아웃 
	}
	
	private void loadTypeface(){
		if(typeface==null){
			typeface = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME);
		}
	}
	
	public void setContentView(int viewId){
		View view =LayoutInflater.from(this).inflate(viewId, null);
		ViewGroup group = (ViewGroup)view;
		
		int childCnt = group.getChildCount();
		
		for(int i=0; i<childCnt; i++){
			View v = group.getChildAt(i);
			if( v instanceof TextView){
				((TextView)v).setTypeface(typeface);
			}
		}
		super.setContentView(view);
	}
	
}

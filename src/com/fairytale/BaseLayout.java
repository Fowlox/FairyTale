package com.fairytale;

import android.content.Context;
import android.view.View;

public class BaseLayout {
	protected View view;
	
	protected BaseLayout(Context context, int layout){
		view = View.inflate(context, layout, null);
		mLog.d("Inflating View");
	}
	
	protected View findViewById(int id){
		return view.findViewById(id);
	}
	public View getView(){
		mLog.d("Get View: "+view.getId());
		return view;
	}
}
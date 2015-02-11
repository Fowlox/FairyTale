package com.fairytale;

import android.content.Context;
import android.view.View;

public class StoryLayout extends BaseLayout {

	public interface Listener {
		
	}
	
	Listener listener;
	View.OnClickListener on_click;
	
	public StoryLayout(Context context) {
		super(context, R.layout.intro_main_layout); //intro_main_layout 수정할 것
		setOnClick();
	}
	
	public void setListener(Listener listener){
		this.listener = listener;
	}

	private void setOnClick(){
		on_click = new View.OnClickListener() {
			public void onClick(View v) {
				if(listener != null){
					
				}
			}
		};
	}
}

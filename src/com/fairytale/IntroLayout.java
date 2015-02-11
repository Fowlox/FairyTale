package com.fairytale;

import android.content.Context;
import android.view.View;

public class IntroLayout extends BaseLayout {

	public interface Listener {
		
	}
	
	Listener listener;
	View.OnClickListener on_click;
	
	public IntroLayout(Context context) {
		super(context, R.layout.intro_main_layout);
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

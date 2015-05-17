package com.fairytale;

import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StoryListLayout extends BaseLayout {
	
	public interface Listener {
		public void goAdditionalStory();
		public void startStory(int story_id);
	}
	
	Listener listener;
	View.OnClickListener on_click;
	StoryListModel model;

	ImageButton additionalBtn;
	HashMap<Integer,ImageButton> story_btns;
	LinearLayout storys;
	
	public StoryListLayout(Context context){
		super(context, R.layout.storylist_main_layout);
		setOnClick();
		
		additionalBtn = (ImageButton)findViewById(R.id.storylist_additional_btn);
		storys = (LinearLayout)findViewById(R.id.storylist_listview); 
		
		additionalBtn.setOnClickListener(on_click);
	}
	
	public void setListener(Listener listener){
		this.listener = listener;
	}
	
	public void setModel(StoryListModel model){
		this.model = model;
	}

	public void genList(Context context){
		Integer[] ids = model.getIDs();
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout plate = null;
		for(int story_loop = 0; story_loop < ids.length; story_loop++){
			if(story_loop%6 == 0) 
				plate = (LinearLayout)inflater.inflate(R.layout.storylist_items_layout, null);
			
			final int id = ids[story_loop];
			ImageButton thumb = ((ImageButton) plate.findViewById(R.id.storylist_thumb1+(2*story_loop)));
			//thumb.setImageBitmap(model.getThumb(id));
			thumb.setImageResource(model.getThumbID(id));
			thumb.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					listener.startStory(id);
				}});
			thumb.setVisibility(View.VISIBLE);
			TextView title = ((TextView) plate.findViewById(R.id.storylist_title1+(2*story_loop)));
			title.setText(model.getTitle(id));
			title.setVisibility(View.VISIBLE);
			
			if(story_loop%6 == 5) storys.addView(plate);
			else if((story_loop+1) == ids.length) storys.addView(plate); 
		}
	}
	private void setOnClick(){
		on_click = new View.OnClickListener() {
			public void onClick(View v) {
				if(listener != null){
					if(v.getId() == R.id.storylist_additional_btn)
						listener.goAdditionalStory();
				}
			}
		};
	}
}
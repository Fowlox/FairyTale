package com.fairytale;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class StoryLayout extends BaseLayout {

	public interface Listener {
		public void pauseBtnHandler();
		public void preBtnHandler();
		public void nextBtnHandler();
		public boolean isPlaying();
		public void sceneStart();
	}
	
	Listener listener;
	View.OnClickListener on_click;
	
	public final static int VISIBLE = View.VISIBLE;
	public final static int INVISIBLE = View.INVISIBLE;
	
	private Button next;
	private Button prev;
	private Button pause;
	private ImageView story_scene;
	
	public StoryLayout(Context context) {
		super(context, R.layout.story_main_layout); //intro_main_layout 수정할 것
		setOnClick();
		
		pause = (Button)findViewById(R.id.pause);
		next = (Button)findViewById(R.id.next);
		prev = (Button)findViewById(R.id.prev);
		
		story_scene = (ImageView)findViewById(R.id.ws_scene);
		
		pause.setOnClickListener(on_click);
		next.setOnClickListener(on_click);
		prev.setOnClickListener(on_click);
	}
	
	public void setListener(Listener listener){
		this.listener = listener;
	}
	
	public void setImage(Drawable background){
		//story_scene.setBackground(background);
		story_scene.setImageDrawable(background);
	}
	
	public void setBtnVisible(int visible_type){
		next.setVisibility(visible_type);
		prev.setVisibility(visible_type);
	}
	public void setOneInvisible(int type){
		if(type == 0){
			prev.setVisibility(INVISIBLE);
		}else if(type == 1){
			next.setVisibility(INVISIBLE);
		}
	}

	private void setOnClick(){
		on_click = new View.OnClickListener() {
			public void onClick(View v) {
				if(listener != null){
					if(v.getId() == R.id.pause){
						if(listener.isPlaying()){
							listener.pauseBtnHandler();
						} else {
							listener.sceneStart();
						}
					}else if(v.getId() == R.id.next){
						listener.nextBtnHandler();
					}else if(v.getId() == R.id.prev){
						listener.preBtnHandler();
					}
				}
			}
		};
	}
}

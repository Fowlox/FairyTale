package com.fairytale;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

public class AudioSensorGameLayout extends BaseLayout {

	public interface Listener {
		public void pauseBtnHandler();
		public void preBtnHandler();
		public void nextBtnHandler();
		public boolean isPlaying();
		public void sceneStart();
	}
	
	private Listener listener;
	private View.OnClickListener on_click;
	private Context context;
	
	public final static int VISIBLE = View.VISIBLE;
	public final static int INVISIBLE = View.INVISIBLE;
	
	private ImageButton next;
	private ImageButton prev;
	private ImageButton pause;
	private ImageView story_scene;
	private AnimationDrawable ani_drawer;
	private BitmapDrawable[] bitmaps;
	
	public AudioSensorGameLayout(Context context) {
		super(context, R.layout.audio_sensor_game_activity); //intro_main_layout �닔�젙�븷 寃�
		this.context = context;
		setOnClick();
		
		pause = (ImageButton)findViewById(R.id.pause);
		next = (ImageButton)findViewById(R.id.next);
		prev = (ImageButton)findViewById(R.id.prev);
		
		story_scene = (ImageView)findViewById(R.id.ws_scene);
		
		pause.setOnClickListener(on_click);
		next.setOnClickListener(on_click);
		prev.setOnClickListener(on_click);
		mLog.d("make ASG Layout Object");
	}
	
	public void setListener(Listener listener){
		this.listener = listener;
	}
	
	public void setAnimationImage(int[] ids){
		recycleBitmap();
		bitmaps = new BitmapDrawable[ids.length];
		ani_drawer = new AnimationDrawable();
		ani_drawer.setOneShot(false);
		int duration = 1000;
		for(int loop = 0; loop < ids.length; loop++){
			mLog.d("Set Animation Frame: ids="+ids[loop]);
			bitmaps[loop] = (BitmapDrawable)context.getResources().getDrawable(ids[loop]);
			ani_drawer.addFrame(bitmaps[loop], duration);
		}
		story_scene.setImageDrawable(ani_drawer);
	}
	public void startAnimation(){
		ani_drawer.start();
	}
	public void stopAnimation(){
		ani_drawer.stop();
	}
	private void recycleBitmap(){
		if(bitmaps != null){
			for(int loop=0;loop<bitmaps.length;loop++)
				bitmaps[loop].getBitmap().recycle();
		}
	}
	
	public void setImage(Drawable background){
		//story_scene.setBackground(background);
		story_scene.setImageDrawable(background);
	}
	public void setImageID(int id){
		story_scene.setImageResource(id);
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
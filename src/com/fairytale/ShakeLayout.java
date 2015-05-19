package com.shake;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ShakeLayout extends BaseLayout{

	private Context context;
	private AnimationDrawable ani_drawer;
	private BitmapDrawable[] bitmaps;
	private ImageView shake_scene;
	private LinearLayout left_blank;
	private LinearLayout right_blank;
	
	protected ShakeLayout(Context context ) {
		super(context, R.layout.activity_shake); //intro_main_layout 수정할 것
		this.context = context;
		setOnClick();
		
		shake_scene = (ImageView)findViewById(R.id.ws_scene);
		left_blank = (LinearLayout)findViewById(R.id.left_blank);
		right_blank = (LinearLayout)findViewById(R.id.right_blank);
	}
	
	public void setAnimationImage(int[] ids){
		recycleBitmap();
		bitmaps = new BitmapDrawable[2];
		ani_drawer = new AnimationDrawable();
		ani_drawer.setOneShot(false);
		int duration = 1000;
		for(int loop = 0; loop < ids.length; loop++){
			mLog.d("Set Animation Frame: ids="+ids[loop]);
			bitmaps[loop] = (BitmapDrawable)context.getResources().getDrawable(ids[loop]);
			ani_drawer.addFrame(bitmaps[loop], duration);
		}
		shake_scene.setImageDrawable(ani_drawer);
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
	
	public void progressBurn(int progress){
		
		LinearLayout.LayoutParams lparam = (LinearLayout.LayoutParams) left_blank.getLayoutParams();
		LinearLayout.LayoutParams rparam = (LinearLayout.LayoutParams) right_blank.getLayoutParams();
		
		lparam.weight = progress;
		rparam.weight = 100-progress;
		
		left_blank.setLayoutParams(lparam);
		right_blank.setLayoutParams(rparam);
		
	}
	
	private void setOnClick(){
		
	}
}

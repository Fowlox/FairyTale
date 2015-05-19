package com.shake;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

public class ShakeLayout extends BaseLayout{

	private Context context;
	private AnimationDrawable ani_drawer;
	private BitmapDrawable[] bitmaps;
	private ImageView shake_scene;
	private ImageView zero_sun;
	private ImageView first_sun;
	private ImageView second_sun;
	private ImageView third_sun;
	private ImageView fourth_sun;
	private ImageView fivth_sun;
	
	protected ShakeLayout(Context context ) {
		super(context, R.layout.activity_shake); //intro_main_layout 수정할 것
		this.context = context;
		setOnClick();
		
		shake_scene = (ImageView)findViewById(R.id.ws_scene);
		zero_sun = (ImageView)findViewById(R.id.zero_sun);
		first_sun = (ImageView)findViewById(R.id.first_sun);
		second_sun = (ImageView)findViewById(R.id.second_sun);
		third_sun = (ImageView)findViewById(R.id.third_sun);
		fourth_sun = (ImageView)findViewById(R.id.fourth_sun);
		fivth_sun = (ImageView)findViewById(R.id.fivth_sun);
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
		shake_scene.setImageResource(R.drawable.scene21_1);
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
		switch (progress){
		case 20:
			zero_sun.setVisibility(View.INVISIBLE);
			first_sun.setVisibility(View.VISIBLE);
			break;
		case 40:
			first_sun.setVisibility(View.INVISIBLE);
			second_sun.setVisibility(View.VISIBLE);
			break;
		case 60:
			second_sun.setVisibility(View.INVISIBLE);
			third_sun.setVisibility(View.VISIBLE);
			break;
		case 80:
			third_sun.setVisibility(View.INVISIBLE);
			fourth_sun.setVisibility(View.VISIBLE);
			break;
		case 100:
			fourth_sun.setVisibility(View.INVISIBLE);
			fivth_sun.setVisibility(View.VISIBLE);
			break;
		}
		
	}
	
	private void setOnClick(){
		
	}
}

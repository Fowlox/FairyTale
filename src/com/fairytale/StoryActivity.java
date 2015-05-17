package com.fairytale;

import com.fairytale.R.drawable;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class StoryActivity extends Activity implements StoryLayout.Listener {
	public final static String STORY_ID = "com.fairytale.story_id";
	
	private MediaPlayer story_sound; //스토리 음악 재생용
	private StoryModel model;
	private StoryLayout layout;
	private DatabaseAccessModel database;
	private ImageDraw drawer;
	
	private int scene_no;
	private int images;
	private int[] image_ids;
	private boolean is_pause;
	
	private float volume;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	
		database = new DatabaseAccessModel(this);
		layout = new StoryLayout(this);
		model = new StoryModel(database, this, 1);
		//model.generateFromDB();
		layout.setListener(this);
		setContentView(layout.getView());
		
		story_sound = new MediaPlayer();//Media player 생
		story_sound.setLooping(false);//반복 설정 해제
		
		scene_no = 1;

		sceneInit();
	}
	
	public boolean isPlaying(){
		return story_sound.isPlaying();
	}
	
	public void sceneInit(){
		if(scene_no == 1) images = 1;
		else images = 2;//model.getItem(scene_no).numberOfImage();
		//layout.setImage(Converter.bitmapToDrawable(this,model.getItem(scene_no).getImage(0).getImage()));
		image_ids = new int[images];
		for(int loop=0;loop<images;loop++){
			if(scene_no == 1) image_ids[loop] = R.raw.scene01_1;
			else image_ids[loop] = R.raw.scene02_1+(scene_no-2)*3+loop;
		}
		try {
			//story_sound.setDataSource(model.getItem(scene_no).getSound(0).getAudio().toString());
			int audio_id;
			if(scene_no == 1) audio_id = R.raw.scene01;
			else audio_id = R.raw.scene02+(scene_no-2)*3;
			story_sound = MediaPlayer.create(getApplicationContext(), audio_id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		sceneStart();
	}
	
	public void sceneStart(){
		is_pause = false;
		//drawer = new ImageDraw();
		//drawer.execute();
		layout.setAnimationImage(image_ids);
		layout.startAnimation();
		if(!story_sound.isPlaying()){
			//story_sound.start();
			fadeIn();
		}
		//해당신이 끝나면 다음,이전 버튼 보이기
		
		fadeOut();
		story_sound.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				layout.stopAnimation();
				layout.setBtnVisible(StoryLayout.VISIBLE);
				if(scene_no == 0)
					layout.setOneInvisible(0);
				else if(scene_no == 25)
					layout.setOneInvisible(1);
			}
		});
	}
	
	//스토리(소리) 일시정지 팝업추가해야함
	public void storyPause(){
		if(story_sound.isPlaying()){
			story_sound.pause();
		} else {
			story_sound.start();
		}
	}
	public void fadeIn(){
		  volume = 0;
		  story_sound.setVolume(0, 0);
		  final Handler h = new Handler();
		  
		  h.postDelayed(new Runnable() {
		    private float time = 0.0f;
		  @Override
		  public void run() {
		    if(!story_sound.isPlaying())
		      story_sound.start();
		    time+=100;
		    story_sound.setVolume(volume, volume);
		      volume += 0.1;
		      if(time<=1000){
		        h.postDelayed(this, 100);
		      }
		  }
		  }, 100);
		}
		    
		public void fadeOut(){
		  int sec = story_sound.getDuration();
		  int cur_time=0;
		  while((sec-cur_time)<1000)
			 cur_time = story_sound.getCurrentPosition();
			
		  volume=1;
		  final Handler h = new Handler();
		  
		  h.postDelayed(new Runnable() {
		  private float time = 0.0f;
		  @Override
		  public void run() {
		    if(!story_sound.isPlaying())
		    	story_sound.start();
		    time+=1000;
		    story_sound.setVolume(volume, volume);
		      volume -= 0.1;
		      if(time<=1000){
		        h.postDelayed(this, 100);
		      }
		  }
		  }, 100);
		}

	@Override
	public void pauseBtnHandler() {
		//일시정지
		layout.stopAnimation();
		story_sound.pause();
		//storyPause();
		
		//일시적으로 토스트 이부분을 팝업뷰로 변경
		Toast toast = Toast.makeText(getApplicationContext(), "Story Pause", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		
		//재생
		story_sound.start();
		layout.startAnimation();
	}

	@Override
	public void preBtnHandler() {
		
		scene_no--;
		layout.setBtnVisible(StoryLayout.INVISIBLE);
		sceneInit();
	}

	@Override
	public void nextBtnHandler() {
		// TODO Auto-generated method stub
		scene_no++;
		layout.setBtnVisible(StoryLayout.INVISIBLE);
		sceneInit();
	}
	
	private class ImageDraw extends AsyncTask<Void, Integer, Void>{
		
		int current_img_no;
		int max_img_no;
		boolean pause;
		
		protected void onPreExecute(){
			super.onPreExecute();
			current_img_no = 0;
			max_img_no = images;
			pause = false;
		}
		
		@Override
		protected Void doInBackground(Void... params){
			while(true){
				if(this.isCancelled()){
					break;
				}
				publishProgress(0);
				if(pause){
					try {
						Thread.sleep(500);
						publishProgress(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
				}
				publishProgress(1,current_img_no);
				
				if(current_img_no < max_img_no) current_img_no++;
				else current_img_no = 0;
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}
			return null;
		}
		
		protected void onProgressUpdate(Integer... progress){
			if(progress[0] == 0) pause = is_pause; 
			else if(progress[0] == 1){
				int id;
				if(scene_no == 1) id = R.raw.scene01_1;
				else id = R.raw.scene02_1+(scene_no-2)*3+progress[1];
				mLog.d("progress image change");
				layout.setImageID(id);
			}
				//layout.setImage(Converter.bitmapToDrawable(getApplicationContext(),model.getItem(scene_no).getImage(progress[1]).getImage()));
		}
		
	}
}

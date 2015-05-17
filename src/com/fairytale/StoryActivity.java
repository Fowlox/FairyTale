package com.fairytale;

import com.fairytale.R.drawable;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
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
	private boolean is_pause;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	
		database = new DatabaseAccessModel(this);
		layout = new StoryLayout(this);
		model = new StoryModel(database, this, 1);
		model.generateFromDB();
		layout.setListener(this);
		setContentView(layout.getView());
		
		story_sound = new MediaPlayer();//Media player 생
		story_sound.setLooping(false);//반복 설정 해제
		
		scene_no = 0;

		sceneInit();
	}
	
	public boolean isPlaying(){
		return story_sound.isPlaying();
	}
	
	public void sceneInit(){
		images = model.getItem(scene_no).numberOfImage();
		//layout.setImage(Converter.bitmapToDrawable(this,model.getItem(scene_no).getImage(0).getImage()));
		try {
			story_sound.setDataSource(model.getItem(scene_no).getSound(0).getAudio().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		sceneStart();
	}
	
	public void sceneStart(){
		is_pause = false;
		drawer = new ImageDraw();
		if(!story_sound.isPlaying()){
			story_sound.start();
		}
		//해당신이 끝나면 다음,이전 버튼 보이기
		story_sound.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				drawer.cancel(true);
				layout.setBtnVisible(StoryLayout.VISIBLE);
				if(scene_no == 0)
					layout.setOneInvisible(0);
				else if(scene_no == model.numberOfItem()-1)
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

	@Override
	public void pauseBtnHandler() {
		//일시정지
		is_pause = true;
		story_sound.pause();
		//storyPause();
		
		//일시적으로 토스트 이부분을 팝업뷰로 변경
		Toast toast = Toast.makeText(getApplicationContext(), "Story Pause", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		
		//재생
		story_sound.start();
		is_pause = false;
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
			else if(progress[0] == 1)
				layout.setImage(Converter.bitmapToDrawable(getApplicationContext(),model.getItem(scene_no).getImage(progress[1]).getImage()));
		}
		
	}
}

package com.fairytale;

import com.example.fairtytale.StoryModel.ItemModel.SoundModel;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class StoryActivity extends Activity implements StoryLayout.LayoutListener{
	
	private MediaPlayer story_sound; //스토리 음악 재생용
	private StoryModel model;
	private StoryLayout layout;
	private DatabaseAccessModel database;

	private Button next;
	private Button prev;
	private Button pause;
	private FrameLayout story_scene;
	
	SoundModel sound_model;
	
	int scene_no=0;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	
		database = new DatabaseAccessModel(this);
		layout = new StoryLayout(this);
		model = new StoryModel(database, this, 1);
		model.generateFromDB();
		
		setContentView(layout.getView());
		
		pause = (Button)findViewById(R.id.pause);
		next = (Button)findViewById(R.id.next);
		prev = (Button)findViewById(R.id.prev);
		
		story_scene = (FrameLayout)findViewById(R.id.ws_scene);
		story_sound = new MediaPlayer();//Media player 생
		story_sound.setLooping(false);//반복 설정 해제
		

		sceneInit(scene_no);
		sceneStart();
		
		pause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(story_sound.isPlaying()){
					pauseBtnHandler();
				} else {
					sceneStart();
				}
			}
		});
		
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nextBtnHandler();
			}
		});
		
		prev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				preBtnHandler();
			}
		});
	}
	
	public void sceneInit(int scene_no){
		story_scene.setBackground(Converter.bitmapToDrawable(model.getItem(scene_no).getImage(0).getImage()));
		try {
			story_sound.setDataSource(model.getItem(scene_no).getSound(0).getAudio().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void sceneStart(){
		if(!story_sound.isPlaying()){
			story_sound.start();
		}
		//해당신이 끝나면 다음,이전 버튼 보이기
		story_sound.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				next.setVisibility(View.VISIBLE);
				prev.setVisibility(View.VISIBLE);
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
		// TODO Auto-generated method stub
		
		storyPause();
		//일시적으로 토스트 이부분을 팝업뷰로 변경
		Toast toast = Toast.makeText(getApplicationContext(), "Story Pause", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	@Override
	public void preBtnHandler() {
		// TODO Auto-generated method stub
		if(scene_no>1){
			scene_no--;
			sceneInit(scene_no);
			
			next.setVisibility(View.INVISIBLE);
			prev.setVisibility(View.INVISIBLE);
			
			sceneStart();
		} else {
			Toast toast = Toast.makeText(getApplicationContext(), "처음 장면입니다.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	@Override
	public void nextBtnHandler() {
		// TODO Auto-generated method stub
		scene_no++;
		sceneInit(scene_no);
		
		next.setVisibility(View.INVISIBLE);
		prev.setVisibility(View.INVISIBLE);
		
		sceneStart();
	}
	
}

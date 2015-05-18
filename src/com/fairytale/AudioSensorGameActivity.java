package com.fairytale;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

public class AudioSensorGameActivity extends Activity implements AudioSensorGameLayout.Listener{

	private MediaRecorder mRecorder = null;

	private StoryModel model;
	private AudioSensorGameLayout layout;
	private DatabaseAccessModel database;
	private int[] image_ids;

	private AudioAsyncTask audioAsyncTask;

	TextView textView;
	Button button1;
	Button button2;

	int blow_value = 0;

	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==0){
				blow_value = getAmplitude();
				mLog.d("amp : "+ blow_value);
				if(blow_value>30000){
					mLog.d("Amp in stop");
					thread.interrupt();
					stop();
					finish();
				}
			}
		}
	}; 

	Thread thread;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_main);

		database = new DatabaseAccessModel(this);
		layout = new AudioSensorGameLayout(this);
		model = new StoryModel(database, this, 1);
		//model.generateFromDB();
		layout.setListener(this);
		setContentView(layout.getView());

		//scene_no = 1;
		sceneInit();

		audioAsyncTask = new AudioAsyncTask();
		audioAsyncTask.execute();
		/*
		start();

		thread = new Thread(new Runnable() {
			public void run() { 

				while(true){
					Message msg = Message.obtain();
					msg.what = 0;
					mHandler.sendMessage(msg);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}   
			}
		});
		thread.start();
*/
	}
	public void start() {
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFile("/dev/null");
			try {
				mRecorder.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mRecorder.start();
		}
	}

	public void stop() {
		if (mRecorder != null) {
			mRecorder.stop();      
			mRecorder.release();
			mRecorder = null;
		}
	}

	public int getAmplitude() {
		if (mRecorder != null)
			return  mRecorder.getMaxAmplitude();
		else
			return 0;

	}

	public void sceneInit(){
		/*
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
		 */
		image_ids = new int[2];
		image_ids[0] = R.raw.sensor_game_1;
		image_ids[1] = R.raw.sensor_game_2;

		sceneStart();
	}

	public void sceneStart(){
		//is_pause = false;
		//drawer = new ImageDraw();
		//drawer.execute();
		layout.setAnimationImage(image_ids);
		layout.startAnimation();
		/*
		if(!story_sound.isPlaying()){
			//story_sound.start();
			fadeIn();
		}
		//占쎈퉸占쎈뼣占쎈뻿占쎌뵠 占쎄국占쎄돌筌롳옙 占쎈뼄占쎌벉,占쎌뵠占쎌읈 甕곌쑵�뱣 癰귣똻�뵠疫뀐옙

		fadeOut();
		story_sound.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				layout.stopAnimation();
				layout.setBtnVisible(AudioSensorGameLayout.VISIBLE);
				if(scene_no == 0)
					layout.setOneInvisible(0);
				else if(scene_no == 25)
					layout.setOneInvisible(1);
			}
		});*/

	}

	public class AudioAsyncTask extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			while(true){
				start();
				try {
					Thread.sleep(1000);
					blow_value = getAmplitude();
					mLog.d("apm : "+blow_value);
					if(blow_value>30000){
						return null;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
						
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			stop();
			onCancelled();
			finish();
		}
		
	}

	@Override
	public void pauseBtnHandler() {
		// TODO Auto-generated method stub

	}
	@Override
	public void preBtnHandler() {
		// TODO Auto-generated method stub

	}
	@Override
	public void nextBtnHandler() {
		// TODO Auto-generated method stub

	}
	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return false;
	}

} 

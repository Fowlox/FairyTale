package com.fairytale;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AudioSensorGameActivity extends Activity implements AudioSensorGameLayout.Listener{

	private MediaRecorder mRecorder = null;

	private AudioSensorGameLayout layout;
	private int[] image_ids;

	private AudioAsyncTask audioAsyncTask;

	TextView textView;
	Button button1;
	Button button2;

	int blow_value = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		mLog.d("start in ASG");
		setContentView(R.layout.audio_sensor_game_activity);

		layout = new AudioSensorGameLayout(this);
		layout.setListener(this);
		setContentView(layout.getView());

		sceneInit();
		
		audioSensing(3);

//		audioAsyncTask = new AudioAsyncTask();
//		audioAsyncTask.execute();
		
	}
	public void audioSensing(int looping){
		for(int sens_loop=0; sens_loop<looping;sens_loop++){
			while(true){
				start();
				try {
					Thread.sleep(1000);
					blow_value = getAmplitude();
					mLog.d("apm : "+blow_value);
					if(blow_value>30000){
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			stop();
		}
		mLog.d("finish in ASG");
		setResult(0);
		finish();
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
		image_ids = new int[2];
		image_ids[0] = R.drawable.sensor_game_1;
		image_ids[1] = R.drawable.sensor_game_2;

		sceneStart();
	}

	public void sceneStart(){
		layout.setAnimationImage(image_ids);
		layout.startAnimation();
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
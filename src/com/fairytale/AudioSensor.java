package com.fairytale;

import java.io.IOException;
import android.media.MediaRecorder;

public class AudioSensor{

	private MediaRecorder mRecorder = null;
	
	int blow_value = 0;
			
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
	
}

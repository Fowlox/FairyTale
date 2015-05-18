package com.fairytale;

import java.io.IOException;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioSensor {

	private MediaRecorder mRecorder = null;


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
			//Log.i("Deb", "stop1");
			mRecorder.stop();      
			//Log.i("Deb", "stop2");
			mRecorder.release();
			//Log.i("Deb", "stop3");
			mRecorder = null;
			//Log.i("Deb", "stop4");
		}
	}

	public int getAmplitude() {
		if (mRecorder != null)
			return  mRecorder.getMaxAmplitude();
		else
			return 0;

	}


}


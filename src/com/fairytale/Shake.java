package com.shake;

import android.app.Activity;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

public class Shake extends Activity implements SensorEventListener{

	private ShakeLayout layout;
	
	private long lasttime;
	private float speed;
	private float lastX;
	private float lastY;
	private float lastZ;
	private float x,y,z;
	
	private static final int SHAKE_THRESHHOLD = 200;
	private static final int DATA_X = SensorManager.DATA_X;
	private static final int DATA_Y = SensorManager.DATA_Y;
	private static final int DATA_Z = SensorManager.DATA_Z;
	
	private SensorManager sensorManager;
	private Sensor accelerormeterSensor;
	private SoundPool sp;
	private int ho;
	
	
	private int images;
	private int[] image_ids;
	
	private int count=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sp = new SoundPool(1,AudioManager.STREAM_MUSIC, 0);
		ho = sp.load(this, R.raw.ho, 1);

		layout = new ShakeLayout(this);
		scene_init();

		setContentView(layout.getView());
	}

	
	public void onStart(){
		super.onStart();
		if(accelerormeterSensor != null){
			sensorManager.registerListener(this, accelerormeterSensor, SensorManager.SENSOR_DELAY_GAME);
		}
	}
	
	public void onStop(){
		super.onStop();
		if(sensorManager !=null){
			sensorManager.unregisterListener(this);
		}
	}
	//센서값이 변하면 자동으로 실
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			long currentTime = System.currentTimeMillis();
			long gapoftime = (currentTime - lasttime);
			if(gapoftime > 100){
				lasttime = currentTime;
				x = event.values[SensorManager.DATA_X];
				y = event.values[SensorManager.DATA_Y];
				z = event.values[SensorManager.DATA_Z];
				
				speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gapoftime * 10000;
				
				if( speed > SHAKE_THRESHHOLD){
					//센서가 움직이면 실행되는 event 부분
					count++;
					if(count%20==0&&count<=100){
						layout.progressBurn(count);
					}
					
					if(count%30==0&&count<100){
						sp.play(ho, 1, 1, 1, 0, 1);
					}
					
					if(count == 100){
						//다음 스토리로 
						Toast toast = Toast.makeText(getApplicationContext(), "아이고 더워라", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER,0,-20);
						toast.show();
					}

				}
				
				lastX = event.values[DATA_X];
				lastY = event.values[DATA_Y];
				lastZ = event.values[DATA_Z];
			}
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}
	
	
	public void scene_init(){
		
		images=2;
		image_ids = new int[images];
		
		image_ids[0] = R.raw.scene21_1;
		image_ids[1] = R.raw.scene21_2;
		
		scene_start();
	}
	
	public void scene_start(){
		
		layout.setAnimationImage(image_ids);
		layout.startAnimation();
	}
}

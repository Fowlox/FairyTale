package com.fairytale;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

public class IntroActivity extends Activity implements IntroLayout.Listener{
	//전역변수
	private DatabaseAccessModel dba;
//	private ServerInteractionModel si;
	
	private IntroLayout layout;
	private IntroLoadingModel model;
	
	private boolean debuging_point = false;
	
	//액티비티 호출지점 콜백:	필요한 요소들의 전역변수 정의와 리스너와 화면을 설정하고, 
	//					비동기 작업을 호출한 뒤 메인 액티비티로 전환한다.
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mLog.d("Intro Activity Created");
	
		dba = new DatabaseAccessModel(this);
//		si = new ServerInteractionModel(this);
		layout = new IntroLayout(this);
		model = new IntroLoadingModel(getBaseContext(),dba);
		mLog.d("Intro Activity predeclare finished");
		
		layout.setListener(this);
		setContentView(layout.getView());

		mLog.d("Test Intro Activity Execution");
		
		new LoadingJob().execute();
		
	}
	
	//메소드가 호출되면 마켓으로 이동시켜주며 그 후 액티비티(어플리케이션)을 종료한다.
	private void goToStore(){
		mLog.i("Store directing");
		Intent market_intent = new Intent(Intent.ACTION_VIEW);
		market_intent.setData(Uri.parse("market://search?q=FairyTale"));
		startActivity(market_intent);
		mLog.d("Intro Activity finish");
		finish();
	}
	
	//메소드가 호출되면 메인 액티비티로 전환한다.
	//메인 액티비티가 종료되면, 돌아오자마자 해당 액티비티를 종료한다.
	private void changeToMain(){
		mLog.i("Change to Main Activity");
		startActivity(new Intent(IntroActivity.this, MainActivity.class));
		
		mLog.d("Intro Activity finish");
		finish();
	}
	
	//마켓 이동 버튼에 대한 액션
	public void storeDirectingBtn(boolean confirm) {
		//확인 버튼 동작
		if(confirm) goToStore();
		//종료 버튼 동작
		else finish();
	}
	
	//디버깅을 위한 버튼 액션
	public void debuging(){
		mLog.d("DE_point:"+debuging_point);
		debuging_point = false;
	}
	
	//비동기 작업을 위한 내부 클래스
	private class LoadingJob extends AsyncTask<Void, Integer, Boolean>{
		//플래그 상수
		private final Integer VERSION_CHECK = 1;
		private final Integer SERVER_CHECK = 2;
		private final Integer UPDATE_CHECK = 3;
		private final Integer DEBUGING = 4;
		private boolean status;
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... non) {
			mLog.d("Enter Background");
			status = true;
			publishProgress(DEBUGING,0);
			mLog.d("Debuging switch! status: "+status);
			//버전 체크
			publishProgress(VERSION_CHECK);
			mLog.d("Change View to Version Check");
			while(status){
				try{
					publishProgress(DEBUGING,1);
					Thread.sleep(10);
				}catch(Exception e){e.printStackTrace();}
			}
			status = true;
			publishProgress(DEBUGING,0);
			if(!versionCheck()) return false;
			//서버의 데이터 정보 체크
			publishProgress(SERVER_CHECK);
			while(status){
				try{
					publishProgress(DEBUGING,1);
					Thread.sleep(100);
				}catch(Exception e){e.printStackTrace();}
			}
			status = true;
			publishProgress(DEBUGING,0);
			getUpdateInfo();
			while(status){
				try{
					publishProgress(DEBUGING,1);
					Thread.sleep(100);
				}catch(Exception e){e.printStackTrace();}
			}
			status = true;
			publishProgress(DEBUGING,0);
			//서버와 디바이스간 비교
			publishProgress(UPDATE_CHECK);
			getDataFromServer();
			while(status){
				try{
					publishProgress(DEBUGING,1);
					Thread.sleep(100);
				}catch(Exception e){e.printStackTrace();}
			}
			publishProgress(DEBUGING,0);
			return true;
		}
		
		protected void onProgressUpdate(Integer... progress){
			super.onProgressUpdate(progress);
			//mLog.d("Enter Progress Update");
			if(progress[0] == VERSION_CHECK){
				layout.setProgressDialog(R.string.intro_version_check);
			}else if(progress[0] == SERVER_CHECK){
				layout.setProgressDialog(R.string.intro_update_check);
			}else if(progress[0] == UPDATE_CHECK){
				
			}else if(progress[0] == DEBUGING){
				if(progress[1] == 0) {
					mLog.d("In Progress Debuging:"+progress.length);
					for(int i=0;i<progress.length;++i) mLog.d(progress[i].toString());
					debuging_point = true;
				}
				else{
					if(!debuging_point) status = false;
				}
			}
			//mLog.d("Exist Progress Update");
		}
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			mLog.d("Finished Asynchronous Task");
			if(result)
				changeToMain();
			else
				layout.viewUpdatePopup();
		}
		private boolean versionCheck(){
			// 서버측에서 최신 어플리케이션 버전 정보 받아오기
			String[] query = new String[1];
			query[0] = "version";
			String[][] result = new String[][]{{"1.0"}};//si.getData(query);
			return model.getAppVersion().equals(result[0][0]);
		}
		private void getUpdateInfo(){
			//스토리1-씬3-이미지6,음성3
			//추가리스트1
			HashMap<Integer,HashMap<Integer,Integer[]>> info = new HashMap<Integer,HashMap<Integer,Integer[]>>();
			HashMap<Integer,Integer[]> item_map = new HashMap<Integer,Integer[]>();
			Integer[][] items = new Integer[3][];
			for(int item_loop = 0; item_loop<items.length;item_loop++){
				items[item_loop] = new Integer[]{2,1,0,0};
				item_map.put(item_loop, items[item_loop]);
			}
			
			info.put(1, item_map);
		}
		private void getDataFromServer(){
			
		}
		
	}
}

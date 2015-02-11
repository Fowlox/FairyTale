package com.fairytale;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

public class IntroActivity extends Activity implements IntroLayout.Listener{
	//전역변수
	private DatabaseAccessModel dba;
	private ExternalDataAccessModel eda;
	private ServerInteractionModel si;
	
	private IntroLayout layout;
	private IntroLoadingModel model;
	
	//액티비티 호출지점 콜백:	필요한 요소들의 전역변수 정의와 리스너와 화면을 설정하고, 
	//					비동기 작업을 호출한 뒤 메인 액티비티로 전환한다.
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mLog.d("Intro Activity Created");
		
		dba = new DatabaseAccessModel(this);
		eda = new ExternalDataAccessModel();
		si = new ServerInteractionModel(this);
		layout = new IntroLayout(this);
		model = new IntroLoadingModel();
		mLog.d("Intro Activity predeclare finished");
		
		layout.setListener(this);
		setContentView(layout.getView());
		
		mLog.d("Test Intro Activity Execution");
		
//		new LoadingJob().execute();
//		mLog.d("Finished Asynchronous Task");
//		changeToMain();
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
		//취소 버튼 동작
		else finish();
	}
	
	//비동기 작업을 위한 내부 클래스
	private class LoadingJob extends AsyncTask<Void, Integer, Void>{
		//플래그 상수
		private final Integer VERSION_CHECK = 1;
		private final Integer SERVER_CHECK = 2;
		private final Integer UPDATE_CHECK = 3;

		@Override
		protected Void doInBackground(Void... non) {
			//버전 체크
			publishProgress(VERSION_CHECK);
			if(!versionCheck())
				getNewVersion();
			//서버의 데이터 정보 체크
			publishProgress(SERVER_CHECK);
			getUpdateInfo();
			//서버와 디바이스간 비교
			checkUpdate();
			return null;
		}
		
		protected void onProgressUpdate(Integer... progress){
			if(progress[0] == VERSION_CHECK){

			}else if(progress[0] == SERVER_CHECK){
				
			}else if(progress[0] == UPDATE_CHECK){
				
			}
		}
		private boolean versionCheck(){
			return true;
		}
		private void getNewVersion(){
			
		}
		private void getUpdateInfo(){
			
		}
		private void checkUpdate(){
			publishProgress(UPDATE_CHECK);
			getDataFromServer();
		}
		private void getDataFromServer(){
			
		}
		
	}
}

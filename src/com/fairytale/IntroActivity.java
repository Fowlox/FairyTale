package com.fairytale;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
//		try {
//			fillDatabase();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		changeToMain();
		//new LoadingJob(this).execute();
		
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
	
	private void fillDatabase() throws Exception{
		if(dba.getInt("STORY_ID", "STORY", new String[]{"STORY_ID"}, new String[]{"1"}) == -1){
			//set data
			//HashMap<Integer,HashMap<Integer,Integer[]>> info = new HashMap<Integer,HashMap<Integer,Integer[]>>();
			//HashMap<Integer,Integer[]> item_map = new HashMap<Integer,Integer[]>();
			mLog.i("Start getUpdateInfo");
			mLog.d("get information of story_id:"+1);
			int story_id = 1;
			
			//AssetManager ast_mgr = this.getResources().getAssets();
			//InputStream is = getResources().openRawResource(R.raw.info);
			//InputStream is = this.getResources().openRawResource(R.raw.info);
			//InputStreamReader isr = new InputStreamReader(is,"utf-8");
			//BufferedReader reader1 = new BufferedReader(isr);
			
			//String[] tdatas = reader1.readLine().split(",");
			String[]tdatas = new String[]{"북풍과 태양","25"};
			//AssetManager tast_mgr = this.getResources().getAssets();
			InputStream tis = this.getResources().openRawResource(R.drawable.thumb);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Converter.drawableToBitmap(Drawable.createFromStream(tis, null)).compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] blob_thumb = stream.toByteArray();
			
			ContentValues table_recoder = new ContentValues();
			table_recoder.put("story_id", story_id);
			table_recoder.put("story_title", tdatas[0]);
			table_recoder.put("story_thumb", blob_thumb);
			dba.insertContentValue("story", table_recoder);
			mLog.d("sroty info -> title:"+tdatas[0]+", number of scene:"+tdatas[1]);
			table_recoder.clear();
			stream.close();
			tis.close();
			//tast_mgr.close();
			
			for(int item_id=1; item_id<=Integer.parseInt(tdatas[1]); item_id++){
				String[] idatas;
				if(item_id == 1) idatas = new String[]{"story","1","1","0","0"};
				else idatas = new String[]{"story","2","1","0","0"};
				
				ContentValues item_recoder = new ContentValues();
				item_recoder.put("story_id", story_id);
				item_recoder.put("item_id", item_id);
				item_recoder.put("item_type", idatas[0]);
				dba.insertContentValue("item", item_recoder);
				item_recoder.clear();
				mLog.d("scene info -> type:"+idatas[0]+", num_img:"+idatas[1]+", num_sound:"+idatas[2]+", num_bgm:"+idatas[3]+", num_txt:"+idatas[4]);
				for(int image_id=1; image_id<=Integer.parseInt(idatas[1]); image_id++){
					//AssetManager iast_mgr = this.getResources().getAssets();
					InputStream iis = this.getResources().openRawResource(model.getImageResource(item_id, image_id));
					ByteArrayOutputStream istream = new ByteArrayOutputStream();
					Converter.drawableToBitmap(Drawable.createFromStream(iis, null)).compress(Bitmap.CompressFormat.PNG, 100, istream);
					byte[] image_blob = istream.toByteArray();
					
					ContentValues image_recoder = new ContentValues();
					image_recoder.put("story_id", story_id);
					image_recoder.put("item_id", item_id);
					image_recoder.put("image_num", image_id);
					image_recoder.put("image", image_blob);
					dba.insertContentValue("image", image_recoder);
					image_recoder.clear();
					istream.close();
					iis.close();
					//iast_mgr.close();
				}
				//AssetManager sast_mgr = this.getResources().getAssets();
				InputStream sis = this.getResources().openRawResource(model.getSoundResource(item_id));
				ByteArrayOutputStream sstream = new ByteArrayOutputStream();
				Converter.drawableToBitmap(Drawable.createFromStream(sis, null)).compress(Bitmap.CompressFormat.PNG, 100, sstream);
				byte[] sound_blob = sstream.toByteArray();
				
				ContentValues sound_recoder = new ContentValues();
				sound_recoder.put("story_id", story_id);
				sound_recoder.put("item_id", item_id);
				sound_recoder.put("sound", sound_blob);
				dba.insertContentValue("sound", sound_recoder);
				sound_recoder.clear();
				sstream.close();
				sis.close();
				//sast_mgr.close();
			}
			//reader1.close();
			//isr.close();
			//is.close();
//			ast_mgr.close();
		}
		mLog.d("db:story----------\n"+dba.selectQuery("story", new String[]{"story_id","story_title"}, null, null, null, null, null).toString());
		mLog.d("db:item----------\n"+dba.selectQuery("item", new String[]{"story_id","item_id","item_type"}, null, null, null, null, null).toString());
		mLog.d("db:image----------\n"+dba.selectQuery("image", new String[]{"story_id","item_id","image_num"}, null, null, null, null, null).toString());
		mLog.d("db:sound----------\n"+dba.selectQuery("sound", new String[]{"story_id","item_id"}, null, null, null, null, null).toString());
	}
	
	//비동기 작업을 위한 내부 클래스
	private class LoadingJob extends AsyncTask<Void, Integer, Boolean>{
		//플래그 상수
		private final Integer VERSION_CHECK = 1;
		private final Integer SERVER_CHECK = 2;
		private final Integer UPDATE_CHECK = 3;
		private final Integer DEBUGING = 4;
		private final Integer UPDATE_PROGRESS = 5;
		private boolean status;
		
		private Context context;
		private InputStream ais;
		private int result;
		
		public LoadingJob(Context context){
			this.context = context;
			ais = null;
		}
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
			//getUpdateInfo();
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
//			try {
//				getDataFromServer();
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
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
			}else if(progress[0] == UPDATE_PROGRESS){
				Context context = getApplicationContext();
				AssetManager asset_mgr = context.getAssets();
				if(progress[1] == 0){
					try {
						ais = asset_mgr.open("story_"+progress[2]+"/info.story");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else if(progress[1] == 1){
					try {
						ais = asset_mgr.open("story_"+progress[2]+"/thumb.png");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else if(progress[1] == 2){
					try {
						ais = asset_mgr.open("story_"+progress[2]+"/scene"+progress[3]+"-"+progress[4]+".png");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else if(progress[1] == 3){
					try {
						ais = asset_mgr.open("story_"+progress[2]+"/scene"+progress[3]+".mp3");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else if(progress[1] == 4){
					result = dba.getInt("STORY_ID", "STORY", new String[]{"STORY_ID"}, new String[]{""+progress[2]});
				}
				asset_mgr.close();
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
			mLog.i("Start getUpdateInfo");
			try {
				mLog.d("get information of story_id:"+1);
				publishProgress(UPDATE_PROGRESS,0,1);
				//BufferedReader reader = new BufferedReader(new InputStreamReader(asset_mgr.open("story_"+1+"/info.story")));
				BufferedReader reader = new BufferedReader(new InputStreamReader(ais));
				String[] datas = reader.readLine().split(",");
				mLog.d("sroty info -> title:"+datas[0]+", number of scene:"+datas[1]);
				Integer[][] items = new Integer[Integer.parseInt(datas[1])][];
				for(int item_loop = 0; item_loop<items.length;item_loop++){
					datas = reader.readLine().split(",");
					mLog.d("scene info -> type:"+datas[0]+", num_img:"+datas[1]+", num_sound:"+datas[2]+", num_bgm:"+datas[3]+", num_txt:"+datas[4]);
					items[item_loop] = new Integer[]{Integer.parseInt(datas[1]),Integer.parseInt(datas[2]),Integer.parseInt(datas[3]),Integer.parseInt(datas[4])};
					item_map.put(item_loop, items[item_loop]);
				}
				
				info.put(1, item_map);
				model.setUpdate(info);
			} catch (IOException e) {
				e.printStackTrace();
			}
			mLog.i("End getUpdateInfo");
		}
		private void getDataFromServer() throws Exception{
			mLog.i("Start getDataFromServer");
			Integer[] ids = model.storyUpdateList();
			InputStream is;
			for(int update_loop = 0; update_loop<ids.length; update_loop++){
				publishProgress(UPDATE_PROGRESS,4,ids[update_loop]);
//				if(dba.getInt("STORY_ID", "STORY", new String[]{"STORY_ID"}, new String[]{""+ids[update_loop]}) == -1){
				if(result == -1){
					int s_id = ids[update_loop];
					publishProgress(UPDATE_PROGRESS,0,1);
//					BufferedReader reader = new BufferedReader(new InputStreamReader(asset_mgr.open("story_"+1+"/info.story")));
					BufferedReader reader = new BufferedReader(new InputStreamReader(ais));
					String[] datas = reader.readLine().split(",");
					
					StoryModel story = new StoryModel(dba, context, s_id);
					story.setTitle(datas[0]);
					
					publishProgress(UPDATE_PROGRESS,1,1);
					is = context.getAssets().open("story_"+s_id+"/thumb.png");
					story.setThumbnail(Converter.drawableToBitmap(Drawable.createFromStream(ais, null)));
					is.close();
					
					story.initialItem(model.itemUpdateLength(s_id));
					for(int item_loop=0; item_loop<model.itemUpdateLength(s_id); item_loop++){
						datas = reader.readLine().split(",");
						story.getItem(item_loop).setType(datas[0]);
						story.getItem(item_loop).initialItem(Integer.parseInt(datas[1]), Integer.parseInt(datas[2]), Integer.parseInt(datas[3]), Integer.parseInt(datas[4]));
						for(int image_loop=0; image_loop<model.imageUpdateLength(s_id, item_loop); image_loop++){
							publishProgress(UPDATE_PROGRESS,2,s_id,item_loop+1,image_loop+1);
							is = context.getAssets().open("story_"+s_id+"/scene"+(item_loop+1)+"-"+(image_loop+1)+".png");	
							story.getItem(item_loop).getImage(image_loop).setImage(Converter.drawableToBitmap(Drawable.createFromStream(ais, null)));
							is.close();
						}
						for(int sound_loop=0; sound_loop<model.soundUpdateLength(s_id, item_loop); sound_loop++){
							File file = File.createTempFile("temp_"+System.currentTimeMillis(), "mp3", context.getCacheDir());
							publishProgress(UPDATE_PROGRESS,3,s_id,item_loop+1);
							is = context.getAssets().open("story_"+s_id+"/scene"+(item_loop+1)+".mp3");
							OutputStream os = new FileOutputStream(file);
							byte[] buffer = new byte[1024];
							int len = 0;
							while((len = ais.read(buffer))>0) os.write(buffer, 0, len);
							story.getItem(item_loop).getSound(sound_loop).setAudio(file);
							os.close();
							is.close();
						}
						for(int bgm_loop=0; bgm_loop<model.bgmUpdateLength(s_id, item_loop); bgm_loop++){
							story.getItem(item_loop).getBgm(bgm_loop).setAudio(null);
						}
						for(int text_loop=0; text_loop<model.textUpdateLength(s_id, item_loop); text_loop++){
							story.getItem(item_loop).getText(text_loop).setText(null);
						}
					}					
					story.insertToDatabase();
				}
			}
			mLog.i("End getDataFromServer");
		}
	}
}

package com.fairytale;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class ServerInteractionModel {
	private final String SERVER_ADDR = "http://104.167.103.95/Android/FairyTale/ServerConnnect.jsp";
	
	private DefaultHttpClient client;
	private String output;
	
	public ServerInteractionModel(Context context){
		client = new DefaultHttpClient();
		
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 5000);
	}
	
	public void sendMsg(String msg){
		//들어온 메세지가 null일 경우 예외처리
		if(msg == null){
			mLog.e("ServerInt: No message sended");
			return;
		}
		
		try{
			//서버에 보낼 쿼리 생성
			HttpPost request = new HttpPost(SERVER_ADDR+"?msg="+msg);
			
			HttpResponse response = client.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
			
			String buf = null;
			String result = "";
			
			while((buf = reader.readLine()) != null){
				result += buf;
			}
			
			output = result;
			mLog.d("ServerInt: Get Data");
			mLog.d("         : "+output);
		}catch(Exception e){
			e.printStackTrace();
			client.getConnectionManager().shutdown();
		}
	}
	
	public String[][] getData(String[] selection){
		try{
			JSONObject json = new JSONObject(output);
			JSONArray j_array = json.getJSONArray("List");
			
			String[][] result = new String[j_array.length()][selection.length];
			for(int j_loop = 0; j_loop < j_array.length(); j_loop++){
				json = j_array.getJSONObject(j_loop);
				if(json != null){
					for(int select_loop = 0; select_loop < selection.length; select_loop++){
						result[j_loop][select_loop] = json.getString(selection[select_loop]);
					}
				}
			}
			
			return result;
			
		}catch(JSONException e){
			e.printStackTrace();
			return null;
		}
	}
}

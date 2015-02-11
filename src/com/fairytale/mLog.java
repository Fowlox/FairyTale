package com.fairytale;

import android.util.Log;

public class mLog {
	
	public static void e(String msg){
		Log.e("error", msg);
	}
	public static void d(String msg){
		Log.d("debug", msg);
	}
	public static void i(String msg){
		Log.i("info", msg);
	}
}

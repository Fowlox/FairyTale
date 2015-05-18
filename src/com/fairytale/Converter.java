package com.fairytale;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Converter {
	public static Bitmap drawableToBitmap(Drawable drawable){
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable)drawable).getBitmap();
		}
	 
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
	 
		return bitmap;
	}
	public static Drawable bitmapToDrawable(Context context, Bitmap bitmap){
		Drawable drawable = new BitmapDrawable(context.getResources(),bitmap);
		return drawable;
	}
	public static byte[] fileToByte(File audio){
		byte[] byte_audio;
		try{
			byte_audio = new byte[(int) audio.length()];
			FileInputStream fis = new FileInputStream(audio);
			fis.read(byte_audio);
			fis.close();
		}catch(IOException e){
			mLog.e("Error in translate byte to File!");
			e.printStackTrace();
			byte_audio = null;
		}
		return byte_audio;
	}
	public static File byteToFile(byte[] byte_audio, Context context){
		File audio;
		try {
			audio = File.createTempFile("temp_"+System.currentTimeMillis(), "mp3", context.getCacheDir());
			audio.deleteOnExit();
			FileOutputStream fos = new FileOutputStream(audio);
			fos.write(byte_audio);
			fos.close();
		} catch (IOException e) {
			mLog.e("Error in translate byte to File!");
			e.printStackTrace();
			audio = null;
		} 
		return audio;
	}
}

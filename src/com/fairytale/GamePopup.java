package com.fairytale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class GamePopup extends Activity {

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.
		//                LayoutParams.FLAG_DIM_BEHIND);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setContentView(R.layout.game_popup);
		
		Intent intent = getIntent(); 
        if(intent != null){
        	ImageView title = (ImageView) findViewById(R.id.popup_title);
            ImageButton img_btn = (ImageButton)findViewById(R.id.game_popup_btn);
            TextView txt_context = (TextView) findViewById(R.id.game_popup_context);
            
            title.setImageResource(intent.getExtras().getInt("popup_title"));
            
            String tmp_string = intent.getStringExtra("popup_context");
            txt_context.setText(tmp_string);
            
            img_btn.setImageResource(intent.getExtras().getInt("popup_btn"));
        } 
	}

	public void onClick(View view){
		if(view.getId() == R.id.game_popup_btn) {
			finish();
		}
	}
}
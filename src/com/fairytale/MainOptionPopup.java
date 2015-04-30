package com.fairytale;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * Created by Administrator on 2015-01-19.
 */
public class MainOptionPopup extends Activity {
    PopupWindow popup;
    View popupview;
    LinearLayout linear;
    private boolean BGM_SWITCH_ON = true;
    private boolean NARR_SWITCH_ON = true;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.
                LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.popup_option);

//        popupview = getLayoutInflater().inflate(R.layout.popup_option, null);
//        popup = new PopupWindow(popupview, 400, 300, true);
//        setContentView(popupview);
//        popup.setAnimationStyle(-1);

    }

    public void onClick(View view){
        if(view.getId() == R.id.bgm_switch) {
            if (BGM_SWITCH_ON) {
                view.setBackgroundResource(R.drawable.bgm_switch_off);
//                AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
//                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                BGM_SWITCH_ON = false;
            }
            else {
                view.setBackgroundResource(R.drawable.bgm_switch_on);
                BGM_SWITCH_ON = true;
            }
        }
        else if(view.getId() == R.id.narr_switch) {
            if (NARR_SWITCH_ON) {
                view.setBackgroundResource(R.drawable.narr_switch_off);
                NARR_SWITCH_ON = false;
            }
            else {
                view.setBackgroundResource(R.drawable.narr_switch_on);
                NARR_SWITCH_ON = true;
            }
        }
        else if(view.getId() == R.id.x_close_btn) {
           finish();
        }
    }
}

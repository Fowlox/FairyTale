package com.fairytale;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Administrator on 2015-01-19.
 */
public class MainDescribeActivity extends Activity {
    private int counter = 1;

    private boolean BGM_SWITCH_ON = true;
    private boolean NARR_SWITCH_ON = true;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.
//                LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.popup_describe);
    }

    public void onClick(View view) {
        TextView text = (TextView) findViewById(R.id.current_page);
        TextView des = (TextView) findViewById(R.id.describe_text);
        ImageButton rightBtn = (ImageButton) findViewById(R.id.right_btn);
        ImageButton leftBtn = (ImageButton) findViewById(R.id.left_btn);

        switch (view.getId()) {
            case R.id.x_close_btn:
                finish();
                break;

            case R.id.left_btn:
                --counter;
                changeText(counter);
                text.setText(counter + "/5");
                if (counter == 1) {
                    leftBtn.setVisibility(View.INVISIBLE);
                }
                rightBtn.setVisibility(View.VISIBLE);
                break;

            case R.id.right_btn:
                ++counter;
                changeText(counter);
                text.setText(counter + "/5");
                if (counter == 5) {
                    rightBtn.setVisibility(View.INVISIBLE);
                }
                leftBtn.setVisibility(View.VISIBLE);
                break;
        }
    }
    public void changeText(int num){
        TextView des = (TextView) findViewById(R.id.describe_text);
        switch(num) {
            case 1:
                des.setText(R.string.des1);
                break;
            case 2:
                des.setText(R.string.des2);
                break;
            case 3:
                des.setText(R.string.des3);
                break;
            case 4:
                des.setText(R.string.des4);
                break;
            case 5:
                des.setText(R.string.des5);
                break;
        }
    }
}

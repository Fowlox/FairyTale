package com.fairytale;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Administrator on 2015-01-19.
 */
public class MainLayout extends BaseLayout {

    protected MainLayout(Context context){
        super(context, R.layout.activity_main);
        setOnClick();
        setOnTouch();
        btnSetting();
    }

    interface LayoutListener {
        public void Btn1Handler();
        public void Btn2Handler();
        public void Btn3Handler();
    }

    private LayoutListener listener;
    private View.OnClickListener on_click;
    private View.OnTouchListener on_touch;

    public void setListener(LayoutListener listener){
        this.listener = listener;
    }


    private void btnSetting(){
        ImageButton start_btn = (ImageButton) findViewById(R.id.right_btn);
        ImageButton describe_btn = (ImageButton) findViewById(R.id.left_btn);
        ImageButton option_btn = (ImageButton) findViewById(R.id.x_close_btn);

//        start_btn.setAlpha((float) 20);
        start_btn.setOnClickListener(on_click);
        start_btn.setOnTouchListener(on_touch);

        describe_btn.setOnClickListener(on_click);
        describe_btn.setOnTouchListener(on_touch);

        option_btn.setOnClickListener(on_click);
        option_btn.setOnTouchListener(on_touch);
    }

    public void viewDetail(){
    }

    private void setOnTouch(){
        on_touch = new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                ImageButton view = (ImageButton) v;
//                view.setScaleType(ImageView.ScaleType.CENTER);    //layout_weight�븣臾몄씤吏� view�뱾�씠 ��吏곸씤�떎. (setPadding�쓣 二쇰㈃..) �굹以묒뿉 洹몃깷 洹몃┝ �븯�굹瑜� �뜑 �꽔�뒗寃껋쑝濡� ��泥�
                if(listener != null){
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN :
//                            view.setPadding(2, 2, 2, 2);
//                            view.setCropToPadding(true);
                            view.setColorFilter(0x55000000);    //Mode �깮�왂
                            break;

                        case MotionEvent.ACTION_MOVE:
                            if (!view.isPressed()){
                                view.setColorFilter(0x00000000);
                            }
                            break;

                        case MotionEvent.ACTION_UP :
//                            view.setPadding(0,0,0,0);
                            view.setColorFilter(0x00000000);
                            break;
                    }
                }
                return false;
            }
        };
    }

    private void setOnClick(){
        on_click = new View.OnClickListener(){
            public void onClick(View view){
                if(listener != null){
                    switch (view.getId()){
                        case R.id.right_btn:
//                            ImageButton start_btn = (ImageButton) findViewById(R.id.start_btn);   OnClick
//                            start_btn.setImageResource(R.drawable.start_btn_click);
                            listener.Btn1Handler();
//                            start_btn.setImageResource(R.drawable.start_btn);
                            break;

                        case R.id.left_btn:
                            listener.Btn2Handler();
                            break;

                        case R.id.x_close_btn:
                            listener.Btn3Handler();
                            break;
                    }
                }
            }
        };
    }
}

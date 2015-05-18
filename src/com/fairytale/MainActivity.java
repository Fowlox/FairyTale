package com.fairytale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;


public class MainActivity extends Activity implements MainLayout.LayoutListener {
    private MainLayout main_layout;
//    private MainDescribeModel main_describe_model;

    //    private static final String Tag = "AsyncPlay";
//    private AsyncPlayer myAsync = null;
    private MediaPlayer my_media = null;

//    Uri music = Uri.parse("android.resource://com.example.administrator.imagetest/"+R.raw.night_of_the_piano);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main_layout = new MainLayout(this);
//        main_describe_model = new MainDescribeModel();

        setContentView(main_layout.getView());
        main_layout.setListener(this);
//        myAsync = new AsyncPlayer(Tag);
        my_media = MediaPlayer.create(this, R.raw.night_of_the_piano);
    }

    protected void onPause(){
//        AudioManager am = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);;
//        am.setStreamMute(AudioManager.STREAM_MUSIC, false);
        super.onPause();
//        myAsync.stop();
        my_media.pause();
    }

    protected void onResume(){
        super.onResume();
//        myAsync.play(this, music, true, AudioManager.STREAM_MUSIC);
        if (!my_media.isPlaying()) {
            my_media.start();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setTitle("종료")
                        .setMessage("종료 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                myAsync.stop();
                                my_media.stop();
                                finish();
                            }
                        })
                        .setNegativeButton("아니오", null).show();
                return false;
            default:
                return false;
        }
    }

    @Override
    public void Btn1Handler() {
        Intent storyListAct = new Intent(this, StoryListActivity.class);
        startActivity(storyListAct);
    }

    @Override
    public void Btn2Handler() {
    	main_layout.setBackground(true);
        Intent howToPopup = new Intent(this, MainDescribeActivity.class);
        startActivity(howToPopup);
        main_layout.setBackground(false);
    }

    @Override
    public void Btn3Handler() {
    	main_layout.setBackground(true);
        Intent optionPopup = new Intent(this, MainOptionPopup.class);
        startActivity(optionPopup);
        main_layout.setBackground(false);
    }

    public MediaPlayer getMediaPlayer(){
        return my_media;
    }
}

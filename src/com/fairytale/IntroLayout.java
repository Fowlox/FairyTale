package com.fairytale;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class IntroLayout extends BaseLayout {
	//해당 레이아웃에서 사용할 리스너의 인터페이스: Controller쪽에서 implements한다.
	public interface Listener {
		public void storeDirectingBtn(boolean confirm);
		public void debuging();
	}
	//전역변수
	private Listener listener;
	private View.OnClickListener on_click;
	//뷰 요소
	private TextView progress_dialog;
	private ProgressBar progress;
	private LinearLayout popup_layout;
	private ImageButton popup_confirm_btn;
	private ImageButton popup_exit_btn;
	
	private Button test_btn;
	
	//생성자:BaseLayout의 생성자를 호출
	public IntroLayout(Context context){
		super(context, R.layout.intro_main_layout);
//		setOnClick();
		setElemOption();
	}
	//리스너를 설정하는 메소드
	public void setListener(Listener listener){
		this.listener = listener;
		setOnClick();
		
		popup_confirm_btn.setOnClickListener(on_click);
		popup_exit_btn.setOnClickListener(on_click);
		test_btn.setOnClickListener(on_click);
	}

	//외부 호출 메소드
	public void setProgressDialog(int msg){
		progress_dialog.setText(msg);
	}
	public void setProgressBar(){
		
	}
	public void viewUpdatePopup(){
		popup_layout.setVisibility(View.VISIBLE);
	}
	
	//사용할 뷰 요소 세팅
	private void setElemOption(){
		progress_dialog = (TextView) findViewById(R.id.intro_progress_dialog);
		progress = (ProgressBar) findViewById(R.id.intro_progress);
		popup_layout = (LinearLayout) findViewById(R.id.intro_popup_layout);
		popup_confirm_btn = (ImageButton) findViewById(R.id.intro_popup_confirm);
		popup_exit_btn = (ImageButton) findViewById(R.id.intro_popup_exit);
		
		test_btn = (Button) findViewById(R.id.intro_debug_btn);
	}
	//버튼 클릭에 대한 액션 핸들러
	private void setOnClick(){
		on_click = new View.OnClickListener() {	
			public void onClick(View v) {
				if(listener != null){
					if(v.getId()==popup_confirm_btn.getId()){
						listener.storeDirectingBtn(true);
					}else if(v.getId() == popup_exit_btn.getId()){
						listener.storeDirectingBtn(false);
					}else if(v.getId() == test_btn.getId()){
						listener.debuging();
					}
				}
			}
		};
	}

}

package com.fairytale;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntroLayout extends BaseLayout {
	//해당 레이아웃에서 사용할 리스너의 인터페이스: Controller쪽에서 implements한다.
	public interface Listener {
		public void storeDirectingBtn(boolean confirm);
	}
	//전역변수
	Listener listener;
	View.OnClickListener on_click;
	//뷰 요소
	TextView progress;
	LinearLayout popup_layout;
	Button popup_btn;
	
	//생성자:BaseLayout의 생성자를 호출
	public IntroLayout(Context context){
		super(context, R.layout.intro_main_layout);
		setOnClick();
		setElemOption();
	}
	//리스너를 설정하는 메소드
	public void setListener(Listener listener){
		this.listener = listener;
	}

	public void setProgress(String msg){
		progress.setText(msg);
	}
	
	//사용할 뷰 요소 세팅
	private void setElemOption(){
		progress = (TextView)findViewById(R.id.progress_update);
		popup_layout = (LinearLayout)findViewById(R.id.popup_layout);
		popup_btn = (Button)findViewById(R.id.popup_btn);
		popup_btn.setOnClickListener(on_click);
		
	}
	//버튼 클릭에 대한 액션 핸들러
	private void setOnClick(){
		on_click = new View.OnClickListener() {
			public void onClick(View v) {
				if(listener != null){
					if(v.getId() == R.id.popup_btn){
						listener.storeDirectingBtn(true);
					}
				}
			}
		};
	}

}

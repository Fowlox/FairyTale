package com.fairytale;

import android.content.ClipData;
import android.content.Context;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GamePuzzleLayout extends BaseLayout {

	public interface Listener {

	}
	//전역변수
	private Listener listener;
	private View.OnTouchListener on_touch;
	private View.OnDragListener on_drag;

	//뷰 요소
	private ImageView puzzle_view1,puzzle_view2,puzzle_view3,puzzle_view4
	,puzzle_view5,puzzle_view6;
	private ImageView puzzle_1,puzzle_2,puzzle_3,puzzle_4,
	puzzle_5,puzzle_6;

	protected GamePuzzleLayout(Context context) {
		super(context, R.layout.activity_game_puzzle);
		// TODO Auto-generated constructor stub
		//	setOnClick();
		setElemOption();

	}

	//리스너 설정 메소드
	public void setListener(Listener listener){
		this.listener = listener;
		setOnDrag();
		setOnTouch();
		
		puzzle_view1.setOnTouchListener(on_touch);
		puzzle_view2.setOnTouchListener(on_touch);
		puzzle_view3.setOnTouchListener(on_touch);
		puzzle_view4.setOnTouchListener(on_touch);
		puzzle_view5.setOnTouchListener(on_touch);
		puzzle_view6.setOnTouchListener(on_touch);
		
		puzzle_1.setOnDragListener(on_drag);
		puzzle_2.setOnDragListener(on_drag);
		puzzle_3.setOnDragListener(on_drag);
		puzzle_4.setOnDragListener(on_drag);
		puzzle_5.setOnDragListener(on_drag);
		puzzle_6.setOnDragListener(on_drag);

	}

	//뷰 요소 세팅
	private void setElemOption(){
		puzzle_view1 = (ImageView) findViewById(R.id.puzzle_imageView1);
		puzzle_view2 = (ImageView) findViewById(R.id.puzzle_imageView2);
		puzzle_view3 = (ImageView) findViewById(R.id.puzzle_imageView3);
		puzzle_view4 = (ImageView) findViewById(R.id.puzzle_imageView4);
		puzzle_view5 = (ImageView) findViewById(R.id.puzzle_imageView5);
		puzzle_view6 = (ImageView) findViewById(R.id.puzzle_imageView6);
		
		puzzle_1 = (ImageView) findViewById(R.id.puzzle_1);
		puzzle_2 = (ImageView) findViewById(R.id.puzzle_2);
		puzzle_3 = (ImageView) findViewById(R.id.puzzle_3);
		puzzle_4 = (ImageView) findViewById(R.id.puzzle_4);
		puzzle_5 = (ImageView) findViewById(R.id.puzzle_5);
		puzzle_6 = (ImageView) findViewById(R.id.puzzle_6);
	
	}

	// 터치 액션 핸들러
	private void setOnTouch(){
		on_touch = new View.OnTouchListener(){
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN){
					ClipData clip = ClipData.newPlainText("", "");
					view.startDrag(clip, new CanvasShadow(view,(int)event.getX(),(int)event.getY()),view,0);
					view.setVisibility(View.INVISIBLE);
					return true;
				}
				return false;
			}		
		};

	}

	//드래그 액션 핸들러
	private void setOnDrag(){
		on_drag = new View.OnDragListener(){
			public boolean onDrag(View v, DragEvent event){
				switch(event.getAction()){
				case DragEvent.ACTION_DRAG_STARTED:
					return true;
				case DragEvent.ACTION_DRAG_ENTERED:
					return true;
				case DragEvent.ACTION_DRAG_EXITED:
					return true;
				case DragEvent.ACTION_DROP:
					View view = (View)event.getLocalState();
					ViewGroup parent = (ViewGroup)view.getParent();
					LinearLayout newparent = (LinearLayout)v;
					if(matchPuzzle(newparent, view)){
						parent.removeView(view);
						view.setVisibility(View.VISIBLE);
					}
					else{
						((View)(event.getLocalState())).setVisibility(View.VISIBLE);
					}
					return true;
				case DragEvent.ACTION_DRAG_ENDED:
					if (event.getResult() == false){
						((View)(event.getLocalState())).setVisibility(View.VISIBLE);
					}
					return true;
				}

				return true;	
			}


		};
	}
	
	// 퍼즐 매치 확인
	private boolean matchPuzzle(View match1, View match2){
		if(match1 == findViewById(R.id.puzzle_1)){
			if(match2 == findViewById(R.id.puzzle_imageView1)){
				match1.setBackgroundResource(R.drawable.puzzle_1);
				return true;
			}
		}
		else if(match1 == findViewById(R.id.puzzle_2)){
			if(match2 == findViewById(R.id.puzzle_imageView2)){
				match1.setBackgroundResource(R.drawable.puzzle_2);
				return true;
			}
		}
		else if(match1 == findViewById(R.id.puzzle_3)){
			if(match2 == findViewById(R.id.puzzle_imageView3)){
				match1.setBackgroundResource(R.drawable.puzzle_3);
				return true;
			}
		}
		else if(match1 == findViewById(R.id.puzzle_4)){
			if(match2 == findViewById(R.id.puzzle_imageView4)){
				match1.setBackgroundResource(R.drawable.puzzle_4);
				return true;
			}
		}
		else if(match1 == findViewById(R.id.puzzle_5)){
			if(match2 == findViewById(R.id.puzzle_imageView5)){
				match1.setBackgroundResource(R.drawable.puzzle_5);
				return true;
			}
		}
		else if(match1 == findViewById(R.id.puzzle_6)){
			if(match2 == findViewById(R.id.puzzle_imageView6)){
				match1.setBackgroundResource(R.drawable.puzzle_6);
				return true;
			}
		}
		return false;

	}



	/*
	View.OnTouchListener mTouchListener = new View.OnTouchListener(){
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN){
				ClipData clip = ClipData.newPlainText("", "");
				view.startDrag(clip, new CanvasShadow(view,(int)event.getX(),(int)event.getY()),view,0);
				view.setVisibility(View.INVISIBLE);
				return true;
			}
			return false;
		}		
	};

	View.OnDragListener mDragListener = new View.OnDragListener(){
		public boolean onDrag(View v, DragEvent event){
			switch(event.getAction()){
			case DragEvent.ACTION_DRAG_STARTED:
				return true;
			case DragEvent.ACTION_DRAG_ENTERED:
				return true;
			case DragEvent.ACTION_DRAG_EXITED:
				return true;
			case DragEvent.ACTION_DROP:
				View view = (View)event.getLocalState();
				ViewGroup parent = (ViewGroup)view.getParent();
				LinearLayout newparent = (LinearLayout)v;
				if(matchPuzzle(newparent, view)){
					parent.removeView(view);
					view.setVisibility(View.VISIBLE);
				}
				else{
					((View)(event.getLocalState())).setVisibility(View.VISIBLE);
				}
				return true;
			case DragEvent.ACTION_DRAG_ENDED:
				if (event.getResult() == false){
					((View)(event.getLocalState())).setVisibility(View.VISIBLE);
				}
				return true;
			}

			return true;	
		}


	};

	private boolean matchPuzzle(View match1, View match2){
		if(match1 == findViewById(R.id.puzzle_1)){
			if(match2 == findViewById(R.id.puzzle_imageView1)){
				match1.setBackgroundResource(R.drawable.puzzle_1);
				return true;
			}
		}
		else if(match1 == findViewById(R.id.puzzle_2)){
			if(match2 == findViewById(R.id.puzzle_imageView2)){
				match1.setBackgroundResource(R.drawable.puzzle_2);
				return true;
			}
		}
		else if(match1 == findViewById(R.id.puzzle_3)){
			if(match2 == findViewById(R.id.puzzle_imageView3)){
				match1.setBackgroundResource(R.drawable.puzzle_3);
				return true;
			}
		}
		else if(match1 == findViewById(R.id.puzzle_4)){
			if(match2 == findViewById(R.id.puzzle_imageView4)){
				match1.setBackgroundResource(R.drawable.puzzle_4);
				return true;
			}
		}
		else if(match1 == findViewById(R.id.puzzle_5)){
			if(match2 == findViewById(R.id.puzzle_imageView5)){
				match1.setBackgroundResource(R.drawable.puzzle_5);
				return true;
			}
		}
		else if(match1 == findViewById(R.id.puzzle_6)){
			if(match2 == findViewById(R.id.puzzle_imageView6)){
				match1.setBackgroundResource(R.drawable.puzzle_6);
				return true;
			}
		}
		return false;

	}
	 */


}

package com.fairytale;

import android.graphics.Canvas;
import android.graphics.Point;
import android.view.View;
import android.view.View.DragShadowBuilder;

public class CanvasShadow extends DragShadowBuilder {
	int mWidth, mHeight;
	int mX, mY;
	
	public CanvasShadow(View v, int x, int y){
		super(v);
		mWidth = v.getWidth();
		mHeight = v.getHeight();
		mX= x;
		mY = y;
	}
	
	public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint){
		shadowSize.set(mWidth,  mHeight);;
		shadowTouchPoint.set(mX,mY);
	}
	
	public void onDrawShadow(Canvas canvas){
		super.onDrawShadow(canvas);
	}
	
}

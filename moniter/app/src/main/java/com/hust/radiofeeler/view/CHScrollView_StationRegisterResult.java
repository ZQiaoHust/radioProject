package com.hust.radiofeeler.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import com.hust.radiofeeler.tab1.Stations_registerResult;

public class CHScrollView_StationRegisterResult extends HorizontalScrollView {

	Stations_registerResult  activity;

	public CHScrollView_StationRegisterResult(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		activity = (Stations_registerResult) context;
	}


	public CHScrollView_StationRegisterResult(Context context, AttributeSet attrs) {
		super(context, attrs);
		activity = (Stations_registerResult) context;
	}

	public CHScrollView_StationRegisterResult(Context context) {
		super(context);
		activity = (Stations_registerResult) context;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//进行触摸赋值
		activity.mTouchView = this;
		return super.onTouchEvent(ev);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		//当当前的CHSCrollView被触摸时，滑动其它
		if(activity.mTouchView == this) {
			activity.onScrollChanged(l, t, oldl, oldt);
		}else{
			super.onScrollChanged(l, t, oldl, oldt);
		}
	}
}

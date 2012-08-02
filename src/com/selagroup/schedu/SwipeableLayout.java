package com.selagroup.schedu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class SwipeableLayout extends LinearLayout {
	private float mDownX;

	public SwipeableLayout(Context context) {
		super(context);
		init();
	}

	public SwipeableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			Log.i("Test", "Detected touch down at: " + ev.getX() + ", " + ev.getY());
			mDownX = ev.getY();

			// Translate to right
			TranslateAnimation anim = null;
			anim = new TranslateAnimation(0.0f, getWidth(), 0.0f, 0.0f);
			anim.setDuration(1000);
			anim.setInterpolator(new LinearInterpolator());
//			startAnimation(anim);

			break;
		case MotionEvent.ACTION_UP:
			Log.i("Test", "Detected touch up at: " + ev.getX() + ", " + ev.getY());
			break;
		case MotionEvent.ACTION_MOVE:
			float currentX = ev.getX();
			int diff = (int) (currentX - mDownX) / 10;
			if (diff >= 0) {
			}
			else {
			}
		}

		return false;
	}
}

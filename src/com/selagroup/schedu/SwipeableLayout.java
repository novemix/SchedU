package com.selagroup.schedu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class SwipeableLayout extends LinearLayout {
	public static final int SWIPE_RIGHT = 0;
	public static final int SWIPE_LEFT = 1;

	private GestureDetector mGestureDetector = new GestureDetector(new MyGestureListener());
	private OnEndSwipeListener mSwipeListener = null;
	private Animation mRightSwipe;
	private Animation mLeftSwipe;
	private Animation mFadeIn;
	
	private float mStartX;

	public SwipeableLayout(Context context) {
		super(context);
		init();
	}

	public SwipeableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {
		// Set up right swipe animation
		mRightSwipe = new TranslateAnimation(0.0f, getWidth(), 0.0f, 0.0f);
		mRightSwipe.setDuration(500);
		mRightSwipe.setInterpolator(new DecelerateInterpolator(1.0f));
		mRightSwipe.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				startAnimation(mFadeIn);
				mSwipeListener.onEndSwipe(SWIPE_RIGHT);
			}
		});

		// Set up left swipe animation
		mLeftSwipe = new TranslateAnimation(0.0f, -getWidth(), 0.0f, 0.0f);
		mLeftSwipe.setDuration(500);
		mLeftSwipe.setInterpolator(new DecelerateInterpolator(1.0f));
		mLeftSwipe.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				startAnimation(mFadeIn);
				mSwipeListener.onEndSwipe(SWIPE_LEFT);
			}
		});

		mFadeIn = new AlphaAnimation(0.0f, 1.0f);
		mFadeIn.setInterpolator(new LinearInterpolator());
		mFadeIn.setDuration(100);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mStartX = event.getX();
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		case MotionEvent.ACTION_MOVE:
//			((LinearLayout.LayoutParams) getLayoutParams()).setMargins((int)(event.getX() - mStartX), 0, 0, 0);
//			requestLayout();
			break;
		}
		// ((LinearLayout.LayoutParams) getLayoutParams()).setMargins(50, 0, 0, 0);
		// requestLayout();
		return false;
	}

	public void setOnEndSwipeListener(OnEndSwipeListener listener) {
		mSwipeListener = listener;
	}

	public interface OnEndSwipeListener {
		public void onEndSwipe(int swipeType);
	}

	private class MyGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			Log.i("Test", "Fling event: " + velocityX + ", " + velocityY);
			if (Math.abs(velocityY) < 1000.0f) {
				if (velocityX > 500.0f) {		// Swipe right
					startAnimation(mRightSwipe);
				}
				else if (velocityX < -500.0f) {	// Swipe left
					startAnimation(mLeftSwipe);
				}
			}
			return true;
		}
	}
}

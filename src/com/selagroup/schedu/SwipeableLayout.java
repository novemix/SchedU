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
	
	private Animation mRightSwipe;
	private Animation mLeftSwipe;
	private Animation mFadeIn;

	private float mStartX;

	public SwipeableLayout(Context context) {
		super(context);
	}

	public SwipeableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
}

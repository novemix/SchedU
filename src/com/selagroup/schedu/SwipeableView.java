package com.selagroup.test2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class SwipeableView extends RelativeLayout {
	private float mDownX;
	
	private RelativeLayout ll_left;
	private RelativeLayout ll_center;
	private RelativeLayout ll_right;
	private int width;
	private int height;
	
	private int mDelta = 0;

	private int mShift = 0;

	public SwipeableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SwipeableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SwipeableView(Context context) {
		super(context);
		init();
	}

	public void init() {
		setGravity(Gravity.TOP);
		setClickable(true);

		ll_left = new RelativeLayout(getContext());
		ll_center = new RelativeLayout(getContext());
		ll_right = new RelativeLayout(getContext());

		ll_left.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
		ll_center.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
		ll_right.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));

		addView(ll_left);
		addView(ll_center);
		addView(ll_right);
		
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				movePanels(event);
				return false;
			}
		});
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		width = getWidth();
		height = getHeight();
		
		ll_left.layout(-width + mDelta, 0, mDelta, height);
		ll_center.layout(mDelta, 0, width + mDelta, height);
		ll_right.layout(width + mDelta, 0, 2 * width + mDelta, height);
	}
	
	public boolean movePanels(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = ev.getX();
			
			break;
		case MotionEvent.ACTION_MOVE:
			float xDiff = ev.getX() - mDownX;
			
			mDelta = mShift + (int) xDiff;
			requestLayout();
			break;
		case MotionEvent.ACTION_UP:
			mShift = mDelta;
			
			// Animate back
//			requestLayout();
			break;
		}

		return false;
	}
}

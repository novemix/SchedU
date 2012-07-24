package com.selagroup.schedu;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Sketch extends View implements OnTouchListener {
	
	ArrayList<Point> mSketch;
	Paint mPaint;
	
	public Sketch(Context myContext){
		  super(myContext);
		  this.setBackgroundColor(Color.WHITE);
//		  this.setMinimumHeight(((View) this.getParent()).getHeight());
//		  this.setMinimumWidth(((View) this.getParent()).getWidth());
		  
		  this.setFocusable(true);
		  this.setFocusableInTouchMode(true);
		  this.setOnTouchListener(this);
		  
		  mSketch = new ArrayList<Point>();
		  mPaint = new Paint();
		  mPaint.setColor(Color.BLACK);
		  mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		}

	public void onDraw(Canvas c){
		Point prevPoint = null;
		mPaint.setStrokeWidth(2);
	  for (Point p : mSketch) {
		  if (prevPoint != null && p.x != -1 && prevPoint.x != -1) {
			  c.drawLine(prevPoint.x, prevPoint.y, p.x, p.y, mPaint);
		  }
//		  else {
//			  c.drawCircle(p.x,p.y,4,mPaint);
//		  }
		  prevPoint = p;
	  }
	}
	
	public boolean onTouch(View v, MotionEvent me){
		  if (me.getAction()==MotionEvent.ACTION_DOWN){
			  mSketch.add(new Point((int) me.getX(), (int) me.getY()));
		  }
		  if (me.getAction()==MotionEvent.ACTION_MOVE){
			  
		    mSketch.add(new Point((int) me.getX() , (int) me.getY()));

		  }
		  if (me.getAction()==MotionEvent.ACTION_UP){
			  mSketch.add(new Point(-1, -1));
		  }
		  invalidate();
		  return true;
		}
}

package com.selagroup.schedu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class Sketch extends View implements OnTouchListener {

	Paint mPaint;
	Canvas mCanvas;
	Context context;
	Bitmap mBitmap;
	float prevX;
	float prevY;
	
	public Sketch(Context myContext) {
		super(myContext);
		context = myContext;
		
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.setOnTouchListener(this);

		prevX = -1;
		prevY = -1;
		
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(2);
	}

    @Override protected void onSizeChanged(int w, int h, int oldw,
            int oldh) {
        int curW = mBitmap != null ? mBitmap.getWidth() : 0;
        int curH = mBitmap != null ? mBitmap.getHeight() : 0;
        if (curW >= w && curH >= h) {
            return;
        }
        
        if (curW < w) curW = w;
        if (curH < h) curH = h;
        
        Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
                                               Bitmap.Config.RGB_565);
        Canvas newCanvas = new Canvas();
        newCanvas.setBitmap(newBitmap);
        if (mBitmap != null) {
            newCanvas.drawBitmap(mBitmap, 0, 0, null);
        }
        mBitmap = newBitmap;
        mCanvas = newCanvas;
        clear();
    }
    
    @Override protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

	public boolean onTouch(View v, MotionEvent me) {
		mPaint.setARGB(255, 0, 0, 0);
		float x = me.getX();
		float y = me.getY();
		if (me.getAction() == MotionEvent.ACTION_DOWN) {
			mCanvas.drawPoint(x, y, mPaint);
			prevX = x;
			prevY = y;
		}
		if (me.getAction() == MotionEvent.ACTION_MOVE) {
			if (prevX != -1) {
				mCanvas.drawLine(prevX, prevY, x, y, mPaint);
				prevX = x;
				prevY = y;
			}
		}
		if (me.getAction() == MotionEvent.ACTION_UP) {
			prevX = -1;
			prevY = -1;
		}
		invalidate();
		return true;
	}

	public void clear() {
		if (mCanvas != null) {
            mPaint.setARGB(0xff, 255, 255, 255);
            mCanvas.drawPaint(mPaint);
            invalidate();
        }
	}
	
	public void save() {
		Toast.makeText(context, context.getFilesDir().toString(), Toast.LENGTH_LONG).show();
		File file = new File(context.getFilesDir().toString() + "/test.png");
		try {
			OutputStream stream = new FileOutputStream(file);
			// Write bitmap to file using JPEG or PNG and 80% quality hint for JPEG.
			mBitmap.compress(CompressFormat.PNG, 80, stream);
			stream.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    
	    Uri contentUri = Uri.fromFile(new File(file.getAbsolutePath()));
	    mediaScanIntent.setData(contentUri);
	    context.sendBroadcast(mediaScanIntent);

	}
}

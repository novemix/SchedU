package com.selagroup.schedu.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.selagroup.schedu.R;

public class SplashActivity extends Activity {
	
	private Timer mTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_splash);
	    
	    // Start on tap
	    findViewById(R.id.splash_layout_root).setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				startApplication();
			    mTimer.cancel();
				return false;
			}
		});
	    
	    mTimer = new Timer(true);
	    mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				startApplication();
			}
		}, 3000);
	}
	
	private void startApplication() {
		startActivity(new Intent(SplashActivity.this, TermActivity.class));
	}
}

package com.selagroup.schedu.activities;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.selagroup.schedu.R;
import com.selagroup.schedu.ScheduApplication;
import com.selagroup.schedu.Utility;
import com.selagroup.schedu.managers.TermManager;
import com.selagroup.schedu.model.Term;

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
		}, 1000);

	}

	/**
	 * Starts the application. If the current time falls within a defined term, show the calendar for that term
	 */
	private void startApplication() {
		ScheduApplication application = ((ScheduApplication) getApplication());
		TermManager termManager = application.getTermManager();
		List<Term> allTerms = termManager.getAll();
		Term currentTerm = Utility.getCurrentTerm(allTerms, Calendar.getInstance());
		if (currentTerm == null) {
			startActivity(new Intent(SplashActivity.this, TermActivity.class));
		}
		else {
			application.setCurrentTerm(currentTerm);
			startActivity(new Intent(SplashActivity.this, AllCoursesActivity.class));
		}
	}
}

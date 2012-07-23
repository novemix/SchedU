/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import com.selagroup.schedu.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * The Class CalendarActivity.
 */
public class CalendarActivity extends Activity {
	
	ScrollView svDay, svWeek;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		
		TextView date = (TextView) findViewById(R.id.calendar_tv_date);
		date.setText("Monday, July 23, 2012");
		
		svDay = (ScrollView) findViewById(R.id.calendar_sv_day);
		svWeek = (ScrollView) findViewById(R.id.calendar_sv_week);
		
		final ToggleButton btnDay = (ToggleButton) findViewById(R.id.calendar_btn_day);
		final ToggleButton btnWeek = (ToggleButton) findViewById(R.id.calendar_btn_week);
		
		OnClickListener buttonListener = new OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.calendar_btn_day:
					btnWeek.setChecked(false);
					btnDay.setChecked(true);
					svWeek.setVisibility(View.GONE);
					svDay.setVisibility(View.VISIBLE);
					break;
				case R.id.calendar_btn_week:
					btnDay.setChecked(false);
					btnWeek.setChecked(true);
					svDay.setVisibility(View.GONE);
					svWeek.setVisibility(View.VISIBLE);
					break;
				}
			}
		};
		
		btnDay.setOnClickListener(buttonListener);
		btnWeek.setOnClickListener(buttonListener);
		
		RelativeLayout courses = (RelativeLayout) findViewById(R.id.calendar_day_courses);
		
		TextView newCourse = new TextView(this);
		newCourse.setText("(code)");
		newCourse.setBackgroundColor(Color.GREEN);
		newCourse.setClickable(true);
		newCourse.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(CalendarActivity.this, CourseActivity.class));
				
			}
		});
		final float scale = getResources().getDisplayMetrics().density;
		int dp = (int) (60 * scale + 0.5f);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, dp);
		params.setMargins(0,(int) (300 * scale + 0.5f), 0, 0);
		newCourse.setLayoutParams(params);
		
		courses.addView(newCourse);
	}
}

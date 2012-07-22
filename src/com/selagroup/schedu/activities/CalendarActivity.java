/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import com.selagroup.schedu.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Class CalendarActivity.
 */
public class CalendarActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		
		RelativeLayout courses = (RelativeLayout) findViewById(R.id.calendar_day_courses);
		
		TextView newCourse = new TextView(this);
		newCourse.setText("(code)");
		newCourse.setBackgroundColor(Color.GREEN);
		newCourse.setClickable(true);
		newCourse.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Toast.makeText(CalendarActivity.this, "You touched me!", Toast.LENGTH_SHORT).show();
				
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

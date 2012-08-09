/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import com.selagroup.schedu.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

/**
 * The Class AllCoursesActivity.
 */
public class AllCoursesActivity extends ListActivity {
	
	private static final int ADD_COURSE_CODE = 1;
	ToggleButton allcourses_btn_edit;
	Button allcourses_btn_add;
	Button allcourses_btn_calendar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allcourses);
		
		initWidgets();
		initListeners();
	}
	
	protected void initWidgets() {
		allcourses_btn_edit = (ToggleButton) findViewById(R.id.allcourses_btn_edit);
		allcourses_btn_add = (Button) findViewById(R.id.allcourses_btn_add);
		allcourses_btn_calendar = (Button) findViewById(R.id.allcourses_btn_calendar);
	}
	
	protected void initListeners() {
		allcourses_btn_add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(AllCoursesActivity.this, AddCourseActivity.class);
				startActivityForResult(intent, ADD_COURSE_CODE);
			}
		});
		
		allcourses_btn_calendar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(AllCoursesActivity.this, CalendarActivity.class));
			}
		});
	}
}

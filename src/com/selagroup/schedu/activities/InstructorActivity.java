/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import com.selagroup.schedu.MyApplication;
import com.selagroup.schedu.R;
import com.selagroup.schedu.managers.InstructorManager;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Instructor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The Class InstructorActivity.
 */
public class InstructorActivity extends Activity {

	InstructorManager mInstructorManager;

	TextView instructor_course_code;
	EditText instructor_name;
	EditText instructor_email;
	EditText instructor_phone;
	EditText instructor_building;
	EditText instructor_room;
	
	Instructor thisInstructor;
	Course thisCourse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructor);

		int courseID = getIntent().getIntExtra("courseID", -1);
		MyApplication app = (MyApplication) getApplication();
		mInstructorManager = app.getInstructorManager();
		String course_code = app.getCourseManager().get(courseID).getCourseCode();

		initWidgets();
	}


	@Override
	public void onBackPressed() {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	}
	
	private void initWidgets() {
		
		instructor_course_code = (TextView) findViewById(R.id.instructor_course_code);
		instructor_name = (EditText) findViewById(R.id.instructor_name);
		instructor_email = (EditText) findViewById(R.id.instructor_email);
		instructor_building = (EditText) findViewById(R.id.instructor_building);
		instructor_room = (EditText) findViewById(R.id.instructor_room);
		
		
		((Button) findViewById(R.id.instructor_btn_add_time)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent(InstructorActivity.this, AddTimeActivity.class));
			}
		});
	}

	void mockup() {
		LinearLayout hours = (LinearLayout) findViewById(R.id.instructor_ll_hours);
		TextView tv1 = new TextView(this);
		tv1.setText("Mon, Wed -- 1pm - 3pm");
		TextView tv2 = new TextView(this);
		tv2.setText("Tue, Thu -- 3pm-4pm");
		TextView tv3 = new TextView(this);
		tv3.setText("Fri -- 11am - 12pm");
		TextView tv4 = new TextView(this);
		tv4.setText("Sat -- 11am - 12pm");
		TextView tv5 = new TextView(this);
		tv5.setText("Sat -- 11am - 12pm");
		TextView tv6 = new TextView(this);
		tv6.setText("Sat -- 11am - 12pm");
		TextView tv7 = new TextView(this);
		tv7.setText("Sat -- 11am - 12pm");
		hours.addView(tv1);
		hours.addView(tv2);
		hours.addView(tv3);
		hours.addView(tv4);
		hours.addView(tv5);
		hours.addView(tv6);
		hours.addView(tv7);
	}
}

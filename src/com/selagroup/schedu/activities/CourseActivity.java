/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import com.selagroup.schedu.MyApplication;
import com.selagroup.schedu.R;
import com.selagroup.schedu.managers.CourseManager;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Instructor;
import com.selagroup.schedu.model.Location;
import com.selagroup.schedu.model.TimePlaceBlock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Class CourseActivity.
 */
public class CourseActivity extends Activity {
	
	public static final int INSTRUCTOR_EDIT_CODE = 1;
	
	TextView course_course_code;
	TextView course_course_name;
	TextView course_time_label;
	TextView course_time;
	TextView course_building;
	TextView course_room;
	TextView course_instructor;
	TextView course_email;
	TextView course_phone;
	TextView course_office;
	
	TextView course_next_code;
	TextView course_next_name;
	TextView course_next_time;
	TextView course_next_instructor;
	
	Button course_btn_map;
	Button course_btn_edit_instructor;
	Button course_btn_notes;
	Button course_btn_reminders;
	Button course_btn_assignments_exams;
	
	Course thisCourse;
	TimePlaceBlock thisBlock;
	Location thisLocation;
	Instructor thisInstructor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course);
	
		Intent intent = getIntent();
		int courseID = intent.getIntExtra("courseID", -1);
		int blockID = intent.getIntExtra("blockID", -1);
		
		thisCourse = ((MyApplication) getApplication()).getCourseManager().get(courseID);
		thisBlock = thisCourse.getScheduleBlock(blockID);
		thisLocation = thisBlock.getLocation();
		thisInstructor = thisCourse.getInstructor();
		
		initWidgets();
		setValues();
		initListeners();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == INSTRUCTOR_EDIT_CODE && resultCode == RESULT_OK) {
			thisInstructor = (Instructor) data.getSerializableExtra("instructor");
			setValues();
		}
	}
	
	private void initWidgets() {
		// Initialize widget handles
		course_course_code = (TextView) findViewById(R.id.course_course_code);
		course_course_name = (TextView) findViewById(R.id.course_course_name);
		course_time_label = (TextView) findViewById(R.id.course_time_label);
		course_time = (TextView) findViewById(R.id.course_time);
		course_building = (TextView) findViewById(R.id.course_building);
		course_room = (TextView) findViewById(R.id.course_room);
		course_instructor = (TextView) findViewById(R.id.course_instructor);
		course_email = (TextView) findViewById(R.id.course_email);
		course_phone = (TextView) findViewById(R.id.course_phone);
		course_office = (TextView) findViewById(R.id.course_office);
		
		course_next_code = (TextView) findViewById(R.id.course_next_code);
		course_next_name = (TextView) findViewById(R.id.course_next_name);
		course_next_time = (TextView) findViewById(R.id.course_next_time);
		course_next_instructor = (TextView) findViewById(R.id.course_next_instructor);
		
		course_btn_map = (Button) findViewById(R.id.course_btn_map);
		course_btn_edit_instructor = (Button) findViewById(R.id.course_btn_edit_instructor);
		course_btn_notes = (Button) findViewById(R.id.course_btn_notes);
		course_btn_reminders = (Button) findViewById(R.id.course_btn_reminders);
		course_btn_assignments_exams = (Button) findViewById(R.id.course_btn_assignments_exams);

	}

	private void setValues() {
		// Set values
		course_course_code.setText(thisCourse.getCourseCode());
		course_course_name.setText(thisCourse.getCourseName());
		// todo: update time label and time display
		course_building.setText(thisLocation.getBuilding());
		course_room.setText(thisLocation.getRoom());
		if (thisInstructor != null) {
			course_instructor.setText(thisInstructor.getName());
			course_email.setText(thisInstructor.getEmail());
			course_phone.setText(thisInstructor.getPhone());
			thisInstructor.populateHours((ScrollView) findViewById(R.id.course_sv_office_hours));
		}
	}
	
	private void initListeners() {
		course_btn_edit_instructor.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(CourseActivity.this, InstructorActivity.class);
				intent.putExtra("courseID", thisCourse.getID());
				startActivityForResult(intent, INSTRUCTOR_EDIT_CODE);
			}
		});
		
		course_btn_notes.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(CourseActivity.this, NotesActivity.class));
			}
		});
		
		course_btn_assignments_exams.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(CourseActivity.this, CourseAssignActivity.class));
			}
		});
	}


	void mockup() {
		LinearLayout hours = (LinearLayout) findViewById(R.id.course_ll_office_hours);
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

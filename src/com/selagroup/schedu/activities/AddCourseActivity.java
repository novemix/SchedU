/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.selagroup.schedu.MyApplication;
import com.selagroup.schedu.R;
import com.selagroup.schedu.managers.CourseManager;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Instructor;
import com.selagroup.schedu.model.Term;
import com.selagroup.schedu.model.TimePlaceBlock;

/**
 * The Class AddCourseActivity.
 */
public class AddCourseActivity extends Activity {
	// Managers
	private CourseManager mCourseManager;

	// Views
	private ListView addcourse_lv_schedule;
	private EditText addcourse_et_course_code;
	private EditText addcourse_et_course_name;
	private EditText addcourse_et_instructor;
	private Button addcourse_btn_add_time;
	private Button addcourse_btn_done;

	// Adapters
	private ArrayAdapter<TimePlaceBlock> mScheduleAdapter;

	// Data
	private Term mCurrentTerm;
	private List<TimePlaceBlock> mScheduleBlocks = new LinkedList<TimePlaceBlock>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addcourse);

		// Get the selected term
		mCurrentTerm = (Term) getIntent().getSerializableExtra("term");
		Log.i("Test", mCurrentTerm.toString());

		// Get the course manager
		mCourseManager = ((MyApplication) getApplication()).getCourseManager();

		initWidgets();
	}

	/**
	 * Initialize widgets for this activity
	 */
	private void initWidgets() {
		addcourse_lv_schedule = (ListView) findViewById(R.id.addcourse_lv_schedule);
		addcourse_et_course_code = (EditText) findViewById(R.id.addcourse_et_course_code);
		addcourse_et_course_name = (EditText) findViewById(R.id.addcourse_et_course_name);
		addcourse_et_instructor = (EditText) findViewById(R.id.addcourse_et_instructor);
		addcourse_btn_add_time = (Button) findViewById(R.id.addcourse_btn_add_time);
		addcourse_btn_done = (Button) findViewById(R.id.addcourse_btn_done);

		// Set up spinner adapter
		mScheduleAdapter = new ArrayAdapter<TimePlaceBlock>(this, android.R.layout.simple_list_item_1, mScheduleBlocks);
		addcourse_lv_schedule.setAdapter(mScheduleAdapter);

		// Set up add time button listener
		addcourse_btn_add_time.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				startActivity(new Intent(AddCourseActivity.this, AddTimeActivity.class));
			}
		});

		// Set up add course button listener
		addcourse_btn_done.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				
				// Get information for the course
				String code = addcourse_et_course_code.getText().toString();
				String name = addcourse_et_course_name.getText().toString();
				String instructorName = addcourse_et_instructor.getText().toString();
				
				// Create new instructor and course
				Instructor instructor = new Instructor(-1, instructorName, "phone", "email");
				Course newCourse = new Course(-1, mCurrentTerm, code, name, instructor);
				
				// Insert new course
				mCourseManager.insert(newCourse);
				
				startActivity(new Intent(AddCourseActivity.this, CalendarActivity.class));
			}
		});
	}
}

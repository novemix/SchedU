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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.selagroup.schedu.MyApplication;
import com.selagroup.schedu.R;
import com.selagroup.schedu.managers.CourseManager;
import com.selagroup.schedu.managers.InstructorManager;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Instructor;
import com.selagroup.schedu.model.Term;
import com.selagroup.schedu.model.TimePlaceBlock;

/**
 * The Class AddCourseActivity.
 */
public class AddCourseActivity extends Activity {
	private static final int ADD_TIME_REQUEST_CODE = 5;

	// Managers
	private CourseManager mCourseManager;
	private InstructorManager mInstructorManager;

	// Views
	private ListView addcourse_lv_schedule;
	private EditText addcourse_et_course_code;
	private EditText addcourse_et_course_name;
	private AutoCompleteTextView addcourse_et_instructor;
	private Button addcourse_btn_add_time;
	private Button addcourse_btn_done;
	private Button addcourse_btn_next;

	// Adapters
	private ArrayAdapter<TimePlaceBlock> mScheduleAdapter;

	// Data
	private Term mCurrentTerm;
	private List<TimePlaceBlock> mScheduleBlocks = new LinkedList<TimePlaceBlock>();
	private List<Instructor> mInstructors;

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
		mInstructorManager = ((MyApplication) getApplication()).getInstructorManager();
		
		mInstructors = mInstructorManager.getAll();
		
		mInstructors.add(new Instructor(-1, "Allan", "", ""));
		mInstructors.add(new Instructor(-1, "Albert", "", ""));
		mInstructors.add(new Instructor(-1, "Bob", "", ""));
		mInstructors.add(new Instructor(-1, "Brian", "", ""));
		mInstructors.add(new Instructor(-1, "Brianne", "", ""));
		mInstructors.add(new Instructor(-1, "Cat", "", ""));
		mInstructors.add(new Instructor(-1, "Carol", "", ""));
		mInstructors.add(new Instructor(-1, "Dan", "", ""));
		mInstructors.add(new Instructor(-1, "Don", "", ""));

		initWidgets();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == ADD_TIME_REQUEST_CODE) {
				
				// Add a block
				TimePlaceBlock block = (TimePlaceBlock) data.getSerializableExtra("block");
				mScheduleBlocks.add(block);
			}
		}
	}

	/**
	 * Initialize widgets for this activity
	 */
	private void initWidgets() {
		addcourse_lv_schedule = (ListView) findViewById(R.id.addcourse_lv_schedule);
		addcourse_et_course_code = (EditText) findViewById(R.id.addcourse_et_course_code);
		addcourse_et_course_name = (EditText) findViewById(R.id.addcourse_et_course_name);
		addcourse_et_instructor = (AutoCompleteTextView) findViewById(R.id.addcourse_et_instructor);
		addcourse_btn_add_time = (Button) findViewById(R.id.addcourse_btn_add_time);
		addcourse_btn_done = (Button) findViewById(R.id.addcourse_btn_done);
		addcourse_btn_next = (Button) findViewById(R.id.addcourse_btn_next);

		// Set up list view adapter for schedule blocks
		mScheduleAdapter = new ArrayAdapter<TimePlaceBlock>(this, android.R.layout.simple_list_item_1, mScheduleBlocks);
		addcourse_lv_schedule.setAdapter(mScheduleAdapter);
		
		// Set up adapter to auto complete instructor
		addcourse_et_instructor.setAdapter(new ArrayAdapter<Instructor>(this, android.R.layout.simple_dropdown_item_1line, mInstructors));
		
		// Set up listener to respond to auto complete (?)
		addcourse_et_instructor.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				
				return false;
			}
		});

		// Set up add time button listener
		addcourse_btn_add_time.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				startActivityForResult(new Intent(AddCourseActivity.this, AddTimeActivity.class), ADD_TIME_REQUEST_CODE);
			}
		});

		// Set up done button listener
		addcourse_btn_done.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {

				// Get information for the course
				String code = addcourse_et_course_code.getText().toString();
				String name = addcourse_et_course_name.getText().toString();
				String instructorName = addcourse_et_instructor.getText().toString();

				// Create new instructor and course
				Instructor instructor = new Instructor(-1, instructorName, "", "");
				Course newCourse = new Course(-1, mCurrentTerm, code, name, instructor);

				// Insert new course
				mCourseManager.insert(newCourse);

				startActivity(new Intent(AddCourseActivity.this, CalendarActivity.class));
			}
		});
		
		// Set up next button listener
		addcourse_btn_next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
	}
}

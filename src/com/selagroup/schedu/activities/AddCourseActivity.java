/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
	private static final int sAddTimeCode = 5;
	private static final int sCalendarCode = 6;

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

	private Builder mBuilder;

	// Adapters
	private ArrayAdapter<TimePlaceBlock> mScheduleAdapter;

	// Data
	private Term mCurrentTerm;
	private List<TimePlaceBlock> mScheduleBlocks = new LinkedList<TimePlaceBlock>();
	private List<Instructor> mInstructors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addcourse);

		// Get the selected term
		MyApplication myApp = ((MyApplication) getApplication());
		mCurrentTerm = myApp.getCurrentTerm();

		// Get the course manager and the instructor manager
		mCourseManager = myApp.getCourseManager();
		mInstructorManager = myApp.getInstructorManager();

		initWidgets();
		initListeners();
		reset();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("Test", "Request code: " + requestCode);
		switch (requestCode) {
		case sAddTimeCode:
			if (resultCode == RESULT_OK) {
				// Add a block
				TimePlaceBlock block = (TimePlaceBlock) data.getSerializableExtra("block");
				mScheduleBlocks.add(block);
				mScheduleAdapter.notifyDataSetChanged();
			}
			break;
		case sCalendarCode:
			reset();
			break;
		default:
			reset();
			break;
		}
	}

	/**
	 * Initialize widgets for this activity
	 */
	private void initWidgets() {
		mBuilder = new Builder(this);
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
	}

	/**
	 * Initializes the listeners.
	 */
	private void initListeners() {
		// Set up add time button listener
		addcourse_btn_add_time.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				startActivityForResult(new Intent(AddCourseActivity.this, AddTimeActivity.class), sAddTimeCode);
			}
		});

		// Set up done button listener
		addcourse_btn_done.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {

				// Try to add another course.
				if (addCourseHelper()) {
					startActivityForResult(new Intent(AddCourseActivity.this, CalendarActivity.class), sCalendarCode);
				}
				else {
					mBuilder.setTitle("Your course has not been added. Continue anyway?");
					mBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							startActivityForResult(new Intent(AddCourseActivity.this, CalendarActivity.class), sCalendarCode);
						}
					});
					mBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

						}
					});
					mBuilder.create();
					mBuilder.show();
				}
			}
		});

		// Set up next button listener
		addcourse_btn_next.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// Try to add the course. If successful, add another course
				if (addCourseHelper()) {
					reset();
				}
			}
		});
	}

	/**
	 * Resets the activity's UI and data
	 */
	private void reset() {
		mInstructors = mInstructorManager.getAll();
		mScheduleBlocks.clear();
		mScheduleAdapter.notifyDataSetChanged();
		addcourse_et_course_code.setText("");
		addcourse_et_course_name.setText("");
		addcourse_et_instructor.setText("");
	}

	/**
	 * Helps to add a course given the information populated in this activity
	 * @return true, if successfully added a course
	 */
	private boolean addCourseHelper() {
		// Get information for the course
		String code = addcourse_et_course_code.getText().toString();
		String name = addcourse_et_course_name.getText().toString();
		String instructorName = addcourse_et_instructor.getText().toString();

		// Create and insert a new instructor or find an existing instructor
		Instructor instructor = new Instructor(-1, instructorName, "", "");
		if (instructorName.equals("")) {
			instructor = null;
		}
		int index = mInstructors.indexOf(instructor);
		if (index != -1) {
			instructor = mInstructors.get(index);
		}
		else {
			mInstructors.add(instructor);
		}

		Course newCourse = new Course(-1, mCurrentTerm, code, name, instructor);

		// Add schedule blocks
		for (TimePlaceBlock block : mScheduleBlocks) {
			newCourse.addScheduleBlock(block);
		}

		// Insert new course if it has a term, a code, and at least one schedule block
		if (mCurrentTerm != null && !code.equals("") && mScheduleBlocks.size() > 0) {
			mCourseManager.insert(newCourse);
			Toast.makeText(AddCourseActivity.this, "Added a new course: " + newCourse, Toast.LENGTH_LONG).show();
			return true;
		}
		else {
			Toast.makeText(AddCourseActivity.this, "Please provide a course code and at least one time block.", Toast.LENGTH_LONG).show();
			return false;
		}
	}
}

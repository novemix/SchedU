/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import java.util.Calendar;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.selagroup.schedu.R;
import com.selagroup.schedu.ScheduApplication;
import com.selagroup.schedu.Utility;
import com.selagroup.schedu.adapters.CourseArrayAdapter;
import com.selagroup.schedu.adapters.CourseArrayAdapter.CourseDeleteListener;
import com.selagroup.schedu.managers.CourseManager;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Term;
import com.selagroup.schedu.model.TimePlaceBlock;

/**
 * The Class AllCoursesActivity.
 */
public class AllCoursesActivity extends ListActivity {

	// Widgets
	private ToggleButton allcourses_btn_edit;
	private ImageButton allcourses_btn_add;
	private ImageButton allcourses_btn_calendar;
	
	// Data
	private boolean mEditMode;
	private Term mCurrentTerm;
	private List<Course> mCourseList;
	private CourseArrayAdapter mCourseAdapter;

	private CourseManager mCourseManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allcourses);

		ScheduApplication app = (ScheduApplication) getApplication();
		mCourseManager = app.getCourseManager();
		mCurrentTerm = app.getCurrentTerm();
		mCourseList = mCourseManager.getAllForTerm(mCurrentTerm.getID());

		mCourseAdapter = new CourseArrayAdapter(this, R.layout.adapter_course_select, mCourseList, new CourseDeleteListener() {
			public void onDelete(Course iCourse) {
				mCourseManager.delete(iCourse);
				mCourseList.remove(iCourse);
				mCourseAdapter.notifyDataSetChanged();
				iCourse = null;
			}
		});
		setListAdapter(mCourseAdapter);

		initWidgets();
		initListeners();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mCourseList.clear();
		mCourseList.addAll(mCourseManager.getAllForTerm(mCurrentTerm.getID()));
		mCourseAdapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Utility.buildOptionsMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (Utility.handleOptionsMenuSelection(AllCoursesActivity.this, item)) {
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	protected void initWidgets() {
		allcourses_btn_edit = (ToggleButton) findViewById(R.id.allcourses_btn_edit);
		allcourses_btn_add = (ImageButton) findViewById(R.id.allcourses_btn_add);
		allcourses_btn_calendar = (ImageButton) findViewById(R.id.allcourses_btn_calendar);
	}

	protected void initListeners() {
		allcourses_btn_edit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mEditMode = !mEditMode;
				mCourseAdapter.setEditEnabled(mEditMode);
				mCourseAdapter.notifyDataSetChanged();
			}
		});
		
		allcourses_btn_add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(AllCoursesActivity.this, AddCourseActivity.class);
				startActivity(intent);
			}
		});

		allcourses_btn_calendar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(AllCoursesActivity.this, CalendarActivity.class));
			}
		});
		
		getListView().setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Intent showCourseIntent = new Intent(AllCoursesActivity.this, CourseActivity.class);
				Course course = mCourseList.get(pos);
				
				//TODO: need to get the next time/day for this course
				Calendar day = null;
				TimePlaceBlock nextBlock = course.getScheduleBlocks().get(0);
				
				showCourseIntent.putExtra("courseID", course.getID());
				showCourseIntent.putExtra("blockID", nextBlock.getID());
				showCourseIntent.putExtra("day", Calendar.getInstance());
				startActivity(showCourseIntent);
            }
		});
	}
}

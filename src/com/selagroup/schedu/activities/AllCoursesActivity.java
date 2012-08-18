/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.selagroup.schedu.R;
import com.selagroup.schedu.ScheduApplication;
import com.selagroup.schedu.Utility;
import com.selagroup.schedu.adapters.CourseArrayAdapter;
import com.selagroup.schedu.adapters.CourseArrayAdapter.CourseDeleteListener;
import com.selagroup.schedu.managers.CourseManager;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Term;

/**
 * The Class AllCoursesActivity.
 */
public class AllCoursesActivity extends ListActivity {

	// Widgets
	private ImageButton allcourses_btn_add;
	private ImageButton allcourses_btn_calendar;

	// Data
	private Term mCurrentTerm;
	private List<Course> mCourseList;
	private CourseArrayAdapter mCourseAdapter;

	private CourseManager mCourseManager;

	private CourseDeleteListener mDeleteListener = new CourseDeleteListener() {
		public void onDelete(Course iCourse) {
			mCourseManager.delete(iCourse);
			mCourseList.remove(iCourse);
			if (mCourseList.isEmpty()) {
				mCourseList.add(null);
			}
			mCourseAdapter.notifyDataSetChanged();
			iCourse = null;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allcourses);

		ScheduApplication app = (ScheduApplication) getApplication();
		mCourseManager = app.getCourseManager();
		mCurrentTerm = app.getCurrentTerm();
		mCourseList = mCourseManager.getAllForTerm(mCurrentTerm.getID());
		if (mCourseList.isEmpty()) {
			mCourseList.add(null);
		}

		initWidgets();
		initListeners();

		mCourseAdapter = new CourseArrayAdapter(this, R.layout.adapter_course_select, mCourseList, mDeleteListener);
		setListAdapter(mCourseAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mCourseList.clear();
		mCourseList.addAll(mCourseManager.getAllForTerm(mCurrentTerm.getID()));
		if (mCourseList.isEmpty()) {
			mCourseList.add(null);
		}
		mCourseAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Utility.buildOptionsMenu(AllCoursesActivity.this, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (Utility.handleOptionsMenuSelection(AllCoursesActivity.this, item)) {
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	protected void initWidgets() {
		allcourses_btn_add = (ImageButton) findViewById(R.id.allcourses_btn_add);
		allcourses_btn_calendar = (ImageButton) findViewById(R.id.allcourses_btn_calendar);
	}

	protected void initListeners() {
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
	}
}

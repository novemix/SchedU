
/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.selagroup.schedu.R;
import com.selagroup.schedu.ScheduApplication;
import com.selagroup.schedu.Utility;
import com.selagroup.schedu.adapters.CourseArrayAdapter;
import com.selagroup.schedu.managers.CourseManager;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Term;

/**
 * The Class AllCoursesActivity.
 */
public class AllCoursesActivity extends ListActivity {

	// Widgets
	private ImageView allcourses_btn_add;
	private ImageView allcourses_btn_calendar;
	private TextView allcourses_tv_term_range;

	// Data
	private Term mCurrentTerm;
	private List<Course> mCourseList;
	private CourseArrayAdapter mCourseAdapter;

	private CourseManager mCourseManager;

	// Comparator
	private static final Comparator<Course> mCourseComparator = new Comparator<Course>() {
		// -1 = lhs < rhs, 1 = lhs > rhs
		public int compare(Course lhs, Course rhs) {
			if (lhs == null) {
				return -1;
			}
			if (rhs == null) {
				return 1;
			}
			int retVal = lhs.getCode().compareTo(rhs.getCode());
			return retVal;
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
		Collections.sort(mCourseList, mCourseComparator);

		initWidgets();
		setValues();
		initListeners();

		mCourseAdapter = new CourseArrayAdapter(this, R.layout.adapter_course_select, mCourseList);
		setListAdapter(mCourseAdapter);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (getIntent().getBooleanExtra("firstStart", false)) {
			openOptionsMenu();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mCourseList.clear();
		mCourseList.addAll(mCourseManager.getAllForTerm(mCurrentTerm.getID()));
		if (mCourseList.isEmpty()) {
			mCourseList.add(null);
		}
		Collections.sort(mCourseList, mCourseComparator);
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
		allcourses_btn_add = (ImageView) findViewById(R.id.allcourses_btn_add);
		allcourses_btn_calendar = (ImageView) findViewById(R.id.allcourses_btn_calendar);
		allcourses_tv_term_range = (TextView) findViewById(R.id.allcourses_tv_term_range);
	}
	
	protected void setValues() {
		String text = mCurrentTerm.getStartDateString() + " - " + mCurrentTerm.getEndDateString();
		allcourses_tv_term_range.setText(text);
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


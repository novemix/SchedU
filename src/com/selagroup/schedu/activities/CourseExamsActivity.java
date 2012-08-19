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
import android.widget.TextView;

import com.selagroup.schedu.R;
import com.selagroup.schedu.ScheduApplication;
import com.selagroup.schedu.Utility;
import com.selagroup.schedu.adapters.ExamArrayAdapter;
import com.selagroup.schedu.managers.ExamManager;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Exam;

/**
 * The Class CourseExamsActivity.
 */
public class CourseExamsActivity extends ListActivity {
	
	ExamManager mExamManager;
	ExamArrayAdapter mExamAdapter;
	List<Exam> mExamList;
	Course mCourse;
	
	ImageButton course_exams_btn_add;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_exams);
		
		initActivity();
		
		initWidgets();
		initListeners();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//TODO: start add/edit activity with result, and only update list if they didn't cancel
		if (mExamList != null) {
			mExamList.clear();
			mExamList.addAll(mExamManager.getAllForCourse(mCourse.getID()));
			mExamAdapter.notifyDataSetChanged();
		}
	}
	
	private void initActivity() {
		mCourse = ((ScheduApplication) getApplication()).getCourseManager().get(getIntent().getIntExtra("courseID", -1));
		((TextView) findViewById(R.id.course_exams_course_code)).setText(mCourse.getCode());
		
		mExamManager = ((ScheduApplication) getApplication()).getExamManager();
		mExamList = mExamManager.getAllForCourse(mCourse.getID());
		mExamAdapter = new ExamArrayAdapter(CourseExamsActivity.this, R.layout.adapter_exam_select, mExamList);
		setListAdapter(mExamAdapter);
	}

	private void initWidgets() {
		course_exams_btn_add = (ImageButton) findViewById(R.id.course_exams_btn_add);
	}

	private void initListeners() {
		course_exams_btn_add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(CourseExamsActivity.this, NewExamActivity.class);
				intent.putExtra("courseID", mCourse.getID());
				startActivity(intent);
			}
		});
	}
	
	/**
	 * Context menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Utility.buildOptionsMenu(CourseExamsActivity.this, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (Utility.handleOptionsMenuSelection(CourseExamsActivity.this, item)) {
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}
}

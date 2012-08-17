/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.selagroup.schedu.R;
import com.selagroup.schedu.ScheduApplication;
import com.selagroup.schedu.Utility;
import com.selagroup.schedu.adapters.WorkArrayAdapter;
import com.selagroup.schedu.managers.AssignmentManager;
import com.selagroup.schedu.model.Assignment;
import com.selagroup.schedu.model.Course;

/**
 * The Class CourseWorkActivity.
 */
public class CourseWorkActivity extends ListActivity {
	
	AssignmentManager mWorkManager;
	WorkArrayAdapter mWorkAdapter;
	List<Assignment> mWorkList;
	Course mCourse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_work);
		
		mWorkManager = ((ScheduApplication) getApplication()).getAssignmentManager();
		
		mCourse = ((ScheduApplication) getApplication()).getCourseManager().get(getIntent().getIntExtra("courseID", -1));
		((TextView) findViewById(R.id.course_work_course_code)).setText(mCourse.getCode());
		
		
		initListeners();
	}
	
	protected void initListeners() {
		((ImageButton) findViewById(R.id.course_work_btn_add)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mWorkList = new ArrayList<Assignment>();
				mWorkList.add(new Assignment(-1, "", null, mCourse));
				
				mWorkAdapter = new WorkArrayAdapter(CourseWorkActivity.this, R.layout.adapter_work_select, mWorkList);
				setListAdapter(mWorkAdapter);
			}
		});
	}
	
	/**
	 * Context menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Utility.buildOptionsMenu(CourseWorkActivity.this, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (Utility.handleOptionsMenuSelection(CourseWorkActivity.this, item)) {
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}
	/*
	private void mockup() {
		final Dialog dlgNewAssignment = new Dialog(this);
		//dlgNewAssignment.setTitle(R.string.dialog_new_assignment_title);
		dlgNewAssignment.requestWindowFeature(Window.FEATURE_NO_TITLE);
 		dlgNewAssignment.setContentView(R.layout.dialog_work);
 		dlgNewAssignment.findViewById(R.id.dialog_work_btn_cancel).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dlgNewAssignment.dismiss();
			}
		});
		((ImageButton) findViewById(R.id.course_work_btn_add)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dlgNewAssignment.show();
			}
		});
	}
	*/
}

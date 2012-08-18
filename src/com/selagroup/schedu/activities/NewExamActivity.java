/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.selagroup.schedu.R;
import com.selagroup.schedu.ScheduApplication;
import com.selagroup.schedu.model.Course;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * The Class NewExamActivity.
 */
public class NewExamActivity extends Activity {
	
	Course mCourse;
	
	Button new_exam_btn_date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_exam);
		
		initActivity();
		
		initWidgets();
		initListeners();
	}

	private void initActivity() {
		mCourse = ((ScheduApplication) getApplication()).getCourseManager().get(getIntent().getIntExtra("courseID", -1));
		((TextView) findViewById(R.id.new_exam_tv_course_code)).setText(mCourse.getCode());
	}
	
	private void initWidgets() {
		new_exam_btn_date = (Button) findViewById(R.id.new_exam_btn_date);
	}
	
	private void initListeners() {
		new_exam_btn_date.setOnClickListener(btnListener);
		((Button) findViewById(R.id.new_exam_btn_start_time)).setOnClickListener(btnListener);
		((Button) findViewById(R.id.new_exam_btn_end_time)).setOnClickListener(btnListener);
	}
	
	private OnClickListener btnListener = new OnClickListener() {
		
		final Calendar examDate = Calendar.getInstance();
		
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.new_exam_btn_date:
				(new DatePickerDialog(NewExamActivity.this, new OnDateSetListener() {
					
					public void onDateSet(DatePicker view, int year, int month, int day) {
						examDate.set(year, month, day);
						new_exam_btn_date.setText((new SimpleDateFormat("EEE, MMMM dd, yyyy")).format(examDate.getTime()));
					}
				}, 2012, 6, 25)).show();
				break;
			case R.id.new_exam_btn_start_time:
				(new TimePickerDialog(NewExamActivity.this, null, 0, 0, false)).show();
				break;
			case R.id.new_exam_btn_end_time:
				(new TimePickerDialog(NewExamActivity.this, null, 0, 0, false)).show();
				break;
			}
		}
	};
}
